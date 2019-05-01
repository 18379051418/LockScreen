package com.xzq.eg.exercise3.utils;

import android.content.Context;

/**
 * created by xzq on 2019/4/25
 */
public class DensityUtil {

    //dp 转 px
    public static int dpTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    //px 转 dp
    public static int pxTodp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
}
