package com.kcode.lib.net;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;
import okio.BufferedSource;
import okio.ForwardingSource;
import okio.Okio;
import okio.Source;

/**
 * Created by caik on 2017/3/8.
 */

public class DownLoadTask extends Thread {

    private String mFilePath;
    private String mDownLoadUrl;
    private ProgressListener mProgressListener;
    private OkHttpClient mClient;

    public DownLoadTask(String filePath, String downLoadUrl, ProgressListener progressListener) {
        mDownLoadUrl = downLoadUrl;
        mFilePath = filePath;
        mProgressListener = progressListener;
    }

    @Override
    public void run() {
        Request request = new Request.Builder()
                .url(mDownLoadUrl)
                .tag("download")
                .build();

        mClient = new OkHttpClient.Builder()
                .addNetworkInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        Response originalResponse = chain.proceed(chain.request());
                        return originalResponse.newBuilder()
                                .body(new ProgressResponseBody(originalResponse.body(), mProgressListener))
                                .build();
                    }
                })
                .build();

        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //写入文件
                int len;
                byte[] buf = new byte[2048];
                InputStream inputStream = response.body().byteStream();
                File file = new File(mFilePath);
                if (file.exists()) {
                    file.delete();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file);

                while ((len = inputStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, len);
                }

                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();
            }
        });

    }

    public void cancel() {

        for(Call call : mClient.dispatcher().queuedCalls()) {
            if(call.request().tag().equals("sss"))
                call.cancel();
        }

        for (Call call : mClient.dispatcher().runningCalls()) {
            if (call.request().tag().equals("download")){
                call.cancel();
            }
        }
    }


    private static class ProgressResponseBody extends ResponseBody {

        private final ResponseBody responseBody;
        private final ProgressListener progressListener;
        private BufferedSource bufferedSource;

        public ProgressResponseBody(ResponseBody responseBody, ProgressListener progressListener) {
            this.responseBody = responseBody;
            this.progressListener = progressListener;
        }

        @Override
        public MediaType contentType() {
            return responseBody.contentType();
        }

        @Override
        public long contentLength() {
            return responseBody.contentLength();
        }

        @Override
        public BufferedSource source() {
            if (bufferedSource == null) {
                bufferedSource = Okio.buffer(source(responseBody.source()));
            }
            return bufferedSource;
        }

        private Source source(Source source) {
            return new ForwardingSource(source) {
                long totalBytesRead = 0L;

                @Override
                public long read(Buffer sink, long byteCount) throws IOException {
                    long bytesRead = super.read(sink, byteCount);
                    // read() returns the number of bytes read, or -1 if this source is exhausted.
                    totalBytesRead += bytesRead != -1 ? bytesRead : 0;
                    progressListener.update(totalBytesRead, responseBody.contentLength(), bytesRead == -1);
                    return bytesRead;
                }
            };
        }
    }

    public interface ProgressListener {
        void update(long bytesRead, long contentLength, boolean done);
    }
}
