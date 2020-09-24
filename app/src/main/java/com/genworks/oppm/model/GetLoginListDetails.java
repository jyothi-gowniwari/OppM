package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetLoginListDetails {

    @SerializedName("session")
    @Expose
    private String session;
    @SerializedName("userid")
    @Expose
    private String userid;
    @SerializedName("vtiger_version")
    @Expose
    private String vtiger_version;
    @SerializedName("mobile_module_version")
    @Expose
    private String mobile_module_version;

    @SerializedName("first_name")
    @Expose
    private String first_name;

    @SerializedName("last_name")
    @Expose
    private String last_name;

    @SerializedName("mobile")
    @Expose
    private String mobile;

    @SerializedName("role")
    @Expose
    private String role;

    @SerializedName("reportto")
    @Expose
    private String reportto;

    public String getFirst_name() {
        return first_name;
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getReportto() {
        return reportto;
    }

    public void setReportto(String reportto) {
        this.reportto = reportto;
    }

    public String getSession() {
        return session;
    }

    public void setSession(String session) {
        this.session = session;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getVtiger_version() {
        return vtiger_version;
    }

    public void setVtiger_version(String vtiger_version) {
        this.vtiger_version = vtiger_version;
    }

    public String getMobile_module_version() {
        return mobile_module_version;
    }

    public void setMobile_module_version(String mobile_module_version) {
        this.mobile_module_version = mobile_module_version;
    }

}
