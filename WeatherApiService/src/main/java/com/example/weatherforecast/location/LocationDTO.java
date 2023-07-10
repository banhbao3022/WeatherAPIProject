package com.example.weatherforecast.location;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonPropertyOrder({"code", "city_name", "region_name", "country_code", "country_name", "enable"})
public class LocationDTO {
    private String code;
    @JsonProperty(value = "city_name")
    private String cityName;
    @JsonProperty(value = "region_name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String regionName;
    @JsonProperty(value = "country_name")
    private String countryName;

    @JsonProperty(value = "country_code")
    private String countryCode;
    private boolean enable;

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

    public boolean isEnable() {
        return enable;
    }

    public void setEnable(boolean enable) {
        this.enable = enable;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }
}
