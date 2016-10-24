package com.bameng.widgets.custom;

import android.graphics.drawable.Animatable;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.facebook.drawee.controller.BaseControllerListener;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.image.ImageInfo;

import java.lang.ref.WeakReference;

/**
 * Created by Administrator on 2016/1/6.
 */
public class FrescoControllerListener extends BaseControllerListener<ImageInfo> {
    WeakReference< SimpleDraweeView> ref;
    int width;
    ImageCallback imageCallback;

    public interface ImageCallback{
        void imageCallback(int width, int height);
    }

    public void setImageCallback(ImageCallback imageCallback){
        this.imageCallback = imageCallback;
    }

    public FrescoControllerListener(SimpleDraweeView iv, int width){
        this.ref= new WeakReference<>(iv);
        this.width=width;
    }

    @Override
    public void onFinalImageSet(String id, ImageInfo imageInfo, Animatable animatable) {
        super.onFinalImageSet(id, imageInfo, animatable);
        if( imageInfo==null) return;
        if( ref.get() ==null ) return;

        int h = imageInfo.getHeight();
        int w = imageInfo.getWidth();

        int ivw = width;
        int ivh = h* ivw / w;
        ViewGroup.LayoutParams layoutParams =  ref.get().getLayoutParams();
        layoutParams.width = ivw;
        layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;

        ref.get().setLayoutParams(layoutParams);

        float ratio = w * 1.0f/h;
        ref.get().setAspectRatio(ratio);

        if(imageCallback!=null){
            imageCallback.imageCallback(ivw,ivh);
        }
    }

    @Override
    public void onFailure(String id, Throwable throwable) {
        super.onFailure(id, throwable);
        if( ref.get() ==null ) return;

        ViewGroup.LayoutParams layoutParams = ref.get().getLayoutParams();
        layoutParams.width = width;
        layoutParams.height = width;
        ref.get().setLayoutParams(layoutParams);
        float ratio = 1.0f;
        ref.get().setAspectRatio(ratio);
    }
}
