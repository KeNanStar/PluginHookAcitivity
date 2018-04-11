package com.hook.activity;

import android.app.Application;
import android.app.Instrumentation;
import android.content.Context;

import com.hook.HookHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author xing.hu
 * @since 2018/4/11, 下午3:33
 */

public class PluginApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        try {
            attachContext();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void attachContext() throws Exception{
        HookHelper.hookActivityManagerNative();
        HookHelper.hookActivityThreadHandler();
    }

}
