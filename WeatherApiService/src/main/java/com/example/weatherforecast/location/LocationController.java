package com.example.weatherforecast.location;

import com.example.weatherforecast.Location;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/locations")
@Validated
public class LocationController {
    private final LocationService locationService;
    private final ModelMapper modelMapper;
    @Autowired
    public LocationController(LocationService locationService, ModelMapper modelMapper) {
        this.locationService = locationService;
        this.modelMapper = modelMapper;
    }

    @PostMapping
    public ResponseEntity<LocationDTO> addLocation(@RequestBody @Valid Location location) throws URISyntaxException {
        Location savedLocation = locationService.addLocation(location);
        URI uri = new URI("/v1/locations/" + savedLocation.getCode());
        return ResponseEntity.created(uri).body(entity2DTO(location));
    }

    @GetMapping
    public ResponseEntity<?> listUntrashLocation() {
        var locations = locationService.listUntrashLocation();
        if(locations.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(listEntity2listDTO(locations));
    }

    @GetMapping("/{code}")
    public ResponseEntity<?> getLocationByCode(@PathVariable String code) {
        Location location = locationService.getLocationByCode(code);
        return ResponseEntity.ok(entity2DTO(location));
    }

    @PutMapping
    public ResponseEntity<?> updateLocation(@RequestBody @Valid Location location) {
        var updatedLocation = locationService.updateLocation(location);
        return ResponseEntity.ok(entity2DTO(updatedLocation));
    }

    @DeleteMapping("/{code}")
    public ResponseEntity<?> deleteLocation(@PathVariable String code) {
        locationService.deleteLocation(code);
        return ResponseEntity.noContent().build();
    }

    private LocationDTO entity2DTO(Location location) {
        return modelMapper.map(location, LocationDTO.class);
    }

    private List<LocationDTO> listEntity2listDTO(List<Location> locations) {
        return locations.stream().map(location -> modelMapper.map(location, LocationDTO.class))
                .collect(Collectors.toList());
    }
}
