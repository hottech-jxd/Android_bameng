package com.bameng.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import com.bameng.R;
import com.bameng.model.RefreshCustomerEvent;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

/**
 * Created by 47483 on 2016.11.09.
 */
public class CustomDoneFrag extends CustomNoDoneFrag {

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
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
        return R.layout.frag_customdone;
    }

    @Subscribe( threadMode = ThreadMode.MAIN)
    public void onEventRefreshData(RefreshCustomerEvent event){
//        if( event.getTabName().equals("DoneFrag") ) {
//            adapter.notifyDataSetChanged();
//        }
    }

    @Override
    public String getPageTitle() {
        return "已处理信息";
    }
}
