package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SyncModule {

    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("result")
    @Expose
    private SyncResults result;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public SyncResults getResult() {
        return result;
    }

    public void setResult(SyncResults result) {
        this.result = result;
    }
}
