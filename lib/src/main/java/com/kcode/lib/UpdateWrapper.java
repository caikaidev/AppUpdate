package com.kcode.lib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.dialog.UpdateActivity;
import com.kcode.lib.net.CheckUpdateTask;

/**
 * Created by caik on 2017/3/8.
 */

public class UpdateWrapper {

    private static UpdateWrapper updateWrapper;

    public static UpdateWrapper get() {
        if (updateWrapper == null) {
            synchronized (UpdateWrapper.class) {
                if (updateWrapper == null){
                    updateWrapper = new UpdateWrapper();
                }
            }
        }

        return updateWrapper;
    }
    private UpdateWrapper() {}

    public void checkUpdate(Activity context, String url){
        checkUpdate(context, url,0);
    }

    public void checkUpdate(final Activity context, String url, long time){
        new CheckUpdateTask(url, new CheckUpdateTask.Callback() {
            @Override
            public void callBack(VersionModel model) {
                if (model == null) {
                    Toast.makeText(context, "最新版本", Toast.LENGTH_SHORT).show();
                    return;
                }

                start2Activity(context, model);
            }
        }).start();
    }

    private void start2Activity(Context context,VersionModel model) {
        Intent intent = new Intent(context, UpdateActivity.class);
        intent.putExtra("model", model);
        context.startActivity(intent);
    }
}
