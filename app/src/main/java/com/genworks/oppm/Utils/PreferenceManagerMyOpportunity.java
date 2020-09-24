package com.genworks.oppm.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManagerMyOpportunity {

    private static PreferenceManagerMyOpportunity jInstance;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public PreferenceManagerMyOpportunity(Context context) {
        prefs = context.getSharedPreferences("Genworks_dashboardopportunity", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.apply();
    }

    public static synchronized PreferenceManagerMyOpportunity getInstance(Context context) {
        if (jInstance != null) {
            return jInstance;
        } else {
            jInstance = new PreferenceManagerMyOpportunity(context);
            return jInstance;
        }
    }

    public void setAPIResponseDashboardOpportunity(String res) {
        editor.putString("response", res);
        editor.apply();
    }

    public String getAPIResponseDashboardOpportunity() {
        return prefs.getString("response", "");
    }


    public void setMultipleDataOpportunity(String scheduleDate, String sales_stage, String support_required,String winprob,String modality,String modifiedtime) {

        editor.putString("scheduleDate", scheduleDate);
        editor.putString("sales_stage", sales_stage);
        editor.putString("support_required", support_required);
        editor.putString("winprob", winprob);
        editor.putString("modality", modality);
        editor.putString("modifiedtime", modifiedtime);
        //editor.apply(); // btw where you call these values  i mean where you use these above value from preferce.
    }


    public String getScheduleDate() {
        return prefs.getString("scheduleDate", "");
    }

    public String getSalesStage() {
        return prefs.getString("Sales Stage", "");
    }

    public String getSupportRequired(){
        return prefs.getString("Support Required","");
    }
    public String getWinprob() {
        return prefs.getString("Win Probability", "");
    }
    public String getModality() {
        return prefs.getString("Modality", "");
    }



}
