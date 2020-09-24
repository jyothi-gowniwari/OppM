package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecordsAccounts {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("accountname")
    @Expose
    private String accountname;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public RecordsAccounts(String id, String accountname) {

        this.id = id;
        this.accountname = accountname;
    }
}
