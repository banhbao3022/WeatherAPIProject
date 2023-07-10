package com.example.weatherforecast.geolocation;

import com.example.weatherforecast.Location;
import com.ip2location.IP2Location;
import com.ip2location.IPResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;

@Service
public class GeolocationService {
    private static final Logger logger = LoggerFactory.getLogger(GeolocationService.class);
    private final String DB_PATH = "/ip2location/IP2LOCATION-LITE-DB3.BIN";
    private final IP2Location location = new IP2Location();
    public GeolocationService() {
        try {
            InputStream inputStream = getClass().getResourceAsStream(DB_PATH);
            byte[] data = inputStream.readAllBytes();
            location.Open(data);
            inputStream.close();
        } catch (IOException | NullPointerException e) {
            logger.error(e.getMessage(), e);
        }
    }
    public Location getLocation(String ipAddress) throws GeolocationException {
        try {
            IPResult result = location.IPQuery(ipAddress);
            if (!result.getStatus().equals("OK")) {
                throw new GeolocationException("Geolocation failed with status: " + result.getStatus());
            }
            return new Location(result.getCity(), result.getRegion(), result.getCountryLong(), result.getCountryShort());
        } catch (IOException e) {
            throw new GeolocationException("Error querying IP database", e);
        }
    }
}
