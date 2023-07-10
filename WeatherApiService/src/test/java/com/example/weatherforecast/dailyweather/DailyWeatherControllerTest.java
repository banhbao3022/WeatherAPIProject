package com.example.weatherforecast.dailyweather;

import com.example.weatherforecast.DailyWeather;
import com.example.weatherforecast.Location;
import com.example.weatherforecast.geolocation.GeolocationException;
import com.example.weatherforecast.geolocation.GeolocationService;
import com.example.weatherforecast.location.LocationNotFoundException;
import com.example.weatherforecast.location.LocationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(DailyWeatherController.class)
public class DailyWeatherControllerTest {
    @Autowired
    MockMvc mockMvc;
    @MockBean
    DailyWeatherService dailyWeatherService;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    GeolocationService geolocationService;
    @MockBean
    LocationService locationService;
    private final String END_POINT_PATH = "/v1/daily";

    @Test
    public void testGetByIPShouldReturn400BadRequestBecauseGeolocationException() throws Exception {
        Mockito.when(geolocationService.getLocation(any())).thenThrow(GeolocationException.class);
        mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void testGetByIPShouldReturn404NotFound() throws Exception {
        Location location = new Location();
        Mockito.when(geolocationService.getLocation(any())).thenReturn(location);
        Mockito.when(dailyWeatherService.getByLocation(any())).thenThrow(LocationNotFoundException.class);
        mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void testGetByIPShouldReturn204NoContent() throws Exception {
        Location location = new Location();
        Mockito.when(geolocationService.getLocation(any())).thenReturn(location);
        Mockito.when(locationService.getLocationByCode(any())).thenReturn(location);
        mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isNoContent()).andDo(print());
    }

    @Test
    public void testGetByIPShouldReturn200Ok() throws Exception {
        Location location = new Location().cityName("Dong Ha").regionName("Quang Tri").countryName("Da Nang");
        DailyWeather dailyWeather1 = new DailyWeather().location(location)
                .dayOfMonth(12).month(10).maxTemp(30).minTemp(20)
                .precipitation(20).status("Cloudy");
        DailyWeather dailyWeather2 = new DailyWeather().location(location)
                .dayOfMonth(13).month(11).maxTemp(30).minTemp(20)
                .precipitation(20).status("Rain");

        Mockito.when(geolocationService.getLocation(any())).thenReturn(location);
        Mockito.when(locationService.getLocationByCode(any())).thenReturn(location);
        Mockito.when(dailyWeatherService.getByLocation(any())).thenReturn(List.of(dailyWeather1, dailyWeather2));
        mockMvc.perform(get(END_POINT_PATH)).andExpect(status().isOk()).andDo(print());
    }

    @Test
    public void testGetByCodeShouldReturn404NotFound() throws Exception {
        String code = "/DN_VN";
        Mockito.when(dailyWeatherService.getByLocation(any())).thenThrow(LocationNotFoundException.class);
        mockMvc.perform(get(END_POINT_PATH + code)).andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void testGetByCodeShouldReturn204NoContent() throws Exception {
        String code = "/DN_VN";
        mockMvc.perform(get(END_POINT_PATH + code)).andExpect(status().isNoContent()).andDo(print());
    }

    @Test
    public void testGetByCodeShouldReturn200Ok() throws Exception {
        String code = "/DN_VN";

        Location location = new Location().cityName("Dong Ha").regionName("Quang Tri").countryName("Da Nang");
        DailyWeather dailyWeather1 = new DailyWeather().location(location)
                .dayOfMonth(12).month(10).maxTemp(30).minTemp(20)
                .precipitation(20).status("Cloudy");
        DailyWeather dailyWeather2 = new DailyWeather().location(location)
                .dayOfMonth(13).month(11).maxTemp(30).minTemp(20)
                .precipitation(20).status("Rain");

        Mockito.when(dailyWeatherService.getByLocation(any())).thenReturn(List.of(dailyWeather1, dailyWeather2));
        mockMvc.perform(get(END_POINT_PATH + code)).andExpect(status().isOk()).andDo(print());
    }
}
