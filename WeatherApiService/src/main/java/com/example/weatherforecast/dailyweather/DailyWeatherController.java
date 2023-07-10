package com.example.weatherforecast.dailyweather;

import com.example.weatherforecast.CommonUtil;
import com.example.weatherforecast.DailyWeather;
import com.example.weatherforecast.Location;
import com.example.weatherforecast.geolocation.GeolocationService;
import com.example.weatherforecast.hourlyweather.BadRequestException;
import com.example.weatherforecast.location.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@Validated
@RequestMapping("/v1/daily")
public class DailyWeatherController {
    private DailyWeatherService dailyWeatherService;
    private ModelMapper modelMapper;
    private GeolocationService geolocationService;
    private LocationService locationService;

    public DailyWeatherController(DailyWeatherService dailyWeatherService, ModelMapper modelMapper, GeolocationService geolocationService, LocationService locationService) {
        this.dailyWeatherService = dailyWeatherService;
        this.modelMapper = modelMapper;
        this.geolocationService = geolocationService;
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<DailyWeatherListDTO> listDailyForecastByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtil.getIpAddress(request);
        Location location = geolocationService.getLocation(ipAddress);
        List<DailyWeather> weatherList = dailyWeatherService.getByLocation(location);
        if (weatherList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(listEntity2DTO(weatherList));
    }

    @GetMapping("/{code}")
    public ResponseEntity<DailyWeatherListDTO> listDailyForecastByLocationCode(@PathVariable String code) {
        Location location = locationService.getLocationByCode(code);
        List<DailyWeather> weatherList = dailyWeatherService.getByLocation(location);
        if (weatherList.isEmpty()) return ResponseEntity.noContent().build();
        return ResponseEntity.ok(listEntity2DTO(weatherList));
    }

    @PutMapping("/{code}")
    public ResponseEntity<DailyWeatherListDTO> updateDailyForecast(@PathVariable String code,
                                                                   @Valid @RequestBody List<DailyWeatherDTO> dailyWeatherDTOS) throws BadRequestException {
        if (dailyWeatherDTOS.isEmpty()) throw new BadRequestException("Daily data is empty");
        for (int i = 0; i < dailyWeatherDTOS.size(); i++) {
            var dailyWeather = dailyWeatherDTOS.get(i);
            if (dailyWeather.getMinTemp() > dailyWeather.getMaxTemp()) {
                throw new BadRequestException("minTemp must be less than maxTemp for temperature at daily weather " + i);
            }
        }
        List<DailyWeather> weatherList = listDTO2Entity(dailyWeatherDTOS);
        List<DailyWeather> weatherUpdatedList = dailyWeatherService.updateByLocationCode(code, weatherList);
        DailyWeatherListDTO weatherListDTO = listEntity2DTO(weatherUpdatedList);
        return ResponseEntity.ok(weatherListDTO);
    }
    private List<DailyWeather> listDTO2Entity(List<DailyWeatherDTO> dailyWeatherDTOS) {
        List<DailyWeather> weatherList = new ArrayList<>();
        dailyWeatherDTOS.forEach(dto -> weatherList.add(modelMapper.map(dto, DailyWeather.class)));
        return weatherList;
    }
    private DailyWeatherListDTO listEntity2DTO(List<DailyWeather> dailyWeathers) {
        DailyWeatherListDTO listDTO = new DailyWeatherListDTO();
        listDTO.setLocation(dailyWeathers.get(0).getId().getLocation().toString());
        dailyWeathers.forEach(dailyWeather -> listDTO.addDailyWeatherDTO(modelMapper.map(dailyWeather, DailyWeatherDTO.class)));
        return listDTO;
    }
}
