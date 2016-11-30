package com.bameng.fragment;


import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.adapter.TabPagerAdapter;
import com.bameng.config.Constants;
import com.bameng.model.SetRightVisibleEvent;
import com.bameng.ui.base.BaseFragment;
import com.bameng.utils.SystemTools;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bameng.R.id.viewPager;

/***
 * 资讯列表
 */
public class NewsFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {

    @Bind(R.id.groupLabel)
    TextView groupLabel;
    @Bind(R.id.storeLabel)
    TextView storeLabel;
    @Bind(R.id.shopLabel)
    TextView shopLabel;
    @Bind(R.id.allyLabel)
    TextView allyLabel;
    @Bind(R.id.raidersViewPager)
    ViewPager raidersViewPager;
    @Bind(R.id.tablayout)
    TabLayout tabLayout;

    public Resources resources;
    public TabPagerAdapter tabPagerAdapter;
    private List<BaseFragment> mFragmentList = new ArrayList<>();

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        resources = this.getResources();

        initSwitch();
    }


    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        raidersViewPager.setCurrentItem( tab.getPosition() );
        int flag = tabPagerAdapter.getItem( tab.getPosition() ).getArguments().getInt("index");
        SetRightVisibleEvent event = new SetRightVisibleEvent( flag == 3 ? true:false );
        EventBus.getDefault().post( event );
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

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

        if(BaseApplication.UserData().getShopType() == Constants.SHOP_BRANCH ) {
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
        //raidersViewPager.setAdapter(tabPagerAdapter);
        raidersViewPager.setOffscreenPageLimit(3);

//        raidersViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
//
//            @Override
//            public void onPageSelected(int index) {
//                 int  idx = tabPagerAdapter.getItem(index).getArguments().getInt("index");
//                changeIndex(idx);
//            }
//
//
//            @Override
//            public void onPageScrolled(int index, float arg1, int pixes) {
//            }
//
//            @Override
//            public void onPageScrollStateChanged(int state) {
//            }
//        });

        //raidersViewPager.setCurrentItem(currentIndex);

        //changeIndex(currentIndex);


        tabLayout.setupWithViewPager(raidersViewPager);
        raidersViewPager.setAdapter( tabPagerAdapter);
        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(this);
    }

    @Override
    public void onReshow() {
        if( tabPagerAdapter.getItem( raidersViewPager.getCurrentItem()).getArguments().getInt("index") == 3 ){
            EventBus.getDefault().post( new SetRightVisibleEvent(true) );
        }else{
            EventBus.getDefault().post(new SetRightVisibleEvent(false));
        }
    }

    @Override
    public void onFragPasue() {

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_news;
    }
}
