package com.bameng.ui.business;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.adapter.MengAdapter;
import com.bameng.fragment.MengFragment;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.DensityUtils;
import com.bameng.utils.SystemTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * 我的联盟 界面
 */
public class MyAlliesActivity extends BaseActivity {

    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    private int currentIndex = 0;
//    @Bind(R.id.ApplyLabel)
//    TextView ApplyLabel;
//    @Bind(R.id.AlliesLabel)
//    TextView AlliesLabel;

    @Bind(R.id.tablayout)
    TabLayout tabLayout;
    @Bind(R.id.viewerPager)
    ViewPager viewPager;



    MengAdapter mengAdapter;

    public Resources resources;

    List<MengFragment> fragmentList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_allies);
        ButterKnife.bind(this);
        initView();
        application = (BaseApplication) this.getApplication();
        resources = this.getResources();
        StartApi();
        //changeIndex(currentIndex);
    }

    @Override
    protected void initView() {
        titleText.setText("我的联盟");
        Drawable leftDraw = ContextCompat.getDrawable(this, R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);

        int minW = DensityUtils.getScreenH(this)/2-20;
        //tabLayout.setMinimumWidth(maxWidth);
        //tabLayout.getTabAt(0).getCustomView().setMinimumWidth(minW);
        //tabLayout.getTabAt(1).getCustomView().setMinimumWidth(minW);


        fragmentList = new ArrayList<>();
        Bundle bd = new Bundle();
        bd.putInt("type", 0);//0盟友申请，1盟友列表
        MengFragment fragment = new MengFragment();
        fragment.setArguments(bd);
        fragmentList.add(fragment);
        bd = new Bundle();
        bd.putInt("type", 1);
        fragment = new MengFragment();
        fragment.setArguments(bd);
        fragmentList.add(fragment);

        mengAdapter = new MengAdapter(getSupportFragmentManager(), fragmentList);


        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        viewPager.setAdapter(mengAdapter);
        tabLayout.setupWithViewPager(viewPager);

    }

//    private void changeIndex(int index) {
//        if (index == 0) {
//            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
//            Drawable drawable_normal = resources.getDrawable(R.color.white);
//            SystemTools.loadBackground(ApplyLabel, drawable_press);
//            SystemTools.loadBackground(AlliesLabel, drawable_normal);
//            ApplyLabel.setTextColor(resources.getColor(R.color.red));
//            AlliesLabel.setTextColor(resources.getColor(R.color.black));
//        } else if (index == 1) {
//            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
//            Drawable drawable_normal = resources.getDrawable(R.color.white);
//            SystemTools.loadBackground(ApplyLabel, drawable_normal);
//            SystemTools.loadBackground(AlliesLabel, drawable_press);
//            ApplyLabel.setTextColor(resources.getColor(R.color.black));
//            AlliesLabel.setTextColor(resources.getColor(R.color.red));
//        }
//    }
    @Override
    protected void StartApi() {

    }

//    @OnClick(R.id.ApplyLabel)
//    void clickapply() {
//        // raidersViewPager.setCurrentItem(1);
//        changeIndex(0);
//    }

//    @OnClick(R.id.AlliesLabel)
//    void clickallies() {
//        //raidersViewPager.setCurrentItem(2);
//        changeIndex(1);
//    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
