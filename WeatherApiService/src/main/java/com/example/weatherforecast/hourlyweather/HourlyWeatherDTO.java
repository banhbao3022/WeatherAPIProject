package com.example.weatherforecast.hourlyweather;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.io.Serializable;

@JsonPropertyOrder({"hour_of_day", "temperature", "precipitation", "status"})
public class HourlyWeatherDTO implements Serializable {
    @JsonProperty("hour_of_day")
    @Range(min = 0, max = 24, message = "Hour of day must be in range of 0 to 24 hour")
    private int hourOfDay;
    @Range(min = -50, max = 50, message = "Temperature must be in range of -50 to 50 ℃")
    private int temperature;
    @Range(min = 0, max = 100, message = "Precipitation must be in range of 0 to 100 percentage")
    private int precipitation;
    @NotNull(message = "Status must not be null")
    @Length(min = 3, max = 50, message = "Status must be in between 3-50 characters")
    private String status;

    public HourlyWeatherDTO() {
    }
    public int getHourOfDay() {
        return hourOfDay;
    }

    public void setHourOfDay(int hourOfDay) {
        this.hourOfDay = hourOfDay;
    }

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
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
    public String toString() {
        return "HourlyWeatherDTO{" +
                "hourOfDay=" + hourOfDay +
                ", temperature=" + temperature +
                ", precipitation=" + precipitation +
                ", status='" + status + '\'' +
                '}';
    }
}
