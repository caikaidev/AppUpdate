package com.kcode.lib.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import java.io.File;

/**
 * Created by caik on 2017/3/9.
 */

public class FileUtils {

    public static String getApkFilePath(Context context,String downLoadUrl) {
        File externalFile = context.getExternalFilesDir(null);
        String filePath = externalFile.getAbsolutePath();
        String fileName;
        if (downLoadUrl.endsWith(".apk")) {
            int index = downLoadUrl.lastIndexOf("/");
            if (index != -1) {
                fileName = downLoadUrl.substring(index);
            } else {
                fileName = context.getPackageName() + ".apk";
            }
        } else {
            fileName = context.getPackageName() + ".apk";
        }

        File file = new File(filePath, fileName);
        return file.getAbsolutePath();
    }

    public static Intent openApkFile(Context context,File outputFile) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", outputFile);
        }else {
            uri = Uri.fromFile(outputFile);
        }

        intent.setDataAndType(uri, "application/vnd.android.package-archive");
        return intent;
    }
}
