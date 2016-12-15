package com.bameng.ui.base;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Message;
import android.view.Gravity;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.bameng.R;
import com.bameng.config.Constants;
import com.bameng.model.ShareModel;
import com.bameng.ui.HomeActivity;
import com.bameng.utils.ToastUtils;
import com.bameng.utils.WindowUtils;
import com.bameng.widgets.SharePopupWindow;

import java.util.HashMap;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.wechat.favorite.WechatFavorite;

public class BaseShareActivity extends BaseActivity implements OnGetShareUrlResultListener, PlatformActionListener {

    protected   ShareUrlSearch shareUrlSearch;
    protected SharePopupWindow sharePopupWindow;
    protected ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void initView() {
        sharePopupWindow = new SharePopupWindow(this);
        sharePopupWindow.showShareWindow();
        sharePopupWindow.setPlatformActionListener(this);
        sharePopupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowUtils.backgroundAlpha ( BaseShareActivity.this , 1.0f );
            }
        });
    }

    @Override
    protected void StartApi() {

    }

    @Override
    public boolean handleMessage(Message msg) {

        switch (msg.what){
            case Constants.SHARE_SUCCESS: {
                //分享成功
                Platform platform = (Platform) msg.obj;
                if ("WechatMoments".equals(platform.getName())) {
                    ToastUtils.showShortToast( "微信朋友圈分享成功");
                    return true;
                } else if ("Wechat".equals(platform.getName())) {
                    ToastUtils.showShortToast( "微信分享成功");
                    return true;
                }  else if (WechatFavorite.NAME.equals(platform.getName())) {
                    ToastUtils.showShortToast("微信收藏成功");
                    return true;
                }
            }
            break;
            case Constants.SHARE_ERROR: {
                //分享失败
                Platform platform = (Platform) msg.obj;
                if ("WechatMoments".equals(platform.getName())) {
                    ToastUtils.showShortToast( "微信朋友圈分享失败");
                    return true;
                } else if ("Wechat".equals(platform.getName())) {
                    ToastUtils.showShortToast( "微信分享失败");
                    return true;
                } else if (WechatFavorite.NAME.equals(platform.getName())) {
                    ToastUtils.showShortToast("微信收藏失败");
                    return true;
                }
            }
            break;
            case Constants.SHARE_CANCEL: {
                //分享取消
                Platform platform = (Platform) msg.obj;
                if ("WechatMoments".equals(platform.getName())) {
                    ToastUtils.showShortToast( "微信朋友圈分享取消");
                    return true;
                } else if ("Wechat".equals(platform.getName())) {
                    ToastUtils.showShortToast( "微信分享取消");
                    return true;
                } else if (WechatFavorite.NAME.equals(platform.getName())) {
                    ToastUtils.showShortToast("微信收藏取消");
                    return true;
                }
            }
            break;
        }
        return false;
    }

    @Override
    public void onGetPoiDetailShareUrlResult(ShareUrlResult shareUrlResult) {
        String url = shareUrlResult.getUrl();
        //ToastUtils.showLongToast( url);
    }

    @Override
    public void onGetLocationShareUrlResult(final ShareUrlResult shareUrlResult) {

        mHandler.post(new Runnable() {
            @Override
            public void run() {
                if(progressDialog!=null){
                    progressDialog.dismiss();
                }
                String url = shareUrlResult.getUrl();
                //ToastUtils.showLongToast( url);
                ShareModel msgModel = new ShareModel ();
                msgModel.setImageData(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_location));
                msgModel.setText("我的位置");
                msgModel.setTitle("我的位置");
                msgModel.setUrl( url );
                sharePopupWindow.initShareParams(msgModel);
                WindowUtils.backgroundAlpha( BaseShareActivity.this , 0.4f);
                sharePopupWindow.showAtLocation( BaseShareActivity.this.getWindow().getDecorView() , Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);

            }
        });




    }

    @Override
    public void onGetRouteShareUrlResult(ShareUrlResult shareUrlResult) {
        String url = shareUrlResult.getUrl();
        //ToastUtils.showLongToast( url);
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


}
