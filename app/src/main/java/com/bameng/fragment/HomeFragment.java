package com.bameng.fragment;


import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;


import com.bameng.R;
import com.bameng.adapter.HomeBannerPagerAdapter;
import com.bameng.model.AdlistModel;
import com.bameng.ui.base.BaseFragment;
import com.handmark.pulltorefresh.library.PullToRefreshBase;
import com.handmark.pulltorefresh.library.PullToRefreshListView;
import com.handmark.pulltorefresh.library.PullToRefreshScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class HomeFragment extends BaseFragment implements PullToRefreshListView.OnRefreshListener {


    @Bind(R.id.homePullRefresh)
    PullToRefreshScrollView homePullRefresh;

    @Bind(R.id.dot)
    LinearLayout dot;

    @Bind(R.id.homeViewPager)
    ViewPager homeViewPager;

    List<AdlistModel> adDataList = null;

    private Handler mhandler;

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initView();
        ButterKnife.bind(this, view);
    }

    public void initView() {
        //adDataList = (List<AdlistModel>) getIntent().getSerializableExtra("data");
        adDataList = new ArrayList<>();
        for(int i=0;i<10;i++)
        {
            AdlistModel map = new AdlistModel();
            map.setItemImgUrl("https://ss0.bdstatic.com/94oJfD_bAAcT8t7mm9GUKT-xh_/timg?image&quality=100&size=b4000_4000&sec=1477108740&di=a906a3d5d1b5e9adf51e540c36da81a4&src=http://imglf0.ph.126.net/1EnYPI5Vzo2fCkyy2GsJKg==/2829667940890114965.jpg");//图像资源的ID
            map.setItemId(i);
            adDataList.add(map);
        }
        initSwitchImg();
    }

    public void scrollToTop() {
        homePullRefresh.getRefreshableView().smoothScrollTo(0, 0);
    }

    private void initSwitchImg() {

//        if(adDataList==null || adDataList.size()<1 ) return;


        initDots();
        //通过适配器引入图片
        homeViewPager.setAdapter(new HomeBannerPagerAdapter(adDataList, getContext(), mhandler));
        homeViewPager.setCurrentItem(0);
        initListener();
        //更新文本内容
        updateTextAndDot();
    }

    /**
     * 初始化监听器
     */
    @SuppressWarnings("deprecation")
    private void initListener() {
        homeViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                updateTextAndDot();

            }

            @Override
            public void onPageScrolled(int position, float positionOffset,
                                       int positionOffsetPixels) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {
                // TODO Auto-generated method stub

            }
        });
    }
    private void updateTextAndDot() {
        int currentPage = homeViewPager.getCurrentItem();

        //改变dot
        for (int i = 0; i < dot.getChildCount(); i++) {
            dot.getChildAt(i).setEnabled(i == currentPage);
        }

    }
    private void initDots() {
        for (int i = 0; i < adDataList.size(); i++) {
            View view = new View(getActivity());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(8, 8);
            if (i != 0) {
                params.leftMargin = 5;
            }

            view.setLayoutParams(params);
            view.setBackgroundResource(R.drawable.selecter_dot);
            dot.addView(view);
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

    @Override
    public void onClick(View view) {

    }

    @Override
    public int getLayoutRes() {
        return R.layout.fragment_home;
    }

    @Override
    public void onRefresh(PullToRefreshBase refreshView) {

    }
}
