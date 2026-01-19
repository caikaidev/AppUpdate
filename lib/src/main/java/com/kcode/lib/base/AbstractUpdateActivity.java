package com.kcode.lib.base;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

/**
 * Created by caik on 2017/3/10.
 */

public abstract class AbstractUpdateActivity extends AppCompatActivity {
    protected abstract Fragment getUpdateDialogFragment();

    protected abstract Fragment getDownLoadDialogFragment();
}
