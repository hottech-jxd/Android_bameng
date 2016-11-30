package com.bameng.adapter;

import android.telephony.CellIdentityGsm;
import android.view.Gravity;
import android.widget.LinearLayout;

import com.bameng.R;
import com.bameng.model.ArticleModel;
import com.bameng.model.ListModel;
import com.bameng.utils.DensityUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;

/**
 * Created by Administrator on 2016/11/16.
 */
public class StoreAdapter extends BaseQuickAdapter<ListModel , BaseViewHolder> {

    public StoreAdapter( ) {
        super( R.layout.article_item ,null );
    }

    public StoreAdapter( int layoutId ){
        super(layoutId, null);
    }

    @Override
    protected void convert(BaseViewHolder baseViewHolder, ListModel listModel ) {
        SimpleDraweeView imv = baseViewHolder.getView(R.id.image);
        if(listModel.isTop()) {
            int wPx = DensityUtils.dip2px(this.mContext, 90);
            int hPx = wPx;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(wPx,hPx);
            layoutParams.gravity= Gravity.CENTER_VERTICAL;
            imv.setLayoutParams(layoutParams);
        }else{

            int wPx = DensityUtils.dip2px(this.mContext, 70);
            int hPx = wPx;
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(wPx,hPx);
            layoutParams.gravity = Gravity.CENTER_VERTICAL;
            imv.setLayoutParams(layoutParams);
        }

       DraweeController draweeController= Fresco.newDraweeControllerBuilder()
               .setUri(listModel.getArticleCover())
               .setAutoPlayAnimations(true).build();
        GenericDraweeHierarchy genericDraweeHierarchy =
                new GenericDraweeHierarchyBuilder(mContext.getResources()).setFailureImage(R.mipmap.ic_picture)
                .setPlaceholderImage(R.mipmap.ic_picture).build();
        imv.setController(draweeController);
        imv.setHierarchy(genericDraweeHierarchy);


        baseViewHolder.setText(R.id.articleTitle, listModel.getArticleTitle());
        baseViewHolder.setText(R.id.articleIntro, listModel.getArticleIntro());
        baseViewHolder.setText(R.id.browseAmount , String.valueOf(listModel.getBrowseAmount()));
        baseViewHolder.setText(R.id.time , listModel.getPublishTimeText());
    }
}
