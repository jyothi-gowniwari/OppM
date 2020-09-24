package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SyncResults {
    public Sync getSync() {
        return sync;
    }

    public void setSync(Sync sync) {
        this.sync = sync;
    }

    @SerializedName("sync")
    @Expose
    private Sync sync;
}
