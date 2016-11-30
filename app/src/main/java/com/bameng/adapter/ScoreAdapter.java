package com.bameng.adapter;


import android.support.v4.content.ContextCompat;

import com.bameng.R;
import com.bameng.model.ScoreModel;
import com.bameng.utils.DateUtils;
import com.bameng.utils.Util;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import static com.baidu.location.h.j.o;

/**
 * Created by Administrator on 2016/11/21.
 */

public class ScoreAdapter extends BaseQuickAdapter<ScoreModel, BaseViewHolder> {
    public ScoreAdapter() {

        super(R.layout.score_item , null);


    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ScoreModel scoreModel) {
        baseViewHolder.setText(R.id.tvname , scoreModel.getRemark());
        baseViewHolder.setText( R.id.tvTime , DateUtils.formatDate( scoreModel.getTime() ,"yyyy.MM.dd HH:mm" ));

        if( scoreModel.getMoney()>=0){
            baseViewHolder.setText(R.id.tvBeans , "+"+String.valueOf( scoreModel.getMoney()));
            baseViewHolder.setTextColor( R.id.tvBeans , ContextCompat.getColor( this.mContext , R.color.red ));
        }else{
            baseViewHolder.setText(R.id.tvBeans , String.valueOf( scoreModel.getMoney()));
            baseViewHolder.setTextColor(R.id.tvBeans , ContextCompat.getColor( this.mContext ,R.color.green));
        }
    }
}