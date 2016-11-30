package com.bameng.adapter;

import android.graphics.Color;
import android.support.v4.content.ContextCompat;

import com.bameng.R;
import com.bameng.model.BeanRecordModel;
import com.bameng.utils.DateUtils;
import com.bameng.utils.Util;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;



/**
 * 盟豆流水
 * Created by Administrator on 2016/11/22.
 */
public class WaitMBeanFlowAdapter extends BaseQuickAdapter <BeanRecordModel, BaseViewHolder> {

    public WaitMBeanFlowAdapter() {
        super( R.layout.waitmbean_item, null );
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BeanRecordModel data ) {
        baseViewHolder.setText( R.id.tvname , data.getRemark()  );

        baseViewHolder.setText( R.id.tvTime , DateUtils.formatDate(data.getTime() , "yyyy.MM.dd HH:mm") );

        if( data.getMoney()>=0 ){
            baseViewHolder.setText( R.id.tvBeans , "+"+String.valueOf( data.getMoney() ));
            baseViewHolder.setTextColor( R.id.tvBeans , ContextCompat.getColor( this.mContext , R.color.red ));
        }else{
            baseViewHolder.setText( R.id.tvBeans , String.valueOf( data.getMoney() ));
            baseViewHolder.setTextColor(R.id.tvBeans , ContextCompat.getColor( this.mContext , R.color.green ));
        }
    }
}
