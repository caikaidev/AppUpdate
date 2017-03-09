package com.kcode.lib;

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

    private Context mContext;
    private String url;
    private long time;

    private UpdateWrapper() {
    }

    public void start() {
        new CheckUpdateTask(url, time, new CheckUpdateTask.Callback() {
            @Override
            public void callBack(VersionModel model) {
                if (model == null) {
                    Toast.makeText(mContext, "最新版本", Toast.LENGTH_SHORT).show();
                    return;
                }

                start2Activity(mContext, model);
            }
        }).start();
    }

    private void start2Activity(Context context, VersionModel model) {
        Intent intent = new Intent(context, UpdateActivity.class);
        intent.putExtra("model", model);
        context.startActivity(intent);
    }

    public static class Builder {
        private UpdateWrapper mUpdateWrapper = new UpdateWrapper();

        public Builder(Context context) {
            mUpdateWrapper.mContext = context;
        }

        public Builder setUrl(String url) {
            mUpdateWrapper.url = url;
            return this;
        }

        public Builder setTime(long time) {
            mUpdateWrapper.time = time;
            return this;
        }

        public UpdateWrapper build() {
            return mUpdateWrapper;
        }
    }
}
