package com.bameng.ui;


import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;

import android.support.v4.content.ContextCompat;
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
import com.bameng.config.Constants;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;

import org.greenrobot.eventbus.EventBus;

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
    String url = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_load_page);
        ButterKnife.bind(this);
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();

        viewPage.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();

        viewPage.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ( );
        ButterKnife.unbind(this);
        if( viewPage !=null ){
            viewPage.setVisibility(View.GONE);
        }
    }

    @Override
    protected void initView() {
        Bundle bundle = this.getIntent().getExtras();
        url = bundle.getString ( Constants.INTENT_URL );


        //设置左侧图标
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back );
        SystemTools.loadBackground(titleLeftImage, leftDraw);

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
        String token = application.readToken()+"(Authorization)";
        if(null==userAgent|| TextUtils.isEmpty(userAgent)){
            userAgent = token;
        }
        else{
            int idx = userAgent.indexOf("(Authorization)");
            if(idx>=0){
                userAgent = userAgent.substring(idx+ 15 );
//                idx = userAgent.lastIndexOf(";");
//                if(idx>=0){
//                    userAgent = userAgent.substring(0,idx);
//                }
            }
            if( !userAgent.startsWith(";")) {
                userAgent = token + ";" + userAgent;
            }else{
                userAgent = token + userAgent;
            }
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

                        if (titleText == null) return;
                        titleText.setText(view.getTitle());

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


        viewPage.setWebChromeClient(new WebChromeClient(){
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
            }

            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
                if (titleText == null) {
                    return;
                }
                if (title == null) {
                    return;
                }

                titleText.setText(title);
            }
        });


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



