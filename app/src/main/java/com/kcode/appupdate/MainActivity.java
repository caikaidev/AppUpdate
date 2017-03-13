package com.kcode.appupdate;

import android.Manifest;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.kcode.lib.UpdateWrapper;
import com.kcode.permissionslib.main.OnRequestPermissionsCallBack;
import com.kcode.permissionslib.main.PermissionCompat;

public class MainActivity extends AppCompatActivity {

    private final static String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    public void check(View view) {
        checkUpdate(0, null);
    }

    public void checkNow(View view) {
        checkUpdate(5 * 60 * 1000, null);
    }

    public void checkCustoms(View view) {
        checkUpdate(0, CustomsUpdateActivity.class);
    }

    private void checkUpdate(final long time, final Class<? extends FragmentActivity> cls) {

        PermissionCompat.Builder pBuilder = new PermissionCompat.Builder(this);
        pBuilder.addPermissions(permissions)
                .addRequestPermissionsCallBack(new OnRequestPermissionsCallBack() {
                    @Override
                    public void onGrant() {

                        UpdateWrapper.Builder builder = new UpdateWrapper.Builder(getApplicationContext())
                                .setTime(time)
                                .setNotificationIcon(R.mipmap.ic_launcher_round)
                                .setUrl("http://45.78.52.169/app/update.json");

                        if (cls != null) {
                            builder.setCustomsActivity(cls);
                        }

                        builder.build().start();
                    }

                    @Override
                    public void onDenied(String s) {

                    }
                });
        pBuilder.build().request();


    }
}
