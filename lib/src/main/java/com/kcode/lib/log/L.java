package com.kcode.lib.log;

import android.util.Log;

import com.kcode.lib.BuildConfig;

/**
 * Created by caik on 2017/3/8.
 */

public class L {
    public static void d(String tag,String msg) {
        if (BuildConfig.DEBUG) {
            Log.d(tag, msg);
        }
    }

    public static void e(String tag,String msg) {
        if (BuildConfig.DEBUG) {
            Log.e(tag, msg);
        }
    }
}
