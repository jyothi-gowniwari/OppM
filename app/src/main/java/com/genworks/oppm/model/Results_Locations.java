package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Results_Locations {


    @SerializedName("records")
    @Expose
    private ArrayList<LocationRecords> records;


    public ArrayList<LocationRecords> getRecords() {
        return records;
    }

    public void setRecords(ArrayList<LocationRecords> records) {
        this.records = records;
    }
}
