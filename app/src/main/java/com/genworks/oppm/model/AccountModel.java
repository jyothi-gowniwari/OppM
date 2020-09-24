package com.genworks.oppm.model;

public class AccountModel {

    private String accountName;
    private String ownershipType;
    private String facilityType;
    private String email;
    private String phone;
    private String assigned;
    private String bill_street;
    private String bill_city;
    private String createdtime;
    private String modifiedtime;
    private String modifiedby;
    private String account_no;
    private String bill_country;
    private String bill_pin;
    private String account_id;


    public AccountModel() {
    }

    public String getAccountName() {
        return accountName;
    }

    public void setAccountName(String accountName) {
        this.accountName = accountName;
    }

    public String getOwnershipType() {
        return ownershipType;
    }

    public void setOwnershipType(String ownershipType) {
        this.ownershipType = ownershipType;
    }

    public String getFacilityType() {
        return facilityType;
    }

    public void setFacilityType(String facilityType) {
        this.facilityType = facilityType;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getBill_street() {
        return bill_street;
    }

    public void setBill_street(String bill_street) {
        this.bill_street = bill_street;
    }

    public String getBill_city() {
        return bill_city;
    }

    public void setBill_city(String bill_city) {
        this.bill_city = bill_city;
    }

    public String getBillingDistrict() {
        return billingDistrict;
    }

    public void setBillingDistrict(String billingDistrict) {
        this.billingDistrict = billingDistrict;
    }

    public String getBillingState() {
        return billingState;
    }

    public void setBillingState(String billingState) {
        this.billingState = billingState;
    }

    public String getBillingPIN() {
        return billingPIN;
    }

    public void setBillingPIN(String billingPIN) {
        this.billingPIN = billingPIN;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    public String getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(String modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public String getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
    }

    public String getAccount_no() {
        return account_no;
    }

    public void setAccount_no(String account_no) {
        this.account_no = account_no;
    }

    public String getBill_country() {
        return bill_country;
    }

    public void setBill_country(String bill_country) {
        this.bill_country = bill_country;
    }

    public String getBill_pin() {
        return bill_pin;
    }

    public void setBill_pin(String bill_pin) {
        this.bill_pin = bill_pin;
    }

    public String getAccount_id() {
        return account_id;
    }

    public void setAccount_id(String account_id) {
        this.account_id = account_id;
    }

    public AccountModel(String accountName, String ownershipType, String facilityType, String email, String phone, String assigned, String bill_street, String bill_city, String billingDistrict, String billingState, String account_no, String createdtime, String modifiedtime, String modifiedby, String bill_country, String bill_pin, String account_id) {
        this.accountName = accountName;
        this.ownershipType = ownershipType;
        this.facilityType = facilityType;
        this.email = email;
        this.phone = phone;
        this.assigned = assigned;
        this.bill_street = bill_street;
        this.bill_city = bill_city;
        this.billingDistrict = billingDistrict;
        this.billingState = billingState;
        this.billingPIN = bill_pin;
        this.account_no=account_no;
        this.createdtime=createdtime;
        this.modifiedtime=modifiedtime;
        this.modifiedby=modifiedby;
        this.bill_country=bill_country;
        this.account_id=account_id;
    }

    private String billingDistrict;
    private String billingState;
    private String billingPIN;



}
