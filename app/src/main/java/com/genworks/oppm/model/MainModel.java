package com.genworks.oppm.model;

import java.util.ArrayList;

public class MainModel {
    private String label; // opportunity/ negotiation or review
    private ArrayList<ModelTest> listOfData; // here list of data like assigned, contact and location ets

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public ArrayList<ModelTest> getListOfData() {
        return listOfData;
    }

    public void setListOfData(ArrayList<ModelTest> listOfData) {
        this.listOfData = listOfData;
    }
}
