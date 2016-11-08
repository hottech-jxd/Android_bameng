package com.bameng.ui.account;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.SystemTools;
import com.huotu.android.library.libedittext.EditText;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhoneChangeActivity extends BaseActivity {

    @Bind(R.id.edtPhone)
    EditText edtPhone;
    @Bind(R.id.edtCode)
    EditText edtCode;
    @Bind(R.id.btn_code)
    TextView btnCode;
    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    public Resources resources;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_reg);
        ButterKnife.bind(this);
        initView();
        StartApi();
    }

    @OnClick(R.id.btn_commit)
    void onBtnCommitClick() {
        //TODO implement
    }

    @Override
    protected void initView() {
        titleText.setText("手机");
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
