package com.xzq.eg.exercise3.broadcast;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.provider.Settings;
import android.support.constraint.ConstraintLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.xzq.eg.exercise3.R;
import com.xzq.eg.exercise3.model.LockScreenModel;
import com.xzq.eg.exercise3.views.LockView;

/**
 * Created by xzq on 2019/2/28.
 * 接收屏幕亮屏广播
 */

public class ScreenBroadcastReceiver extends BroadcastReceiver implements View.OnClickListener, LockView.OnUnLockListener {

    private static final String TAG = ScreenBroadcastReceiver.class.getSimpleName();
    private ConstraintLayout floatWindow;


    @Override
    public void onReceive(Context context, Intent intent) {
        if (Intent.ACTION_SCREEN_ON.equals(intent.getAction())) {
            Log.d(TAG, "onReceive: screen on : " + System.currentTimeMillis());
        } else if (Intent.ACTION_SCREEN_OFF.equals(intent.getAction())) {
            Log.d(TAG, "onReceive: screen off : " + System.currentTimeMillis());
            showLockScreen(context);
        }
    }

    private void showLockScreen(Context context) {
        if (Settings.canDrawOverlays(context)) {
            showFloatingWindow(context);
        } else {
            Log.d("test", "权限不足，显示失败");
        }
    }

    //悬浮窗模式的锁屏
    @SuppressLint("SetTextI18n")
    private void showFloatingWindow(Context context) {
        //获取WindowManager服务
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        floatWindow = (ConstraintLayout) LayoutInflater.from(context).inflate(R.layout.lock_window, null);
        Button btQuit = floatWindow.findViewById(R.id.bt_quit);
        btQuit.setOnClickListener(this);
        LockView lockView = floatWindow.findViewById(R.id.lv);
        lockView.setOnUnlockListener(this);
        Log.d("test", "test:" + LockScreenModel.getInstance().getPwd());
        if (LockScreenModel.getInstance().getPwd().equals("error"))
            lockView.setVisibility(View.GONE);

        //设置LayoutParams
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            layoutParams.type = WindowManager.LayoutParams.TYPE_PHONE;
        }
        layoutParams.format = PixelFormat.RGBA_8888;
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.MATCH_PARENT;

        if (windowManager != null)
            windowManager.addView(floatWindow, layoutParams);
    }

    @Override
    public void onClick(View v) {
        quit(v.getContext());
    }

    private void quit(Context context) {
        WindowManager windowManager =
                (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        if (windowManager != null) windowManager.removeViewImmediate(floatWindow);
    }

    @Override
    public boolean isUnlockSuccess(String result) {
        return LockScreenModel.getInstance().getPwd().equals(result);
    }

    @Override
    public void onSuccess() {
        Toast.makeText(floatWindow.getContext(), "unlock success", Toast.LENGTH_SHORT).show();
        quit(floatWindow.getContext());
    }

    @Override
    public void onFailure() {

    }

    private void animatorInit(ConstraintLayout constraintLayout, int height) {

    }
}
