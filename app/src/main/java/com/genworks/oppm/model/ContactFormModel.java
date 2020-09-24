package com.genworks.oppm.model;

public class ContactFormModel {

    private String salutationType;
    private String specialization;
    private String jobTitle;
    private String ContactType;
    private String createdBy;


    public String getSalutationType() {
        return salutationType;
    }

    public void setSalutationType(String salutationType) {
        this.salutationType = salutationType;
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
        return ContactType;
    }

    public void setContactType(String contactType) {
        ContactType = contactType;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public ContactFormModel(String salutationType, String specialization, String jobTitle, String contactType, String createdBy) {
        this.salutationType = salutationType;
        this.specialization = specialization;
        this.jobTitle = jobTitle;
        ContactType = contactType;
        this.createdBy = createdBy;
    }
}
