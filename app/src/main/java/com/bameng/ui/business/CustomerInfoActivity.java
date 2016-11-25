package com.bameng.ui.business;

import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.adapter.TabPagerAdapter;
import com.bameng.fragment.CustomDoneFrag;
import com.bameng.fragment.CustomNoDoneFrag;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.SystemTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * 客户信息界面
 */
public class CustomerInfoActivity extends BaseActivity {

    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    private int currentIndex = 0;
    @Bind(R.id.titleRightImage)
    ImageView titleRightImage;
    @Bind(R.id.nodoneLabel)
    TextView nodoneLabel;
    @Bind(R.id.doneLabel)
    TextView doneLabel;
    @Bind(R.id.customViewPager)
    ViewPager customViewPager;
    public TabPagerAdapter tabPagerAdapter;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();
    public Resources resources;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);
        ButterKnife.bind(this);
        initView();
        application = (BaseApplication) this.getApplication();
        resources = this.getResources();
        StartApi();
        changeIndex(currentIndex);
        initSwitch();
    }
    private void initSwitch() {
        CustomDoneFrag customDoneFrag = new CustomDoneFrag();
        CustomNoDoneFrag customNoDoneFrag = new CustomNoDoneFrag();
        Bundle b = new Bundle();
        b.putInt("index", 0);
        b.putInt("type",1);
        customNoDoneFrag.setArguments(b);
        mFragmentList.add(customNoDoneFrag);
        b = new Bundle();
        b.putInt("index", 1);
        b.putInt("type",2);
        customDoneFrag.setArguments(b);
        mFragmentList.add(customDoneFrag);
        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(), mFragmentList);
        customViewPager.setAdapter(tabPagerAdapter);
        customViewPager.setOffscreenPageLimit(2);
        customViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {


            }

            @Override
            public void onPageScrolled(int index, float arg1, int pixes) {
                if (pixes != 0) {
                }
                if (pixes == 0) {
                    currentIndex = index;
                    changeIndex(currentIndex);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        customViewPager.setCurrentItem(currentIndex);

    }

    @Override
    protected void initView() {
        titleText.setText("客户信息");
        Drawable rightDraw = ContextCompat.getDrawable(this , R.mipmap.ic_newadd);
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleRightImage,rightDraw);
        SystemTools.loadBackground(titleLeftImage, leftDraw);
    }

    private void changeIndex(int index) {
        if (index == 0) {
            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
            Drawable drawable_normal = resources.getDrawable(R.color.white);
            SystemTools.loadBackground(nodoneLabel, drawable_press);
            SystemTools.loadBackground(doneLabel, drawable_normal);
            //nodoneLabel.setTextColor(resources.getColor(R.color.red));
            nodoneLabel.setTextColor(Color.parseColor("#d8ae76") );
            doneLabel.setTextColor(resources.getColor(R.color.black));
        } else if (index == 1) {
            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
            Drawable drawable_normal = resources.getDrawable(R.color.white);
            SystemTools.loadBackground(nodoneLabel, drawable_normal);
            SystemTools.loadBackground(doneLabel, drawable_press);
            nodoneLabel.setTextColor(resources.getColor(R.color.black));
            //doneLabel.setTextColor(resources.getColor(R.color.red));
            doneLabel.setTextColor(Color.parseColor("#d8ae76"));
        }
    }


    @Override
    protected void StartApi() {

    }

    @OnClick(R.id.nodoneLabel)
    void clickDoing() {
        customViewPager.setCurrentItem(0);
        changeIndex(customViewPager.getCurrentItem());
    }

    @OnClick(R.id.doneLabel)
    void clickDone() {
        customViewPager.setCurrentItem(1);
        changeIndex(customViewPager.getCurrentItem());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }


    @OnClick(R.id.titleRightImage)
    void clickRightImage(){
        ActivityUtils.getInstance().showActivity(CustomerInfoActivity.this,SubmitCustomerInfoActivity.class);
    }
    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
