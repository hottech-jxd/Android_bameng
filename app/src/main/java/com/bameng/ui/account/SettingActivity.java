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
import com.bameng.R2;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SettingActivity extends BaseActivity {

    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R2.id.activity_setting)
    LinearLayout activitySetting;
    @BindView(R2.id.titleLayoutL)
    RelativeLayout titleLayoutL;
    @BindView(R2.id.changePswL)
    LinearLayout changePswL;
    @BindView(R2.id.cleanL)
    LinearLayout cleanL;
    @BindView(R2.id.aboutAppL)
    LinearLayout aboutAppL;
    @BindView(R2.id.agreementL)
    LinearLayout agreementL;
    public Resources resources;
    @BindView(R2.id.btn_signout)
    Button btn_signout;
    @BindView(R2.id.txtCache)
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
        //Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

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
