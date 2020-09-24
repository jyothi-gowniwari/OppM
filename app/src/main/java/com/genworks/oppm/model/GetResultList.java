package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetResultList {

    public GetLoginList getLogin() {
        return login;
    }

    public void setLogin(GetLoginList login) {
        this.login = login;
    }

    @SerializedName("login")
    @Expose
    private GetLoginList login;


}
