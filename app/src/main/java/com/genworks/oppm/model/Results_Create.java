package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Results_Create {

    @SerializedName("sync")
    @Expose
    private SyncCreate sync;

    public SyncCreate getSync() {
        return sync;
    }

    public void setSync(SyncCreate sync) {
        this.sync = sync;
    }
}
