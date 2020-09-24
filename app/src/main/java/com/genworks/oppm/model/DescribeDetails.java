package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class DescribeDetails {

    @SerializedName("label")
    @Expose
    private String label;
    @SerializedName("name")
    @Expose
    private String name;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCreateable() {
        return createable;
    }

    public void setCreateable(String createable) {
        this.createable = createable;
    }

    public String getUpdateable() {
        return updateable;
    }

    public void setUpdateable(String updateable) {
        this.updateable = updateable;
    }

    public String getDeleteable() {
        return deleteable;
    }

    public void setDeleteable(String deleteable) {
        this.deleteable = deleteable;
    }

    public String getRetrieveable() {
        return retrieveable;
    }

    public void setRetrieveable(String retrieveable) {
        this.retrieveable = retrieveable;
    }

    @SerializedName("createable")
    @Expose
    private String createable;
    @SerializedName("updateable")
    @Expose
    private String updateable;
    @SerializedName("deleteable")
    @Expose
    private String deleteable;
    @SerializedName("retrieveable")
    @Expose
    private String retrieveable;

    @SerializedName("idPrefix")
    @Expose
    private String idPrefix;
    @SerializedName("isEntity")
    @Expose
    private String isEntity;
    @SerializedName("allowDuplicates")
    @Expose
    private String allowDuplicates;

    public String getIdPrefix() {
        return idPrefix;
    }

    public void setIdPrefix(String idPrefix) {
        this.idPrefix = idPrefix;
    }

    public String getIsEntity() {
        return isEntity;
    }

    public void setIsEntity(String isEntity) {
        this.isEntity = isEntity;
    }

    public String getAllowDuplicates() {
        return allowDuplicates;
    }

    public void setAllowDuplicates(String allowDuplicates) {
        this.allowDuplicates = allowDuplicates;
    }

    public String getLabelFields() {
        return labelFields;
    }

    public void setLabelFields(String labelFields) {
        this.labelFields = labelFields;
    }

    @SerializedName("labelFields")
    @Expose
    private String labelFields;



    @SerializedName("fields")
    @Expose
    private ArrayList<Fields> fields;

    public ArrayList<Fields> getFields() {
        return fields;
    }

    public void setFields(ArrayList<Fields> fields) {
        this.fields = fields;
    }
}
