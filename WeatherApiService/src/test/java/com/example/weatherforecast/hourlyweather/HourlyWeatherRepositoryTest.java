package com.example.weatherforecast.hourlyweather;

import com.example.weatherforecast.HourlyWeather;
import com.example.weatherforecast.HourlyWeatherId;
import com.example.weatherforecast.Location;
import com.example.weatherforecast.RealtimeWeather;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class HourlyWeatherRepositoryTest {

    @Autowired
    private HourlyWeatherRepository hourlyWeatherRepository;

    @Test
    public void testAddHourlyWeather() {
        Location location = new Location().code("DELHI_IN");

        HourlyWeather hourlyWeather = new HourlyWeather().id(location, 8).temperature(30)
                .status("Cloudy").precipitation(30);

        HourlyWeather saved = hourlyWeatherRepository.save(hourlyWeather);
        assertThat(saved.getId().getLocation().getCode()).isEqualTo(location.getCode());
    }

    @Test
    public void testDeleteHourlyWeather() {
        Location location = new Location().code("DELHI_IN");

        HourlyWeatherId id = new HourlyWeatherId();
        id.setHourOfDay(7);
        id.setLocation(location);

        hourlyWeatherRepository.deleteById(id);

        Optional<HourlyWeather> op = hourlyWeatherRepository.findById(id);
        assertThat(op.isPresent()).isFalse();
    }

    @Test
    public void testFindByLocationCodeNotFound() {
        String locationCode = "DN_VN";
        int currentHour = 10;

        List<HourlyWeather> hourlyWeathers = hourlyWeatherRepository.findByLocationCode(locationCode, currentHour);
        assertThat(hourlyWeathers.isEmpty()).isTrue();
    }

    @Test
    public void testFindByLocationCodeFound() {
        String locationCode = "DN_VN";
        int currentHour = 5;

        List<HourlyWeather> hourlyWeathers = hourlyWeatherRepository.findByLocationCode(locationCode, currentHour);
        assertThat(hourlyWeathers.size()).isGreaterThan(0);
    }
}
