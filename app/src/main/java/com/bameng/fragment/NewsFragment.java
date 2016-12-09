package com.bameng.fragment;


import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.view.View;
import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.R2;
import com.bameng.adapter.TabPagerAdapter;
import com.bameng.config.Constants;
import com.bameng.model.SetRightVisibleEvent;
import com.bameng.ui.base.BaseFragment;
import org.greenrobot.eventbus.EventBus;
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

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        initSwitch();
    }

    @Override
    public void onResume() {
        super.onResume();
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
            //shopLabel.setVisibility(View.VISIBLE);
        }else{
            //shopLabel.setVisibility(View.GONE);
        }

        b = new Bundle();
        b.putInt("index", 3);
        allyFrag.setArguments(b);
        mFragmentList.add(allyFrag);
        tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager(), mFragmentList);
        raidersViewPager.setOffscreenPageLimit(3);

        tabLayout.setupWithViewPager(raidersViewPager);
        raidersViewPager.setAdapter( tabPagerAdapter);
        tabLayout.addOnTabSelectedListener(this);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public void onReshow() {
        if( tabPagerAdapter.getItem( raidersViewPager.getCurrentItem()).getArguments().getInt("index") == 3 ){
            EventBus.getDefault().post( new SetRightVisibleEvent( Constants.TAG_2 , true) );
        }else{
            EventBus.getDefault().post( new SetRightVisibleEvent( Constants.TAG_2 , false));
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
