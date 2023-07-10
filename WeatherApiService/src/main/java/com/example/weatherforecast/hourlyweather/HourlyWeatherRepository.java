package com.example.weatherforecast.hourlyweather;

import com.example.weatherforecast.HourlyWeather;
import com.example.weatherforecast.HourlyWeatherId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface HourlyWeatherRepository extends CrudRepository<HourlyWeather, HourlyWeatherId> {

    @Query("select h from HourlyWeather h where h.id.location.code = ?1 and h.id.hourOfDay >= ?2")
    List<HourlyWeather> findByLocationCode(String locationCode, int currentHour);
}
