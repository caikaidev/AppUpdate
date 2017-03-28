package com.kcode.lib.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by caik on 2017/3/28.
 */

public class ToastUtils {

    public static void show(Context context,int msgId) {
        show(context,context.getResources().getString(msgId));
    }

    public static void show(Context context,String msg) {
        Toast.makeText(context,msg,Toast.LENGTH_SHORT).show();
    }
}
