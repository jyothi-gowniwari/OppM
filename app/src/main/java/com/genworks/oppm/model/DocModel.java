package com.genworks.oppm.model;

public class DocModel {

    private String id;
    private String filename;
    private String path;
    private String doc_oneid;
    private String doc_onefilename;
    private String doc_twofilename;
    private String gstfilename;
    private String purchasefilename;
    private String checquefilename;
    private String adharfilename;
    private String adharpath;

    public String getChecquepath() {
        return checquepath;
    }

    public void setChecquepath(String checquepath) {
        this.checquepath = checquepath;
    }

    private String gstpath;
    private String purchasepath;
    private String doconepath;
    private String doctwopath;
    private String checquepath;

    public String getAdharpath() {
        return adharpath;
    }

    public void setAdharpath(String adharpath) {
        this.adharpath = adharpath;
    }

    public String getGstpath() {
        return gstpath;
    }

    public void setGstpath(String gstpath) {
        this.gstpath = gstpath;
    }

    public String getPurchasepath() {
        return purchasepath;
    }

    public void setPurchasepath(String purchasepath) {
        this.purchasepath = purchasepath;
    }

    public String getDoconepath() {
        return doconepath;
    }

    public void setDoconepath(String doconepath) {
        this.doconepath = doconepath;
    }

    public String getDoctwopath() {
        return doctwopath;
    }

    public void setDoctwopath(String doctwopath) {
        this.doctwopath = doctwopath;
    }

    public String getAdharfilename() {
        return adharfilename;
    }

    public void setAdharfilename(String adharfilename) {
        this.adharfilename = adharfilename;
    }

    public String getPurchasefilename() {
        return purchasefilename;
    }

    public void setPurchasefilename(String purchasefilename) {
        this.purchasefilename = purchasefilename;
    }

    public String getGstfilename() {
        return gstfilename;
    }

    public void setGstfilename(String gstfilename) {
        this.gstfilename = gstfilename;
    }

    public String getDoc_twofilename() {
        return doc_twofilename;
    }

    public void setDoc_twofilename(String doc_twofilename) {
        this.doc_twofilename = doc_twofilename;
    }

    private String doc_onepath;
    private String doc_twoid;

    public String getDoc_twoid() {
        return doc_twoid;
    }

    public void setDoc_twoid(String doc_twoid) {
        this.doc_twoid = doc_twoid;
    }

    public String getDoc_oneid() {
        return doc_oneid;
    }

    public void setDoc_oneid(String doc_oneid) {
        this.doc_oneid = doc_oneid;
    }

    public String getDoc_onefilename() {
        return doc_onefilename;
    }

    public void setDoc_onefilename(String doc_onefilename) {
        this.doc_onefilename = doc_onefilename;
    }

    public String getDoc_onepath() {
        return doc_onepath;
    }

    public void setDoc_onepath(String doc_onepath) {
        this.doc_onepath = doc_onepath;
    }

    public String getId() {
        return id;
    }

    public String getChecquefilename() {
        return checquefilename;
    }

    public void setChecquefilename(String checquefilename) {
        this.checquefilename = checquefilename;
    }

    public DocModel(String filename, String doc_onefilename, String doc_twofilename, String gstfilename, String purchasefilename, String checquefilename,String adharfilename,
                    String path,String adharpath,String gstpath,String purchasepath,String checquepath,String doc_onepath,String doctwopath) {

        this.filename = filename;
        this.path = path;
        this.doc_onefilename=doc_onefilename;
        this.doc_onepath=doc_onepath;
        this.doc_twofilename=doc_twofilename;
        this.gstfilename=gstfilename;
        this.purchasefilename=purchasefilename;
        this.checquefilename=checquefilename;
        this.adharfilename=adharfilename;
        this.adharpath=adharpath;
        this.gstpath=gstpath;
        this.purchasepath=purchasepath;
        this.checquepath=checquepath;
        this.doctwopath=doctwopath;
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


}
