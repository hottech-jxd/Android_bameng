package com.bameng.adapter;

import android.support.v4.content.ContextCompat;
import android.widget.LinearLayout;

import com.bameng.R;
import com.bameng.model.ListModel;
import com.bameng.model.OrderModel;
import com.bameng.utils.DensityUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2016/11/16.
 */
public class OrderAdapter extends BaseQuickAdapter<OrderModel, BaseViewHolder> {

    public OrderAdapter( ) {
        super( R.layout.layout_order_item ,null );
    }

    public OrderAdapter(int layoutId ){
        super(layoutId, null);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, OrderModel orderModel ) {
        SimpleDraweeView imv = baseViewHolder.getView(R.id.image);

       DraweeController draweeController= Fresco.newDraweeControllerBuilder()
                .setAutoPlayAnimations(true).build();
        imv.setController(draweeController);
        imv.setImageURI( orderModel.getPictureUrl() );

        baseViewHolder.setText(R.id.tvname, orderModel.getUserName());
        baseViewHolder.setText(R.id.tvphone, orderModel.getMobile());
        baseViewHolder.setText(R.id.tvBeans , String.valueOf(orderModel.getMengbeans()));

        if( orderModel.getStatus() == 1 ){
            baseViewHolder.setTextColor( R.id.status , ContextCompat.getColor( mContext , R.color.green) );
        }else if(orderModel.getStatus() == 2){
            baseViewHolder.setTextColor( R.id.status , ContextCompat.getColor( mContext, R.color.red));
        }else if( orderModel.getStatus() == 0){
            baseViewHolder.setTextColor(R.id.status, ContextCompat.getColor(mContext, R.color.red));
        }

        baseViewHolder.setText(R.id.status ,  orderModel.getStatusName());
    }
}
