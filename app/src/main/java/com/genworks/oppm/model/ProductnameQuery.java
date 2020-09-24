package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ProductnameQuery {


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
        private Results_ProductnameQ result;

    public Results_ProductnameQ getResult() {
        return result;
    }

    public void setResult(Results_ProductnameQ result) {
        this.result = result;
    }
}
