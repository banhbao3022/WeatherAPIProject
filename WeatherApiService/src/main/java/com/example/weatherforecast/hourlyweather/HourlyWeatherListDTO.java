package com.example.weatherforecast.hourlyweather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class HourlyWeatherListDTO {
    private String location;
    @JsonProperty("hourly_forecast")
    private List<HourlyWeatherDTO> hourlyWeatherDTOS = new ArrayList<>();

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<HourlyWeatherDTO> getHourlyWeatherDTOS() {
        return hourlyWeatherDTOS;
    }

    public void setHourlyWeatherDTOS(List<HourlyWeatherDTO> hourlyWeatherDTOS) {
        this.hourlyWeatherDTOS = hourlyWeatherDTOS;
    }

    public void addHourlyWeatherDTO(HourlyWeatherDTO hourlyWeatherDTO) {
        this.hourlyWeatherDTOS.add(hourlyWeatherDTO);
    }
}
