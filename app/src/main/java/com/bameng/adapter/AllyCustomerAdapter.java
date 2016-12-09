package com.bameng.adapter;

import com.bameng.R;
import com.bameng.model.CustomerModel;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Administrator on 2016/11/24.
 */

public class AllyCustomerAdapter extends CustomerDetailAdapter {

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CustomerModel customerModel ) {
        baseViewHolder.setText( R.id.name, customerModel.getName());
        baseViewHolder.setText(R.id.moblie , customerModel.getMobile());
        baseViewHolder.setVisible(R.id.lay_btn ,false );
        if (customerModel.getStatus()==1) {
            baseViewHolder.setText(R.id.status,"已同意");

        }else if (customerModel.getStatus()==2){
            baseViewHolder.setText(R.id.status , "已拒绝");

        }else {
            baseViewHolder.setText(R.id.status , "未审核");

        }

        //baseViewHolder.addOnClickListener(R.id.llItem);



    }

}
