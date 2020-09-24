package com.genworks.oppm.model;

import com.google.gson.annotations.SerializedName;

public class LoginAndFetchModules {

    @SerializedName("success")
    private String success;



    @SerializedName("result")
    private Results result;

    public Results getResult() {
        return result;
    }

    public void setResult(Results result) {
        this.result = result;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }
}
