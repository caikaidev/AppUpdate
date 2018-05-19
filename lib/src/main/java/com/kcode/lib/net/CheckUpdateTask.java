package com.kcode.lib.net;

import android.content.Context;

import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.log.L;
import com.kcode.lib.utils.PackageUtils;

import org.json.JSONException;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Created by caik on 2017/3/8.
 */

public class CheckUpdateTask extends Thread {

    private static final String TAG = "CheckUpdateTask";

    private Context mContext;
    private Callback mCallBack;
    private String mCheckUpdateUrl;
    private Boolean mIsPost;
    private Map<String, String> mPostParams;

    public CheckUpdateTask(Context context, String checkUpdateUrl, Boolean isPost, Map<String, String> postParams, Callback callBack) {
        mContext = context;
        mCheckUpdateUrl = checkUpdateUrl;
        mIsPost = isPost;
        mPostParams = postParams;
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

            if (mIsPost) {
                StringBuilder mStringBuilder = new StringBuilder("");
                if (mPostParams != null) {
                    Set set = mPostParams.entrySet();
                    Iterator iterator = set.iterator();
                    while (iterator.hasNext()) {
                        Map.Entry mEntry = (Map.Entry) iterator.next();
                        mStringBuilder.append(mEntry.getKey());
                        mStringBuilder.append("=");
                        mStringBuilder.append(mEntry.getValue());
                        if (iterator.hasNext()) {
                            mStringBuilder.append("&");
                        }
                    }
                }

                String urlParameters = mStringBuilder.toString();
                byte[] postData = urlParameters.getBytes(Charset.forName("UTF-8"));
                int postDataLength = postData.length;

                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
                connection.setRequestProperty("charset", "utf-8");
                connection.setRequestProperty("Content-Length", Integer.toString(postDataLength));
                connection.setDoOutput(true);
                connection.setUseCaches(false);

                DataOutputStream wr = new DataOutputStream(connection.getOutputStream());
                wr.write(postData);
                wr.flush();
            }

            InputStream in = new BufferedInputStream(connection.getInputStream());
            String data = read(in);
            in.close();
            L.d(TAG, "result:" + data);
            VersionModel model = new VersionModel();
            try {
                model.parse(data);
                mCallBack.callBack(model, hasNewVersion(PackageUtils.getVersionCode(mContext), model.getVersionCode()));
            } catch (JSONException e) {
                e.printStackTrace();
                mCallBack.callBack(null, false);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
            mCallBack.callBack(null, false);
        } catch (IOException e) {
            e.printStackTrace();
            mCallBack.callBack(null, false);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }

    private boolean hasNewVersion(int old, int n) {
        return old < n;
    }

    private static String read(final InputStream in) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        int b;
        while ((b = in.read()) != -1) {
            out.write(b);
        }
        return out.toString();
    }

    public interface Callback {
        void callBack(VersionModel model, boolean hasNewVersion);
    }
}
