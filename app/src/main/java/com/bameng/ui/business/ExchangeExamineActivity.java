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

public class ExchangeExamineActivity extends BaseActivity {
    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    private int currentIndex = 0;
    @Bind(R.id.nodoneLabel)
    TextView nodoneLabel;
    @Bind(R.id.doneLabel)
    TextView doneLabel;
    public Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_examine);
        ButterKnife.bind(this);
        initView();
        application = (BaseApplication) this.getApplication();
        resources = this.getResources();
        StartApi();
        changeIndex(currentIndex);
    }

    @Override
    protected void initView() {
        titleText.setText("兑换审核");
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
    }

    private void changeIndex(int index) {
        if (index == 0) {
            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
            Drawable drawable_normal = resources.getDrawable(R.color.white);
            SystemTools.loadBackground(nodoneLabel, drawable_press);
            SystemTools.loadBackground(doneLabel, drawable_normal);
            nodoneLabel.setTextColor(resources.getColor(R.color.red));
            doneLabel.setTextColor(resources.getColor(R.color.black));
        } else if (index == 1) {
            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
            Drawable drawable_normal = resources.getDrawable(R.color.white);
            SystemTools.loadBackground(nodoneLabel, drawable_normal);
            SystemTools.loadBackground(doneLabel, drawable_press);
            nodoneLabel.setTextColor(resources.getColor(R.color.black));
            doneLabel.setTextColor(resources.getColor(R.color.red));
        }
    }
    @Override
    protected void StartApi() {

    }
    @OnClick(R.id.nodoneLabel)
    void clickDoing() {
        // raidersViewPager.setCurrentItem(1);
        changeIndex(0);
    }

    @OnClick(R.id.doneLabel)
    void clickDone() {
        //raidersViewPager.setCurrentItem(2);
        changeIndex(1);
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
