package com.example.weatherforecast.dailyweather;

import com.example.weatherforecast.DailyWeather;
import com.example.weatherforecast.DailyWeatherId;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface DailyWeatherRepository extends CrudRepository<DailyWeather, DailyWeatherId> {

    @Query("select d from DailyWeather d where d.id.location.code = ?1 and d.id.location.trashed = false")
    List<DailyWeather> findByLocationCode(String code);
}
