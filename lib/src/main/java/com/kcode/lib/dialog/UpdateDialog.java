package com.kcode.lib.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.kcode.lib.R;
import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.common.Constant;
import com.kcode.lib.log.L;
import com.kcode.lib.utils.PackageUtils;
import com.kcode.lib.utils.PublicFunctionUtils;

/**
 * Created by caik on 2017/3/8.
 */

public class UpdateDialog extends DialogFragment implements View.OnClickListener{

    private static final String TAG = "UpdateDialog";

    private UpdateActivity mActivity;
    private VersionModel mModel;
    private TextView mTvContent;
    private Button mBtnCancel;
    private Button mBtnUpdate;

    public static UpdateDialog newInstance(VersionModel model) {

        Bundle args = new Bundle();
        args.putSerializable(Constant.MODEL, model);
        UpdateDialog fragment = new UpdateDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mModel = (VersionModel) getArguments().getSerializable(Constant.MODEL);
        return inflater.inflate(R.layout.fragment_update,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mTvContent = (TextView) view.findViewById(R.id.tvContent);
        mBtnCancel = (Button) view.findViewById(R.id.btnCancel);
        mBtnCancel.setOnClickListener(this);
        mBtnUpdate = (Button) view.findViewById(R.id.btnUpdate);
        mBtnUpdate.setOnClickListener(this);

        showDialogIfNeedUpdate();
        if (mModel.isMustUpdate()) {
            mBtnCancel.setVisibility(View.GONE);
            PublicFunctionUtils.setLastCheckTime(getContext(),0);
        }
    }

    private void showDialogIfNeedUpdate() {
        if (mModel.getVersionCode() > PackageUtils.getVersionCode(getContext())) {
            L.d(TAG,"有版本更新");
            mTvContent.setText(mModel.getContent().replaceAll("#","\\\n"));
        }else {
            isLatest();
            getActivity().finish();
        }
    }

    private void isLatest() {
        Toast.makeText(getContext(), "当前已是最新版本", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnCancel) {
            onCancel();
        }else if(id == R.id.btnUpdate){
            onUpdate();
        }
    }

    private void onCancel() {
        getActivity().finish();
    }

    private void onUpdate(){
        mActivity.showDownLoadProgress();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof UpdateActivity) {
            mActivity = (UpdateActivity) activity;
        }
    }
}
