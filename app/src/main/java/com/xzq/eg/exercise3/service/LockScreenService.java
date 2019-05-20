package com.xzq.eg.exercise3.service;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

import com.xzq.eg.exercise3.broadcast.ScreenBroadcastReceiver;

public class LockScreenService extends Service {

    private ScreenBroadcastReceiver screenBroadcastReceiver;

    public LockScreenService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        screenReceiverRegister();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, Service.START_FLAG_RETRY, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(screenBroadcastReceiver);
    }

    /**
     * 静态注册无效，只能使用动态注册
     */
    private void screenReceiverRegister() {
        screenBroadcastReceiver = new ScreenBroadcastReceiver();
        IntentFilter screenFilter = new IntentFilter();
        screenFilter.addAction(Intent.ACTION_SCREEN_ON);
        screenFilter.addAction(Intent.ACTION_SCREEN_OFF);
        registerReceiver(screenBroadcastReceiver, screenFilter);
    }
}
