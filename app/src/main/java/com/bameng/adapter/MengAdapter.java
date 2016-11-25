package com.bameng.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.bameng.fragment.MengFragment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.RecursiveTask;

/**
 * Created by Administrator on 2016/11/19.
 */

public class MengAdapter extends FragmentPagerAdapter {
    String[] titles = {"盟友申请","盟友列表"};
    List<MengFragment> fragmentList;

    public MengAdapter(FragmentManager fragmentManager , List<MengFragment> fragments) {
        super(fragmentManager);
        this.fragmentList =fragments;

    }

    @Override
    public int getCount() {
        return fragmentList.size();
    }

    @Override
    public Fragment getItem(int position) {
        return fragmentList.get(position);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }
}
