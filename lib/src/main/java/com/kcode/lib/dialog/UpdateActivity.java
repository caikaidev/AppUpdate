package com.kcode.lib.dialog;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ViewGroup;

import com.kcode.lib.R;
import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.common.Constant;

public class UpdateActivity extends AppCompatActivity{

    private int notificationIcon;
    private VersionModel mModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setFinishOnTouchOutside(false);
        notificationIcon = getIntent().getIntExtra(Constant.NOTIFICATION_ICON, 0);
        mModel = (VersionModel) getIntent().getSerializableExtra("model");
        if (mModel == null) {
            finish();
            return;
        }

        getSupportFragmentManager()
                .beginTransaction()
                .add(R.id.container, UpdateDialog.newInstance(mModel))
                .commit();

    }

    public void showDownLoadProgress() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, DownLoadDialog.newInstance(mModel.getUrl(),notificationIcon))
                .commit();
    }

}
