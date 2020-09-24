package com.genworks.oppm.model;

import com.google.gson.JsonElement;

public class Users {

   private String userid;
   private String username;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public Users(String userid, JsonElement username) {
        this.userid = userid;
        this.username = String.valueOf(username);
    }

    public void setUsername(String username) {
        this.username = username;
    }
}

