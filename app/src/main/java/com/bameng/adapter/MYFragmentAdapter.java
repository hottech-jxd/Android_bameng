package com.bameng.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;


import java.util.List;

/**
 * Created by Administrator on 2016/11/21.
 */
public class MYFragmentAdapter<T extends Fragment> extends FragmentPagerAdapter {
    List<T> fragmentList;
    String[] titles ;
    public MYFragmentAdapter(FragmentManager fragmentManager , List<T> fragments, String[] titles ) {
        super(fragmentManager);
        this.fragmentList =fragments;
        this.titles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        return (Fragment) fragmentList.get(position);
    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
