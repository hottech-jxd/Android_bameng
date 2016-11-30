package com.bameng.ui.business;

import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.SwipeRefreshLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.adapter.MYFragmentAdapter;
import com.bameng.fragment.CashFragment;
import com.bameng.ui.base.BaseActivity;
import com.bameng.ui.base.BaseFragment;
import com.bameng.utils.SystemTools;
import com.chad.library.adapter.base.BaseQuickAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.R.attr.fragment;

/***
 * 兑换审核 界面
 */
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

    @Bind(R.id.tablayout)
    TabLayout tabLayout;

    @Bind(R.id.viewPager)
    ViewPager viewPager;

    public Resources resources;

    MYFragmentAdapter<BaseFragment> fragmentAdapter;
    String[] titles;
    List<BaseFragment> fragments;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exchange_examine);
        ButterKnife.bind(this);
        initView();
        resources = this.getResources();
    }

    @Override
    protected void initView() {
        titleText.setText("兑换审核");
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back);
        SystemTools.loadBackground(titleLeftImage, leftDraw);

        titles = new String[]{"未处理申请","已处理审核"};
        fragments = new ArrayList<>();
        CashFragment fragment1 = new CashFragment();
        Bundle bd = new Bundle();
        bd.putInt("type",0);
        fragment1.setArguments(bd);
        fragments.add(fragment1);
        CashFragment fragment2 = new CashFragment();
        bd = new Bundle();
        bd.putInt("type",1);
        fragment2.setArguments(bd);
        fragments.add(fragment2);

        fragmentAdapter = new MYFragmentAdapter<>(getSupportFragmentManager(), fragments, titles);

        tabLayout.setupWithViewPager(viewPager);
        viewPager.setAdapter( fragmentAdapter);

       tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
           @Override
           public void onTabSelected(TabLayout.Tab tab) {
               viewPager.setCurrentItem( tab.getPosition() );
           }

           @Override
           public void onTabUnselected(TabLayout.Tab tab) {

           }

           @Override
           public void onTabReselected(TabLayout.Tab tab) {

           }
       });
    }

//    private void changeIndex(int index) {
//        if (index == 0) {
//            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
//            Drawable drawable_normal = resources.getDrawable(R.color.white);
//            SystemTools.loadBackground(nodoneLabel, drawable_press);
//            SystemTools.loadBackground(doneLabel, drawable_normal);
//            nodoneLabel.setTextColor(resources.getColor(R.color.red));
//            doneLabel.setTextColor(resources.getColor(R.color.black));
//        } else if (index == 1) {
//            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
//            Drawable drawable_normal = resources.getDrawable(R.color.white);
//            SystemTools.loadBackground(nodoneLabel, drawable_normal);
//            SystemTools.loadBackground(doneLabel, drawable_press);
//            nodoneLabel.setTextColor(resources.getColor(R.color.black));
//            doneLabel.setTextColor(resources.getColor(R.color.red));
//        }
//    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }
}
