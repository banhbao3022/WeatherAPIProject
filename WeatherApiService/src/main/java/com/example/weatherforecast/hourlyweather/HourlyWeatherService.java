package com.example.weatherforecast.hourlyweather;

import com.example.weatherforecast.HourlyWeather;
import com.example.weatherforecast.Location;
import com.example.weatherforecast.location.LocationNotFoundException;
import com.example.weatherforecast.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

@Service
public class HourlyWeatherService {
    private HourlyWeatherRepository hourlyWeatherRepository;
    private LocationRepository locationRepository;

    public HourlyWeatherService(HourlyWeatherRepository hourlyWeatherRepository, LocationRepository locationRepository) {
        this.hourlyWeatherRepository = hourlyWeatherRepository;
        this.locationRepository = locationRepository;
    }

    List<HourlyWeather> getById(Location locationFromIP, int currentHours) throws LocationNotFoundException {
        Location location = locationRepository.findByCountryCodeAndAndCityName(locationFromIP.getCountryCode(),
                locationFromIP.getCityName());
        if(location == null) {
            throw new LocationNotFoundException(locationFromIP.getCityName(), locationFromIP.getCountryName());
        }
        return hourlyWeatherRepository.findByLocationCode(location.getCode(), currentHours);
    }

    List<HourlyWeather> updateByLocationCode(String locationCode, List<HourlyWeather> hourlyWeathersInRequest) throws LocationNotFoundException {
        Location location = locationRepository.getLocationByCode(locationCode);
        if (location == null) {
            throw new LocationNotFoundException(locationCode);

        }
        hourlyWeathersInRequest.forEach(hourlyWeather -> hourlyWeather.getId().setLocation(location));

        List<HourlyWeather> deleteHourlyWeather = new ArrayList<>();
        List<HourlyWeather> hourlyWeathersInDB = location.getHourlyWeathers();
        hourlyWeathersInDB.forEach(hourlyWeather -> {
            if (!hourlyWeathersInRequest.contains(hourlyWeather)) {
                deleteHourlyWeather.add(hourlyWeather.sallowCopy());
            }
        });

        deleteHourlyWeather.forEach(hourlyWeathersInDB::remove);

        return (List<HourlyWeather>) hourlyWeatherRepository.saveAll(hourlyWeathersInRequest);
    }

}
