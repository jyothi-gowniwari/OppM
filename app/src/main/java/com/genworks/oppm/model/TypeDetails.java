package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class TypeDetails {

    @SerializedName("name")
    @Expose
    private String name;



    @SerializedName("users")
    @Expose
    private Users users;

    public Users getUsers() {
        return users;
    }

    public void setUsers(Users users) {
        this.users = users;
    }

    @SerializedName("picklistValues")
    @Expose
    private ArrayList<PickListValues> picklistValues;

    public ArrayList<PickListValues> getPicklistValues() {
        return picklistValues;
    }

    public void setPicklistValues(ArrayList<PickListValues> picklistValues) {
        this.picklistValues = picklistValues;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
