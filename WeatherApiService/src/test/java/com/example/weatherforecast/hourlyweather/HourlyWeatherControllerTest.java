package com.example.weatherforecast.hourlyweather;

import com.example.weatherforecast.HourlyWeather;
import com.example.weatherforecast.Location;
import com.example.weatherforecast.geolocation.GeolocationException;
import com.example.weatherforecast.geolocation.GeolocationService;
import com.example.weatherforecast.location.LocationNotFoundException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(HourlyWeatherController.class)
public class HourlyWeatherControllerTest {
    private final String END_POINT_PATH = "/v1/hourly";
    private final String X_CURRENT_HOUR = "x-current-hour";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    GeolocationService geolocationService;
    @MockBean
    HourlyWeatherService hourlyWeatherService;

    @Test
    public void testGetIPShouldReturn400BadRequestBecauseNoHeaderXCurrentHour() throws Exception {
        mockMvc.perform(get(END_POINT_PATH))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void testGetIPShouldReturn400BadRequestBecauseGeolocationException() throws Exception {
        int currentHour = 9;

        Mockito.when(geolocationService.getLocation(anyString())).thenThrow(GeolocationException.class);
        mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void testGetIPShouldReturn204NoContent() throws Exception {
        int currentHour = 9;
        Location location = new Location();
        Mockito.when(geolocationService.getLocation(anyString())).thenReturn(location);
        Mockito.when(hourlyWeatherService.getById(location,currentHour)).thenReturn(Collections.emptyList());

        mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
                .andExpect(status().isNoContent()).andDo(print());
    }

    @Test
    public void testGetIPShouldReturn200OK() throws Exception {
        int currentHour = 9;
        Location location = new Location().code("DN_VN").cityName("Da Nang")
                .regionName("Da Nang").countryName("Viet Nam").countryCode("VN");

        HourlyWeather hourlyWeather1 = new HourlyWeather().id(location, 8).temperature(20)
                .status("Cloudy").precipitation(30);

        HourlyWeather hourlyWeather2 = new HourlyWeather().id(location, 9).temperature(30)
                .status("Rain").precipitation(50);

        Mockito.when(geolocationService.getLocation(anyString())).thenReturn(location);
        Mockito.when(hourlyWeatherService.getById(location,currentHour)).thenReturn(List.of(hourlyWeather1, hourlyWeather2));

        mockMvc.perform(get(END_POINT_PATH).header(X_CURRENT_HOUR, String.valueOf(currentHour)))
                .andExpect(status().isOk()).andDo(print());
    }

}
