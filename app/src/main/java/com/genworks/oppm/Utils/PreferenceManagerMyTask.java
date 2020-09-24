package com.genworks.oppm.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManagerMyTask {

    private static PreferenceManagerMyTask jInstance;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public PreferenceManagerMyTask(Context context) {
        prefs = context.getSharedPreferences("Genworks_dashboardtask", Context.MODE_PRIVATE);
        editor = prefs.edit();
        editor.apply();
    }

    public static synchronized PreferenceManagerMyTask getInstance(Context context) {
        if (jInstance != null) {
            return jInstance;
        } else {
            jInstance = new PreferenceManagerMyTask(context);
            return jInstance;
        }
    }

    public void setAPIResponseDashboardTask(String res) {
        editor.putString("response", res);
        editor.apply();
    }

    public String getAPIResponseDashboardTask() {
        return prefs.getString("response", "");
    }


    public void setMultipleDataOutcome(String scheduleDate, String outcome, String status,String tasktype) {

        editor.putString("scheduleDate", scheduleDate);
        editor.putString("outcome", outcome);
        editor.putString("tasktype", tasktype);
        editor.putString("status", status);
        //editor.apply(); // btw where you call these values  i mean where you use these above value from preferce.
    }


    public String getScheduleDate() {
        return prefs.getString("scheduleDate", "");
    }

    public String getOutcome() {
        return prefs.getString("outcome", "");
    }

        public String getTasktype(){
        return prefs.getString("tasktype","");
    }
    public String getStatus() {
        return prefs.getString("status", "");
//    }


    }
}
