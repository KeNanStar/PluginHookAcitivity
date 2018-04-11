package com.hook.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }


    public void jumpToTargetActivity(View view){
        Intent intent = new Intent(this, TargetActivity.class);
        startActivity(intent);
    }
}
