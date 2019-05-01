package com.xzq.eg.exercise3;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.Log;
import android.widget.Toast;

import com.xzq.eg.exercise3.base.BaseActivity;
import com.xzq.eg.exercise3.model.LockScreenModel;
import com.xzq.eg.exercise3.service.LockScreenService;
import com.xzq.eg.exercise3.views.LockView;

public class MainActivity extends BaseActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    public LockView lockView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        startLockScreenService();
    }

    private void startLockScreenService() {
        if (!Settings.canDrawOverlays(this)) {
            Toast.makeText(this, "当前无权限，请授权", Toast.LENGTH_SHORT).show();
            startActivityForResult(new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + getPackageName())), 0);
        } else {
            startService(new Intent(MainActivity.this, LockScreenService.class));
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0) {
            if (!Settings.canDrawOverlays(this)) {
                Toast.makeText(this, "授权失败", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
                startService(new Intent(MainActivity.this, LockScreenService.class));
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // 通过 ID 找到控件
        lockView = findViewById(R.id.lv);
        // 初始化事件
        initEvents();
    }

    /**
     * 初始化事件
     */
    private void initEvents() {
        // 为LockView设置监听器
        lockView.setOnInputPwdListener(pwd -> {
            Log.d("test", "" + pwd);
            if (pwd.length() > 3) {
                LockScreenModel.getInstance().setPwd(pwd);
            } else {
                Toast.makeText(this, "至少需要四个点相连", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
