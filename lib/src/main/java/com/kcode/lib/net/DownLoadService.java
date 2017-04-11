package com.kcode.lib.net;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;

import com.kcode.lib.R;
import com.kcode.lib.utils.FileUtils;

import java.io.File;

/**
 * Created by caik on 2017/3/8.
 */

public class DownLoadService extends Service {

    private static final int NOTIFICATION_ID = 0;
    private int notificationIcon;
    private String filePath;
    private boolean isBackground = false;
    private DownLoadTask mDownLoadTask;
    private DownLoadTask.ProgressListener mProgressListener;
    private NotificationManager mNotificationManager;
    private NotificationCompat.Builder mBuilder;

    @Override
    public void onCreate() {
        super.onCreate();
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

    }

    public void startDownLoad(String url) {
        filePath = FileUtils.getApkFilePath(getApplicationContext(),url);
        mDownLoadTask = new DownLoadTask(filePath, url, new DownLoadTask.ProgressListener() {
            @Override
            public void done() {
                mNotificationManager.cancel(NOTIFICATION_ID);
                if (isBackground) {
                    //下载完成，直接进行安装
                    startActivity(FileUtils.openApkFile(getApplicationContext(),new File(filePath)));
                }else {
                    if (mProgressListener != null) {
                        mProgressListener.done();
                    }
                }
            }

            @Override
            public void update(long bytesRead, long contentLength) {
                if (isBackground) {
                    int currentProgress = (int) (bytesRead * 100 / contentLength);
                    if (currentProgress < 1) {
                        currentProgress = 1;
                    }
                    notification(currentProgress);
                    return;
                }

                if (mProgressListener != null) {
                    mProgressListener.update(bytesRead, contentLength);
                }

            }

            @Override
            public void onError() {
                if (mProgressListener != null) {
                    mProgressListener.onError();
                }
                cancelNotification();
            }
        });
        mDownLoadTask.start();
    }

    public void setBackground(boolean background) {
        isBackground = background;
    }

    private final DownLoadBinder mDownLoadBinder = new DownLoadBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mDownLoadBinder;
    }

    public void cancel() {
        if (mDownLoadBinder != null) {
            mDownLoadTask.interrupt();
            mDownLoadTask = null;
        }

    }

    public void setNotificationIcon(int notificationIcon) {
        this.notificationIcon = notificationIcon;
    }

    public class DownLoadBinder extends Binder {
        public DownLoadService getService() {
            return DownLoadService.this;
        }
    }

    public void registerProgressListener(DownLoadTask.ProgressListener progressListener) {
        mProgressListener = progressListener;
    }

    public void showNotification(int current) {
        mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setContentTitle("文件下载")
                .setContentText("正在下载中...")
                .setSmallIcon(notificationIcon == 0 ? R.drawable.ic_launcher : notificationIcon);
        mBuilder.setProgress(100, current, false);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());

    }

    private void notification(int current) {
        if (mBuilder == null) {
            showNotification(current);
            return;
        }
        mBuilder.setProgress(100, current, false);
        mNotificationManager.notify(NOTIFICATION_ID, mBuilder.build());
    }

    private void cancelNotification() {
        mNotificationManager.cancel(NOTIFICATION_ID);
    }

}
