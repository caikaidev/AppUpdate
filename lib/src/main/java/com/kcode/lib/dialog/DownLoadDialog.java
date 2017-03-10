package com.kcode.lib.dialog;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.kcode.lib.R;
import com.kcode.lib.common.Constant;
import com.kcode.lib.log.L;
import com.kcode.lib.net.DownLoadService;
import com.kcode.lib.net.DownLoadTask;
import com.kcode.lib.utils.FileUtils;

import java.io.File;

/**
 * Created by caik on 2017/3/9.
 */

public class DownLoadDialog extends DialogFragment implements View.OnClickListener {

    private static final String TAG = "DownLoadDialog";
    private static final String TITLE_FORMAT = "正在下载（%s/%s）";
    private String mDownloadUrl;
    private int notificationIcon;
    private int currentProgress;
    private Button mBtnCancel;
    private Button mBtnBackground;
    private TextView mTvTitle;
    private ProgressBar mProgressBar;
    private DownLoadService mDownLoadService;

    public static DownLoadDialog newInstance(String downLoadUrl,int notificationIcon) {

        Bundle args = new Bundle();
        args.putString(Constant.URL, downLoadUrl);
        args.putInt(Constant.NOTIFICATION_ICON,notificationIcon);
        DownLoadDialog fragment = new DownLoadDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        mDownloadUrl = getArguments().getString(Constant.URL);
        notificationIcon = getArguments().getInt(Constant.NOTIFICATION_ICON);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTvTitle = (TextView) view.findViewById(R.id.title);
        mBtnCancel = (Button) view.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);
        mBtnBackground = (Button) view.findViewById(R.id.btnBackground);
        mBtnBackground.setOnClickListener(this);
        mProgressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        mProgressBar.setMax(100);

        Intent intent = new Intent(getContext(), DownLoadService.class);
        getActivity().bindService(intent,mConnection , Context.BIND_AUTO_CREATE);

    }

    private ServiceConnection mConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            DownLoadService.DownLoadBinder binder = (DownLoadService.DownLoadBinder) service;
            mDownLoadService = binder.getService();
            mDownLoadService.registerProgressListener(mProgressListener);
            mDownLoadService.startDownLoad(mDownloadUrl);
            mDownLoadService.setNotificationIcon(notificationIcon);
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mDownLoadService = null;
        }
    };

    private DownLoadTask.ProgressListener mProgressListener = new DownLoadTask.ProgressListener() {
        @Override
        public void update(final long bytesRead, final long contentLength, final boolean done) {

            currentProgress = (int) (bytesRead * 100 / contentLength);
            if (currentProgress < 1) {
                currentProgress = 1;
            }
            L.d(TAG, "" + bytesRead + "," + contentLength + ";current=" + currentProgress);
            Message message = mHandler.obtainMessage();
            message.what = done ? DONE : LOADING;
            message.arg1 = currentProgress;
            Bundle bundle = new Bundle();
            bundle.putLong("bytesRead", bytesRead);
            bundle.putLong("contentLength", contentLength);
            message.setData(bundle);
            message.sendToTarget();
        }
    };

    private String getFilePath() {
        String filePath = getContext().getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath();
        String fileName;
        if (mDownloadUrl.endsWith(".apk")) {
            int index = mDownloadUrl.lastIndexOf("/");
            if (index != -1) {
                fileName = mDownloadUrl.substring(index);
            } else {
                fileName = getActivity().getPackageName() + ".apk";
            }
        } else {
            fileName = getActivity().getPackageName() + ".apk";
        }

        File file = new File(filePath, fileName);
        return file.getAbsolutePath();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnCancel) {
            doCancel();
        } else if (id == R.id.btnBackground) {
            doBackground();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unbindService(mConnection);
    }

    private void doCancel() {
        mDownLoadService.cancel();
        getActivity().finish();
        Toast.makeText(getContext(), "已取消更新", Toast.LENGTH_SHORT).show();
    }

    private void doBackground() {
        mDownLoadService.setBackground(true);
        mDownLoadService.showNotification(currentProgress);
        getActivity().finish();
        Toast.makeText(getContext(), "正在后台进行更新", Toast.LENGTH_SHORT).show();
    }

    private final static int LOADING = 1000;
    private final static int DONE = 1001;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    mProgressBar.setProgress(msg.arg1);
                    Bundle bundle = msg.getData();
                    long bytesRead = bundle.getLong("bytesRead");
                    long contentLength = bundle.getLong("contentLength");
                    mTvTitle.setText(String.format(TITLE_FORMAT,
                            FileUtils.setFileSize(bytesRead),
                            FileUtils.setFileSize(contentLength)));
                    break;
                case DONE:
                    getActivity().startActivity(FileUtils.openApkFile(new File(getFilePath())));
                    getActivity().finish();
                    Toast.makeText(getActivity(), "下载完成", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

}
