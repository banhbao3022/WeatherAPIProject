package com.example.weatherforecast;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "locations")
public class Location {
    @Id
    @Column(length = 12, nullable = false, unique = true)
    @NotNull(message = "Location code cannot be null")
    @Length(min = 3, max = 12, message = "Location code must have 3-12 characters")
    private String code;
    @Column(length = 128, nullable = false)
    @JsonProperty(value = "city_name")
    @NotNull(message = "City name cannot be null")
    @Length(min = 3, max = 128, message = "City name must have 3-128 characters")
    private String cityName;
    @Column(length = 128)
    @JsonProperty(value = "region_name")
    @Length(min = 3, max = 128, message = "Region name must have 3-128 characters")
    private String regionName;
    @Column(length = 64, nullable = false)
    @JsonProperty(value = "country_name")
    @NotNull(message = "Country name cannot be null")
    @Length(min = 3, max = 64, message = "Country name must have 3-128 characters")
    private String countryName;
    @Column(length = 2)
    @JsonProperty(value = "country_code")
    @NotNull(message = "Country code cannot be null")
    @Length(min = 2, max = 2, message = "Country code must have 2 characters")
    private String countryCode;
    private boolean enable;
    @JsonIgnore
    private boolean trashed;
    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL)
    @PrimaryKeyJoinColumn
    @JsonIgnore
    private RealtimeWeather realtimeWeather;
    @OneToMany(mappedBy = "id.location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<HourlyWeather> hourlyWeathers = new ArrayList<>();

    @OneToMany(mappedBy = "id.location", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DailyWeather> dailyWeathers = new ArrayList<>();

    public Location() {
    }

    public Location(String cityName, String regionName, String countryName, String countryCode) {
        super();
        this.cityName = cityName;
        this.regionName = regionName;
        this.countryName = countryName;
        this.countryCode = countryCode;
    }

    public Location code(String code) {
        setCode(code);
        return this;
    }

    public Location cityName(String cityName) {
        setCityName(cityName);
        return this;
    }

    public Location regionName(String regionName) {
        setRegionName(regionName);
        return this;
    }

    public Location countryName(String countryName) {
        setCountryName(countryName);
        return this;
    }

    public Location countryCode(String countryCode) {
        setCountryCode(countryCode);
        return this;
    }

    public void addHourlyWeather(HourlyWeather hourlyWeather) {
        this.hourlyWeathers.add(hourlyWeather);
    }
    public void addDailyWeather(DailyWeather dailyWeather) {
        this.dailyWeathers.add(dailyWeather);
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getRegionName() {
        return regionName;
    }

    public void setRegionName(String regionName) {
        this.regionName = regionName;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public boolean isTrashed() {
        return trashed;
    }

    public void setTrashed(boolean trashed) {
        this.trashed = trashed;
    }

    public List<HourlyWeather> getHourlyWeathers() {
        return hourlyWeathers;
    }

    public void setHourlyWeathers(List<HourlyWeather> hourlyWeathers) {
        this.hourlyWeathers = hourlyWeathers;
    }

    public List<DailyWeather> getDailyWeathers() {
        return dailyWeathers;
    }

    public void setDailyWeathers(List<DailyWeather> dailyWeathers) {
        this.dailyWeathers = dailyWeathers;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Location location = (Location) o;
        return Objects.equals(code, location.code);
    }

    public RealtimeWeather getRealtimeWeather() {
        return realtimeWeather;
    }

    public void setRealtimeWeather(RealtimeWeather realtimeWeather) {
        this.realtimeWeather = realtimeWeather;
    }

    @Override
    public int hashCode() {
        return Objects.hash(code);
    }

    @Override
    public String toString() {
        return cityName + ", " + (regionName != null ? regionName + ", " : "") + countryName;
    }
}
