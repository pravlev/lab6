package com.lev_prav.common.data;

import com.opencsv.bean.CsvBindByName;

import java.io.Serializable;
import java.util.Objects;

public class Location implements Serializable {
    @CsvBindByName
    private Double locationX; //Поле не может быть null
    @CsvBindByName
    private Integer locationY; //Поле не может быть null
    @CsvBindByName
    private Float locationZ; //Поле не может быть null
    @CsvBindByName
    private String locationName; //Поле может быть null

    public Location(Double x, Integer y, Float z, String name) {
        this.locationX = x;
        this.locationY = y;
        this.locationZ = z;
        this.locationName = name;
    }

    public Location() {
    }

    public Double getLocationX() {
        return locationX;
    }

    public void setLocationX(Double locationX) {
        this.locationX = locationX;
    }

    public Integer getLocationY() {
        return locationY;
    }

    public void setLocationY(Integer locationY) {
        this.locationY = locationY;
    }

    public Float getLocationZ() {
        return locationZ;
    }

    public void setLocationZ(Float locationZ) {
        this.locationZ = locationZ;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Location location = (Location) o;
        return locationX.equals(location.locationX) && locationY.equals(location.locationY) && locationZ.equals(location.locationZ) && locationName.equals(location.locationName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(locationX, locationY, locationZ, locationName);
    }

    @Override
    public String toString() {
        return "Location{"
                + "x=" + locationX
                + ", y=" + locationY
                + ", z=" + locationZ
                + ", name='" + locationName + '\''
                + '}';
    }
}
