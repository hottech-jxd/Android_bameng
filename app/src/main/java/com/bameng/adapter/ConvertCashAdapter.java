package com.bameng.adapter;


import android.support.v4.content.ContextCompat;

import com.bameng.R;
import com.bameng.model.ConvertFlowModel;
import com.bameng.model.ScoreModel;
import com.bameng.utils.DateUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Administrator on 2016/11/21.
 */

public class ConvertCashAdapter extends BaseQuickAdapter<ConvertFlowModel , BaseViewHolder> {
    public ConvertCashAdapter() {
        super(R.layout.convertcash_item , null);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ConvertFlowModel convertFlowModel ) {
        baseViewHolder.setText(R.id.tvname , convertFlowModel.getName() );
        baseViewHolder.setText( R.id.tvTime , DateUtils.formatDate( convertFlowModel.getTime() ,"yyyy.MM.dd HH:mm" ));
        baseViewHolder.setText(R.id.tvBeans , String.valueOf( convertFlowModel.getMoney()));
    }
}