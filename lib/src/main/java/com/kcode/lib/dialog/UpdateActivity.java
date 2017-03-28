package com.kcode.lib.dialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import com.kcode.lib.R;
import com.kcode.lib.base.AbstractUpdateActivity;
import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.common.Constant;

public class UpdateActivity extends AbstractUpdateActivity{

    private int notificationIcon;
    protected VersionModel mModel;
    protected String mToastMsg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setFinishOnTouchOutside(false);
        notificationIcon = getIntent().getIntExtra(Constant.NOTIFICATION_ICON, 0);
        mModel = (VersionModel) getIntent().getSerializableExtra(Constant.MODEL);
        mToastMsg = getIntent().getStringExtra(Constant.TOAST_MSG);
        if (mModel == null) {
            finish();
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, getUpdateDialogFragment())
                .commit();

    }

    public void showDownLoadProgress() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, getDownLoadDialogFragment())
                .commit();
    }

    @Override
    public void onBackPressed() {
        if (mModel.isMustUpdate()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected Fragment getUpdateDialogFragment() {
        return UpdateDialog.newInstance(mModel,mToastMsg);
    }

    @Override
    protected Fragment getDownLoadDialogFragment() {
        return DownLoadDialog.newInstance(mModel.getUrl(),notificationIcon);
    }
}
