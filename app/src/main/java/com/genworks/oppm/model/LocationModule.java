package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class LocationModule {
    @SerializedName("success")
    @Expose
    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }


    public Results_Locations getResult() {
        return result;
    }

    public void setResult(Results_Locations result) {
        this.result = result;
    }

    @SerializedName("result")
    @Expose
    private Results_Locations result;

}
