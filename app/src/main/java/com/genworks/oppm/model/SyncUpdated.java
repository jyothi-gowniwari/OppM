package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SyncUpdated {

    @SerializedName("blocks")
    @Expose
    private ArrayList<SyncBlocks> blocks;

    public ArrayList<SyncBlocks> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<SyncBlocks> blocks) {
        this.blocks = blocks;
    }

    @SerializedName("id")
    @Expose
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
