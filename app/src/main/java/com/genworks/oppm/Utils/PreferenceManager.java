package com.genworks.oppm.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static PreferenceManager jInstance;
    private final SharedPreferences prefs;
    private final SharedPreferences.Editor editor;

    private PreferenceManager(Context context) {
        prefs = context.getSharedPreferences("Genworks_Account", Context.MODE_PRIVATE);
        editor = prefs.edit();
    }

    public static synchronized PreferenceManager getInstance(Context context) {
        if (jInstance != null) {
            return jInstance;
        } else {
            jInstance = new PreferenceManager(context);
            return jInstance;
        }
    }


    public void setAPIResponseAccount(String res){
        editor.putString("response",res);
        editor.apply();
    }
    public String getAPIResponseAccount(){
        return prefs.getString("response","");
    }

    public void setMultipleAccountData(String accountName, String ownershipType, String facilityType, String email, String phone, String assigned, String bill_street, String bill_city, String billingDistrict, String billingState,String account_no, String createdtime, String modifiedtime, String modifiedby, String bill_country,String bill_pin,String account_id) {
        editor.putString("accountName", accountName);
        editor.putString("ownershipType", ownershipType);
        editor.putString("facilityType", facilityType);
        editor.putString("email", email);
        editor.putString("phone", phone);
        editor.putString("assigned", assigned);
        editor.putString("bill_street", bill_street);
        editor.putString("bill_city", bill_city);
        editor.putString("billingDistrict",billingDistrict);
        editor.putString("billingState",billingState);
        editor.putString("account_no",account_no);
        editor.putString("createdtime",createdtime);
        editor.putString("modifiedtime",modifiedtime);
        editor.putString("bill_pin",bill_pin);
        editor.putString("bill_country",bill_country);
        editor.putString("account_id",account_id);

        editor.apply(); // btw where you call these values  i mean where you use these above value from preferce.
    }
    public String getAccountName(){
        return prefs.getString("accountName","");
    }
    public String getAssigned(){
        return prefs.getString("assigned","");
    }
}
