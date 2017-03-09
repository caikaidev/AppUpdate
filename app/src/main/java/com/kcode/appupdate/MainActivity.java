package com.kcode.appupdate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kcode.lib.UpdateWrapper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UpdateWrapper updateWrapper = new UpdateWrapper.Builder(getApplicationContext())
                .setTime(0)
                .setUrl("http://114.55.236.165/logistical/app/update.json").build();

        updateWrapper.start();
    }
}
