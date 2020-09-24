package com.genworks.oppm.views;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.provider.SyncStateContract;
import android.util.Log;

public class LogoutService extends Service {
    public static CountDownTimer timer;
    @Override
    public void onCreate(){
        super.onCreate();
        timer  = new CountDownTimer(10000 + 100, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                //Some code
              //  Log.v(SyncStateContract.Constants.TAG, "Service Started");
            }

            @Override
            public void onFinish() {
              //  Log.v(SyncStateContract.Constants.TAG, "Call Logout by Service");
                // Code for Logout
                stopSelf();
            }
        };
        timer.start();
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
