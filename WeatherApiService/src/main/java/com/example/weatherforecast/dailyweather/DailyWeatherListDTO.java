package com.example.weatherforecast.dailyweather;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.ArrayList;
import java.util.List;

public class DailyWeatherListDTO {
    private String location;
    @JsonProperty("daily_forecast")
    private List<DailyWeatherDTO> listDailyWeatherDTO = new ArrayList<>();

    public DailyWeatherListDTO location(String locationCode) {
        this.location = locationCode;
        return this;
    }
    public void addDailyWeatherDTO(DailyWeatherDTO dto) {
        listDailyWeatherDTO.add(dto);
    }
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public List<DailyWeatherDTO> getListDailyWeatherDTO() {
        return listDailyWeatherDTO;
    }

    public void setListDailyWeatherDTO(List<DailyWeatherDTO> listDailyWeatherDTO) {
        this.listDailyWeatherDTO = listDailyWeatherDTO;
    }
}
