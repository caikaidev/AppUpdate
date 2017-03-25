package com.kcode.lib;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;

import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.common.Constant;
import com.kcode.lib.dialog.UpdateActivity;
import com.kcode.lib.log.L;
import com.kcode.lib.net.CheckUpdateTask;
import com.kcode.lib.utils.PublicFunctionUtils;

/**
 * Created by caik on 2017/3/8.
 */

public class UpdateWrapper {

    private static final String TAG = "UpdateWrapper";

    private Context mContext;
    private String url;
    private CheckUpdateTask.Callback mCallback;
    private int notificationIcon;
    private long time;
    private Class<? extends FragmentActivity> cls;

    private UpdateWrapper() {
    }

    public void start() {

        if (TextUtils.isEmpty(url)) {
            throw new RuntimeException("url not be null");
        }

        if (checkUpdateTime(time)) {
            L.d(TAG,"距离上次更新时间太近");
            return;
        }
        new CheckUpdateTask(url, new CheckUpdateTask.Callback() {
            @Override
            public void callBack(VersionModel model) {
                //记录本次更新时间
                PublicFunctionUtils.setLastCheckTime(mContext, System.currentTimeMillis());
                mCallback.callBack(model);
                start2Activity(mContext, model);
            }

            @Override
            public void isLatestVersion() {
                mCallback.isLatestVersion();
            }
        }).start();
    }

    private boolean checkUpdateTime(long time) {
        long lastCheckUpdateTime = PublicFunctionUtils.getLastCheckTime(mContext);
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCheckUpdateTime > time ){
            return false;
        }
        return true;
    }

    private void start2Activity(Context context, VersionModel model) {
        try {
            Intent intent = new Intent(context, cls == null ? UpdateActivity.class : cls);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra(Constant.MODEL, model);
            intent.putExtra(Constant.NOTIFICATION_ICON, notificationIcon);
            context.startActivity(intent);
        } catch (Exception e) {

        }

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

        public Builder setNotificationIcon(int notificationIcon) {
            mUpdateWrapper.notificationIcon = notificationIcon;
            return this;
        }

        public Builder setCustomsActivity(Class<? extends FragmentActivity> cls) {
            mUpdateWrapper.cls = cls;
            return this;
        }

        public Builder setCallback(CheckUpdateTask.Callback callback) {
            mUpdateWrapper.mCallback = callback;
            return this;
        }

        public UpdateWrapper build() {
            return mUpdateWrapper;
        }
    }
}
