package com.bameng.adapter;

import android.support.v4.content.ContextCompat;
import android.support.v4.os.TraceCompat;
import android.view.View;

import com.bameng.R;
import com.bameng.model.CustomerModel;
import com.bameng.model.ScoreModel;
import com.bameng.utils.DateUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import static com.bameng.R.id.status;

/**
 * Created by Administrator on 2016/11/23.
 */

public class CustomerDetailAdapter extends BaseQuickAdapter<CustomerModel,BaseViewHolder> {
    public CustomerDetailAdapter(){
        super(R.layout.customdone_item,null);
    }

    public CustomerDetailAdapter(int resId){
        super(resId,null);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CustomerModel customerModel ) {
        baseViewHolder.setText( R.id.name, customerModel.getName());
        baseViewHolder.setText(R.id.moblie , customerModel.getMobile());
        if (customerModel.getStatus()==1) {
            baseViewHolder.setVisible(R.id.status,true);
            baseViewHolder.setText(status,"已同意");
            baseViewHolder.setTextColor(R.id.status , ContextCompat.getColor( mContext , R.color.green ) );
            baseViewHolder.setVisible(R.id.lay_btn ,false );
        }else if (customerModel.getStatus()==2){
            baseViewHolder.setVisible(R.id.status,true);
            baseViewHolder.setText(status , "已拒绝");
            baseViewHolder.setTextColor(R.id.status , ContextCompat.getColor( mContext  , R.color.red ));
            baseViewHolder.setVisible(R.id.lay_btn ,false );
        }else {
            baseViewHolder.setVisible(R.id.status,false);
            baseViewHolder.setText(status , "未审核");
            baseViewHolder.setTextColor(R.id.status , ContextCompat.getColor( mContext  , R.color.red ));
            baseViewHolder.setVisible(R.id.lay_btn , true);
        }

        baseViewHolder.setVisible(R.id.progressBar , customerModel.isDoing()? true :false );
        baseViewHolder.setVisible( R.id.btnAgree , customerModel.isDoing()? false:true);
        baseViewHolder.setVisible( R.id.btnReject , customerModel.isDoing()? false:true);
        baseViewHolder.addOnClickListener(R.id.btnAgree);
        baseViewHolder.addOnClickListener(R.id.btnReject);
        //baseViewHolder.addOnClickListener(R.id.img);
        //baseViewHolder.addOnClickListener(status );
        //baseViewHolder.addOnClickListener(R.id.name);
        //baseViewHolder.addOnClickListener(R.id.moblie);
    }
}
