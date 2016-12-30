package com.bameng.adapter;

import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.support.v4.os.TraceCompat;
import android.view.View;

import com.bameng.R;
import com.bameng.model.CustomerModel;
import com.bameng.model.ScoreModel;
import com.bameng.utils.DateUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

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
        if(customerModel.getIsSave()==0) {
            baseViewHolder.setText(R.id.name, customerModel.getName());
            baseViewHolder.setText(R.id.moblie, customerModel.getMobile());
        }else{
            baseViewHolder.setText(R.id.name, "提交人:"+ customerModel.getBelongOneName() );
            baseViewHolder.setText(R.id.moblie,"提交信息:照片");
            SimpleDraweeView iv = baseViewHolder.getView(R.id.img);
            iv.setImageURI(Uri.parse( customerModel.getDataImg() ));
        }

        if (customerModel.getStatus()==1) {
            baseViewHolder.setVisible(R.id.status,true);
            if(customerModel.getInShop()==1){
                baseViewHolder.setText(status, "已进店");
            }else {
                baseViewHolder.setText(status, "已同意");
            }
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
            baseViewHolder.setVisible(R.id.lay_btn , false);
        }

        baseViewHolder.setVisible(R.id.progressBar , customerModel.isDoing()? true :false );
        baseViewHolder.setVisible( R.id.btnAgree , customerModel.isDoing()? false:true);
        baseViewHolder.setVisible( R.id.btnReject , customerModel.isDoing()? false:true);
        baseViewHolder.addOnClickListener(R.id.btnAgree);
        baseViewHolder.addOnClickListener(R.id.btnReject);

    }
}
