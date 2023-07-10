package com.example.weatherforecast.realtimeweather;

import com.example.weatherforecast.RealtimeWeather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.Date;
import java.util.Optional;

@DataJpaTest
@Rollback(value = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class RealtimeWeatherRepositoryTest {
    @Autowired
    RealtimeWeatherRepository realtimeWeatherRepository;

    @Test
    public void updateRealtimeWeather() {
        String locationCode = "DELHI_IN";
        Optional<RealtimeWeather> optional = realtimeWeatherRepository.findById(locationCode);
        assertThat(optional.isPresent()).isTrue();
        var realtimeWeather = optional.get();
        realtimeWeather.setTemperature(10);
        realtimeWeather.setHumidity(70);
        realtimeWeather.setPrecipitation(40);
        realtimeWeather.setStatus("Cloudy");
        realtimeWeather.setWindSpeed(20);
        realtimeWeather.setLastUpdated(new Date());
        RealtimeWeather save = realtimeWeatherRepository.save(realtimeWeather);
        assertThat(save.getHumidity()).isEqualTo(70);
    }

    @Test
    public void testFindByCountryCodeAndCityNotFound() {
        String countryCode = "ABC";
        String city = "abc";
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode, city);
        assertThat(realtimeWeather).isNull();
    }

    @Test
    public void testFindByCountryCodeAndCityFound() {
        String countryCode = "IN";
        String city = "Delhi";
        RealtimeWeather realtimeWeather = realtimeWeatherRepository.findByCountryCodeAndCity(countryCode, city);
        assertThat(realtimeWeather).isNotNull();
        assertThat(realtimeWeather.getLocation().getCountryCode()).isEqualTo(countryCode);
        assertThat(realtimeWeather.getLocation().getCityName()).isEqualTo(city);
    }
}
