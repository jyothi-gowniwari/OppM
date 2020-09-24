package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Results_ProductnameQ {

    @SerializedName("records")
    @Expose
    private ArrayList<RecordsProducts> recordsProducts;

    public ArrayList<RecordsProducts> getRecordsProducts() {
        return recordsProducts;
    }

    public void setRecordsProducts(ArrayList<RecordsProducts> recordsProducts) {
        this.recordsProducts = recordsProducts;
    }
}
