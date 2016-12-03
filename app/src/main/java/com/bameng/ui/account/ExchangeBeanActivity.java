package com.bameng.ui.account;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.SystemTools;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * 兑换界面
 */
public class ExchangeBeanActivity extends BaseActivity {

    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.txt_mbean)
    TextView txtMbean;
    @BindView(R2.id.tv_record)
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
        //Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);


        txtMbean.setText( BaseApplication.UserData().getMengBeans() );
    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

}
