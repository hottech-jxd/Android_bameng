package com.bameng.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.ui.base.BaseFragment;

import java.util.List;

/**
 * 切换页面适配类
 */
public class TabPagerAdapter extends FragmentPagerAdapter {

    private List<BaseFragment> fragmentList;
    private Context context;

    public TabPagerAdapter( Context context , FragmentManager fm, List<BaseFragment> fragmentList) {
        super(fm);
        this.fragmentList = fragmentList;
        this.context = context;
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

    @Override
    public CharSequence getPageTitle(int position) {
        return fragmentList.get(position).getPageTitle();
    }

    public View getCustomTabItem(int position , boolean showBadge ){
        View view =LayoutInflater.from(context).inflate(R.layout.tablayout_item,null);
        TextView tv = (TextView) view.findViewById(R.id.textview);
        tv.setText( getPageTitle(position) );
        View badge = view.findViewById(R.id.badgeview);
        badge.setBackgroundResource(showBadge?R.drawable.circle_red:R.drawable.circle_white);
        return view;
    }
}
