package com.example.weatherforecast.realtimeweather;

import com.example.weatherforecast.Location;
import com.example.weatherforecast.RealtimeWeather;
import com.example.weatherforecast.location.LocationNotFoundException;
import com.example.weatherforecast.location.LocationRepository;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class RealTimeWeatherService {
    private LocationRepository locationRepository;
    private RealtimeWeatherRepository realtimeWeatherRepository;

    public RealTimeWeatherService(LocationRepository locationRepository, RealtimeWeatherRepository realtimeWeatherRepository) {
        this.locationRepository = locationRepository;
        this.realtimeWeatherRepository = realtimeWeatherRepository;
    }

    public RealtimeWeather getByLocation(Location location)  {
        String countryCode = location.getCountryCode();
        String cityName = location.getCityName();
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode, cityName);
        if (realtimeWeather == null) {
            throw new LocationNotFoundException(cityName, location.getCountryName());
        }
        return realtimeWeather;
    }

    public RealtimeWeather getByLocationCode(String locationCode)  {
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByLocationCode(locationCode);
        if (realtimeWeather == null) {
            throw new LocationNotFoundException(locationCode);
        }
        return realtimeWeather;
    }

    public RealtimeWeather update(String locationCode, RealtimeWeather realtimeWeather)  {
        Location location = locationRepository.getLocationByCode(locationCode);
        if (location == null) {
            throw new LocationNotFoundException(locationCode);
        }
        realtimeWeather.setLocation(location);
        realtimeWeather.setLastUpdated(new Date());

        if (location.getRealtimeWeather() == null) {
            location.setRealtimeWeather(realtimeWeather);
            Location updatedLocation = locationRepository.save(location);
            return updatedLocation.getRealtimeWeather();
        }

        location.setRealtimeWeather(realtimeWeather);
        return realtimeWeatherRepository.save(realtimeWeather);
    }
}
