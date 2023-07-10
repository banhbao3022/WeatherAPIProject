package com.example.weatherforecast.hourlyweather;

public class BadRequestException extends Exception {
    public BadRequestException(String message) {
        super(message);
    }
}
