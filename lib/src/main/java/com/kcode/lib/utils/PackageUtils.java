package com.kcode.lib.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by caik on 2017/3/8.
 */

public class PackageUtils {
    public static int getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            if (info == null) {
                return 0;
            }
            return info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            return 0;
        }
    }

    public static String getVersionName(Context context) {
        PackageManager manager = context.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            if (info == null) {
                return "1.0";
            }
            return info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            return "1.0";
        }
    }
}
