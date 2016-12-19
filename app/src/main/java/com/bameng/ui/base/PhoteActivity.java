package com.bameng.ui.base;

import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.utils.DensityUtils;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.app.TakePhotoActivity;
import com.jph.takephoto.app.TakePhotoFragmentActivity;
import com.jph.takephoto.compress.CompressConfig;
import com.jph.takephoto.model.CropOptions;
import com.jph.takephoto.model.LubanOptions;
import com.jph.takephoto.model.TakePhotoOptions;
import com.umeng.analytics.MobclickAgent;

import me.shaohui.advancedluban.Luban;

import static android.R.attr.width;

/**
 * Created by Administrator on 2016/11/28.
 */

public abstract class PhoteActivity extends TakePhotoActivity {
    public BaseApplication application;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );

        application = ( BaseApplication) this.getApplication ();
    }

    @Override
    protected void onResume() {
        super.onResume();

        MobclickAgent.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if( keyCode == KeyEvent.KEYCODE_BACK) {
            this.finish();
        }

        return super.onKeyDown(keyCode,event);
    }

    public void onGoBackButton(View v) {
        if (v.getId() == R.id.titleLeftImage) {
            finish();
        }
    }

    protected void selectByFile(Uri imageUri){

        getConfig();
        getCropOptions();

        TakePhoto takePhoto = getTakePhoto();
        takePhoto.onPickFromGalleryWithCrop(imageUri,getCropOptions());


    }

    protected void selectByCamera(Uri imageUri){
        getConfig();
        getCropOptions();

        TakePhoto takePhoto = getTakePhoto();
        takePhoto.onPickFromCaptureWithCrop(imageUri,getCropOptions());
    }


    protected void getConfig(){

        TakePhoto takePhoto = getTakePhoto();

        int maxSize= Integer.parseInt("102400");
        CompressConfig config;

        int width = DensityUtils.getScreenW(this);
        int height = DensityUtils.getScreenH(this);

        LubanOptions option=new LubanOptions.Builder()
                .setGear(Luban.CUSTOM_GEAR)
                .setMaxHeight(height)
                .setMaxWidth(width)
                .setMaxSize(maxSize)
                .create();
        config= CompressConfig.ofLuban(option);
        boolean showProgressBar = true;



        takePhoto.onEnableCompress(config,showProgressBar);

        //takePhoto.setTakePhotoOptions(new TakePhotoOptions.Builder().setWithOwnGallery(true).create());

    }


    protected CropOptions getCropOptions(){
        int cropHeight = DensityUtils.getScreenH(this);
        int cropWidth = DensityUtils.getScreenW(this);

        //int height= Integer.parseInt(etCropHeight.getText().toString());
        //int width= Integer.parseInt(etCropWidth.getText().toString());
        boolean withWonCrop= false;//rgCropTool.getCheckedRadioButtonId()==R.id.rbCropOwn? true:false;

        CropOptions.Builder builder=new CropOptions.Builder();

        //if(rgCropSize.getCheckedRadioButtonId()==R.id.rbAspect){
        //    builder.setAspectX(cropWidth).setAspectY(cropHeight);
        //}else {
            builder.setOutputX(cropWidth).setOutputY(cropHeight);
        //}
        builder.setWithOwnCrop(withWonCrop);
        return builder.create();
    }


}
