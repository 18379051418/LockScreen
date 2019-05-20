package com.xzq.eg.exercise3.model;

import android.content.Context;
import android.content.SharedPreferences;

import com.xzq.eg.exercise3.App;

import io.reactivex.annotations.NonNull;

/**
 * created by xzq on 2019/4/28
 */
public class LockScreenModel {

    private LockScreenModel() {
    }

    public static LockScreenModel getInstance() {
        return SingleInstanceHandler.INSTANCE;
    }

    //获取密码
    public @NonNull
    String getPwd() {
        SharedPreferences sp =
                App.INSTANCE.getSharedPreferences("soduku", Context.MODE_PRIVATE);
        return sp.getString("pwd", "error");
    }

    //设置密码
    public void setPwd(String pwd) {
        SharedPreferences sp = App.INSTANCE.getSharedPreferences("soduku", Context.MODE_PRIVATE);
        sp.edit()
                .putString("pwd", pwd)
                .apply();
    }

    //清除密码
    public void clearPwd() {
        SharedPreferences sp = App.INSTANCE.getSharedPreferences("soduku", Context.MODE_PRIVATE);
        sp.edit()
                .putString("pwd", "error")
                .apply();
    }

    private static class SingleInstanceHandler {
        private static LockScreenModel INSTANCE = new LockScreenModel();
    }
}
