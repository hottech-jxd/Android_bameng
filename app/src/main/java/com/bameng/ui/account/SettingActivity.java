package com.bameng.ui.account;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.config.Constants;
import com.bameng.model.CloseEvent;
import com.bameng.ui.WebViewActivity;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.login.PhoneLoginActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.DataCleanManager;
import com.bameng.utils.PreferenceHelper;
import com.bameng.utils.SystemTools;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
    @Bind(R.id.txtCache)
    TextView tvCache;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        EventBus.getDefault().register(this);

        ButterKnife.bind(this);
        resources = this.getResources();
        initView();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initView() {

        titleText.setText("设置");
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);

        try {
            tvCache.setText(DataCleanManager.getTotalCacheSize(this));
        }catch (Exception ex){
            Log.e( SettingActivity.class.getName() , "获得缓存大小失败" );
            tvCache.setText("0");
        }
    }
    @OnClick(R.id.changePswL) void changePswLClick() {
        ActivityUtils.getInstance().showActivity(SettingActivity.this,ChangePswActivity.class);
    }
    @OnClick(R.id.cleanL) void cleanLClick() {
        DataCleanManager.clearAllCache(this);
        tvCache.setText("0");
    }
    @OnClick(R.id.aboutAppL) void aboutAppLClick() {
        Intent intent = new Intent(this,WebViewActivity.class);
        intent.putExtra(Constants.INTENT_URL, BaseApplication.BaseData().getAboutUrl());
        ActivityUtils.getInstance().showActivity(SettingActivity.this, intent );
    }

    @OnClick(R.id.agreementL) void agreementLClick() {
        Intent intent = new Intent(this,WebViewActivity.class);
        intent.putExtra(Constants.INTENT_URL, BaseApplication.BaseData().getAgreementUrl());
        ActivityUtils.getInstance().showActivity(SettingActivity.this, intent );
    }
    @OnClick(R.id.btn_signout) void btn_signoutClick() {
        BaseApplication.single.writeUserToken("");
        EventBus.getDefault().post(new CloseEvent());
        ActivityUtils.getInstance().skipActivity(this, PhoneLoginActivity.class);
    }

    @Override
    protected void StartApi() {
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEventClose(CloseEvent event){
        finish();
    }
}
