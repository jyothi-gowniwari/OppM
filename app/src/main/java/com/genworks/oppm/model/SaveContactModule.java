package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SaveContactModule {


        @SerializedName("success")
        @Expose
        private String success;

        public String getSuccess() {
            return success;
        }

        public void setSuccess(String success) {
            this.success = success;
        }



        @SerializedName("result")
        @Expose
        private Results_Create result;

    public Results_Create getResult() {
        return result;
    }

    public void setResult(Results_Create result) {
        this.result = result;
    }
}
