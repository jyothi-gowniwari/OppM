package com.genworks.oppm.model;

public class MyTaskModel {

    private String date_start;
    private String outcome;
    private String tasktype;
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTasktype() {
        return tasktype;
    }

    public void setTasktype(String tasktype) {
        this.tasktype = tasktype;
    }

    public MyTaskModel(String date_start, String outcome,String status,String tasktype) {
        this.date_start = date_start;
        this.outcome = outcome;
        this.status=status;
        this.tasktype=tasktype;


    }




    public String getDate_start() {
        return date_start;
    }

    public void setDate_start(String date_start) {
        this.date_start = date_start;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }
}
