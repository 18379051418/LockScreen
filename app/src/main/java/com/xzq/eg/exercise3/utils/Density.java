package com.xzq.eg.exercise3.utils;

import android.app.Activity;
import android.app.Application;
import android.content.ComponentCallbacks;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.DisplayMetrics;

/*
 * package com.software.instantshoot.utils.kotlin
 * @author xzq
 * created at 2018/10/27 14:08
 * explanation: 今日头条适配方案
 */
public class Density {
    private static float appDensity = 0f;
    private static float appScaledDensity = 0f;

    private static DisplayMetrics appDisplayMetrics;

    private static float width = 0f;

    public static void setDensity(Application app, float width) {
        appDisplayMetrics = app.getResources().getDisplayMetrics();
        Density.width = width;
        registerActivityLifecycleCallbacks(app);

        if (appDensity == 0f) {
            //初始化
            appDensity = appDisplayMetrics.density;
            appScaledDensity = appDisplayMetrics.scaledDensity;

            //添加字体变化的监听
            app.registerComponentCallbacks(new ComponentCallbacks() {
                @Override
                public void onConfigurationChanged(Configuration newConfig) {
                    if (newConfig.fontScale > 0) {
                        appScaledDensity = app.getResources().getDisplayMetrics().scaledDensity;
                    }
                }

                @Override
                public void onLowMemory() {

                }
            });
        }
    }

    private static void setDefault(Activity activity) {
        setAppOrientation(activity);
    }

    private static void setAppOrientation(Activity activity) {
        float targetDensity = 0f;
        float targetScaledDensity;
        int targetDensityDpi;
        try {
            targetDensity = appDisplayMetrics.widthPixels / width;
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        targetScaledDensity = targetDensity * (appScaledDensity / appDensity);
        targetDensityDpi = 160 * (int) targetDensity;

        DisplayMetrics activityDisplayMetrics = activity.getResources().getDisplayMetrics();
        activityDisplayMetrics.density = targetDensity;
        activityDisplayMetrics.scaledDensity = targetScaledDensity;
        activityDisplayMetrics.densityDpi = targetDensityDpi;
    }

    private static void registerActivityLifecycleCallbacks(Application app) {
        app.registerActivityLifecycleCallbacks(new Application.ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                setDefault(activity);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
    }
}
