package com.bameng.adapter;

import com.bameng.R;
import com.bameng.model.ListModel;
import com.bameng.model.WorkReportModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * Created by Administrator on 2016/11/17.
 */

public class WorkReportAdapter extends BaseQuickAdapter<WorkReportModel, BaseViewHolder> {
    public WorkReportAdapter( ) {
        super( R.layout.layout_msg_item ,null );
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, WorkReportModel workReportModel) {
        baseViewHolder.setText( R.id.tvText , workReportModel.getReportTitle()  );
        baseViewHolder.setText(R.id.tvTime , workReportModel.getTime());
        baseViewHolder.setVisible( R.id.tvFrag , false );
    }
}
