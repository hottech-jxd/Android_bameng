package com.bameng.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.TabPagerAdapter;
import com.bameng.config.Constants;
import com.bameng.model.BadgeEvent;
import com.bameng.model.BadgeNewEvent;
import com.bameng.model.SetRightVisibleEvent;
import com.bameng.ui.base.BaseFragment;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;

/***
 * 资讯列表
 */
public class NewsFragment extends BaseFragment implements TabLayout.OnTabSelectedListener {

    @BindView(R2.id.raidersViewPager)
    ViewPager raidersViewPager;
    @BindView(R2.id.tablayout)
    TabLayout tabLayout;

    TabPagerAdapter tabPagerAdapter;
    List<BaseFragment> mFragmentList = new ArrayList<>();
    List<Boolean> badgeList=new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        //EventBus.getDefault().register(this);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initSwitch();
    }

    @Override
    public void onResume() {
        super.onResume();

        boolean hasNews = BaseApplication.readNewsMessage();
        badgeList.set( badgeList.size()-1 , hasNews);
        setupTabItem();
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        raidersViewPager.setCurrentItem( tab.getPosition() );
        int flag = tabPagerAdapter.getItem( tab.getPosition() ).getArguments().getInt("index");
        SetRightVisibleEvent event = new SetRightVisibleEvent( Constants.TAG_2 , flag == 3 ? true:false );
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
        //AllyFrag allyFrag = new AllyFrag();

        Bundle b = new Bundle();
        b.putInt("index", 0);
        groupFrag.setArguments(b);
        mFragmentList.add(groupFrag);
        badgeList.add(false);

        b = new Bundle();
        b.putInt("index", 1);
        storeFrag.setArguments(b);
        mFragmentList.add(storeFrag);
        badgeList.add(false);

        if(BaseApplication.UserData().getShopType() == Constants.SHOP_BRANCH ) {
            b = new Bundle();
            b.putInt("index", 2);
            ShopFrag shopFrag = new ShopFrag();
            shopFrag.setArguments(b);
            mFragmentList.add(shopFrag);
            badgeList.add(false);
        }

        //b = new Bundle();
        //b.putInt("index", 3);
        //allyFrag.setArguments(b);
        //mFragmentList.add(allyFrag);
        tabPagerAdapter = new TabPagerAdapter( getContext() , getChildFragmentManager(), mFragmentList);
        raidersViewPager.setOffscreenPageLimit(3);
        badgeList.add(false);

        tabLayout.setupWithViewPager(raidersViewPager);
        raidersViewPager.setAdapter( tabPagerAdapter);
        tabLayout.addOnTabSelectedListener(this);

        setupTabItem();
    }

    private void setupTabItem(){
        for(int i=0;i<mFragmentList.size();i++){
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            View customView = tab.getCustomView();
            if (customView != null) {
                ViewParent parent = customView.getParent();
                if (parent != null) {
                    ((ViewGroup) parent).removeView(customView);
                }
            }
            tab.setCustomView(tabPagerAdapter.getCustomTabItem(i,badgeList.get(i)));
        }

        tabLayout .getTabAt(tabLayout.getSelectedTabPosition()).getCustomView().setSelected(true);
    }

    @Override
    public void onDestroyView() {
        //EventBus.getDefault().unregister(this);
        super.onDestroyView();
    }

    @Override
    public void onReshow() {
//        if( tabPagerAdapter.getItem( raidersViewPager.getCurrentItem()).getArguments().getInt("index") == 3 ){
//            EventBus.getDefault().post( new SetRightVisibleEvent( Constants.TAG_2 , true) );
//        }else{
//            EventBus.getDefault().post( new SetRightVisibleEvent( Constants.TAG_2 , false));
//        }
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

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventUpdateBadge(BadgeEvent event ){
//       badgeList.set( badgeList.size()-1 , event.isShowNew());
//        setupTabItem();
//    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void onEventUpdateBadge(BadgeNewEvent event){
//        badgeList.set( badgeList.size()-1 , event.isShowNew());
//        setupTabItem();
//    }

}
