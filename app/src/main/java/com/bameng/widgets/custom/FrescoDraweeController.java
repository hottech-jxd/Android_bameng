package com.bameng.widgets.custom;

import android.net.Uri;
import android.text.TextUtils;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.core.ImagePipelineFactory;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

/**
 * Created by Administrator on 2016/1/6.
 */
public class FrescoDraweeController {
    public static void loadImage(SimpleDraweeView simpleDraweeView , int width , String url){
//        DraweeController draweeController = Fresco
//                .newDraweeControllerBuilder()
//                .setAutoPlayAnimations(true)
//                .setTapToRetryEnabled(true)
//                .setUri( url )
//                .setOldController(simpleDraweeView.getController())
//                .setControllerListener( new FrescoControllerListener(simpleDraweeView , width))
//                .build();
//        simpleDraweeView.setController( draweeController);
        loadImage(simpleDraweeView, width, url , null);
    }

    public static void loadImage(SimpleDraweeView simpleDraweeView , int width , String url , FrescoControllerListener.ImageCallback imageCallback){
        FrescoControllerListener listener =  new FrescoControllerListener(simpleDraweeView , width);
        listener.setImageCallback(imageCallback);
        DraweeController draweeController = Fresco
                .newDraweeControllerBuilder()
                .setAutoPlayAnimations(true)
                .setTapToRetryEnabled(true)
                .setUri( url )
                .setOldController(simpleDraweeView.getController())
                .setControllerListener( listener )
                .build();
        simpleDraweeView.setController( draweeController);
    }

    public static void loadImage(SimpleDraweeView simpleDraweeView , int width , int height , String url){
        if(TextUtils.isEmpty( url )) return;

        ResizeOptions resizeOptions = new ResizeOptions( width, height);

        ImageRequest imageRequest = ImageRequestBuilder
                .newBuilderWithSource(Uri.parse(url))
                .setResizeOptions(resizeOptions).build();

        DraweeController draweeController = Fresco.newDraweeControllerBuilder()
                .setOldController(simpleDraweeView.getController())
                .setImageRequest(imageRequest)
                .setTapToRetryEnabled(true)
                .build();

//        ViewGroup.LayoutParams layoutParams = simpleDraweeView.getLayoutParams();
//        if( layoutParams!=null) {
//            layoutParams.height = height;
//            layoutParams.width = width;
//            simpleDraweeView.setLayoutParams(layoutParams);
//        }
        simpleDraweeView.setController(draweeController);
    }

    public static void clearCache(){
        ImagePipelineFactory.getInstance().getImagePipeline().clearCaches();

    }

    public static void clearDiskCaches(){
        ImagePipelineFactory.getInstance().getImagePipeline().clearDiskCaches();
    }

    public static void clearMoneyCaches(){
        ImagePipelineFactory.getInstance().getImagePipeline().clearMemoryCaches();
    }

}
