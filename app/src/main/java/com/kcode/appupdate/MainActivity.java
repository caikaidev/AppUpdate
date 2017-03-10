package com.kcode.appupdate;

import android.app.Activity;
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
        checkUpdate(0,null);
    }

    public void checkNow(View view) {
        checkUpdate(5 * 60 * 1000,null);
    }

    public void checkCustoms(View view) {
        checkUpdate(0,CustomsUpdateActivity.class);
    }

    private void checkUpdate(long time, Class<? extends Activity> cls) {
        UpdateWrapper.Builder builder = new UpdateWrapper.Builder(getApplicationContext())
                .setTime(time)
                .setNotificationIcon(R.mipmap.ic_launcher_round)
                .setUrl("http://45.78.52.169/app/update.json");

        if (cls != null) {
            builder.setCustomsActivity(cls);
        }

        builder.build().start();
    }
}
