package com.bameng.adapter;


import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.config.Constants;
import com.bameng.model.CashCouponModel;
import com.bameng.model.MengModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Administrator on 2016/11/21.
 */

public class CashAdapter extends BaseQuickAdapter<CashCouponModel, BaseViewHolder> {
    public CashAdapter() {

        super(R.layout.cashcard_item, null);


    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CashCouponModel object) {
        baseViewHolder.setText( R.id.tvMoney , "￥"+String.valueOf( object.getMoney() ));
        baseViewHolder.setText( R.id.tvName , object.getName());
        baseViewHolder.setText(R.id.tvDue , "使用期限:"+ object.getDue());

        baseViewHolder.addOnClickListener(R.id.ivShare);

//        int userType = BaseApplication.UserData() ==null ? -1 : BaseApplication.UserData().getUserIdentity();
//        baseViewHolder.setVisible( R.id.ivInShare , userType == Constants.MENG_ZHU );
    }
}