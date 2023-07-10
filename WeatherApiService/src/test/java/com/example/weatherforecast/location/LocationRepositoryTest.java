package com.example.weatherforecast.location;

import com.example.weatherforecast.HourlyWeather;
import com.example.weatherforecast.Location;
import com.example.weatherforecast.RealtimeWeather;
import com.example.weatherforecast.realtimeweather.RealtimeWeatherRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.Date;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class LocationRepositoryTest {
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private RealtimeWeatherRepository realtimeWeatherRepository;
    @Test
    public void testAddLocation() {
        Location location = new Location();
        location.setCode("DELHI_IN");
        location.setCityName("Delhi");
        location.setRegionName("Delhi");
        location.setCountryCode("IN");
        location.setCountryName("India");
        location.setEnable(false);

        Location save = locationRepository.save(location);
        assertThat(save).isNotNull();
        assertThat(save.getCode()).isEqualTo("DELHI_IN");
    }

    @Test
    public void testListUntrashLocation() {
        List<Location> locations = locationRepository.listUntrashLocations();
        locations.forEach(System.out::println);
    }

    @Test
    public void testListLocationByCode() {
        Location location = locationRepository.getLocationByCode("DELHI_IN");
        assertThat(location).isNotNull();
        assertThat(location.getCode()).isEqualTo("DELHI_IN");

        location = locationRepository.getLocationByCode("ABCD");
        assertThat(location).isNull();
    }

    @Test
    public void testAddRealtimeWeatherData() {
        Location location = locationRepository.getLocationByCode("LACA_US");
        RealtimeWeather realtimeWeather = location.getRealtimeWeather();

        if (realtimeWeather == null) {
            realtimeWeather = new RealtimeWeather();
            realtimeWeather.setLocation(location);
            location.setRealtimeWeather(realtimeWeather);
        }

        realtimeWeather.setTemperature(10);
        realtimeWeather.setHumidity(30);
        realtimeWeather.setPrecipitation(40);
        realtimeWeather.setStatus("Cloudy");
        realtimeWeather.setWindSpeed(20);
        realtimeWeather.setLastUpdated(new Date());

        Location savedLocation = locationRepository.save(location);
        assertThat(location.getRealtimeWeather()).isNotNull();
        assertThat(location.getRealtimeWeather().getLocationCode()).isEqualTo(savedLocation.getCode());
    }

    @Test
    public void testAddHourlyWeatherData() {
        Location location = new Location().code("DN_VN").cityName("Da Nang")
                .regionName("Da Nang").countryName("Viet Nam").countryCode("VN");

        HourlyWeather hourlyWeather1 = new HourlyWeather().id(location, 8).temperature(20)
                .status("Cloudy").precipitation(30);

        HourlyWeather hourlyWeather2 = new HourlyWeather().id(location, 9).temperature(30)
                .status("Rain").precipitation(50);

        location.addHourlyWeather(hourlyWeather1);
        location.addHourlyWeather(hourlyWeather2);

        Location save = locationRepository.save(location);
        assertThat(save.getHourlyWeathers().size()).isEqualTo(2);
    }

    @Test
    public void testFindByCountryCodeAndCityNameNotFound() {
        String countryCode=  "VN";
        String cityName = "Quang Tri";
        Location location = locationRepository.findByCountryCodeAndAndCityName(countryCode, cityName);

        assertThat(location).isNull();
    }
    @Test
    public void testFindByCountryCodeAndCityNameFound() {
        String countryCode=  "VN";
        String cityName = "Da Nang";
        Location location = locationRepository.findByCountryCodeAndAndCityName(countryCode, cityName);

        assertThat(location).isNotNull();
    }
}
