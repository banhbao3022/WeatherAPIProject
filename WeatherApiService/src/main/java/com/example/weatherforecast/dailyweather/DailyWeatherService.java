package com.example.weatherforecast.dailyweather;

import com.example.weatherforecast.DailyWeather;
import com.example.weatherforecast.Location;
import com.example.weatherforecast.location.LocationNotFoundException;
import com.example.weatherforecast.location.LocationRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Service
@Transactional
public class DailyWeatherService {
    private final DailyWeatherRepository dailyWeatherRepository;

    private final LocationRepository locationRepository;

    public DailyWeatherService(DailyWeatherRepository dailyWeatherRepository, LocationRepository locationRepository) {
        this.dailyWeatherRepository = dailyWeatherRepository;
        this.locationRepository = locationRepository;
    }

    public List<DailyWeather> getByLocation(Location location) {
        Location locationDB = locationRepository.findByCountryCodeAndAndCityName(location.getCountryCode(), location.getCityName());
        if (locationDB == null) throw new LocationNotFoundException(location.getCityName(), location.getCountryName());

        return dailyWeatherRepository.findByLocationCode(locationDB.getCode());
    }

    public List<DailyWeather> updateByLocationCode(String locationCode, List<DailyWeather> weatherList) {
        Location locationDB = locationRepository.getLocationByCode(locationCode);
        if (locationDB == null) throw new LocationNotFoundException(locationCode);
        weatherList.forEach(dailyWeather -> dailyWeather.getId().setLocation(locationDB));
        List<DailyWeather> deleteDailyWeather = new ArrayList<>();
        List<DailyWeather> dailyWeathersInDB = locationDB.getDailyWeathers();
        dailyWeathersInDB.forEach(dailyWeather -> {
            if (!weatherList.contains(dailyWeather)) {
                deleteDailyWeather.add(dailyWeather.getShallowCopy());
            }
        });
        deleteDailyWeather.forEach(dailyWeathersInDB::remove);
        return (List<DailyWeather>) dailyWeatherRepository.saveAll(weatherList);
    }
}
