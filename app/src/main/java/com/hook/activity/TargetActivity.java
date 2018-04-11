package com.hook.activity;

import android.app.Activity;
import android.os.Bundle;

/**
 * TargetActivity没有在AndroidManifest里面注册
 */
public class TargetActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_target);
    }
}
