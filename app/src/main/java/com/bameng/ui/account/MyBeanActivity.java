package com.bameng.ui.account;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.SystemTools;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MyBeanActivity extends BaseActivity {

    @Bind(R.id.txt_income)
    TextView txtIncome;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.txt_outbean)
    TextView txtOutbean;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_bean);
        ButterKnife.bind(this);
        initView();
        StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("盟豆");
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
