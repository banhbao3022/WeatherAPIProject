package com.example.weatherforecast;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

import java.util.Objects;

@Entity
@Table(name = "weather_daily")
public class DailyWeather {
    @EmbeddedId
    private DailyWeatherId id = new DailyWeatherId();
    private int minTemp;
    private int maxTemp;
    private int precipitation;
    @Column(length = 50)
    private String status;

    public DailyWeather location(Location location) {
        getId().setLocation(location);
        return this;
    }
    public DailyWeather month(int month) {
        getId().setMonth(month);
        return this;
    }

    public DailyWeather dayOfMonth(int dayOfMonth) {
        getId().setDayOfMonth(dayOfMonth);
        return this;
    }

    public DailyWeather maxTemp(int temperature) {
        setMaxTemp(temperature);
        return this;
    }

    public DailyWeather minTemp(int temperature) {
        setMinTemp(temperature);
        return this;
    }

    public DailyWeather precipitation(int precipitation) {
        setPrecipitation(precipitation);
        return this;
    }

    public DailyWeather status(String status) {
        setStatus(status);
        return this;
    }

    public DailyWeather getShallowCopy() {
        DailyWeather dailyWeather = new DailyWeather();
        dailyWeather.setId(this.id);
        return dailyWeather;
    }

    public DailyWeatherId getId() {
        return id;
    }

    public void setId(DailyWeatherId id) {
        this.id = id;
    }

    public int getMinTemp() {
        return minTemp;
    }

    public void setMinTemp(int minTemp) {
        this.minTemp = minTemp;
    }

    public int getMaxTemp() {
        return maxTemp;
    }

    public void setMaxTemp(int maxTemp) {
        this.maxTemp = maxTemp;
    }

    public int getPrecipitation() {
        return precipitation;
    }

    public void setPrecipitation(int precipitation) {
        this.precipitation = precipitation;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyWeather weather = (DailyWeather) o;
        return Objects.equals(id, weather.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
