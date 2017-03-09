package com.kcode.lib.net;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.common.Constant;
import com.kcode.lib.dialog.UpdateActivity;
import com.kcode.lib.log.L;
import com.kcode.lib.utils.PackageUtils;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by caik on 2017/3/8.
 */

public class DownLoadService extends IntentService {

    private static final String TAG = "DownLoadService";

    private String url;
    private long time;

    private OkHttpClient mOkHttpClient = new OkHttpClient();

    public DownLoadService() {
        super(DownLoadService.class.getSimpleName());
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        initData(intent);
        try {
            String result = requestVersionFromServer();
            VersionModel model = new VersionModel();
            model.parse(result);
            showDialogIfNeedUpdate(model);

        } catch (IOException e) {
            isLatest();
            L.e(TAG,"服务器请求失败");
        }
    }

    private void initData(Intent intent) {
        url = intent.getStringExtra(Constant.URL);
        time = intent.getLongExtra(Constant.TIME, 0);
    }

    private String requestVersionFromServer() throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = mOkHttpClient.newCall(request).execute();
        return response.body().toString();
    }

    private void showDialogIfNeedUpdate(VersionModel model) {
        if (model.getVersionCode() > PackageUtils.getVersionCode(getApplicationContext())) {
            L.d(TAG,"有版本更新");
            Intent intent = new Intent(DownLoadService.this, UpdateActivity.class);
            startActivity(intent);
        }else {
            isLatest();
        }
    }

    private void isLatest() {
        Toast.makeText(getApplicationContext(), "当前已是最新版本", Toast.LENGTH_SHORT).show();
    }
}
