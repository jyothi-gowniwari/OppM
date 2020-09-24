package com.genworks.oppm.model;

public class ContactModel {

    private String salutationtype;
    private String firstname;
    private String lastname;
    private String contactsource;
    private String createdtime;
    private String modifiedtime;
    private String modifiedby;
    private String comment;
    private String assigned;
    private String contactid;
    private String createdby;
    private String contact_id;

    public String getContact_id() {
        return contact_id;
    }

    public void setContact_id(String contact_id) {
        this.contact_id = contact_id;
    }

    public String getCreatedby() {
        return createdby;
    }

    public void setCreatedby(String createdby) {
        this.createdby = createdby;
    }

    public String getContactsource() {
        return contactsource;
    }

    public void setContactsource(String contactsource) {
        this.contactsource = contactsource;
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

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getSalutationtype() {
        return salutationtype;
    }

    public void setSalutationtype(String salutationtype) {
        this.salutationtype = salutationtype;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getAccounName() {
        return accounName;
    }

    public void setAccounName(String accounName) {
        this.accounName = accounName;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public String getContactType() {
        return contactType;
    }

    public void setContactType(String contactType) {
        this.contactType = contactType;
    }

    public String getEmailID() {
        return emailID;
    }

    public void setEmailID(String emailID) {
        this.emailID = emailID;
    }

    public String getMobilePhone() {
        return mobilePhone;
    }

    public void setMobilePhone(String mobilePhone) {
        this.mobilePhone = mobilePhone;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getContactid() {
        return contactid;
    }

    public void setContactid(String contactid) {
        this.contactid = contactid;
    }

    public ContactModel(String salutationtype, String firstname, String lastname, String accounName, String specialization, String jobTitle, String contactType, String emailID, String mobilePhone,String createdtime, String contactsource,  String modifiedtime, String modifiedby,String comment, String assigned, String contactid,String createdby,String contact_id) {
        this.salutationtype = salutationtype;
        this.firstname = firstname;
        this.lastname = lastname;
        this.accounName = accounName;
        this.specialization = specialization;
        this.jobTitle = jobTitle;
        this.contactType = contactType;
        this.emailID = emailID;
        this.mobilePhone = mobilePhone;
        this.comment=comment;
        this.contactsource=contactsource;
        this.modifiedby=modifiedby;
        this.modifiedtime=modifiedtime;
        this.createdtime=createdtime;
        this.assigned=assigned;
        this.contactid=contactid;
        this.createdby=createdby;
        this.contact_id=contact_id;
    }

    private String accounName;
    private String specialization;
    private String jobTitle;
    private String contactType;
    private String emailID;
    private String mobilePhone;


}
