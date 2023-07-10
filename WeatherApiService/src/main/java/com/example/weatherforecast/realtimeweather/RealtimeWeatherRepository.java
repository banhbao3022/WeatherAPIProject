package com.example.weatherforecast.realtimeweather;

import com.example.weatherforecast.Location;
import com.example.weatherforecast.RealtimeWeather;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RealtimeWeatherRepository extends CrudRepository<RealtimeWeather, String> {
    @Query("select r from RealtimeWeather r where r.location.countryCode = ?1 and r.location.cityName = ?2")
    RealtimeWeather findByCountryCodeAndCity(String countryCode, String city);
    @Query("select r from RealtimeWeather r where r.location.code = ?1 and r.location.trashed = false")
    RealtimeWeather findByLocationCode(String locationCode);
}
