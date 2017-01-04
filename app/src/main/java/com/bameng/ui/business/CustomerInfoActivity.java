package com.bameng.ui.business;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.TabPagerAdapter;
import com.bameng.fragment.CustomDoneFrag;
import com.bameng.fragment.CustomNoDoneFrag;
import com.bameng.fragment.MsgFrag;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.base.BaseFragment;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.SystemTools;
import com.bameng.widgets.BMPopWindow;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * 客户信息界面
 */
public class CustomerInfoActivity extends BaseActivity implements TabLayout.OnTabSelectedListener , View.OnClickListener{
   public  final static int REQUEST_CODE_REFRESH=100;
    @BindView(R2.id.titleText)
    TextView titleText;
    @BindView(R2.id.titleLeftImage)
    ImageView titleLeftImage;
    private int currentIndex = 0;
    @BindView(R2.id.titleRightImage)
    ImageView titleRightImage;
    @BindView(R.id.titleRightText)
    TextView titleRightText;

    @BindView(R2.id.customViewPager)
    ViewPager customViewPager;
    @BindView(R2.id.tablayout)
    TabLayout tabLayout;

    public TabPagerAdapter tabPagerAdapter;
    private List<BaseFragment> mFragmentList = new ArrayList<>();
    final int request_code_newadd = 108;
    BMPopWindow bmPopWindow;
    List<String> menus;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_info);
        ButterKnife.bind(this);
        initView();
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
        tabPagerAdapter = new TabPagerAdapter( this, getSupportFragmentManager(), mFragmentList);
        customViewPager.setAdapter(tabPagerAdapter);
        customViewPager.setOffscreenPageLimit(2);

//        customViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int index) {
//
//
//            }
//
//            @Override
//            public void onPageScrolled(int index, float arg1, int pixes) {
//                if (pixes != 0) {
//                }
//                if (pixes == 0) {
//                    currentIndex = index;
//                    changeIndex(currentIndex);
//                }
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });
        //customViewPager.setCurrentItem(currentIndex);

        tabLayout.setupWithViewPager(customViewPager);
        tabLayout.addOnTabSelectedListener(this);

    }

    @Override
    protected void initView() {
        titleText.setText("客户信息");

        menus= new ArrayList<>();
        menus.add("新增客户资料");
        menus.add("新增客户照片");

        titleRightImage.setBackgroundResource(R.drawable.title_left_back);
        titleRightImage.setImageResource(R.mipmap.ic_newadd);

        titleRightText.setText("新增");
        titleRightText.setBackgroundResource(R.drawable.item_click_selector);

        titleLeftImage.setBackgroundResource(R.drawable.title_left_back);
        titleLeftImage.setImageResource(R.mipmap.ic_back);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        customViewPager.setCurrentItem( tab.getPosition() );
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }

    @Override
    protected void StartApi() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //ButterKnife.unbind(this);
    }


    @OnClick({R.id.titleRightImage,R.id.titleRightText})
    void clickRightImage(){
        //ActivityUtils.getInstance().showActivity(CustomerInfoActivity.this,SubmitCustomerInfoActivity.class);
        popWindow();
    }

    /***
     * 弹出选择框
     */
    void popWindow(){
        if(bmPopWindow==null){
            bmPopWindow = new BMPopWindow(this, menus ,this);
        }
        bmPopWindow.show();
    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(resultCode !=RESULT_OK ) return;
        if(requestCode== REQUEST_CODE_REFRESH){
            ((CustomNoDoneFrag)mFragmentList.get( customViewPager.getCurrentItem() )).loadData();
        }
    }

    @Override
    public void onClick(View view) {
        if( ((TextView)view).getText().equals( "新增客户资料") ){
            ActivityUtils.getInstance().showActivityForResult(CustomerInfoActivity.this, REQUEST_CODE_REFRESH , SubmitCustomerInfoActivity.class);
        }else{
            ActivityUtils.getInstance().showActivityForResult(CustomerInfoActivity.this,  REQUEST_CODE_REFRESH , SubmitCustomerPictureActivity.class);
        }
    }
}
