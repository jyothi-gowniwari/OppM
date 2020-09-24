package com.genworks.oppm.model;

import com.google.gson.annotations.SerializedName;

public class Login {

    @SerializedName("success")
    private String success;
    @SerializedName("error")
    private Error error;

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public GetLoginList getResult() {
        return result;
    }

    public void setResult(GetLoginList result) {
        this.result = result;
    }

    @SerializedName("result")
    private GetLoginList result;
}
