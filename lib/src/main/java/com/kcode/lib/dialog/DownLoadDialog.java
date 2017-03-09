package com.kcode.lib.dialog;

import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
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
import com.kcode.lib.log.L;
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
    private Button mBtnCancel;
    private Button mBtnBackground;
    private TextView mTvTitle;
    private ProgressBar mProgressBar;
    private DownLoadTask mDownLoadTask;

    public static DownLoadDialog newInstance(String downLoadUrl) {

        Bundle args = new Bundle();
        args.putString("url", downLoadUrl);
        DownLoadDialog fragment = new DownLoadDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_download, container, false);
        mDownloadUrl = getArguments().getString("url");
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

        mDownLoadTask = new DownLoadTask(getFilePath(), mDownloadUrl, new DownLoadTask.ProgressListener() {
            @Override
            public void update(final long bytesRead, final long contentLength, final boolean done) {

                int current = (int) (bytesRead * 100 / contentLength);
                if (current < 1) {
                    current = 1;
                }
                L.d(TAG, "" + bytesRead + "," + contentLength + ";current=" + current);
                Message message = mHandler.obtainMessage();
                message.what = done ? DONE : LOADING;
                message.arg1 = current;
                Bundle bundle = new Bundle();
                bundle.putLong("bytesRead", bytesRead);
                bundle.putLong("contentLength", contentLength);
                message.setData(bundle);
                message.sendToTarget();
            }
        });
        mDownLoadTask.start();
    }

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

    private void doCancel() {
        mDownLoadTask.cancel();
        mDownLoadTask.interrupt();
        mDownLoadTask = null;

        getActivity().finish();
        Toast.makeText(getContext(), "已取消更新", Toast.LENGTH_SHORT).show();
    }

    private void doBackground() {
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
