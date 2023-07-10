package com.example.weatherforecast.location;

public class LocationNotFoundException extends RuntimeException{
    public LocationNotFoundException(String locationCode) {
        super("No location found with the given location code: " + locationCode);
    }
    public LocationNotFoundException(String cityName, String countryName) {
        super("No location found with the given city name - " + cityName + " and country name - " + countryName);
    }
}
