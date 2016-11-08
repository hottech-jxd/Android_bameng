package com.bameng.ui.business;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.SystemTools;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyAlliesActivity extends BaseActivity {

    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    private int currentIndex = 0;
    @Bind(R.id.ApplyLabel)
    TextView ApplyLabel;
    @Bind(R.id.AlliesLabel)
    TextView AlliesLabel;
    public Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_allies);
        ButterKnife.bind(this);
        initView();
        application = (BaseApplication) this.getApplication();
        resources = this.getResources();
        StartApi();
        changeIndex(currentIndex);
    }

    @Override
    protected void initView() {
        titleText.setText("我的联盟");
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
    }

    private void changeIndex(int index) {
        if (index == 0) {
            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
            Drawable drawable_normal = resources.getDrawable(R.color.white);
            SystemTools.loadBackground(ApplyLabel, drawable_press);
            SystemTools.loadBackground(AlliesLabel, drawable_normal);
            ApplyLabel.setTextColor(resources.getColor(R.color.red));
            AlliesLabel.setTextColor(resources.getColor(R.color.black));
        } else if (index == 1) {
            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
            Drawable drawable_normal = resources.getDrawable(R.color.white);
            SystemTools.loadBackground(ApplyLabel, drawable_normal);
            SystemTools.loadBackground(AlliesLabel, drawable_press);
            ApplyLabel.setTextColor(resources.getColor(R.color.black));
            AlliesLabel.setTextColor(resources.getColor(R.color.red));
        }
    }
    @Override
    protected void StartApi() {

    }

    @OnClick(R.id.ApplyLabel)
    void clickapply() {
        // raidersViewPager.setCurrentItem(1);
        changeIndex(0);
    }

    @OnClick(R.id.AlliesLabel)
    void clickallies() {
        //raidersViewPager.setCurrentItem(2);
        changeIndex(1);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
