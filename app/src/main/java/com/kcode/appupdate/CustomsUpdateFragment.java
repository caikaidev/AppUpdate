package com.kcode.appupdate;

import android.os.Bundle;
import android.view.View;

import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.common.Constant;
import com.kcode.lib.dialog.UpdateDialog;

/**
 * Created by caik on 2017/3/10.
 */

public class CustomsUpdateFragment extends UpdateDialog {

    public static CustomsUpdateFragment newInstance(VersionModel model,String toastMsg) {

        Bundle args = new Bundle();
        args.putSerializable(Constant.MODEL, model);
        args.putString(Constant.TOAST_MSG, toastMsg);
        CustomsUpdateFragment fragment = new CustomsUpdateFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_update_dialog;
    }

    @Override
    protected void setContent(View view, int contentId) {
        super.setContent(view, R.id.content);
    }

    @Override
    protected void bindUpdateListener(View view, int updateId) {
        super.bindUpdateListener(view, R.id.update);
    }

    @Override
    protected void bindCancelListener(View view, int cancelId) {
        super.bindCancelListener(view, R.id.cancel);
    }

    @Override
    protected void initIfMustUpdate(View view, int id) {
        super.initIfMustUpdate(view, R.id.cancel);
    }
}
