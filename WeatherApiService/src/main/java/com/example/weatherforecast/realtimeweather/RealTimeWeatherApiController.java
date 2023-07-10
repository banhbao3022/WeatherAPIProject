package com.example.weatherforecast.realtimeweather;

import com.example.weatherforecast.CommonUtil;
import com.example.weatherforecast.Location;
import com.example.weatherforecast.RealtimeWeather;
import com.example.weatherforecast.geolocation.GeolocationException;
import com.example.weatherforecast.geolocation.GeolocationService;
import com.example.weatherforecast.location.LocationNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/realtime")
public class RealTimeWeatherApiController {
    private Logger logger = LoggerFactory.getLogger(RealTimeWeatherApiController.class);
    private RealTimeWeatherService realTimeWeatherService;
    private GeolocationService geolocationService;
    private ModelMapper modelMapper;

    public RealTimeWeatherApiController(RealTimeWeatherService realTimeWeatherService, GeolocationService geolocationService, ModelMapper modelMapper) {
        this.realTimeWeatherService = realTimeWeatherService;
        this.geolocationService = geolocationService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<?> getRealtimeByIPAddress(HttpServletRequest request) {
        String ipAddress = CommonUtil.getIpAddress(request);
        Location location = geolocationService.getLocation(ipAddress);
        RealtimeWeather realtimeWeather = realTimeWeatherService.getByLocation(location);
        return ResponseEntity.ok(entityToDto(realtimeWeather));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getRealTimeByLocationCode(@PathVariable String code) {
        RealtimeWeather realtimeWeather = realTimeWeatherService.getByLocationCode(code);
        return ResponseEntity.ok(entityToDto(realtimeWeather));
    }

    @PutMapping("/{code}")
    public ResponseEntity<?> updateRealTimeWeather(@PathVariable String code,
                                                   @RequestBody @Valid RealtimeWeather realtimeWeather){
        realtimeWeather.setLocationCode(code);
        RealtimeWeather updated = realTimeWeatherService.update(code, realtimeWeather);
        return ResponseEntity.ok(entityToDto(updated));
    }

    private RealtimeWeatherDTO entityToDto(RealtimeWeather realtimeWeather) {
        return modelMapper.map(realtimeWeather, RealtimeWeatherDTO.class);
    }
}
