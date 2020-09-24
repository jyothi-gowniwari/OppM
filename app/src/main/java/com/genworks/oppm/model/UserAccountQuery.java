package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserAccountQuery {

    @SerializedName("success")
    @Expose
    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }


    public Results_AccountQ getResult() {
        return result;
    }

    public void setResult(Results_AccountQ result) {
        this.result = result;
    }

    @SerializedName("result")
    @Expose
    private Results_AccountQ result;
}
