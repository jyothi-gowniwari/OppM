package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserOpportunityQuery {
    @SerializedName("success")
    @Expose
    private String success;

    @SerializedName("result")
    @Expose
    private Results_OpportunityQ result;

    public String getSuccess() {
        return success;
    }

    public void setSuccess(String success) {
        this.success = success;
    }

    public Results_OpportunityQ getResult() {
        return result;
    }

    public void setResult(Results_OpportunityQ result) {
        this.result = result;
    }
}
