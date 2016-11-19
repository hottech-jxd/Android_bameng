package com.bameng.adapter;

import android.widget.LinearLayout;

import com.bameng.R;
import com.bameng.model.CustomerModel;
import com.bameng.model.ListModel;
import com.bameng.model.UserData;
import com.bameng.utils.DensityUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2016/11/16.
 */

public class ChooseCustomerAdapter extends BaseQuickAdapter<UserData, BaseViewHolder> {

    public ChooseCustomerAdapter( ) {
        super( R.layout.choose_item ,null );
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, UserData  userData ) {
        baseViewHolder.setImageResource( R.id.image , userData.isSelected() ? R.mipmap.ic_choose : R.mipmap.ic_nochoose);
        baseViewHolder.setText( R.id.name , userData.getNickName());
        baseViewHolder.setText(R.id.moblie, userData.getUserMobile());
    }
}
