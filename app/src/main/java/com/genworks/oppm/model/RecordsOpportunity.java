package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecordsOpportunity {
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPotentialname() {
        return potentialname;
    }

    public RecordsOpportunity(String id, String potentialname) {
        this.id = id;
        this.potentialname = potentialname;
    }

    public void setPotentialname(String potentialname) {
        this.potentialname = potentialname;
    }

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("potentialname")
    @Expose
    private String potentialname;
}
