package com.bameng.fragment;



import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.bameng.R;
import com.bameng.ui.account.SettingActivity;
import com.bameng.ui.base.BaseFragment;
import com.bameng.utils.ActivityUtils;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class UserFragment extends BaseFragment {

    @Bind(R.id.img_setting)
    ImageView img_setting;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        ButterKnife.bind(this, view);
    }

    public void initView(){

    }

    @Override
    public void onReshow() {

    }

    @Override
    public void onFragPasue() {

    }

    @OnClick(R.id.img_setting)
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.img_setting:
                ActivityUtils.getInstance().showActivity(getActivity(), SettingActivity.class);
                break;
            default:
                break;
        }

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_user;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
}
