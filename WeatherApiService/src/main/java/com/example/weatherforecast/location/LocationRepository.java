package com.example.weatherforecast.location;


import com.example.weatherforecast.Location;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LocationRepository extends CrudRepository<Location, String> {
    @Query("select l from Location l where l.trashed = false")
    List<Location> listUntrashLocations();

    @Query("select l from Location l where l.trashed = false and l.code = ?1")
    Location getLocationByCode(String code);

    @Modifying
    @Query("update Location l set l.trashed = true where l.code = ?1")
    void trashByCode(String code);
    @Query("select l from Location l where l.countryCode = ?1 and l.cityName = ?2 and l.trashed = false")
    Location findByCountryCodeAndAndCityName(String countryCode, String cityName);
}
