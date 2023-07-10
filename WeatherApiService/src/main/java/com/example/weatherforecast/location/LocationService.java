package com.example.weatherforecast.location;

import com.example.weatherforecast.Location;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class LocationService {
    private final LocationRepository locationRepository;

    @Autowired
    public LocationService(LocationRepository locationRepository) {
        this.locationRepository = locationRepository;
    }

    public Location addLocation(Location location) {
        return locationRepository.save(location);
    }

    public List<Location> listUntrashLocation() {
        return locationRepository.listUntrashLocations();
    }

    public Location getLocationByCode(String code) {
        Location location = locationRepository.getLocationByCode(code);
        if(location == null) throw new LocationNotFoundException(code);
        return location;
    }

    public Location updateLocation(Location updatedLocation) {
        var location = getLocationByCode(updatedLocation.getCode());

        location.setCityName(updatedLocation.getCityName());
        location.setRegionName(updatedLocation.getRegionName());
        location.setCountryName(updatedLocation.getCountryName());
        location.setCountryCode(updatedLocation.getCountryCode());
        location.setEnable(updatedLocation.isEnable());

        return locationRepository.save(location);
    }

    public void deleteLocation(String code) {
        getLocationByCode(code);
        locationRepository.trashByCode(code);
    }
}
