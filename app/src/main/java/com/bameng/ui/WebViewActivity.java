package com.bameng.ui;


import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Message;

import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;


import com.bameng.R;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.AuthParamUtils;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 单张展示web页面
 */
public class WebViewActivity extends BaseActivity {


    @Bind(R.id.main_webview)
    WebView viewPage;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.titleText)
    TextView titleText;
    String url = "http://bmadmin.fancat.cn/article/details.html";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_load_page);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void initView() {
        loadPage();
    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {
        return false;
    }


    public void loadPage() {
        viewPage.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        viewPage.setVerticalScrollBarEnabled(false);
        viewPage.setClickable(true);
        viewPage.getSettings().setUseWideViewPort(true);
        //是否需要避免页面放大缩小操作

        viewPage.getSettings().setSupportZoom(true);
        viewPage.getSettings().setBuiltInZoomControls(true);
        viewPage.getSettings().setJavaScriptEnabled(true);
        viewPage.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
        viewPage.getSettings().setSaveFormData(true);
        viewPage.getSettings().setAllowFileAccess(true);
        viewPage.getSettings().setLoadWithOverviewMode(false);
        viewPage.getSettings().setSavePassword(true);
        viewPage.getSettings().setLoadsImagesAutomatically(true);
        viewPage.getSettings().setDomStorageEnabled(true);
        viewPage.loadUrl(url);

        //设置angent
        String userAgent = viewPage.getSettings().getUserAgentString();
        Map<String, String> map = new HashMap<>();
        map.put("version", application.getAppVersion());
        map.put("timestamp", String.valueOf(System.currentTimeMillis()));
        map.put("os", "android");
        AuthParamUtils authParamUtils = new AuthParamUtils();
        String sign = authParamUtils.getSign(map);
        if(null==userAgent|| TextUtils.isEmpty(userAgent))
        {
            userAgent = "mobile;"+sign;
        }
        else
        {
            userAgent += ";mobile;"+sign;
        }
        viewPage.getSettings().setUserAgentString(userAgent);
        viewPage.setWebViewClient(
                new WebViewClient() {


                    @Override
                    public void onPageStarted(WebView view, String url, Bitmap favicon) {
                        super.onPageStarted(view, url, favicon);
                    }

                    @Override
                    public void onPageFinished(WebView view, String url) {
                        //页面加载完成后,读取菜单项
                        super.onPageFinished(view, url);
                    }

                    @Override
                    public void onReceivedError(
                            WebView view, int errorCode, String description,
                            String failingUrl
                    )
                    {
                        super.onReceivedError(view, errorCode, description, failingUrl);
                    }

                }
        );




    }

    @OnClick(R.id.titleLeftImage)
    void doBack() {
        WebViewActivity.this.finish();
    }


    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            closeSelf(WebViewActivity.this);
            return true;
        }
        return super.dispatchKeyEvent(event);
    }

}



