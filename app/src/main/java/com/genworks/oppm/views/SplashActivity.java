package com.genworks.oppm.views;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.genworks.oppm.R;
import com.genworks.oppm.Utils.SharedPreferenceClass;


public class SplashActivity extends AppCompatActivity {

    boolean isonetimelogin=false;
    private String sessionId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splashscreen);
        new Handler().postDelayed(new Runnable() {


            @Override
            public void run() {
                SharedPreferenceClass shared=new SharedPreferenceClass(SplashActivity.this);
                if(shared.getKeyLoginStatus().equals("SUCCESS")) {
                    String session_Id = String.valueOf(shared.getKeyLoginSession());
                    Log.d("session_Id", session_Id);
                    String firstname = String.valueOf(shared.getKeyLoginFirstname());
                    Log.d("first_name", firstname);
                    String lastname = String.valueOf(shared.getKeyLoginLastname());
                    Log.d("lastname", lastname);
                    String mobile = String.valueOf(shared.getKeyLoginMobile());
                    Log.d("mobile", mobile);
                    String role = String.valueOf(shared.getKeyLoginRole());
                    Log.d("role", role);
                    String reportto = String.valueOf(shared.getKeyLoginReportto());
                    Log.d("reportto", reportto);
                    String username = String.valueOf(shared.getKeyLoginUsername());
                    Log.d("username", username);
                    if (!isonetimelogin){
                    Intent intentmain = new Intent(SplashActivity.this, MainActivity.class);
                    intentmain.putExtra("session_Id", session_Id);
                    intentmain.putExtra("firstname", firstname);
                    intentmain.putExtra("lastname", lastname);
                    intentmain.putExtra("mobile", mobile);
                    intentmain.putExtra("role", role);
                    intentmain.putExtra("reportto", reportto);
                    intentmain.putExtra("username", username);
                    startActivity(intentmain);
                    finish();
                }
                }
                else
                {
                    isonetimelogin=true;
                    Intent intentlogin = new Intent(SplashActivity.this, LoginActivity.class);
                    startActivity(intentlogin);
                    finish();
                }

            }
        }, 5000);
    }
    @Override
    public void onResume() {
        super.onResume();
        if(LogoutService.timer!=null) {
            LogoutService.timer.start();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(LogoutService.timer!=null){

            LogoutService.timer.cancel();
        }
    }

}
