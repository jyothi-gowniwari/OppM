package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserModule {
    @SerializedName("success")
    @Expose
    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Results_Users getResult() {
        return result;
    }

    public void setResult(Results_Users result) {
        this.result = result;
    }

    @SerializedName("result")
    @Expose
    private Results_Users result;

}
