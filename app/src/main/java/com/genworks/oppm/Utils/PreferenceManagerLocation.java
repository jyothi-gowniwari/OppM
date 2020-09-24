package com.genworks.oppm.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManagerLocation {

    private static PreferenceManagerLocation jInstance;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    public PreferenceManagerLocation(Context context) {
        prefs = context.getSharedPreferences("Genworks_location", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static synchronized PreferenceManagerLocation getInstance(Context context) {
        if (jInstance != null) {
            return jInstance;
        } else {
            jInstance = new PreferenceManagerLocation(context);
            return jInstance;
        }
    }

    public void setAPIResponseLocation(String res){
        editor.putString("response",res);
        editor.apply();
    }
    public String getAPIResponseLocation(){
        return prefs.getString("response","");
    }




    public void setMultipleData(String location){

        editor.putString("location", location);

        editor.apply(); // btw where you call these values  i mean where you use these above value from preferce.
    }


    public String getLocation(){
        return prefs.getString("location","");
    }



}
