package com.example.headsupnotification;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

/**
 * Description:
 * Author: qiubing
 * Date: 2017-08-21 14:24
 */
public class HeadsUpWindowService extends Service {
    private static final String TAG = "HeadsUpWindowService";
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        Log.e(TAG,"onCreate()...");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int i, int i1) {
        Log.e(TAG,"onStartCommand()...intent = " + intent);
        HeadsUpWindowManager.createHeadsUpWindow(getApplicationContext());
        return super.onStartCommand(intent, i, i1);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        Log.e(TAG,"onUnbind()...intent = " + intent);
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.e(TAG,"onDestroy()...");
        HeadsUpWindowManager.removeSmallWindow(getApplicationContext());
        super.onDestroy();
    }
}
