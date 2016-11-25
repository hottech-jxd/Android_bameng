package com.bameng.widgets.custom;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.widget.LinearLayout;

import com.bameng.R;
import com.bameng.config.Constants;
import com.bameng.model.AdBannerConfig;
import com.bameng.model.AdImageBean;
import com.bameng.model.SlideListModel;
import com.bameng.ui.WebViewActivity;
import com.bameng.utils.ActivityUtils;
import com.bameng.utils.DensityUtils;
import com.bameng.widgets.FrescoHolderView;
import com.bigkoo.convenientbanner.holder.CBViewHolderCreator;
import com.bigkoo.convenientbanner.listener.OnItemClickListener;



/**
 * Banner组件
 * Created by jinxiangdong on 2016/1/13.
 */
public class AdBannerWidget extends com.bigkoo.convenientbanner.ConvenientBanner
        implements OnItemClickListener, FrescoControllerListener.ImageCallback{
    private AdBannerConfig config;

    public AdBannerWidget(Context context) {
        super(context);
    }

    public AdBannerWidget(Context context , AdBannerConfig  config ) {
        super(context);

        this.config = config;
        final int iwidth = DensityUtils.getScreenW(getContext()); //this.config.getWidth();
        int iHeight = 150;//LayoutParams.WRAP_CONTENT;
        iHeight = DensityUtils.dip2px( getContext(), iHeight);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( iwidth , iHeight );
        this.setLayoutParams(layoutParams);

        this.setPages(new CBViewHolderCreator() {
            @Override
            public Object createHolder() {
                return new FrescoHolderView( iwidth , AdBannerWidget.this );
            }
        }, config.getImages() )
        //设置两个点图片作为翻页指示器，不设置则没有指示器，可以根据自己需求自行配合自己的指示器,不需要圆点指示器可用不设
        .setPageIndicator(new int[]{R.mipmap.ic_page_indicator, R.mipmap.ic_page_indicator_focused})
        .setOnItemClickListener(this);

        this.stopTurning();
        if(!config.isAutoPlay()) return;
        if(config.getImages().size()<=1) return;


        int time = config.getImages().size()*1500;
        this.startTurning( time );
    }

    @Override
    public void onItemClick(int position) {
        SlideListModel bean = config.getImages().get( position );
        go(bean);
    }

    @Override
    public void imageCallback(int width, int height) {
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams( width , height );
        this.setLayoutParams(layoutParams);
    }

    protected void go(SlideListModel bean){
        String relateUrl = bean.getLinkUrl();
        Intent intent = new Intent(getContext() , WebViewActivity.class);
        intent.putExtra(Constants.INTENT_URL, relateUrl);
        ActivityUtils.getInstance().showActivity((Activity) getContext(),intent);
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();

        this.stopTurning();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        int he=0;
//        int count = this.getViewPager().getChildCount();
//        for(int i = 0;i<count;i++){
//            View v = this.getViewPager().getChildAt(i);
//            v.measure( widthMeasureSpec , MeasureSpec.makeMeasureSpec( 0 , MeasureSpec.UNSPECIFIED ) );
//            int h = v.getMeasuredHeight();
//            if(h>he){he=h;}
//        }
//        heightMeasureSpec = MeasureSpec.makeMeasureSpec(he,MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

}
