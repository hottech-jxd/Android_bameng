package com.bameng.fragment;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.ui.account.MyAccountActivity;
import com.bameng.ui.account.SettingActivity;
import com.bameng.ui.account.UserInfoActivity;
import com.bameng.ui.base.BaseFragment;
import com.bameng.ui.business.MyCashCardActivity;
import com.bameng.ui.business.OrderListActivity;
import com.bameng.utils.ActivityUtils;
import com.facebook.drawee.view.SimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by 47483 on 2016.11.02.
 */
public class RichesFrag extends BaseFragment {


    @Bind(R.id.img_user)
    SimpleDraweeView img_user;
    @Bind(R.id.txt_name)
    TextView txtName;
    @Bind(R.id.txt_Points)
    TextView txtPoints;
    @Bind(R.id.img_setting)
    ImageView imgSetting;
    @Bind(R.id.layaccount)
    LinearLayout layaccount;
    @Bind(R.id.txt_mbean)
    TextView txtMbean;
    @Bind(R.id.txt_nosettlembean)
    TextView txtNosettlembean;
    @Bind(R.id.txt_integral)
    TextView txtIntegral;
    @Bind(R.id.laysign)
    LinearLayout laysign;
    @Bind(R.id.laymoneycard)
    LinearLayout laymoneycard;
    @Bind(R.id.layorder)
    LinearLayout layorder;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
    }


    @Override
    public void onReshow() {

    }

    @Override
    public void onFragPasue() {

    }

    @Override
    public void onClick(View view) {

    }
    @OnClick({R.id.img_setting,R.id.img_user,R.id.layaccount,R.id.laysign,R.id.layorder,R.id.laymoneycard})
    void onclick(View view) {
        switch (view.getId()) {
            case R.id.layaccount:
                ActivityUtils.getInstance().showActivity(getActivity(), MyAccountActivity.class);
                break;
            case R.id.img_setting:
                ActivityUtils.getInstance().showActivity(getActivity(), SettingActivity.class);
                break;
            case R.id.img_user:
                ActivityUtils.getInstance().showActivity(getActivity(), UserInfoActivity.class);
                break;
            case R.id.layorder:
                ActivityUtils.getInstance().showActivity(getActivity(), OrderListActivity.class);
                break;
            case R.id.laymoneycard:
                ActivityUtils.getInstance().showActivity(getActivity(), MyCashCardActivity.class);
                break;
            default:
                break;
        }

    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }
    @Override
    public int getLayoutRes() {
        return R.layout.frag_riches;
    }
}
