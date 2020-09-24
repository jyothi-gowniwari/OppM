package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Values {

    @SerializedName("value")
    @Expose
    private String value;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    @SerializedName("label")
    @Expose
    private String label;

}
