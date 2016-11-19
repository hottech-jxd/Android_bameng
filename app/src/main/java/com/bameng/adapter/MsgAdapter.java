package com.bameng.adapter;

import com.bameng.R;
import com.bameng.model.ListModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Administrator on 2016/11/17.
 */

public class MsgAdapter extends BaseQuickAdapter<ListModel, BaseViewHolder> {
    public MsgAdapter( ) {
        super( R.layout.layout_msg_item ,null );
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ListModel listModel) {
        baseViewHolder.setText( R.id.tvText , listModel.getArticleIntro()  );
        baseViewHolder.setText(R.id.tvTime , listModel.getPublishTimeText());
        baseViewHolder.setBackgroundRes( R.id.tvFrag , listModel.getIsRead() ==0 ? R.drawable.circle_red : R.drawable.circle_white );
    }
}
