package com.bameng.ui.business;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.adapter.TabPagerAdapter;
import com.bameng.fragment.OrderFragment;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.SystemTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.baidu.location.h.j.m;

/***
 * 我的订单
 */
public class OrderListActivity extends BaseActivity {
    @Bind(R.id.ListSwitch)
    LinearLayout listSwitch;
    @Bind(R.id.allLabel)
    TextView allLabel;
    @Bind(R.id.nodoneLabel)
    TextView nodoneLabel;
    @Bind(R.id.doneLabel)
    TextView doneLabel;
    @Bind(R.id.backLabel)
    TextView backLabel;
    @Bind(R.id.orderViewPager)
    ViewPager orderViewPager;
    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.titleRightText)
    TextView titleRightText;
    @Bind(R.id.titleRightImage)
    ImageView  titleRightImage;

    private Resources resources;
    private int currentIndex = 0;
    public TabPagerAdapter tabPagerAdapter;
    private List<Fragment> mFragmentList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        initView();
        resources = this.getResources();
        changeIndex(currentIndex);
    }


    @Override
    protected void initView() {
        titleText.setText("我的订单");
        titleRightImage.setBackgroundResource(R.mipmap.ic_newadd);
        titleLeftImage.setVisibility(View.VISIBLE);
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);

        orderViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                int  type = tabPagerAdapter.getItem(position).getArguments().getInt("type");
                changeIndex( type );
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        OrderFragment orderFragment1 = new OrderFragment();
        Bundle bd = new Bundle();
        bd.putInt("type", -1);
        orderFragment1.setArguments(bd);
        mFragmentList.add(orderFragment1);

        OrderFragment orderFragment2 = new OrderFragment();
        bd = new Bundle();
        bd.putInt("type", 0);
        orderFragment2.setArguments(bd);
        mFragmentList.add(orderFragment2);

        OrderFragment orderFragment3 = new OrderFragment();
        bd = new Bundle();
        bd.putInt("type", 1);
        orderFragment3.setArguments(bd);
        mFragmentList.add(orderFragment3);

        OrderFragment orderFragment4 = new OrderFragment();
        bd = new Bundle();
        bd.putInt("type", 2);
        orderFragment4.setArguments(bd);
        mFragmentList.add(orderFragment4);

        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),mFragmentList);
        orderViewPager.setAdapter(tabPagerAdapter);

    }

    private void changeIndex(int index) {
        if (index == -1 ) {
            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
            Drawable drawable_normal = resources.getDrawable(R.color.white);
            SystemTools.loadBackground(allLabel, drawable_press);
            SystemTools.loadBackground(doneLabel, drawable_normal);
            SystemTools.loadBackground(nodoneLabel, drawable_normal);
            SystemTools.loadBackground(backLabel, drawable_normal);
            allLabel.setTextColor(resources.getColor(R.color.red));
            backLabel.setTextColor(resources.getColor(R.color.black));
            nodoneLabel.setTextColor(resources.getColor(R.color.black));
            doneLabel.setTextColor(resources.getColor(R.color.black));
            orderViewPager.setCurrentItem(0);
        } else if (index == 0) {
            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
            Drawable drawable_normal = resources.getDrawable(R.color.white);
            SystemTools.loadBackground(allLabel, drawable_normal);
            SystemTools.loadBackground(nodoneLabel, drawable_press);
            SystemTools.loadBackground(doneLabel, drawable_normal);
            SystemTools.loadBackground(backLabel, drawable_normal);
            allLabel.setTextColor(resources.getColor(R.color.black));
            nodoneLabel.setTextColor(resources.getColor(R.color.red));
            doneLabel.setTextColor(resources.getColor(R.color.black));
            backLabel.setTextColor(resources.getColor(R.color.black));
            orderViewPager.setCurrentItem(1);
        } else if (index == 1) {
            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
            Drawable drawable_normal = resources.getDrawable(R.color.white);
            SystemTools.loadBackground(allLabel, drawable_normal);
            SystemTools.loadBackground(nodoneLabel, drawable_normal);
            SystemTools.loadBackground(doneLabel, drawable_press);
            SystemTools.loadBackground(backLabel, drawable_normal);
            allLabel.setTextColor(resources.getColor(R.color.black));
            nodoneLabel.setTextColor(resources.getColor(R.color.black));
            doneLabel.setTextColor(resources.getColor(R.color.red));
            backLabel.setTextColor(resources.getColor(R.color.black));

            orderViewPager.setCurrentItem(2);

        }else if (index ==2){
            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
            Drawable drawable_normal = resources.getDrawable(R.color.white);
            SystemTools.loadBackground(allLabel, drawable_normal);
            SystemTools.loadBackground(nodoneLabel, drawable_normal);
            SystemTools.loadBackground(doneLabel, drawable_normal);
            SystemTools.loadBackground(backLabel, drawable_press);
            allLabel.setTextColor(resources.getColor(R.color.black));
            nodoneLabel.setTextColor(resources.getColor(R.color.black));
            doneLabel.setTextColor(resources.getColor(R.color.black));
            backLabel.setTextColor(resources.getColor(R.color.red));

            orderViewPager.setCurrentItem(3);
        }
    }

    @OnClick(R.id.titleRightImage)
    void clicknew(){
        ActivityUtils.getInstance().showActivity(OrderListActivity.this,NewOrderActivity.class);
    }

    @OnClick(R.id.allLabel)
    void clickAll() {
        changeIndex(-1);
    }

    @OnClick(R.id.nodoneLabel)
    void clickDoing() {
        changeIndex(0);
    }

    @OnClick(R.id.doneLabel)
    void clickDone() {
        changeIndex(1);
    }

    @OnClick(R.id.backLabel)
    void clickback() {
        changeIndex(2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
