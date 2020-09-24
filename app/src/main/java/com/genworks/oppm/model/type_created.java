package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class type_created {

    @SerializedName("defaultValue")
    @Expose
    private defaultValueCreated defaultValue;

    public defaultValueCreated getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(defaultValueCreated defaultValue) {
        this.defaultValue = defaultValue;
    }
}
