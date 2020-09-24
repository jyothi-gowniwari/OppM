package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class DocumentModel {
    @SerializedName("success")
    @Expose
    private String success;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public ClosedwonResults getResult() {
        return result;
    }

    public void setResult(ClosedwonResults result) {
        this.result = result;
    }

    @SerializedName("result")
    @Expose
    private ClosedwonResults result;
}
