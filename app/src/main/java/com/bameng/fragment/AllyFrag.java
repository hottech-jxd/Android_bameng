package com.bameng.fragment;

import android.os.Bundle;
import android.view.View;
import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.adapter.MsgAdapter;
import com.bameng.biz.UnReadMessageUtil;
import com.bameng.config.Constants;
import com.bameng.model.BadgeEvent;
import com.bameng.model.BadgeNewEvent;
import com.bameng.model.ListModel;
import com.bameng.ui.base.BaseShareActivity;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import static com.bameng.BaseApplication.readNewsCount;

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

    @Override
    protected void setBadge(ListModel model ) {
        if(model.getIsRead()==1) return;
        int count = BaseApplication.readNewsCount();
        count = count-1;

        EventBus.getDefault().post( new BadgeNewEvent(count>0));
        if(count<1) count=0;
        BaseApplication.writeMessageCount(count);
        //UnReadMessageUtil.getUnReadMessage();
    }

    @Override
    protected void updateBadge() {
        super.updateBadge();
        List<ListModel> data = baseAdapter.getData();
        if(data ==null) return;
        boolean hasUnRead=false;
        for(int i=0;i<data.size();i++){
            if( data.get(i).getIsRead()==0){
                hasUnRead=true;
                break;
            }
        }
        EventBus.getDefault().post(new BadgeNewEvent( hasUnRead ));
    }
}
