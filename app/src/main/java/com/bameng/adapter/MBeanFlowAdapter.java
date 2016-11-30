package com.bameng.adapter;

import com.bameng.R;
import com.bameng.model.BeanRecordModel;
import com.bameng.utils.DateUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

/**
 * 盟豆流水
 * Created by Administrator on 2016/11/22.
 */
public class MBeanFlowAdapter extends BaseQuickAdapter <BeanRecordModel, BaseViewHolder> {

    public MBeanFlowAdapter() {
        super( R.layout.mbean_item, null );
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, BeanRecordModel data ) {
        baseViewHolder.setText( R.id.tvname , data.getStatus()== 0? "支出" :"收入"  );

        baseViewHolder.setText( R.id.tvTime , DateUtils.formatDate( data.getTime() , "yyyy.MM.dd HH:mm" ));

        baseViewHolder.setText( R.id.tvBeans , String.valueOf( data.getMoney() ));

        baseViewHolder.setText(R.id.tvRemarks , data.getRemark() );
    }
}
