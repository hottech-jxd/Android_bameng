package com.bameng.ui.business;

import android.graphics.drawable.Drawable;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.SystemTools;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AlliesExamineActivity extends BaseActivity {

    @Bind(R.id.txt_realname)
    TextView txtRealname;
    @Bind(R.id.txt_rank)
    TextView txtRank;
    @Bind(R.id.txt_sex)
    TextView txtSex;
    @Bind(R.id.txt_phone)
    TextView txtPhone;
    @Bind(R.id.txt_time)
    TextView txtTime;
    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allies_examine);
        ButterKnife.bind(this);
        initView();
        StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("盟友审核详情");
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
