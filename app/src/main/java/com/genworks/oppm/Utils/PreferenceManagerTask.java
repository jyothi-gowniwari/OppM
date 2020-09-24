package com.genworks.oppm.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;

public class PreferenceManagerTask {

    private static PreferenceManagerTask jInstance;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public PreferenceManagerTask(Context context) {
        prefs = context.getSharedPreferences("Genworks_task", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static synchronized PreferenceManagerTask getInstance(Context context) {
        if (jInstance != null) {
            return jInstance;
        } else {
            jInstance = new PreferenceManagerTask(context);
            return jInstance;
        }
    }

    public void setAPIResponseTask(String res){
        editor.putString("response",res);
        editor.apply();
    }
    public String getAPIResponseTask(){
        return prefs.getString("response","");
    }
    public void setMultipleData(String subject, String taskType,String assigned,String scheduleDate,String location,String opportunityNo,String status,String outcome){
        editor.putString("subject", subject);
        editor.putString("taskType", taskType);
        editor.putString("assigned", assigned);
        editor.putString("scheduleDate", scheduleDate);
        editor.putString("location", location);
        editor.putString("opportunityNo", opportunityNo);
        editor.putString("status", status);
        editor.putString("outcome", outcome);
        editor.apply(); // btw where you call these values  i mean where you use these above value from preferce.
    }

    public String getSubject(){
        return prefs.getString("subject","");
    }

    public String getTaskType(){
        return prefs.getString("taskType","");
    }

    public String getAssigned(){
        return prefs.getString("assigned","");
    }
    public String getScheduleDate(){
        return prefs.getString("scheduleDate","");
    }
    public String getLocation(){
        return prefs.getString("location","");
    }
    public String getOpportunityNo(){
        return prefs.getString("opportunityNo","");
    }
    public String getStatus(){
        return prefs.getString("status","");
    }
    public String getOutcome(){
        return prefs.getString("outcome","");
    }
    public String getlocation(){
        return prefs.getString("location","");
    }


}
