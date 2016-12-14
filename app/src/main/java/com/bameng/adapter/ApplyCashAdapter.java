package com.bameng.adapter;

import com.bameng.R;
import com.bameng.model.ConvertFlowModel;
import com.bameng.model.MengModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;


/**
 * Created by Administrator on 2016/11/16.
 */
public class ApplyCashAdapter extends BaseQuickAdapter<ConvertFlowModel, BaseViewHolder> {
    private int type;

    public ApplyCashAdapter() {
        super(R.layout.cash_apply_item, null);
    }

    public ApplyCashAdapter(int type) {
        super(R.layout.cash_apply_item, null);
        this.type = type;
    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, ConvertFlowModel obj) {
        setItem(baseViewHolder, obj);
    }

    void setItem(BaseViewHolder baseViewHolder, ConvertFlowModel obj) {
        SimpleDraweeView imv = baseViewHolder.getView(R.id.img);
        imv.setImageURI(obj.getHeadimg());

        baseViewHolder.setText(R.id.name, "盟友:" + obj.getName());
        baseViewHolder.setText(R.id.beam, String.valueOf(obj.getMoney()));
        String statusName = obj.getStatus() == 0 ? "未审核" : obj.getStatus() == 1 ? "已审核" : "拒绝";
        baseViewHolder.setText(R.id.status, statusName);

        baseViewHolder.addOnClickListener(R.id.btnAgree);
        baseViewHolder.addOnClickListener(R.id.btnReject);

        baseViewHolder.setVisible(R.id.lay_btn, obj.getStatus() == 0);
        baseViewHolder.setVisible(R.id.status, type == 1);
    }
}
