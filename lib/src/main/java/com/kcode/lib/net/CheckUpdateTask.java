package com.kcode.lib.net;

import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.log.L;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by caik on 2017/3/8.
 */

public class CheckUpdateTask extends Thread {

    private static final String TAG = "CheckUpdateTask";

    private Callback mCallBack;
    private String mCheckUpdateUrl;

    public CheckUpdateTask(String checkUpdateUrl, Callback callBack) {
        mCheckUpdateUrl = checkUpdateUrl;
        this.mCallBack = callBack;
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(mCheckUpdateUrl);
            connection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            String data = read(in);
            L.d(TAG, "result:" + data);
            VersionModel model = new VersionModel();
            model.parse(data);
            mCallBack.callBack(model);

        } catch (MalformedURLException e) {
            e.printStackTrace();
            mCallBack.callBack(null);
        } catch (IOException e) {
            e.printStackTrace();
            mCallBack.callBack(null);
        }   finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    private static String read(final InputStream in) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        int b;
        while((b = in.read()) != -1) {
            out.write(b);
        }
        return out.toString();
    }

    public interface Callback {
        void callBack(VersionModel model);
    }
}
