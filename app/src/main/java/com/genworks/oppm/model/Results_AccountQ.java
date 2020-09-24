package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Results_AccountQ {


    public ArrayList<RecordsAccounts> getRecordsAccounts() {
        return recordsAccounts;
    }

    public void setRecordsAccounts(ArrayList<RecordsAccounts> recordsAccounts) {
        this.recordsAccounts = recordsAccounts;
    }

    @SerializedName("records")
    @Expose
    private ArrayList<RecordsAccounts> recordsAccounts;


}
