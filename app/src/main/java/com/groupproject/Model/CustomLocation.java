package com.groupproject.Model;


public class CustomLocation {


    private Double latitude;
    private Double longitude;

    public CustomLocation() {}

    public CustomLocation(Double latitude, Double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Double getLatitude() {
        return latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

}