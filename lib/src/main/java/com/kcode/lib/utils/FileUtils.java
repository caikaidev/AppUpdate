package com.kcode.lib.utils;

import android.content.Intent;
import android.net.Uri;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by caik on 2017/3/9.
 */

public class FileUtils {
    /**
     * 文件大小单位转换
     *
     * @param size
     * @return
     */
    public static String setFileSize(long size) {
        DecimalFormat df = new DecimalFormat("###.##");
        float f = ((float) size / (float) (1024 * 1024));

        if (f < 1.0) {
            float f2 = ((float) size / (float) (1024));

            return df.format(new Float(f2).doubleValue()) + "KB";

        } else {
            return df.format(new Float(f).doubleValue()) + "M";
        }

    }

    public static Intent openApkFile(File outputFile) {
        Intent intent = new Intent();
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setAction(android.content.Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(outputFile),
                "application/vnd.android.package-archive");
        return intent;
    }
}
