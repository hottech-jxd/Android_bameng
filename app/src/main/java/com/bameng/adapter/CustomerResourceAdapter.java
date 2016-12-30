package com.bameng.adapter;

import android.net.Uri;
import android.support.v4.content.ContextCompat;

import com.bameng.R;
import com.bameng.model.CustomerModel;
import com.bameng.model.CustomerResourceModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

import static com.bameng.R.mipmap.icon_username;

/**
 * Created by Administrator on 2016/11/24.
 */
public class CustomerResourceAdapter extends BaseQuickAdapter<CustomerResourceModel,BaseViewHolder> {
    public CustomerResourceAdapter(){
        super(R.layout.customdone_item,null);
    }
    @Override
    protected void convert(BaseViewHolder baseViewHolder, CustomerResourceModel customerModel ) {
        if(customerModel.getType()==0) {
            baseViewHolder.setText(R.id.name, customerModel.getName());
            baseViewHolder.setText(R.id.moblie, customerModel.getMobile());
            SimpleDraweeView iv = baseViewHolder.getView(R.id.img);
            iv.setImageURI(Uri.parse( "res:///"+ R.mipmap.icon_username ));
        }else{
            baseViewHolder.setText(R.id.name, "提交人:"+ customerModel.getSubmitName() );
            baseViewHolder.setText(R.id.moblie,"提交信息:照片");
            SimpleDraweeView iv = baseViewHolder.getView(R.id.img);
            iv.setImageURI(Uri.parse( customerModel.getDataImg() ));
        }

        baseViewHolder.setVisible(R.id.lay_btn ,false );
        baseViewHolder.setVisible(R.id.status,false);
//        if (customerModel.getStatus()==1) {
//            baseViewHolder.setTextColor(R.id.status, ContextCompat.getColor( mContext , R.color.green ));
//            if(customerModel.getInShop()==1){
//                baseViewHolder.setText(R.id.status, "已进店");
//            }else {
//                baseViewHolder.setText(R.id.status, "已同意");
//            }
//        }else if (customerModel.getStatus()==2){
//            baseViewHolder.setTextColor(R.id.status, ContextCompat.getColor( mContext , R.color.red ));
//            baseViewHolder.setText(R.id.status , "已拒绝");
//        }else {
//            baseViewHolder.setTextColor(R.id.status,ContextCompat.getColor(mContext,R.color.black));
//            baseViewHolder.setText(R.id.status , "未审核");
//        }
    }

}
