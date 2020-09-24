package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SyncCreate {
    public ArrayList<SyncUpdatedCreate> getUpdated() {
        return updated;
    }

    public void setUpdated(ArrayList<SyncUpdatedCreate> updated) {
        this.updated = updated;
    }

    @SerializedName("updated")
    @Expose
    private ArrayList<SyncUpdatedCreate> updated;


}
