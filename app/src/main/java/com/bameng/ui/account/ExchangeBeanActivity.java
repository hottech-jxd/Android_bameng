package com.bameng.ui.account;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.SystemTools;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExchangeBeanActivity extends BaseActivity {

    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.txt_mbean)
    TextView txtMbean;
    @Bind(R.id.tv_record)
    TextView tvRecord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_bean);
        ButterKnife.bind(this);
        initView();
        StartApi();

    }

    @OnClick(R.id.btn_exchange) void onBtnExchangeClick() {
        ActivityUtils.getInstance().showActivity(ExchangeBeanActivity.this,ExchangeConfirmActivity.class);
    }
    @OnClick(R.id.tv_record) void onrecordClick(){
        ActivityUtils.getInstance().showActivity(ExchangeBeanActivity.this,ExchangeRecordActivity.class);
    }

    @Override
    protected void initView() {
        titleText.setText("兑换");
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
