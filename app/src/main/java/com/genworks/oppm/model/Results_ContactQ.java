package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Results_ContactQ {

    @SerializedName("records")
    @Expose
    private ArrayList<RecordsContacts> recordsContacts;

    public ArrayList<RecordsContacts> getRecordsContacts() {
        return recordsContacts;
    }

    public void setRecordsContacts(ArrayList<RecordsContacts> recordsContacts) {
        this.recordsContacts = recordsContacts;
    }
}
