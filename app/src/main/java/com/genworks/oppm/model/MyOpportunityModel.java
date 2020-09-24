package com.genworks.oppm.model;

public class MyOpportunityModel {

    private String date_start;
    private String sales_stage;
    private String support_required;
    private String win_prob;
    private String modality;
    private String modifiedtime;

    public String getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(String modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public String getDate_start() {
        return date_start;
    }

    public MyOpportunityModel(String date_start, String sales_stage, String support_required, String win_prob, String modality,String modifiedtime) {
        this.date_start = date_start;
        this.sales_stage = sales_stage;
        this.support_required = support_required;
        this.win_prob = win_prob;
        this.modality = modality;
        this.modifiedtime=modifiedtime;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getSales_stage() {
        return sales_stage;
    }

    public void setSales_stage(String sales_stage) {
        this.sales_stage = sales_stage;
    }

    public String getSupport_required() {
        return support_required;
    }

    public void setSupport_required(String support_required) {
        this.support_required = support_required;
    }

    public String getWin_prob() {
        return win_prob;
    }

    public void setWin_prob(String win_prob) {
        this.win_prob = win_prob;
    }

    public String getModality() {
        return modality;
    }

    public void setModality(String modality) {
        this.modality = modality;
    }
}
