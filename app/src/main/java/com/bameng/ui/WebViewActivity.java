package com.bameng.ui;


import android.drm.DrmStore;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Message;

import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;


import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.config.Constants;
import com.bameng.model.RefreshWebViewEvent;
import com.bameng.model.ShareModel;
import com.bameng.ui.base.BaseActivity;
import com.bameng.utils.AuthParamUtils;
import com.bameng.utils.SystemTools;
import com.bameng.utils.ToastUtils;
import com.bameng.utils.WindowUtils;
import com.bameng.widgets.SharePopupWindow;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.wechat.favorite.WechatFavorite;



/**
 * 单张展示web页面
 */
public class WebViewActivity extends BaseActivity implements PlatformActionListener{

    @Bind(R.id.main_webview)
    WebView viewPage;
    @Bind(R.id.titleLeftImage)
    ImageView titleLeftImage;
    @Bind(R.id.titleText)
    TextView titleText;
    @Bind(R.id.titleRightImage)
    ImageView titleRightImage;

    String url = "";

    SharePopupWindow sharePopupWindow;


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
        EventBus.getDefault().post(new RefreshWebViewEvent());
    }

    @Override
    protected void initView() {
        Bundle bundle = this.getIntent().getExtras();
        url = bundle.getString ( Constants.INTENT_URL );

        //设置左侧图标
        Drawable leftDraw = ContextCompat.getDrawable( this , R.mipmap.ic_back );
        SystemTools.loadBackground(titleLeftImage, leftDraw);
        Drawable rightDraw = ContextCompat.getDrawable( this , R.mipmap.ic_share );
        SystemTools.loadBackground(titleRightImage, rightDraw );
        titleRightImage.setVisibility(View.GONE);

        sharePopupWindow = new SharePopupWindow(this);
        sharePopupWindow.showShareWindow();
        sharePopupWindow.setPlatformActionListener(this);
        sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowUtils.backgroundAlpha ( WebViewActivity.this , 1.0f );
            }
        });

        loadPage();
    }

    @OnClick(R.id.titleRightImage)
    void onClick(View v){
        if( v.getId() == R.id.titleRightImage){
            share();
        }
    }

    void share(){
        viewPage.loadUrl("javascript:getShareData();");
    }

    @Override
    protected void StartApi() {
    }

    @Override
    public boolean handleMessage(Message msg) {
        switch (msg.what) {
            //分享
            case Constants.SHARE_SUCCESS: {
                //分享成功
                Platform platform = (Platform) msg.obj;
                if ("WechatMoments".equals(platform.getName())) {
                    ToastUtils.showShortToast( "微信朋友圈分享成功");
                } else if ("Wechat".equals(platform.getName())) {
                    ToastUtils.showShortToast( "微信分享成功");
                } else if ("QZone".equals(platform.getName())) {
                    ToastUtils.showShortToast( "QQ空间分享成功");
                } else if ("SinaWeibo".equals(platform.getName())) {
                    ToastUtils.showShortToast( "新浪微博分享成功");
                } else if (WechatFavorite.NAME.equals(platform.getName())) {
                    ToastUtils.showShortToast("微信收藏成功");
                }
            }
            break;
            case Constants.SHARE_ERROR: {
                //分享失败
                Platform platform = (Platform) msg.obj;
                if ("WechatMoments".equals(platform.getName())) {
                    ToastUtils.showShortToast( "微信朋友圈分享失败");
                } else if ("Wechat".equals(platform.getName())) {
                    ToastUtils.showShortToast( "微信分享失败");
                } else if ("QZone".equals(platform.getName())) {
                    ToastUtils.showShortToast( "QQ空间分享失败");
                } else if ("SinaWeibo".equals(platform.getName())) {
                    ToastUtils.showShortToast( "新浪微博分享失败");
                } else if (WechatFavorite.NAME.equals(platform.getName())) {
                    ToastUtils.showShortToast("微信收藏失败");
                }
            }
            break;
            case Constants.SHARE_CANCEL: {
                //分享取消
                Platform platform = (Platform) msg.obj;
                if ("WechatMoments".equals(platform.getName())) {
                    ToastUtils.showShortToast( "微信朋友圈分享取消");
                } else if ("Wechat".equals(platform.getName())) {
                    ToastUtils.showShortToast( "微信分享取消");
                } else if ("QZone".equals(platform.getName())) {
                    ToastUtils.showShortToast( "QQ空间分享取消");
                } else if ("SinaWeibo".equals(platform.getName())) {
                    ToastUtils.showShortToast( "新浪微博分享取消");
                } else if (WechatFavorite.NAME.equals(platform.getName())) {
                    ToastUtils.showShortToast("微信收藏取消");
                }
            }
            break;
        }
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
        viewPage.addJavascriptInterface(this, "android");
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

    @Override
    public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {
        Message msg = Message.obtain();
        msg.what = Constants.SHARE_SUCCESS;
        msg.obj = platform;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onError(Platform platform, int i, Throwable throwable) {
        Message msg = Message.obtain();
        msg.what = Constants.SHARE_ERROR;
        msg.obj = platform;
        mHandler.sendMessage(msg);
    }

    @Override
    public void onCancel(Platform platform, int i) {
        Message msg = Message.obtain();
        msg.what = Constants.SHARE_CANCEL;
        msg.obj = platform;
        mHandler.sendMessage(msg);
    }

    @JavascriptInterface
    public void enableShare( String state ){
        if(TextUtils.isEmpty( state ) || state.equals("true")) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(titleRightImage==null) return;
                    titleRightImage.setVisibility(View.VISIBLE);
                }
            });

        }else{
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if(titleRightImage==null)return;
                    titleRightImage.setVisibility(View.GONE);
                }
            });
        }
    }

    @JavascriptInterface
    public void sendShare(final String title, final String desc, final String link, final String img_url) {
        if (this == null) return;
        if (this.sharePopupWindow == null) return;

        this.mHandler.post(new Runnable() {
            @Override
            public void run() {

//                if( WebViewActivity.this ==null ) return;
//                if( progress !=null ){
//                    progress.dismissView();
//                }

                String sTitle = title;
                if( TextUtils.isEmpty( sTitle ) ){
                    sTitle = getString(R.string.app_name) + "分享";
                }
                String sDesc = desc;
                if( TextUtils.isEmpty( sDesc ) ){
                    sDesc = sTitle;
                }
                String imageUrl = img_url;
                if(TextUtils.isEmpty ( imageUrl )) {
                    imageUrl = Constants.COMMON_SHARE_LOGO;
                }

                String sLink = link;
//                if( TextUtils.isEmpty( sLink ) ){
//                    sLink = application.obtainMerchantUrl();
//                }
                ShareModel msgModel = new ShareModel ();
                msgModel.setImageUrl(imageUrl);
                msgModel.setText(sDesc);
                msgModel.setTitle(sTitle);
                msgModel.setUrl(sLink);
                //msgModel.setImageData(BitmapFactory.decodeResource( resources , R.drawable.ic_launcher ));
                sharePopupWindow.initShareParams(msgModel);
                WindowUtils.backgroundAlpha( WebViewActivity.this , 0.4f);
                sharePopupWindow.showAtLocation( WebViewActivity.this.titleRightImage, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

            }
        });
    }


}



