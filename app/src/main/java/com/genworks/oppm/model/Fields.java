package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Fields {

    @SerializedName("name")
    @Expose
    private String name;

    @SerializedName("label")
    @Expose
    private String label;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public String getMandatory() {
        return mandatory;
    }

    public void setMandatory(String mandatory) {
        this.mandatory = mandatory;
    }



    @SerializedName("mandatory")
    @Expose
    private String mandatory;

    @SerializedName("type")
    @Expose
    private TypeDetails type;

    public TypeDetails getType() {
        return type;
    }

    public void setType(TypeDetails type) {
        this.type = type;
    }

    @SerializedName("isunique")
    @Expose
    private String isunique;

    @SerializedName("nullable")
    @Expose
    private String nullable;

    @SerializedName("editable")
    @Expose
    private String editable;

    public String getIsunique() {
        return isunique;
    }

    public void setIsunique(String isunique) {
        this.isunique = isunique;
    }

    public String getNullable() {
        return nullable;
    }

    public void setNullable(String nullable) {
        this.nullable = nullable;
    }

    public String getEditable() {
        return editable;
    }

    public void setEditable(String editable) {
        this.editable = editable;
    }

    public String getSummaryfield() {
        return summaryfield;
    }

    public void setSummaryfield(String summaryfield) {
        this.summaryfield = summaryfield;
    }

    public String getDefault1() {
        return default1;
    }

    public void setDefault1(String default1) {
        this.default1 = default1;
    }

    public String getHeaderfield() {
        return headerfield;
    }

    public void setHeaderfield(String headerfield) {
        this.headerfield = headerfield;
    }

    @SerializedName("summaryfield")
    @Expose
    private String summaryfield;

    @SerializedName("default")
    @Expose
    private String default1;

    @SerializedName("headerfield")
    @Expose
    private String headerfield;




}
