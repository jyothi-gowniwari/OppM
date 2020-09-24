package com.genworks.oppm.model;

public class TaskModel {

    private String subject;
    private String taskType;
    private String assigned;
    private String scheduleDate;
    private String location;
    private String opportunityNo;
    private String remark;
    private String createdtime;

    public String getTask_id() {
        return task_id;
    }

    public void setTask_id(String task_id) {
        this.task_id = task_id;
    }

    private String parent_id;
    private String task_id;

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreatedtime() {
        return createdtime;
    }

    public void setCreatedtime(String createdtime) {
        this.createdtime = createdtime;
    }

    public String getModifiedtime() {
        return modifiedtime;
    }

    public void setModifiedtime(String modifiedtime) {
        this.modifiedtime = modifiedtime;
    }

    public String getModifiedby() {
        return modifiedby;
    }

    public void setModifiedby(String modifiedby) {
        this.modifiedby = modifiedby;
    }

    public String getActivitytype() {
        return activitytype;
    }

    public void setActivitytype(String activitytype) {
        this.activitytype = activitytype;
    }

    private String modifiedtime;
    private String modifiedby;
    private String activitytype;

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTaskType() {
        return taskType;
    }

    public void setTaskType(String taskType) {
        this.taskType = taskType;
    }

    public String getAssigned() {
        return assigned;
    }

    public void setAssigned(String assigned) {
        this.assigned = assigned;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOpportunityNo() {
        return opportunityNo;
    }

    public void setOpportunityNo(String opportunityNo) {
        this.opportunityNo = opportunityNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getOutcome() {
        return outcome;
    }

    public void setOutcome(String outcome) {
        this.outcome = outcome;
    }

    private String status;

    public TaskModel(String subject, String taskType, String assigned, String scheduleDate, String location, String opportunityNo, String status, String outcome,String createdtime,String modifiedtime,String activitytype,String modifiedby,String remark,String parent_id,String task_id) {
        this.subject = subject;
        this.taskType = taskType;
        this.assigned = assigned;
        this.scheduleDate = scheduleDate;
        this.location = location;
        this.opportunityNo = opportunityNo;
        this.status = status;
        this.outcome = outcome;
        this.createdtime=createdtime;
        this.modifiedtime=modifiedtime;
        this.activitytype=activitytype;
        this.modifiedby=modifiedby;
        this.remark=remark;
        this.parent_id=parent_id;
        this.task_id=task_id;
    }

    private String outcome;





}
