package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationRecords {

    @SerializedName("locationno")
    @Expose
    private String locationno;
    @SerializedName("location_tks_district")
    @Expose
    private String location_tks_district;

    public LocationRecords(String locationno, String location_tks_district, String location_tks_circle, String id) {
        this.locationno = locationno;
        this.location_tks_district = location_tks_district;
        this.location_tks_circle = location_tks_circle;
        this.id = id;
    }

    @SerializedName("location_tks_circle")
    @Expose
    private String location_tks_circle;
    @SerializedName("id")
    @Expose
    private String id;

    public String getLocationno() {
        return locationno;
    }

    public void setLocationno(String locationno) {
        this.locationno = locationno;
    }

    public String getLocation_tks_district() {
        return location_tks_district;
    }

    public void setLocation_tks_district(String location_tks_district) {
        this.location_tks_district = location_tks_district;
    }

    public String getLocation_tks_circle() {
        return location_tks_circle;
    }

    public void setLocation_tks_circle(String location_tks_circle) {
        this.location_tks_circle = location_tks_circle;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
