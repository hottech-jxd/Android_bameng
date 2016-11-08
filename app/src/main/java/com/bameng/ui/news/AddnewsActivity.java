package com.bameng.ui.news;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.SystemTools;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddnewsActivity extends BaseActivity {


    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.titleText)
    TextView titleText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addnews);
        ButterKnife.bind(this);
        initView();
        StartApi();
    }

    @Override
    protected void initView() {
        titleText.setText("新增资讯");
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
    }

    @Override
    protected void StartApi() {

    }

    @OnClick(R.id.chooseObjectL)
    void choose(){
        ActivityUtils.getInstance().showActivity(AddnewsActivity.this,ChooseObjectActivity.class);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
