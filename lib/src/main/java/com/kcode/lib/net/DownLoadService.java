package com.kcode.lib.net;

import android.app.NotificationManager;
import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.Environment;
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
        filePath = getFilePath(url);
        mDownLoadTask = new DownLoadTask(filePath, url, new DownLoadTask.ProgressListener() {
            @Override
            public void update(long bytesRead, long contentLength, boolean done) {

                if (isBackground) {
                    if (done) {
                        //下载完成，直接进行安装
                        startActivity(FileUtils.openApkFile(new File(filePath)));
                    } else {
                        int currentProgress = (int) (bytesRead * 100 / contentLength);
                        if (currentProgress < 1) {
                            currentProgress = 1;
                        }
                        notification(currentProgress);
                    }
                    return;
                }
                if (mProgressListener != null) {
                    mProgressListener.update(bytesRead, contentLength, done);
                }


            }
        });
        mDownLoadTask.start();
    }

    public void setBackground(boolean background) {
        isBackground = background;
    }

    private String getFilePath(String url) {
        String filePath = getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String fileName;
        if (url.endsWith(".apk")) {
            int index = url.lastIndexOf("/");
            if (index != -1) {
                fileName = url.substring(index);
            } else {
                fileName = getPackageName() + ".apk";
            }
        } else {
            fileName = getPackageName() + ".apk";
        }

        File file = new File(filePath, fileName);
        return file.getAbsolutePath();
    }

    private final DownLoadBinder mDownLoadBinder = new DownLoadBinder();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mDownLoadBinder;
    }

    public void cancel() {
        mDownLoadTask.cancel();
        mDownLoadTask.interrupt();
        mDownLoadTask = null;
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
        mNotificationManager.notify(0, mBuilder.build());

    }

    private void notification(int current) {
        if (mBuilder == null) {
            showNotification(current);
            return;
        }
        mBuilder.setProgress(100, current, false);
        mNotificationManager.notify(0, mBuilder.build());
    }

}
