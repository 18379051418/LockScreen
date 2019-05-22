package com.xzq.eg.exercise3;

import android.app.Application;
import android.content.Intent;
import android.content.IntentFilter;

import com.xzq.eg.exercise3.broadcast.ScreenBroadcastReceiver;
import com.xzq.eg.exercise3.utils.Density;

/**
 * created by xzq on 2019/4/22
 */
public class App extends Application {
    public static App INSTANCE;

    @Override
    public void onCreate() {
        super.onCreate();
        Density.setDensity(this, 360);
        INSTANCE = this;


        ScreenBroadcastReceiver screenBroadcastReceiver = new ScreenBroadcastReceiver();
        IntentFilter screenFilter = new IntentFilter();
        screenFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenBroadcastReceiver, screenFilter);
    }
}
