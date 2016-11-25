package com.bameng.fragment;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.adapter.TabPagerAdapter;
import com.bameng.ui.base.BaseFragment;
import com.bameng.utils.SystemTools;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/***
 * 资讯列表
 */
public class NewsFragment extends BaseFragment {

    @Bind(R.id.groupLabel)
    TextView groupLabel;
    @Bind(R.id.storeLabel)
    TextView storeLabel;
    @Bind(R.id.shopLabel)
    TextView shopLabel;
    @Bind(R.id.allyLabel)
    TextView allyLabel;
    public Resources resources;
    private int currentIndex = 0;
    @Bind(R.id.raidersViewPager)
    ViewPager raidersViewPager;
    public TabPagerAdapter tabPagerAdapter;
    private List<Fragment> mFragmentList = new ArrayList<Fragment>();


    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        resources = this.getResources();

        initSwitch();

        if(BaseApplication.UserData().getUserIdentity() ==1){
            allyLabel.setText(getString(R.string.tab_mengyou));
        }else{
            allyLabel.setText(getString(R.string.tab_mengzhu));
        }
    }

    private void initSwitch() {
        GroupFrag groupFrag = new GroupFrag();
        StoreFrag storeFrag = new StoreFrag();

        AllyFrag allyFrag = new AllyFrag();
        Bundle b = new Bundle();
        b.putInt("index", 0);
        groupFrag.setArguments(b);
        mFragmentList.add(groupFrag);

        b = new Bundle();
        b.putInt("index", 1);
        storeFrag.setArguments(b);
        mFragmentList.add(storeFrag);


        if(BaseApplication.UserData().getShopType() == 2) {
            b = new Bundle();
            b.putInt("index", 2);
            ShopFrag shopFrag = new ShopFrag();
            shopFrag.setArguments(b);
            mFragmentList.add(shopFrag);
            shopLabel.setVisibility(View.VISIBLE);
        }else{
            shopLabel.setVisibility(View.GONE);
        }

        b = new Bundle();
        b.putInt("index", 3);
        allyFrag.setArguments(b);
        mFragmentList.add(allyFrag);
        tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager(), mFragmentList);
        raidersViewPager.setAdapter(tabPagerAdapter);
        raidersViewPager.setOffscreenPageLimit(4);
        raidersViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int index) {
                 int  idx = tabPagerAdapter.getItem(index).getArguments().getInt("index");
                changeIndex(idx);
            }


            @Override
            public void onPageScrolled(int index, float arg1, int pixes) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

        raidersViewPager.setCurrentItem(currentIndex);
        changeIndex(currentIndex);
    }


    private void changeIndex(int index) {
        if (index == 0) {
            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
            Drawable drawable_normal = resources.getDrawable(R.color.white);
            SystemTools.loadBackground(groupLabel, drawable_press);
            SystemTools.loadBackground(storeLabel, drawable_normal);
            SystemTools.loadBackground(shopLabel, drawable_normal);
            SystemTools.loadBackground(allyLabel, drawable_normal);
            groupLabel.setTextColor(resources.getColor(R.color.red));
            storeLabel.setTextColor(resources.getColor(R.color.black));
            shopLabel.setTextColor(resources.getColor(R.color.black));
            allyLabel.setTextColor(resources.getColor(R.color.black));
        } else if (index == 1) {
            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
            Drawable drawable_normal = resources.getDrawable(R.color.white);
            SystemTools.loadBackground(groupLabel, drawable_normal);
            SystemTools.loadBackground(storeLabel, drawable_press);
            SystemTools.loadBackground(shopLabel, drawable_normal);
            SystemTools.loadBackground(allyLabel, drawable_normal);
            groupLabel.setTextColor(resources.getColor(R.color.black));
            storeLabel.setTextColor(resources.getColor(R.color.red));
            shopLabel.setTextColor(resources.getColor(R.color.black));
            allyLabel.setTextColor(resources.getColor(R.color.black));
        } else if (index == 2){
            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
            Drawable drawable_normal = resources.getDrawable(R.color.white);
            SystemTools.loadBackground(groupLabel, drawable_normal);
            SystemTools.loadBackground(storeLabel, drawable_normal);
            SystemTools.loadBackground(shopLabel, drawable_press);
            SystemTools.loadBackground(allyLabel, drawable_normal);
            groupLabel.setTextColor(resources.getColor(R.color.black));
            storeLabel.setTextColor(resources.getColor(R.color.black));
            shopLabel.setTextColor(resources.getColor(R.color.red));
            allyLabel.setTextColor(resources.getColor(R.color.black));
        } else if (index == 3){
            Drawable drawable_press = resources.getDrawable(R.drawable.switch_press);
            Drawable drawable_normal = resources.getDrawable(R.color.white);
            SystemTools.loadBackground(groupLabel, drawable_normal);
            SystemTools.loadBackground(storeLabel, drawable_normal);
            SystemTools.loadBackground(shopLabel, drawable_normal);
            SystemTools.loadBackground(allyLabel, drawable_press);
            groupLabel.setTextColor(resources.getColor(R.color.black));
            storeLabel.setTextColor(resources.getColor(R.color.black));
            shopLabel.setTextColor(resources.getColor(R.color.black));
            allyLabel.setTextColor(resources.getColor(R.color.red));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }


    @Override
    public void onReshow() {

    }

    @Override
    public void onFragPasue() {

    }

    @OnClick(R.id.groupLabel)
    void groupLabel() {
        raidersViewPager.setCurrentItem(0);
        changeIndex(0);
    }

    @OnClick(R.id.storeLabel)
    void storeLabel() {
        raidersViewPager.setCurrentItem(1);
        changeIndex(1);
    }
    @OnClick(R.id.shopLabel)
    void shopLabel() {
        raidersViewPager.setCurrentItem(2);
        changeIndex(2);
    }
    @OnClick(R.id.allyLabel)
    void allyLabel() {
        raidersViewPager.setCurrentItem(3);
        changeIndex(3);
    }
    @Override
    public void onClick(View view) {

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_news;
    }
}
