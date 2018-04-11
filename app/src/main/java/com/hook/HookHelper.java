package com.hook;

import android.os.Handler;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Proxy;

/**
 * @author xing.hu
 * @since 2018/4/11, 下午3:38
 */

public class HookHelper {
    public static final String TARGET_INTENT = "target_intent";

    public static void hookActivityManagerNative() throws ClassNotFoundException,
            NoSuchMethodException, InvocationTargetException,
            IllegalAccessException, NoSuchFieldException {
        //
        Class<?> activityManagerNative = Class.forName("android.app.ActivityManagerNative");

        Field gDefaultField = activityManagerNative.getDeclaredField("gDefault");
        gDefaultField.setAccessible(true);

        Object gDefault = gDefaultField.get(null);

        //gDefault是一个 android.util.Singleton对象; 我们取出这个单例里面的字段
        Class<?> singleton = Class.forName("android.util.Singleton");
        Field mInstanceField = singleton.getDeclaredField("mInstance");
        mInstanceField.setAccessible(true);

        // 得到ActivityManagerNative的gDefault对象，即IActivityManager对象
        Object activityManager = mInstanceField.get(gDefault);

        // 创建即IActivityManager对象的代理对象, 然后替换这个字段
        Class<?> iActivityManagerInterface = Class.forName("android.app.IActivityManager");
        Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                new Class<?>[] { iActivityManagerInterface }, new IActivityManagerHandler(activityManager));
        mInstanceField.set(gDefault, proxy);

    }

    public static void hookActivityThreadHandler() throws Exception {

        // 先获当前的ActivityThread对象
        Class<?> activityThreadClass = Class.forName("android.app.ActivityThread");
        Field currentActivityThreadField = activityThreadClass.getDeclaredField("sCurrentActivityThread");
        currentActivityThreadField.setAccessible(true);
        Object currentActivityThread = currentActivityThreadField.get(null);

        // ActivityThread一个进程只有一个,我们获取这个对象的mH
        Field mHField = activityThreadClass.getDeclaredField("mH");
        mHField.setAccessible(true);
        Handler mH = (Handler) mHField.get(currentActivityThread);


        Field mCallBackField = Handler.class.getDeclaredField("mCallback");
        mCallBackField.setAccessible(true);

        mCallBackField.set(mH, new ActivityThreadCallback(mH));

    }

}
