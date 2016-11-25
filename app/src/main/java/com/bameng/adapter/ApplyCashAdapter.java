package com.bameng.adapter;

import android.opengl.Visibility;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.bameng.R;
import com.bameng.model.ConvertFlowModel;
import com.bameng.model.MengModel;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import static android.R.attr.type;
import static com.bameng.R.id.status;

/**
 * Created by Administrator on 2016/11/16.
 */
public class ApplyCashAdapter extends BaseQuickAdapter<ConvertFlowModel, BaseViewHolder> {

    public ApplyCashAdapter(  ) {

        super(  R.layout.cash_apply_item , null );

    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, ConvertFlowModel obj ) {
        setItem (baseViewHolder, obj );
    }

    void setItem(BaseViewHolder baseViewHolder, ConvertFlowModel obj ){
        SimpleDraweeView imv = baseViewHolder.getView(R.id.img);
        DraweeController draweeController= Fresco.newDraweeControllerBuilder()
                .setUri(obj.getHeadimg())
                .setAutoPlayAnimations(true).build();
        imv.setController(draweeController);
//
        baseViewHolder.setText( R.id.name , obj.getName());
        baseViewHolder.setText( R.id.beam , String.valueOf(obj.getMoney()));
        String statusName = obj.getStatus()==0? "未审核": obj.getStatus()==1?"已审核":"拒绝";
        baseViewHolder.setText( R.id.status ,  statusName);

        baseViewHolder.addOnClickListener(R.id.btnAgree);
        baseViewHolder.addOnClickListener(R.id.btnReject);

        baseViewHolder.setVisible( R.id.lay_btn, obj.getStatus()==0? true : false);

    }

}
