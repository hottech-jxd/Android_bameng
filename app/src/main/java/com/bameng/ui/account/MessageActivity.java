package com.bameng.ui.account;

import android.app.Activity;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.TabPagerAdapter;
import com.bameng.fragment.MsgFrag;
import com.bameng.ui.AllyHomeActivity;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.base.BaseFragment;
import com.bameng.ui.news.AddnewsActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.DensityUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * 我的消息 界面
 */
public class MessageActivity extends BaseActivity  implements TabLayout.OnTabSelectedListener{
    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    private int currentIndex = 0;
    @BindView(R2.id.titleRightImage)
    ImageView titleRightImage;
    @BindView(R.id.titleRightText)
    TextView titleRightText;
    @BindView(R.id.layTitleRight)
    LinearLayout layTitleRight;

    @BindView(R2.id.customViewPager)
    ViewPager customViewPager;
    @BindView(R2.id.tablayout)
    TabLayout tabLayout;

    TabPagerAdapter tabPagerAdapter;
    List<BaseFragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        titleText.setText("我的消息");

        titleRightImage.setImageResource(R.mipmap.ic_newadd);

        titleRightText.setText("新增");
        int leftPx = DensityUtils.dip2px(this,5);
        titleRightText.setPadding( leftPx , titleRightText.getPaddingTop() , titleRightText.getPaddingRight(),titleRightText.getPaddingBottom() );
        layTitleRight.setBackgroundResource(R.drawable.item_click_selector);

        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);

        MsgFrag sendFrag =new MsgFrag();
        Bundle bd = new Bundle();
        bd.putInt("type",1);
        bd.putInt("sendtype",1);
        sendFrag.setArguments(bd);
        mFragmentList.add(sendFrag);
        MsgFrag reveiceFrag =new MsgFrag();
        Bundle receivebd = new Bundle();
        receivebd.putInt("type",1);
        receivebd.putInt("sendtype",0);
        reveiceFrag.setArguments(receivebd);
        mFragmentList.add(reveiceFrag);
        tabPagerAdapter=new TabPagerAdapter(this, getSupportFragmentManager(), mFragmentList);
        customViewPager.setAdapter(tabPagerAdapter);
        customViewPager.setOffscreenPageLimit(2);

        tabLayout.setupWithViewPager(customViewPager);
        tabLayout.addOnTabSelectedListener(this);
    }

    @OnClick(R.id.layTitleRight)
    void onClick(View v){
        ActivityUtils.getInstance().showActivity(this, AddnewsActivity.class);
    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
