package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class ClosedwonResults {

    @SerializedName("pan")
    @Expose
    private PanModel pan;

    @SerializedName("gst")
    @Expose
    private GSTModel gst;

    @SerializedName("purchase_order")
    @Expose
    private Purchase_orderModel purchase_order;

    public GSTModel getGst() {
        return gst;
    }

    public void setGst(GSTModel gst) {
        this.gst = gst;
    }

    @SerializedName("pndt")
    @Expose
    private PndtModel pndt;

    @SerializedName("adhar")
    @Expose
    private AdharModel adhar;

    public AdharModel getAdhar() {
        return adhar;
    }

    public void setAdhar(AdharModel adhar) {
        this.adhar = adhar;
    }

    public PanModel getPan() {
        return pan;
    }

    public void setPan(PanModel pan) {
        this.pan = pan;
    }

    public PndtModel getPndt() {
        return pndt;
    }

    public void setPndt(PndtModel pndt) {
        this.pndt = pndt;
    }

    public Purchase_orderModel getPurchase_order() {
        return purchase_order;
    }

    public void setPurchase_order(Purchase_orderModel purchase_order) {
        this.purchase_order = purchase_order;
    }

    public ChecqueModel getChecque() {
        return checque;
    }

    public void setChecque(ChecqueModel checque) {
        this.checque = checque;
    }

    public Doc_oneModel getDoc_one() {
        return doc_one;
    }

    public void setDoc_one(Doc_oneModel doc_one) {
        this.doc_one = doc_one;
    }

    public Doc_twoModel getDoc_two() {
        return doc_two;
    }

    public void setDoc_two(Doc_twoModel doc_two) {
        this.doc_two = doc_two;
    }

    @SerializedName("checque")
    @Expose
    private ChecqueModel checque;
    @SerializedName("doc_one")
    @Expose
    private Doc_oneModel doc_one;

    @SerializedName("doc_two")
    @Expose
    private Doc_twoModel doc_two;
}
