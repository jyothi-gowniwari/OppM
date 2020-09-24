package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RecordsProducts {

    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("productname")
    @Expose
    private String productname;


    public String getId() {
        return id;
    }

    public RecordsProducts(String id, String productname) {
        this.id = id;
        this.productname = productname;

    }

    public void setId(String id) {
        this.id = id;
    }

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }
}
