package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SyncBlocks {

    @SerializedName("label")
    @Expose
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ArrayList<SynFields> getFields() {
        return fields;
    }

    public void setFields(ArrayList<SynFields> fields) {
        this.fields = fields;
    }

    @SerializedName("fields")
    @Expose
    private ArrayList<SynFields> fields;


}
