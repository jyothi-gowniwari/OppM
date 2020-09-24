package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Results {

    @SerializedName("login")
    @Expose
    private GetLoginListDetails login;


    @SerializedName("modules")
    @Expose
    private ArrayList<LoginListForModules> modules;

    public ArrayList<LoginListForModules> getModules() {
        return modules;
    }

    public void setModules(ArrayList<LoginListForModules> modules) {
        this.modules = modules;
    }

    public GetLoginListDetails getLogin() {
        return login;
    }

    public void setLogin(GetLoginListDetails login) {
        this.login = login;
    }
}
