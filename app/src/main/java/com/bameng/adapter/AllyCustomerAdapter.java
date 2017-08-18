package com.bameng.adapter;

import android.net.Uri;
import android.support.v4.content.ContextCompat;

import com.bameng.R;
import com.bameng.model.CustomerModel;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2016/11/24.
 */

public class AllyCustomerAdapter extends CustomerDetailAdapter {

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CustomerModel customerModel ) {
        if(customerModel.getIsSave()==0) {
            baseViewHolder.setText(R.id.name, customerModel.getName());
            baseViewHolder.setText(R.id.moblie, customerModel.getMobile());
            SimpleDraweeView iv = baseViewHolder.getView(R.id.img);
            iv.setImageURI(Uri.parse( "res:///"+ R.mipmap.icon_username ) );
        }else{
            baseViewHolder.setText(R.id.name, "提交人:"+ customerModel.getBelongOneName() );
            baseViewHolder.setText(R.id.moblie,"提交信息:照片");
            SimpleDraweeView iv = baseViewHolder.getView(R.id.img);
            iv.setImageURI(Uri.parse( customerModel.getDataImg() ));
        }

        baseViewHolder.setVisible(R.id.lay_btn ,false );
        if (customerModel.getStatus()==1) {
            baseViewHolder.setTextColor(R.id.status, ContextCompat.getColor( mContext , R.color.green ));
            if(customerModel.getInShop()==1){
                baseViewHolder.setText(R.id.status, "已进店");
            }else {
                baseViewHolder.setText(R.id.status, "已同意");
            }
        }else if (customerModel.getStatus()==2){
            baseViewHolder.setTextColor(R.id.status, ContextCompat.getColor( mContext , R.color.red ));
            baseViewHolder.setText(R.id.status , "已拒绝");
        }else if(customerModel.getStatus() == 3){
            baseViewHolder.setVisible(R.id.status, true);
            baseViewHolder.setText(R.id.status , "未生成订单");
            baseViewHolder.setTextColor(R.id.status, ContextCompat.getColor(mContext,R.color.red));
        }else if(customerModel.getStatus()==4){
            baseViewHolder.setVisible(R.id.status, true);
            baseViewHolder.setText(R.id.status, "已生成订单");
            baseViewHolder.setTextColor(R.id.status, ContextCompat.getColor(mContext,R.color.red));
        }else if(customerModel.getStatus()==5){
            baseViewHolder.setVisible(R.id.status, true);
            baseViewHolder.setText(R.id.status , "已失效");
            baseViewHolder.setTextColor(R.id.status, ContextCompat.getColor(mContext,R.color.red));
        }else {
            baseViewHolder.setTextColor(R.id.status,ContextCompat.getColor(mContext,R.color.black));
            baseViewHolder.setText(R.id.status , "未审核");
        }
        //baseViewHolder.addOnClickListener(R.id.llItem);

    }

}
