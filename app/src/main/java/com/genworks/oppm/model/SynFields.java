package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class SynFields {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("value")
    @Expose
    private Object value;

    public String getHdnGrandTotal() {
        return hdnGrandTotal;
    }

    public void setHdnGrandTotal(String hdnGrandTotal) {
        this.hdnGrandTotal = hdnGrandTotal;
    }

    @SerializedName("hdnGrandTotal")
    @Expose
    private String hdnGrandTotal;
    @SerializedName("quantity")
    @Expose
    private String quantity;

    public String getQuantity() {
        return quantity;
    }

    public void setQuantity(String quantity) {
        this.quantity = quantity;
    }

    public String getListprice() {
        return listprice;
    }

    public void setListprice(String listprice) {
        this.listprice = listprice;
    }

    @SerializedName("listprice")
    @Expose
    private String listprice;

    public String getPre_tax_total() {
        return pre_tax_total;
    }

    public void setPre_tax_total(String pre_tax_total) {
        this.pre_tax_total = pre_tax_total;
    }

    @SerializedName("pre_tax_total")
    @Expose
    private String pre_tax_total;



    public Object getValue() {
        return value;
    }

    public void setValue(Object value) {
        this.value = value;
    }

    public SynFields(String name, Object value, String label,String hdnGrandTotal,String pre_tax_total,String quantity,String listprice) {
        this.name = name;
        this.value = value;
        this.label = label;
        this.hdnGrandTotal=hdnGrandTotal;
        this.pre_tax_total=pre_tax_total;
        this.quantity=quantity;
        this.listprice=listprice;
    }

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

    @SerializedName("label")
    @Expose
    private String label;


}
