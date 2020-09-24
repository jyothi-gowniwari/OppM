package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserContactQuery {
    @SerializedName("success")
    @Expose
    private String success;
    @SerializedName("result")
    @Expose
    private Results_ContactQ result;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Results_ContactQ getResult() {
        return result;
    }

    public void setResult(Results_ContactQ result) {
        this.result = result;
    }
}
