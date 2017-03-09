package com.kcode.appupdate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.kcode.lib.UpdateWrapper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UpdateWrapper.get().checkUpdate(this,"http://114.55.236.165/logistical/app/update.json");
    }
}
