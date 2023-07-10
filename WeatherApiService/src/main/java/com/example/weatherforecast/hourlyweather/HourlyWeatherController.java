package com.example.weatherforecast.hourlyweather;

import com.example.weatherforecast.CommonUtil;
import com.example.weatherforecast.HourlyWeather;
import com.example.weatherforecast.Location;
import com.example.weatherforecast.geolocation.GeolocationException;
import com.example.weatherforecast.geolocation.GeolocationService;
import com.example.weatherforecast.location.LocationNotFoundException;
import com.example.weatherforecast.location.LocationRepository;
import com.example.weatherforecast.location.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/v1/hourly")
@Validated
public class HourlyWeatherController {
    private GeolocationService geolocationService;
    private HourlyWeatherService hourlyWeatherService;
    private ModelMapper modelMapper;

    private LocationService locationService;

    public HourlyWeatherController(GeolocationService geolocationService, HourlyWeatherService hourlyWeatherService, ModelMapper modelMapper, LocationService locationService) {
        this.geolocationService = geolocationService;
        this.hourlyWeatherService = hourlyWeatherService;
        this.modelMapper = modelMapper;
        this.locationService = locationService;
    }

    @GetMapping
    public ResponseEntity<?> listHourlyForecastByIPAddress(HttpServletRequest request) {
        try {
            String ipAddress = CommonUtil.getIpAddress(request);
            Location locationFromIP = geolocationService.getLocation(ipAddress);
            int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));
            List<HourlyWeather> hourlyWeathers = hourlyWeatherService.getById(locationFromIP, currentHour);
            if (hourlyWeathers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(listEntity2DTO(hourlyWeathers));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> listHourlyForecastByLocationCode(@PathVariable String code, HttpServletRequest request) {
        try {
            Location location = locationService.getLocationByCode(code);
            int currentHour = Integer.parseInt(request.getHeader("X-Current-Hour"));
            List<HourlyWeather> hourlyWeathers = hourlyWeatherService.getById(location, currentHour);
            if (hourlyWeathers.isEmpty()) {
                return ResponseEntity.noContent().build();
            }
            return ResponseEntity.ok(listEntity2DTO(hourlyWeathers));
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateHourlyWeather(@PathVariable String code,
                                                 @RequestBody @Valid List<HourlyWeatherDTO> listDTO) throws BadRequestException {
        if (listDTO.isEmpty()) {
            throw new BadRequestException("Hourly forecast data can not be empty");
        }
        List<HourlyWeather> hourlyWeathers = listDTO2EntityList(listDTO);
        List<HourlyWeather> updatedList = hourlyWeatherService.updateByLocationCode(code, hourlyWeathers);
        return ResponseEntity.ok(listEntity2DTO(updatedList));
    }

    private List<HourlyWeather> listDTO2EntityList(List<HourlyWeatherDTO> listDTO) {
        List<HourlyWeather> hourlyWeathers = new ArrayList<>();
        listDTO.forEach(dto -> hourlyWeathers.add(modelMapper.map(dto, HourlyWeather.class)));
        return hourlyWeathers;
    }

    private HourlyWeatherListDTO listEntity2DTO(List<HourlyWeather> hourlyWeathers) {
        HourlyWeatherListDTO list = new HourlyWeatherListDTO();
        list.setLocation(hourlyWeathers.get(0).getId().getLocation().toString());
        hourlyWeathers.forEach(hourlyWeather -> {
            list.addHourlyWeatherDTO(modelMapper.map(hourlyWeather, HourlyWeatherDTO.class));
        });
        return list;
    }
}
