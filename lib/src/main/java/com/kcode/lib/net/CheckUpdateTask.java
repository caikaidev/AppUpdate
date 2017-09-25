package com.kcode.lib.net;

import android.content.Context;

import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.log.L;
import com.kcode.lib.utils.PackageUtils;

import org.json.JSONException;

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

    private Context mContext;
    private Callback mCallBack;
    private String mCheckUpdateUrl;

    public CheckUpdateTask(Context context,String checkUpdateUrl, Callback callBack) {
        mContext = context;
        mCheckUpdateUrl = checkUpdateUrl;
        this.mCallBack = callBack;
    }

    @Override
    public void run() {
        HttpURLConnection connection = null;
        try {
            URL url = new URL(mCheckUpdateUrl);
            if (mCheckUpdateUrl.startsWith("https://")) {
                TrustAllCertificates.install();
            }

            connection = (HttpURLConnection) url.openConnection();
            InputStream in = new BufferedInputStream(connection.getInputStream());
            String data = read(in);
            L.d(TAG, "result:" + data);
            VersionModel model = new VersionModel();
            try {
                model.parse(data);
                mCallBack.callBack(model,hasNewVersion(PackageUtils.getVersionCode(mContext),model.getVersionCode()));
            } catch (JSONException e) {
                e.printStackTrace();
                mCallBack.callBack(null,false);
            }


        } catch (MalformedURLException e) {
            e.printStackTrace();
            mCallBack.callBack(null,false);
        } catch (IOException e) {
            e.printStackTrace();
            mCallBack.callBack(null,false);
        }   finally {
            if (connection != null) {
                connection.disconnect();
            }
        }

    }

    private boolean hasNewVersion(int old,int n) {
        return old < n;
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
        void callBack(VersionModel model,boolean hasNewVersion);
    }
}
