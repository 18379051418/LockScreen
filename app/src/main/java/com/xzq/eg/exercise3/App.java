package com.xzq.eg.exercise3;

import android.app.Application;

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
    }
}
