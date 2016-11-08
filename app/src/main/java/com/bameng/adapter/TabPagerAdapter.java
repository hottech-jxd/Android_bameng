package com.bameng.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 切换页面适配类
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    private List<Fragment> fragmentList;

    public TabPagerAdapter(FragmentManager fm, List<Fragment> fragmentList)
    {
        super(fm);
        this.fragmentList = fragmentList;
    }

    @Override
    public Fragment getItem(int position) {
        return (fragmentList == null || fragmentList.size() == 0) ? null
                : fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList == null ? 0 : fragmentList.size();
    }

}
