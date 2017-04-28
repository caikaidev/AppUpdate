package com.kcode.lib.dialog;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.ViewGroup;

import com.kcode.lib.R;
import com.kcode.lib.base.AbstractUpdateActivity;
import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.common.Constant;
import com.kcode.lib.utils.PackageUtils;

public class UpdateActivity extends AbstractUpdateActivity implements DownLoadDialog.OnFragmentOperation {

    private int notificationIcon;
    protected VersionModel mModel;
    protected String mToastMsg;
    protected boolean mIsShowToast;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setFinishOnTouchOutside(false);
        notificationIcon = getIntent().getIntExtra(Constant.NOTIFICATION_ICON, 0);
        mModel = (VersionModel) getIntent().getSerializableExtra(Constant.MODEL);
        mToastMsg = getIntent().getStringExtra(Constant.TOAST_MSG);
        mIsShowToast = getIntent().getBooleanExtra(Constant.IS_SHOW_TOAST_MSG, true);
        if (mModel == null) {
            finish();
            return;
        }

        showUpdateDialog();

    }

    private void showUpdateDialog() {
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
        if (PackageUtils.getVersionCode(getApplicationContext()) < mModel.getMinSupport()) {
            return;
        }
        super.onBackPressed();
    }

    @Override
    protected Fragment getUpdateDialogFragment() {
        return UpdateDialog.newInstance(mModel, mToastMsg, mIsShowToast);
    }

    @Override
    protected Fragment getDownLoadDialogFragment() {
        return DownLoadDialog.newInstance(mModel.getUrl(), notificationIcon, PackageUtils.getVersionCode(getApplicationContext()) < mModel.getMinSupport());
    }

    @Override
    public void onFailed() {
        showUpdateDialog();
    }
}
