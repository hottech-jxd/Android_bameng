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
        baseViewHolder.setText( R.id.name, "客户名称:"+customerModel.getName());
        baseViewHolder.setText(R.id.moblie , "联系方式:"+customerModel.getMobile());
        if (customerModel.getStatus()==1) {
            baseViewHolder.setText(status,"已同意");
            baseViewHolder.setVisible(R.id.lay_btn ,false );
        }else if (customerModel.getStatus()==2){
            baseViewHolder.setText(status , "已拒绝");
            baseViewHolder.setVisible(R.id.lay_btn ,false );
        }else {
            baseViewHolder.setText(status , "未审核");
            baseViewHolder.setVisible(R.id.lay_btn , true);
        }
        //baseViewHolder.setTag(Customer);
        //baseViewHolder.reject.setTag(Customer);
        baseViewHolder.setVisible(R.id.progressBar , customerModel.isDoing()? true :false );
        baseViewHolder.setVisible( R.id.btnAgree , customerModel.isDoing()? false:true);
        baseViewHolder.setVisible( R.id.btnReject , customerModel.isDoing()? false:true);
        baseViewHolder.addOnClickListener(R.id.btnAgree);
        baseViewHolder.addOnClickListener(R.id.btnReject);
        //holder.agress.setOnClickListener(this);
        //holder.reject.setOnClickListener(this);
        baseViewHolder.addOnClickListener(R.id.img);
        //holder.img.setOnClickListener(this);
        //holder.img.setTag(Customer);
        //baseViewHolder.addOnClickListener(R.id.llItem);
        baseViewHolder.addOnClickListener(status );
        //holder.status.setTag(Customer);
        baseViewHolder.addOnClickListener(R.id.name);
        //holder.name.setTag(Customer);
        baseViewHolder.addOnClickListener(R.id.moblie);
        //holder.moblie.setTag(Customer);



    }
}
