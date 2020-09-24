package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Results_OpportunityQ {
    @SerializedName("records")
    @Expose
    private ArrayList<RecordsOpportunity> recordsOpportunities;

    public ArrayList<RecordsOpportunity> getRecordsOpportunities() {
        return recordsOpportunities;
    }

    public void setRecordsOpportunities(ArrayList<RecordsOpportunity> recordsOpportunities) {
        this.recordsOpportunities = recordsOpportunities;
    }
}
