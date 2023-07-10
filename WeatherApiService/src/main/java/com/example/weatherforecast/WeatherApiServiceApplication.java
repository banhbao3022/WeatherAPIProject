package com.example.weatherforecast;

import com.example.weatherforecast.dailyweather.DailyWeatherDTO;
import com.example.weatherforecast.hourlyweather.HourlyWeatherDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.modelmapper.convention.MatchingStrategies;
import org.modelmapper.spi.MatchingStrategy;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class WeatherApiServiceApplication {
    @Bean
    public ModelMapper getModelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        var typeMap = modelMapper.typeMap(HourlyWeather.class, HourlyWeatherDTO.class);
        typeMap.addMapping(src -> src.getId().getHourOfDay(), HourlyWeatherDTO::setHourOfDay);

        var typeMap1 = modelMapper.typeMap(HourlyWeatherDTO.class, HourlyWeather.class);
        typeMap1.addMapping(HourlyWeatherDTO::getHourOfDay,(des, value) -> des.getId().setHourOfDay(value != null ? (int) value : 0));

        var mapDailyWeather2dto = modelMapper.typeMap(DailyWeather.class, DailyWeatherDTO.class);
        mapDailyWeather2dto.addMapping(src -> src.getId().getDayOfMonth(), DailyWeatherDTO::setDayOfMonth);
        mapDailyWeather2dto.addMapping(src -> src.getId().getMonth(), DailyWeatherDTO::setMonth);

        var mapDto2DailyWeather = modelMapper.typeMap(DailyWeatherDTO.class, DailyWeather.class);
        mapDto2DailyWeather.addMapping(DailyWeatherDTO::getDayOfMonth, (des, value) -> des.getId().setDayOfMonth(value != null ? (int) value : 0));
        mapDto2DailyWeather.addMapping(DailyWeatherDTO::getMonth, (des, value) -> des.getId().setMonth(value != null ? (int) value : 0));

        return modelMapper;
    }
    public static void main(String[] args) {
        SpringApplication.run(WeatherApiServiceApplication.class, args);
    }

}
