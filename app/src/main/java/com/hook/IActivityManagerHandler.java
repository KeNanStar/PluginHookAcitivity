package com.hook;

import android.content.ComponentName;
import android.content.Intent;
import android.util.Log;

import com.hook.activity.ProxyActivity;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

/**
 * @author xing.hu
 * @since 2018/4/11, 下午8:16
 */

public class IActivityManagerHandler implements InvocationHandler {
    private static final String TAG = IActivityManagerHandler.class.getSimpleName();
    protected  Object mBase;

    private static final String START_ACTIVITY_METHOD = "startActivity";

    private static final String PACKAGE_NAME = "com.hook.activity";


    public IActivityManagerHandler(Object base){
        mBase = base;
    }



    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (START_ACTIVITY_METHOD.equals(method.getName())){
            if(args == null){
                 return method.invoke(mBase, args);
            }

            Intent targetIntent;
            int index = 0;

            //找到TargetActivity的Intent
            for (int i = 0; i < args.length; i++) {
                if (args[i] instanceof Intent) {
                    index = i;
                    break;
                }
            }
            targetIntent = (Intent) args[index];

            Intent pluginIntent = new Intent();


            //启动的TargetActivity替换为ProxyActivity
            ComponentName componentName = new ComponentName(PACKAGE_NAME, ProxyActivity.class.getName());
            pluginIntent.setComponent(componentName);

            // TargetActivity的Intent保存起来
            pluginIntent.putExtra(HookHelper.TARGET_INTENT, targetIntent);
            args[index] = pluginIntent;

            return method.invoke(mBase, args);

        }

        return method.invoke(mBase, args);
    }
}
