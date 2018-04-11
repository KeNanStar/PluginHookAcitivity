package com.hook;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;

/**
 * @author xing.hu
 * @since 2018/4/11, 下午8:27
 */

public class ActivityThreadCallback implements Handler.Callback {
    private static final String TAG = ActivityThreadCallback.class.getSimpleName();
    protected Handler mBase;
    // ActivityThread里面 "LAUNCH_ACTIVITY" 这个字段的值是100
    private static final int LAUNCH_ACTIVITY = 100;


    public ActivityThreadCallback(Handler base) {
        mBase = base;
    }

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what) {
            case LAUNCH_ACTIVITY:
                handleLaunchActivity(msg);
                break;
        }

        mBase.handleMessage(msg);
        return true;
    }

    private void handleLaunchActivity(Message msg) {
        // 取出TargetActivity,去替换ProxyActivity
        Object obj = msg.obj;
        try {
            Field intent = obj.getClass().getDeclaredField("intent");
            intent.setAccessible(true);
            Intent raw = (Intent) intent.get(obj);

            Intent targetIntent = raw.getParcelableExtra(HookHelper.TARGET_INTENT);
            raw.setComponent(targetIntent.getComponent());

        } catch (Exception e) {
            Log.e(TAG, "handleLaunchActivity error: " + e.getMessage());
        }
    }
}
