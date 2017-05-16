package com.kcode.lib.net;

import com.kcode.lib.log.L;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by caik on 2017/3/8.
 */

public class DownLoadTask extends Thread {

    private static final String TAG = "DownLoadTask";

    private String mFilePath;
    private String mDownLoadUrl;
    private ProgressListener mProgressListener;

    public DownLoadTask(String filePath, String downLoadUrl, ProgressListener progressListener) {
        mDownLoadUrl = downLoadUrl;
        mFilePath = filePath;
        mProgressListener = progressListener;
    }

    @Override
    public void run() {

        HttpURLConnection connection = null;
        try {
            URL url = new URL(mDownLoadUrl);
            if (mDownLoadUrl.startsWith("https://")) {
                TrustAllCertificates.install();
            }
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Keep-Alive", "header");

            InputStream in = new BufferedInputStream(connection.getInputStream());
            int count = connection.getContentLength();
            if (count <= 0) {
                L.e(TAG,"file length must > 0");
                return;
            }

            if (in == null) {
                L.e(TAG,"InputStream not be null");
                return;
            }

            writeToFile(count,in,mFilePath);

        } catch (Exception e) {
            e.printStackTrace();
            if (mProgressListener != null) {
                mProgressListener.onError();
            }
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    private void writeToFile(int count,InputStream in,String filePath) throws IOException {
        int len;
        byte[] buf = new byte[2048];
        File file = new File(filePath);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        int bytesRead = 0;
        while ((len = in.read(buf)) != -1) {
            fileOutputStream.write(buf, 0, len);
            bytesRead += len;
            mProgressListener.update(bytesRead,count);
        }

        mProgressListener.done();

        fileOutputStream.flush();
        fileOutputStream.close();
        in.close();
    }

    public interface ProgressListener {
        void done();

        void update(long bytesRead, long contentLength);

        void onError();
    }
}
