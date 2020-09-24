package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class SyncBlocksCreate {


    @SerializedName("blocks")
    @Expose
    private ArrayList<SyncBlocks> blocks;

    public ArrayList<SyncBlocks> getBlocks() {
        return blocks;
    }

    public void setBlocks(ArrayList<SyncBlocks> blocks) {
        this.blocks = blocks;
    }

    @SerializedName("label")
    @Expose
    private String label;

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }
}
