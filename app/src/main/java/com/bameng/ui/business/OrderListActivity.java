package com.bameng.ui.business;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
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
import com.bameng.ui.base.BaseFragment;
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
public class OrderListActivity extends BaseActivity implements TabLayout.OnTabSelectedListener{
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
    @Bind(R.id.tablayout)
    TabLayout tabLayout;

    TabPagerAdapter tabPagerAdapter;
    List<BaseFragment> mFragmentList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        ButterKnife.bind(this);
        initView();
    }


    @Override
    protected void initView() {
        titleText.setText("我的订单");
        titleRightImage.setBackgroundResource(R.mipmap.ic_newadd);
        titleLeftImage.setVisibility(View.VISIBLE);
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);

        OrderFragment orderFragment1 = new OrderFragment();
        Bundle bd = new Bundle();
        bd.putInt("type", -1);
        bd.putString("name","全部");
        orderFragment1.setArguments(bd);
        mFragmentList.add(orderFragment1);

        OrderFragment orderFragment2 = new OrderFragment();
        bd = new Bundle();
        bd.putInt("type", 0);
        bd.putString("name","未成交");
        orderFragment2.setArguments(bd);
        mFragmentList.add(orderFragment2);

        OrderFragment orderFragment3 = new OrderFragment();
        bd = new Bundle();
        bd.putInt("type", 1);
        bd.putString("name","成交");
        orderFragment3.setArguments(bd);
        mFragmentList.add(orderFragment3);

        OrderFragment orderFragment4 = new OrderFragment();
        bd = new Bundle();
        bd.putInt("type", 2);
        bd.putString("name","退单");
        orderFragment4.setArguments(bd);
        mFragmentList.add(orderFragment4);

        tabPagerAdapter = new TabPagerAdapter(getSupportFragmentManager(),mFragmentList);
        orderViewPager.setAdapter(tabPagerAdapter);

        tabLayout.setupWithViewPager(orderViewPager);
        tabLayout.addOnTabSelectedListener(this);

    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        orderViewPager.setCurrentItem( tab.getPosition() );
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @OnClick(R.id.titleRightImage)
    void clicknew(){
        ActivityUtils.getInstance().showActivity(OrderListActivity.this,NewOrderActivity.class);
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
