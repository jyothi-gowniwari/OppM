package com.genworks.oppm.Utils;

import android.content.Context;
import android.content.SharedPreferences;
public class SharedPreferenceClass {

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Context _context;
    int PRIVATE_MODE = 0;
    private static final String PREF_NAME = "login";

    public static String KEY_LOGIN_STATUS= "KEY_LOGIN_STATUS";
    public static String KEY_LOGIN_SESSION= "KEY_LOGIN_SESSION";
    public static String KEY_LOGIN_FIRSTNAME= "KEY_LOGIN_FIRSTNAME";
    public static String KEY_LOGIN_LASTNAME= "KEY_LOGIN_LASTNAME";
    public static String KEY_LOGIN_MOBILE= "KEY_LOGIN_MOBILE";
    public static String KEY_LOGIN_ROLE= "KEY_LOGIN_ROLE";
    public static String KEY_LOGIN_REPORTTO= "KEY_LOGIN_REPORTTO";
    public static String KEY_LOGIN_USERNAME= "KEY_LOGIN_USERNAME";

    public SharedPreferenceClass(Context context){
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, 0);
        editor = pref.edit();
    }



    public void setLoginStatus(String loginStatus)
    {
        editor.remove(KEY_LOGIN_STATUS);
        editor.putString(KEY_LOGIN_STATUS, loginStatus);
        editor.commit();
    }

    public void setLoginSession(String loginSession)
    {
        editor.remove(KEY_LOGIN_SESSION);
        editor.putString(KEY_LOGIN_SESSION, loginSession);
        editor.commit();
    }

    public String getKeyLoginSession()
    {
        return pref.getString(KEY_LOGIN_SESSION,"");
    }

    public String getKeyLoginStatus()
    {
        return pref.getString(KEY_LOGIN_STATUS,"");
    }

    public void setKeyLoginFirstname(String loginFirstname)
    {
        editor.remove(KEY_LOGIN_FIRSTNAME);
        editor.putString(KEY_LOGIN_FIRSTNAME, loginFirstname);
        editor.commit();
    }

    public String getKeyLoginFirstname()
    {
        return pref.getString(KEY_LOGIN_FIRSTNAME,"");
    }

    public void setKeyLoginLastname(String loginLastname)
    {
        editor.remove(KEY_LOGIN_LASTNAME);
        editor.putString(KEY_LOGIN_LASTNAME, loginLastname);
        editor.commit();
    }

    public String getKeyLoginLastname()
    {
        return pref.getString(KEY_LOGIN_LASTNAME,"");
    }

    public void setKeyLoginMobile(String loginMobile)
    {
        editor.remove(KEY_LOGIN_MOBILE);
        editor.putString(KEY_LOGIN_MOBILE, loginMobile);
        editor.commit();
    }

    public String getKeyLoginMobile()
    {
        return pref.getString(KEY_LOGIN_MOBILE,"");
    }

    public void setKeyLoginRole(String loginRole)
    {
        editor.remove(KEY_LOGIN_ROLE);
        editor.putString(KEY_LOGIN_ROLE, loginRole);
        editor.commit();
    }

    public String getKeyLoginRole()
    {
        return pref.getString(KEY_LOGIN_ROLE,"");
    }

    public void setKeyLoginReportto(String loginReportto)
    {
        editor.remove(KEY_LOGIN_REPORTTO);
        editor.putString(KEY_LOGIN_REPORTTO, loginReportto);
        editor.commit();
    }

    public String getKeyLoginReportto()
    {
        return pref.getString(KEY_LOGIN_REPORTTO,"");
    }

    public void setKeyLoginUsername(String loginUsername)
    {
        editor.remove(KEY_LOGIN_USERNAME);
        editor.putString(KEY_LOGIN_USERNAME, loginUsername);
        editor.commit();
    }

    public String getKeyLoginUsername()
    {
        return pref.getString(KEY_LOGIN_USERNAME,"");
    }
}
