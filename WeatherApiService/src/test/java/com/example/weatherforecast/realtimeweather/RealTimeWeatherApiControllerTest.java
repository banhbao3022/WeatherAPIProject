package com.example.weatherforecast.realtimeweather;

import com.example.weatherforecast.Location;
import com.example.weatherforecast.RealtimeWeather;
import com.example.weatherforecast.geolocation.GeolocationException;
import com.example.weatherforecast.geolocation.GeolocationService;
import com.example.weatherforecast.location.LocationNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Date;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(RealTimeWeatherApiController.class)
public class RealTimeWeatherApiControllerTest {
    private static final String END_POINT_PATH = "/v1/realtime";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    GeolocationService geolocationService;
    @MockBean
    RealTimeWeatherService realTimeWeatherService;

    @Test
    public void testGetShouldReturnStatus400BadRequest() throws Exception {
        Mockito.when(geolocationService.getLocation(anyString())).thenThrow(GeolocationException.class);
        mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isBadRequest())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturnStatus404NotFound() throws Exception {
        Location location = new Location();

        Mockito.when(geolocationService.getLocation(anyString())).thenReturn(location);
        Mockito.when(realTimeWeatherService.getByLocation(location)).thenThrow(LocationNotFoundException.class);

        mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isNotFound())
                .andDo(print());
    }

    @Test
    public void testGetShouldReturnStatus200OK() throws Exception {
        Location location = new Location();
        location.setCode("DELHI_IN");
        location.setCityName("Delhi");
        location.setRegionName("Delhi");
        location.setCountryCode("IN");
        location.setCountryName("India");

        RealtimeWeather realtimeWeather = new RealtimeWeather();
        realtimeWeather.setTemperature(10);
        realtimeWeather.setHumidity(30);
        realtimeWeather.setPrecipitation(40);
        realtimeWeather.setStatus("Cloudy");
        realtimeWeather.setWindSpeed(20);
        realtimeWeather.setLastUpdated(new Date());
        realtimeWeather.setLocation(location);
        location.setRealtimeWeather(realtimeWeather);

        Mockito.when(geolocationService.getLocation(anyString())).thenReturn(location);
        Mockito.when(realTimeWeatherService.getByLocation(location)).thenReturn(realtimeWeather);

        mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isOk())
                .andDo(print());
    }
}
