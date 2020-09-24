package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GSTModel {

    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public GSTModel(String id, String filename, String path) {
        this.id = id;
        this.filename = filename;
        this.path = path;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    @SerializedName("filename")
    @Expose
    private String filename;
    @SerializedName("path")
    @Expose
    private String path;
}

