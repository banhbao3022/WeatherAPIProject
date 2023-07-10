package com.example.weatherforecast.location;

import com.example.weatherforecast.Location;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(LocationController.class)
public class LocationControllerTest {
    private static final String END_POINT = "/v1/locations";
    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @MockBean
    LocationService locationService;

    @Test
    public void testAddLocation_Return400BadRequest() throws Exception {
        Location location = new Location();

        mockMvc.perform(post(END_POINT).contentType("application/json")
                .content(objectMapper.writeValueAsString(location)))
                .andExpect(status().isBadRequest()).andDo(print());

    }

    @Test
    public void testAddLocation_Return201IsCreated() throws Exception {
        Location location = new Location();
        location.setCode("LACA_US");
        location.setCityName("Los Angeles");
        location.setRegionName("California");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnable(false);

        Mockito.when(locationService.addLocation(location)).thenReturn(location);

        mockMvc.perform(post(END_POINT).contentType("application/json")
                        .content(objectMapper.writeValueAsString(location)))
                .andExpect(status().isCreated()).andDo(print())
                .andExpect(jsonPath("$.code").value("LACA_US"))
                .andExpect(header().string("Location", "/v1/locations/LACA_US"))
                .andExpect(content().contentType("application/json"));
    }

    @Test
    public void testAddLocation_InvalidRequestBody() throws Exception {
        Location location = new Location();

        MvcResult mvcResult = mockMvc.perform(post(END_POINT).contentType("application/json")
                        .content(objectMapper.writeValueAsString(location)))
                .andExpect(status().isBadRequest()).andDo(print()).andReturn();
        assertThat(mvcResult.getResponse().getContentAsString()).contains("Country name cannot be null");

    }

    @Test
    public void testListUntrashLocation_Return204NoContent() throws Exception {
        Mockito.when(locationService.listUntrashLocation()).thenReturn(Collections.emptyList());

        mockMvc.perform(get(END_POINT).contentType("application/json"))
                .andExpect(status().isNoContent()).andDo(print());
    }

    @Test
    public void testListUntrashLocation_Return200OK() throws Exception {
        Location location = new Location();
        location.setCode("LACA_US");
        location.setCityName("Los Angeles");
        location.setRegionName("California");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnable(false);

        Mockito.when(locationService.listUntrashLocation()).thenReturn(List.of(location));

        mockMvc.perform(get(END_POINT).contentType("application/json"))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$[0].code").value("LACA_US"));
    }

    @Test
    public void testGetLocationByCoe_Return405MethodNotAllowed() throws Exception {
        String uri = END_POINT + "/ABCD";
        mockMvc.perform(post(uri).contentType("application/json"))
                .andExpect(status().isMethodNotAllowed()).andDo(print());

    }

    @Test
    public void testGetLocationByCoe_Return404NotFound() throws Exception {
        String uri = END_POINT + "/ABCD";
        Mockito.when(locationService.getLocationByCode(anyString())).thenThrow(LocationNotFoundException.class);
        mockMvc.perform(get(uri).contentType("application/json"))
                .andExpect(status().isNotFound()).andDo(print());

    }

    @Test
    public void testGetLocationByCoe_Return200Ok() throws Exception {
        Location location = new Location();
        location.setCode("LACA_US");
        location.setCityName("Los Angeles");
        location.setRegionName("California");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnable(false);

        String uri = END_POINT + "/" + location.getCode();
        Mockito.when(locationService.getLocationByCode(location.getCode())).thenReturn(location);
        mockMvc.perform(get(uri).contentType("application/json"))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.code").value(location.getCode()));

    }

    @Test
    public void testUpdateLocation_Return400BadRequest() throws Exception {
        Location location = new Location();
        location.setRegionName("California");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnable(false);

        mockMvc.perform(put(END_POINT).contentType("application/json")
                        .content(objectMapper.writeValueAsString(location)))
                .andExpect(status().isBadRequest()).andDo(print());
    }

    @Test
    public void testUpdateLocation_Return404NotFound() throws Exception {
        Location location = new Location();
        location.setCode("ABCD");
        location.setCityName("Los Angeles");
        location.setRegionName("California");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnable(false);

        Mockito.when(locationService.updateLocation(location))
                .thenThrow(new LocationNotFoundException("Not found"));

        mockMvc.perform(put(END_POINT).contentType("application/json")
                        .content(objectMapper.writeValueAsString(location)))
                .andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void testUpdateLocation_Return200Ok() throws Exception {
        Location location = new Location();
        location.setCode("LACA_US");
        location.setCityName("Los Angeles");
        location.setRegionName("California");
        location.setCountryCode("US");
        location.setCountryName("United States of America");
        location.setEnable(false);

        Mockito.when(locationService.updateLocation(location))
                .thenReturn(location);

        mockMvc.perform(put(END_POINT).contentType("application/json")
                        .content(objectMapper.writeValueAsString(location)))
                .andExpect(status().isOk()).andDo(print())
                .andExpect(jsonPath("$.code").value(location.getCode()));
    }

    @Test
    public void testDeleteLocation_Return404NotFound() throws Exception {
        Mockito.doThrow(LocationNotFoundException.class).when(locationService).deleteLocation("ABC");

        mockMvc.perform(delete(END_POINT + "/ABC"))
                .andExpect(status().isNotFound()).andDo(print());
    }

    @Test
    public void testDeleteLocation_Return204NoContent() throws Exception {
        Mockito.doNothing().when(locationService).deleteLocation("ABC");

        mockMvc.perform(delete(END_POINT + "/ABC"))
                .andExpect(status().isNoContent()).andDo(print());
    }
}
