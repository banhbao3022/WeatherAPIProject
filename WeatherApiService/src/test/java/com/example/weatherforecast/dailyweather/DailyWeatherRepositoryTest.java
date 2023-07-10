package com.example.weatherforecast.dailyweather;

import com.example.weatherforecast.DailyWeather;
import com.example.weatherforecast.DailyWeatherId;
import com.example.weatherforecast.Location;
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
public class DailyWeatherRepositoryTest {

    @Autowired
    private DailyWeatherRepository dailyWeatherRepository;

    @Test
    public void testAdd() {
        String locationCode = "HN_VN";
        Location location = new Location().code(locationCode);

        DailyWeather weather1 = new DailyWeather()
                .location(location).month(12)
                .dayOfMonth(12).maxTemp(100).minTemp(20).precipitation(1).status("Clweoud");
        DailyWeather save = dailyWeatherRepository.save(weather1);
        assertThat(save.getId().getLocation().getCode()).isEqualTo(locationCode);
    }

    @Test
    public void testDelete() {
        String locationCode = "HN_VN";
        Location location = new Location().code(locationCode);

        DailyWeatherId id = new DailyWeatherId();
        id.setDayOfMonth(12);
        id.setMonth(12);
        id.setLocation(location);
        dailyWeatherRepository.deleteById(id);
        Optional<DailyWeather> op = dailyWeatherRepository.findById(id);
        assertThat(op.isEmpty()).isTrue();
    }

    @Test
    public void testFindByLocationCodeFound() {
        String locationCode = "HN_VN";
        List<DailyWeather> dailyWeathers = dailyWeatherRepository.findByLocationCode(locationCode);
        assertThat(dailyWeathers.size()).isGreaterThan(0);
    }

    @Test
    public void testFindByLocationCodeNotFound() {
        String locationCode = "ABC";
        List<DailyWeather> dailyWeathers = dailyWeatherRepository.findByLocationCode(locationCode);
        assertThat(dailyWeathers.isEmpty()).isTrue();
    }

}
