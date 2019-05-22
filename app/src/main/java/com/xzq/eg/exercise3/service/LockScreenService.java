package com.xzq.eg.exercise3.service;

import android.app.Service;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;

import com.xzq.eg.exercise3.ICat;

import java.util.List;

public class LockScreenService extends Service {

    private final String A_PackageName = "com.xzq.alive.assistant";
    private final String A_ServicePath = "com.xzq.alive.assistant.AssistantService";
    private ICat mBinderFromA;

    private ServiceConnection conn = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinderFromA = ICat.Stub.asInterface(service);
            if (mBinderFromA != null) {
                try {
                    Log.d("测试", "收到保活助手A的数据:name= " +
                            mBinderFromA.getName() + ": age=" +
                            mBinderFromA.getAge() + "----");
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            bindAliveA();
        }
    };

    private ICat.Stub mBinderToA = new ICat.Stub() {
        @Override
        public String getName() {
            return "我是锁屏软件";
        }

        @Override
        public String getAge() {
            return "21";
        }
    };



    public LockScreenService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (!isApkInstalled(A_PackageName)) {
            Log.d("测试", "----保活助手未安装----");
            stopSelf();
            return;
        }
        Log.d("测试", "----保活助手已安装----");
        bindAliveA();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("测试", "lock screen service : on bind");
        return mBinderToA;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);
    }

    @Override
    public void onDestroy() {
        Log.d("测试", "lock screen service : destroy ");
        super.onDestroy();
    }

    private void bindAliveA() {
        Intent serveIntent = new Intent();
        serveIntent.setClassName(A_PackageName, A_ServicePath);
        bindService(serveIntent, conn, Context.BIND_AUTO_CREATE);
    }

    private boolean isApkInstalled(String packageName) {
        PackageManager manager = getPackageManager();
        List<PackageInfo> infos = manager.getInstalledPackages(0);
        for (int i = 0; i < infos.size(); i++) {
            if (infos.get(i).packageName.equalsIgnoreCase(packageName)) {
                return true;
            }
        }
        return false;
    }

}
