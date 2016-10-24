//package com.bameng.ui;
//
//import android.content.Context;
//import android.content.Intent;
//import android.content.res.Resources;
//import android.graphics.Bitmap;
//import android.graphics.drawable.Drawable;
//import android.net.Uri;
//import android.os.Build;
//import android.os.Bundle;
//import android.os.Handler;
//import android.os.Message;
//import android.text.TextUtils;
//import android.view.Gravity;
//import android.view.KeyEvent;
//import android.view.View;
//import android.view.WindowManager;
//import android.webkit.GeolocationPermissions;
//import android.webkit.JavascriptInterface;
//import android.webkit.JsResult;
//import android.webkit.ValueCallback;
//import android.webkit.WebChromeClient;
//import android.webkit.WebSettings;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.ImageView;
//import android.widget.ProgressBar;
//import android.widget.RelativeLayout;
//import android.widget.TextView;
//import android.widget.Toast;
//
//
//import com.bameng.BaseApplication;
//import com.bameng.ui.base.BaseActivity;
//import com.bameng.widgets.TipAlertDialog;
//
//import java.util.HashMap;
//
//import butterknife.Bind;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import in.srain.cube.views.ptr.PtrClassicFrameLayout;
//import in.srain.cube.views.ptr.PtrDefaultHandler;
//import in.srain.cube.views.ptr.PtrFrameLayout;
//import in.srain.cube.views.ptr.PtrHandler;
//
////import com.handmark.pulltorefresh.library.PullToRefreshBase;
////import com.handmark.pulltorefresh.library.PullToRefreshWebView;
//
///**
// * 单张展示web页面
// */
//public class WebViewActivity extends BaseActivity  {
//
//
//    @Override
//    protected void onCreate ( Bundle savedInstanceState ) {
//        super.onCreate(savedInstanceState);
//        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE | WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
//
//
//        this.setContentView(R.layout.new_load_page);
//        initView();
//    }
//
//    @Override
//    protected void initView() {
//
//    }
//
//
//    private void loadPage() {
//        viewPage.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
//        viewPage.setVerticalScrollBarEnabled(false);
//        viewPage.setClickable(true);
//        viewPage.getSettings().setUseWideViewPort(true);
//        //是否需要避免页面放大缩小操作
//        //viewPage.getSettings().setSupportZoom(true);
//        //viewPage.getSettings().setBuiltInZoomControls(true);
//        viewPage.getSettings().setJavaScriptEnabled(true);
//        viewPage.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
//        viewPage.getSettings().setSaveFormData(true);
//        viewPage.getSettings().setAllowFileAccess(true);
//        viewPage.getSettings().setLoadWithOverviewMode(false);
//        viewPage.getSettings().setSavePassword(true);
//        viewPage.getSettings().setLoadsImagesAutomatically(true);
//        viewPage.getSettings().setDomStorageEnabled(true);
//        viewPage.getSettings().setAppCacheEnabled(true);
//        viewPage.getSettings().setDatabaseEnabled(true);
//        String dir = BaseApplication.single.getDir("database", Context.MODE_PRIVATE).getPath();
//        viewPage.getSettings().setGeolocationDatabasePath(dir);
//        viewPage.getSettings().setGeolocationEnabled(true);
//        viewPage.addJavascriptInterface(this, "android");
//
//
//        viewPage.loadUrl(url);
//
//    }
//        @OnClick(R.id.titleLeftImage)
//        void doBack () {
//            WebViewActivity.this.finish();
//        }
//
//
//        @Override
//        public boolean dispatchKeyEvent (KeyEvent event){
//            if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
//                closeSelf(WebViewActivity.this);
//                return true;
//            }
//            return super.dispatchKeyEvent(event);
//        }
//
//    }
//
//
//}
