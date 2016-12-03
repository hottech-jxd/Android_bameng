package com.bameng.ui.business;

import android.graphics.drawable.Drawable;
import android.os.Message;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.TabPagerAdapter;
import com.bameng.config.Constants;
import com.bameng.fragment.OrderFragment;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.base.BaseFragment;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.SystemTools;
import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * 我的订单
 */
public class OrderListActivity extends BaseActivity implements TabLayout.OnTabSelectedListener{
    @BindView(R2.id.ListSwitch)
    LinearLayout listSwitch;
    @BindView(R2.id.allLabel)
    TextView allLabel;
    @BindView(R2.id.nodoneLabel)
    TextView nodoneLabel;
    @BindView(R2.id.doneLabel)
    TextView doneLabel;
    @BindView(R2.id.backLabel)
    TextView backLabel;
    @BindView(R2.id.orderViewPager)
    ViewPager orderViewPager;
    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    @BindView(R2.id.titleRightText)
    TextView titleRightText;
    @BindView(R2.id.titleRightImage)
    ImageView  titleRightImage;
    @BindView(R2.id.tablayout)
    TabLayout tabLayout;

    TabPagerAdapter tabPagerAdapter;
    List<BaseFragment> mFragmentList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_list);
        unbinder = ButterKnife.bind(this);
        initView();
    }

    void visibleRightImageByUserType(){
        if(BaseApplication.UserData() !=null && BaseApplication.UserData().getUserIdentity() == Constants.MENG_ZHU){
            titleRightImage.setVisibility(View.VISIBLE);
            //titleRightImage.setBackgroundResource(R.mipmap.ic_newadd);
            titleRightImage.setBackgroundResource(R.drawable.title_left_back);
            titleRightImage.setImageResource(R.mipmap.ic_newadd);
        }else{
            titleRightImage.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initView() {
        titleText.setText("我的订单");

        titleLeftImage.setVisibility(View.VISIBLE);
        //Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        //SystemTools.loadBackground(titleLeftImage, leftDraw);
        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource( R.mipmap.ic_back );


        visibleRightImageByUserType();

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
        //ButterKnife.unbind(this);

    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
