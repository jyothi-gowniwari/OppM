package com.genworks.oppm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class Results_Account {



        @SerializedName("describe")
        @Expose
        private DescribeDetails describe;


        public DescribeDetails getDescribe() {
            return describe;
        }

        public void setDescribe(DescribeDetails describe) {
            this.describe = describe;
        }

        public ArrayList<Fields> getFields() {
            return fields;
        }

        public void setFields(ArrayList<Fields> fields) {
            this.fields = fields;
        }

        @SerializedName("fields")
        @Expose
        private ArrayList<Fields> fields;
    }



