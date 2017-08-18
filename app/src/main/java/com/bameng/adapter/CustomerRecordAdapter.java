package com.bameng.adapter;

import com.bameng.R;
import com.bameng.model.CustomerRecord;
import com.bameng.utils.DateUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

/**
 * 客户动态记录
 * Created by Administrator on 2017/8/1.
 */
public class CustomerRecordAdapter extends BaseQuickAdapter<CustomerRecord , BaseViewHolder> {

    public CustomerRecordAdapter(){
        super(R.layout.layout_customer_record_item , null);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, CustomerRecord customerRecord) {
        baseViewHolder.setText( R.id.customerRecord_content , customerRecord.getAssertContent() );
        baseViewHolder.setText( R.id.customerReocord_time ,   customerRecord.getCreateTime()  );
    }
}
