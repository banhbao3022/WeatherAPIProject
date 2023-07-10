package com.example.weatherforecast;

import jakarta.persistence.*;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DailyWeatherId implements Serializable {
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_code")
    private Location location;
    private int dayOfMonth;
    private int month;

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        DailyWeatherId that = (DailyWeatherId) o;
        return dayOfMonth == that.dayOfMonth && month == that.month && Objects.equals(location, that.location);
    }

    @Override
    public int hashCode() {
        return Objects.hash(location, dayOfMonth, month);
    }
}
