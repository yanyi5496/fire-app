package com.yanyi.fire.util;

import android.annotation.SuppressLint;
import android.content.Context;

import androidx.multidex.MultiDexApplication;

/**
 * Application
 *
 * @author APP_TEAM
 */
public class MyApplication extends MultiDexApplication {

    @SuppressLint("StaticFieldLeak")
    private static MyApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        MyApplication.sInstance = this;
    }

    /**
     * 获取全局context
     */
    public static Context getAppContext() {
        return sInstance.getApplicationContext();
    }

    public static MyApplication getInstance() {
        return MyApplication.sInstance;
    }


}
