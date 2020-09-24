package com.genworks.oppm.Utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManagerContact {
        private static PreferenceManagerContact jInstance;
        private final SharedPreferences prefs;
        private final SharedPreferences.Editor editor;

        private PreferenceManagerContact(Context context) {
            prefs = context.getSharedPreferences("Genworks_Contact", Context.MODE_PRIVATE);
            editor = prefs.edit();
        }
        public static synchronized com.genworks.oppm.Utils.PreferenceManagerContact getInstance(Context context) {
            if (jInstance != null) {
                return jInstance;
            } else {
                jInstance = new com.genworks.oppm.Utils.PreferenceManagerContact(context);
                return jInstance;
            }
        }
        public void setAPIResponseContact(String res){
            editor.putString("response",res);
            editor.apply();
        }
        public String getAPIResponseContact(){
            return prefs.getString("response","");
        }

        public void setMultipleContactData(String salutationtype, String firstname, String lastname, String accounName, String specialization, String jobTitle, String contactType, String emailID, String mobilePhone, String modifiedtime, String modifiedby,String createdtime, String contactsource, String comment, String assigned, String contactid,String contact_id) {
            editor.putString("salutationtype", salutationtype);
            editor.putString("firstname", firstname);
            editor.putString("lastname", lastname);
            editor.putString("accounName", accounName);
            editor.putString("specialization", specialization);
            editor.putString("jobTitle", jobTitle);
            editor.putString("contactType", contactType);
            editor.putString("emailID", emailID);
            editor.putString("mobilePhone",mobilePhone);
            editor.putString("modifiedby",modifiedby);
            editor.putString("contactsource",contactsource);
            editor.putString("createdtime",createdtime);
            editor.putString("modifiedtime",modifiedtime);
            editor.putString("comment",comment);
            editor.putString("assigned",assigned);
            editor.putString("contactid",contactid);
            editor.putString("contact_id",contact_id);

            editor.apply(); // btw where you call these values  i mean where you use these above value from preferce.
        }
        public String getMobilePhone(){
            return prefs.getString("mobilePhone","");
        }
    }

