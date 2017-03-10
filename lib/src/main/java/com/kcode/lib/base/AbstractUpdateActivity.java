package com.kcode.lib.base;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by caik on 2017/3/10.
 */

public abstract class AbstractUpdateActivity extends AppCompatActivity {
    protected abstract Fragment getUpdateDialogFragment();

    protected abstract Fragment getDownLoadDialogFragment();
}
