package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Sync {

    @SerializedName("nextSyncToken")
    @Expose
    private String nextSyncToken;

    @SerializedName("updated")
    @Expose
    private ArrayList<SyncUpdated> updated;

    public String getNextSyncToken() {
        return nextSyncToken;
    }

    public void setNextSyncToken(String nextSyncToken) {
        this.nextSyncToken = nextSyncToken;
    }

    public ArrayList<SyncUpdated> getUpdated() {
        return updated;
    }

    public void setUpdated(ArrayList<SyncUpdated> updated) {
        this.updated = updated;
    }
}
