package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLoginList {

    @SerializedName("login")
    @Expose
    private GetLoginListDetails login;

    public GetLoginListDetails getLogin() {
        return login;
    }

    public void setLogin(GetLoginListDetails login) {
        this.login = login;
    }


}
