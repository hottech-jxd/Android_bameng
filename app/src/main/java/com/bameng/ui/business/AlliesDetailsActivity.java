package com.bameng.ui.business;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.SystemTools;
import com.bameng.widgets.UserInfoView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class AlliesDetailsActivity extends BaseActivity {

    @Bind(R.id.txt_user)
    TextView txtUser;
    @Bind(R.id.txt_name)
    TextView txtName;
    @Bind(R.id.txt_realname)
    TextView txtRealname;
    @Bind(R.id.txt_rank)
    TextView txtRank;
    @Bind(R.id.txt_sex)
    TextView txtSex;
    @Bind(R.id.txt_submitnum)
    TextView txtSubmitnum;
    @Bind(R.id.txt_doneorder)
    TextView txtDoneorder;
    @Bind(R.id.txt_phone)
    TextView txtPhone;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.txt_time)
    TextView txtTime;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_allies_details);
        ButterKnife.bind(this);
        initView();
        StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("盟友详情");
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
