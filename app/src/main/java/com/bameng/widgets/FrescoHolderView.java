package com.bameng.widgets;

import android.content.Context;
import android.view.View;
import android.widget.LinearLayout;

import com.bameng.model.SlideListModel;
import com.bameng.widgets.custom.FrescoControllerListener;
import com.bameng.widgets.custom.FrescoDraweeController;
import com.bigkoo.convenientbanner.holder.Holder;
import com.facebook.drawee.view.SimpleDraweeView;

/**
 * Created by Administrator on 2016/1/13.
 */
public class FrescoHolderView implements Holder<SlideListModel> {
    private SimpleDraweeView iv;
    private int width;
    private FrescoControllerListener.ImageCallback imageCallback;

    public FrescoHolderView(int w , FrescoControllerListener.ImageCallback imageCallback){
        this.width = w;
        this.imageCallback = imageCallback;
    }
    @Override
    public View createView(Context context ) {
        iv = new SimpleDraweeView(context);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(width, LinearLayout.LayoutParams.WRAP_CONTENT);
        iv.setLayoutParams(layoutParams);
        return iv;
    }
    @Override
    public void UpdateUI(Context context, int position, SlideListModel data) {

        String imageUrl =data.getPicUrl();
        FrescoDraweeController.loadImage(iv, width,  imageUrl , imageCallback );
    }
}
