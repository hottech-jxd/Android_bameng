//package com.bameng.ui;
//
//
//import android.content.ComponentName;
//import android.content.Intent;
//import android.graphics.Bitmap;
//import android.graphics.drawable.BitmapDrawable;
//import android.os.Bundle;
//import android.os.Handler;
//import android.text.TextUtils;
//import android.util.DisplayMetrics;
//import android.util.Log;
//import android.view.Gravity;
//import android.view.View;
//import android.view.animation.AlphaAnimation;
//import android.view.animation.Animation;
//import android.view.animation.Animation.AnimationListener;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//
//
//import com.bameng.BaseApplication;
//import com.bameng.R;
//import com.bameng.config.Constants;
//import com.bameng.ui.base.BaseActivity;
//import com.bameng.widgets.MsgPopWindow;
//
//import java.io.IOException;
//import java.io.InputStream;
//import java.lang.ref.WeakReference;
//import java.util.ArrayList;
//import java.util.List;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//
//public class SplashActivity extends BaseActivity {
//
//
//    protected void onCreate ( Bundle savedInstanceState ) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_splash);
//        ButterKnife.bind(this);
//        DisplayMetrics metrics = new DisplayMetrics();
//        getWindowManager().getDefaultDisplay().getMetrics(metrics);
//        Constants.SCREEN_DENSITY = metrics.density;
//        Constants.SCREEN_HEIGHT = metrics.heightPixels;
//        Constants.SCREEN_WIDTH = metrics.widthPixels;
//        mHandler = new Handler(getMainLooper());
//
//        //initView();
//
//
//
//    }
//
//    @Override
//    protected void initView() {
//
//    }
//
//
//}
//
