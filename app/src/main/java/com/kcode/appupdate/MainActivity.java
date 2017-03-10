package com.kcode.appupdate;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kcode.lib.UpdateWrapper;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void check(View view) {
        checkUpdate(0);
    }

    public void checkNow(View view) {
        checkUpdate(5 * 60 * 1000);
    }

    private void checkUpdate(long time) {
        UpdateWrapper updateWrapper = new UpdateWrapper.Builder(getApplicationContext())
                .setTime(time)
                .setNotificationIcon(R.mipmap.ic_launcher_round)
                .setUrl("http://114.55.236.165/logistical/app/update.json").build();

        updateWrapper.start();
    }
}
