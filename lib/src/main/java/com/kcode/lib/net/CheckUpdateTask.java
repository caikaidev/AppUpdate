package com.kcode.lib.net;

import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.log.L;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by caik on 2017/3/8.
 */

public class CheckUpdateTask extends Thread {

    private static final String TAG = "CheckUpdateTask";

    private Callback mCallBack;
    private String mCheckUpdateUrl;

    private OkHttpClient mOkHttpClient = new OkHttpClient();

    public CheckUpdateTask(String checkUpdateUrl, Callback callBack) {
        mCheckUpdateUrl = checkUpdateUrl;
        this.mCallBack = callBack;
    }

    @Override
    public void run() {

        Request request = new Request.Builder()
                .url(mCheckUpdateUrl)
                .build();

        mOkHttpClient.newCall(request).enqueue(new okhttp3.Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                mCallBack.isLatestVersion();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String result = response.body().string();
                L.d(TAG,result);
                VersionModel model = new VersionModel();
                model.parse(result);
                mCallBack.callBack(model);

            }
        });

    }

    public interface Callback {
        void callBack(VersionModel model);

        void isLatestVersion();
    }
}
