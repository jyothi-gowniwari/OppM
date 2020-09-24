package com.genworks.oppm.model;

public class ProductModel {
    private String productname;
    private String product_id;
    private String unitprice;
    private String total;
    private String netprice;

    public String getProductname() {
        return productname;
    }

    public void setProductname(String productname) {
        this.productname = productname;
    }

    public ProductModel(String productname, String product_id, String unitprice, String total, String netprice) {
        this.productname = productname;
        this.product_id = product_id;
        this.unitprice = unitprice;
        this.total = total;
        this.netprice = netprice;
    }

    public String getProduct_id() {
        return product_id;
    }

    public void setProduct_id(String product_id) {
        this.product_id = product_id;
    }

    public String getUnitprice() {
        return unitprice;
    }

    public void setUnitprice(String unitprice) {
        this.unitprice = unitprice;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getNetprice() {
        return netprice;
    }

    public void setNetprice(String netprice) {
        this.netprice = netprice;
    }
}
