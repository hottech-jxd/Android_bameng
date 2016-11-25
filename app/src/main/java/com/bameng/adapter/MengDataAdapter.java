package com.bameng.adapter;

import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.bameng.R;
import com.bameng.model.ListModel;
import com.bameng.model.MengModel;
import com.bameng.model.UserData;
import com.bameng.utils.DensityUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2016/11/16.
 */
public class MengDataAdapter extends BaseQuickAdapter<MengModel, BaseViewHolder> {
    int type = 0;
    public MengDataAdapter( int type ) {

        super( type == 0 ? R.layout.meng_apply_item: R.layout.meng_item, null);

        this.type=type;

    }


    @Override
    protected void convert(BaseViewHolder baseViewHolder, MengModel userData ) {
      if( type == 0){
          setApplyItem(baseViewHolder,userData);
      }else{
          setItem(baseViewHolder,userData);
      }
    }

    void setItem(BaseViewHolder baseViewHolder, MengModel userData){
        SimpleDraweeView imv = baseViewHolder.getView(R.id.img);
        DraweeController draweeController= Fresco.newDraweeControllerBuilder()
                .setUri(userData.getUserHeadImg())
                .setAutoPlayAnimations(true).build();
        imv.setController(draweeController);


        baseViewHolder.setText( R.id.name , userData.getRealName() );

        baseViewHolder.setText( R.id.levelname , userData.getLevelName());

    }


    void setApplyItem(BaseViewHolder baseViewHolder, MengModel userData){
        SimpleDraweeView imv = baseViewHolder.getView(R.id.img);


        DraweeController draweeController= Fresco.newDraweeControllerBuilder()
                .setUri(userData.getUserHeadImg())
                .setAutoPlayAnimations(true).build();
        imv.setController(draweeController);

        baseViewHolder.setText( R.id.name , "客户名称:"+userData.getUserName());
        baseViewHolder.setText( R.id.moblie , "联系方式:"+userData.getMobile());

        ProgressBar progressBar= baseViewHolder.getView(R.id.progressBar);
        progressBar.setVisibility( userData.isDoing() ? View.VISIBLE : View.GONE );
        Button btnAgress = baseViewHolder.getView(R.id.btnAgree);
        btnAgress.setVisibility(userData.isDoing()? View.GONE:View.VISIBLE);
        Button btnReject= baseViewHolder.getView(R.id.btnReject);
        btnReject.setVisibility(userData.isDoing()? View.GONE:View.VISIBLE);

        baseViewHolder.setText(R.id.status , userData.getStatusName());

        baseViewHolder.addOnClickListener(R.id.btnAgree);
        baseViewHolder.addOnClickListener(R.id.btnReject);

        if( userData.getStatus() == 2 ){
            baseViewHolder.setVisible( R.id.lay_btn , false );
        }else{
            baseViewHolder.setVisible( R.id.lay_btn, true);
        }
    }
}
