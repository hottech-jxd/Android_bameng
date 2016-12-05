package com.bameng.fragment;

import android.os.Bundle;
import android.view.View;
import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.adapter.MsgAdapter;
import com.bameng.config.Constants;

/**
 * 盟主/盟友 资讯列表
 * Created by 47483 on 2016.11.01.
 */
public class AllyFrag extends StoreFrag {

    public AllyFrag(){
        layoutId = R.layout.layout_msg_item;
        if(BaseApplication.UserData().getUserIdentity() == Constants.MENG_ZHU ) {
            type= 4;
        }else {
            type = 3;
        }
        baseAdapter = new MsgAdapter();
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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
    public void onDestroyView() {
        super.onDestroyView();
    }

    @Override
    public String getPageTitle() {
         if(BaseApplication.UserData() !=null && BaseApplication.UserData().getUserIdentity() == Constants.MENG_ZHU){
             return "盟友消息";
         }else{
             return "盟主消息";
         }
    }
}
