package com.bameng.ui.account;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.ui.WebViewActivity;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.PreferenceHelper;
import com.bameng.utils.SystemTools;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.activity_setting)
    LinearLayout activitySetting;
    @Bind(R.id.titleLayoutL)
    RelativeLayout titleLayoutL;
    @Bind(R.id.changePswL)
    LinearLayout changePswL;
    @Bind(R.id.cleanL)
    LinearLayout cleanL;
    @Bind(R.id.aboutAppL)
    LinearLayout aboutAppL;
    @Bind(R.id.agreementL)
    LinearLayout agreementL;
    public Resources resources;
    @Bind(R.id.btn_signout)
    Button btn_signout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        ButterKnife.bind(this);
        resources = this.getResources();
        initView();
    }

    @Override
    protected void initView() {
        titleText.setText("设置");
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
    }
    @OnClick(R.id.changePswL) void changePswLClick() {
        ActivityUtils.getInstance().showActivity(SettingActivity.this,ChangePswActivity.class);
    }
    @OnClick(R.id.cleanL) void cleanLClick() {

    }
    @OnClick(R.id.aboutAppL) void aboutAppLClick() {
        ActivityUtils.getInstance().showActivity(SettingActivity.this, WebViewActivity.class);

    }
    @OnClick(R.id.agreementL) void agreementLClick() {

    }
    @OnClick(R.id.btn_signout) void btn_signoutClick() {
        BaseApplication.single.writeUserToken("");
        ActivityUtils.getInstance().skipActivity(this, PhoneLoginActivity.class);
    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
