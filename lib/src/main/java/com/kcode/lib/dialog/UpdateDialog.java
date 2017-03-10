package com.kcode.lib.dialog;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.kcode.lib.R;
import com.kcode.lib.base.AbstractFragment;
import com.kcode.lib.bean.VersionModel;
import com.kcode.lib.common.Constant;
import com.kcode.lib.utils.PackageUtils;
import com.kcode.lib.utils.PublicFunctionUtils;

/**
 * Created by caik on 2017/3/8.
 */

public class UpdateDialog extends AbstractFragment implements View.OnClickListener {

    private UpdateActivity mActivity;
    protected VersionModel mModel;

    public static UpdateDialog newInstance(VersionModel model) {

        Bundle args = new Bundle();
        args.putSerializable(Constant.MODEL, model);
        UpdateDialog fragment = new UpdateDialog();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mModel = (VersionModel) getArguments().getSerializable(Constant.MODEL);
        closeIfNoNewVersionUpdate();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(getLayout(), container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setContent(view, R.id.tvContent);
    }

    private void closeIfNoNewVersionUpdate() {
        if (mModel.getVersionCode() <= PackageUtils.getVersionCode(getContext())) {
            isLatest();
            getActivity().finish();
        }
    }

    private String getContent() {
        StringBuilder sb = new StringBuilder();
        sb.append("版本号：")
                .append(mModel.getVersionName())
                .append("\n")
                .append("\n")
                .append("更新内容：")
                .append("\n")
                .append(mModel.getContent().replaceAll("#", "\\\n"));
        return sb.toString();
    }

    private void isLatest() {
        Toast.makeText(getContext(), "当前已是最新版本", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btnCancel) {
            onCancel();
        } else if (id == R.id.btnUpdate) {
            onUpdate();
        }
    }

    protected void onCancel() {
        getActivity().finish();
    }

    protected void onUpdate() {
        mActivity.showDownLoadProgress();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (activity instanceof UpdateActivity) {
            mActivity = (UpdateActivity) activity;
        }
    }

    @Override
    protected int getLayout() {
        return R.layout.fragment_update;
    }

    @Override
    protected void setContent(View view,int contentId) {
        TextView tvContext = (TextView) view.findViewById(contentId);
        tvContext.setText(getContent());
    }

    protected void initIfMustUpdate(View view,int id) {
        if (mModel.isMustUpdate()) {
            view.findViewById(id).setVisibility(View.GONE);
            PublicFunctionUtils.setLastCheckTime(getContext(), 0);
        }
    }

    @Override
    protected void initView(View view) {
        bindUpdateListener(view,R.id.btnUpdate);
        bindCancelListener(view, R.id.btnCancel);
        initIfMustUpdate(view,R.id.btnCancel);
    }

    @Override
    protected void bindUpdateListener(View view,int updateId) {
        view.findViewById(updateId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onUpdate();
            }
        });
    }

    @Override
    protected void bindCancelListener(View view, int cancelId) {
        view.findViewById(cancelId).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onCancel();
            }
        });
    }
}
