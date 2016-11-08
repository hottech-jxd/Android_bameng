package com.bameng.ui.business;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.SystemTools;

import butterknife.Bind;
import butterknife.ButterKnife;

public class RwordActivity extends BaseActivity {

    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    public Resources resources;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rword);
        ButterKnife.bind(this);
        initView();
        application = (BaseApplication) this.getApplication();
        resources = this.getResources();
        StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("盟友奖励设置");
        titleLeftImage.setVisibility(View.VISIBLE);
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
