package com.genworks.oppm.model;

import com.google.gson.annotations.SerializedName;

public class AccountDescribe {

    @SerializedName("success")
    private String success;

    @SerializedName("result")
    private Results_Account result;

    public Results_Account getResult() {
        return result;
    }

    public void setResult(Results_Account result) {
        this.result = result;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
