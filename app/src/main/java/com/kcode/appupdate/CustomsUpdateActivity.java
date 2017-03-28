package com.kcode.appupdate;

import android.support.v4.app.Fragment;

import com.kcode.lib.dialog.UpdateActivity;

/**
 * Created by caik on 2017/3/10.
 */

public class CustomsUpdateActivity extends UpdateActivity {
    @Override
    protected Fragment getUpdateDialogFragment() {
        return CustomsUpdateFragment.newInstance(mModel,"当前已经是最新版本");
    }
}
