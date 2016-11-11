package com.bameng.ui.base;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.bameng.BaseApplication;
import com.bameng.R;
import com.bameng.utils.ToastUtils;
import com.bameng.utils.Util;

import org.greenrobot.eventbus.EventBus;


public abstract class BaseActivity extends FragmentActivity implements Handler.Callback {

    public BaseApplication application;
    protected Handler mHandler = null;
    protected static final String NULL_NETWORK = "无网络或当前网络不可用!";
    protected boolean goback=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate ( savedInstanceState );
        mHandler=new Handler(this);
        application = ( BaseApplication ) this.getApplication ();
        //禁止横屏
        BaseActivity.this.setRequestedOrientation( ActivityInfo.SCREEN_ORIENTATION_PORTRAIT );
    }

    public void setImmerseLayout(View view){
        if (application.isKITKAT ()) {
            Window window = getWindow();
            window.setFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
//            int statusBarHeight = this.getStatusBarHeight ( this.getBaseContext ( ) );
//            view.setPadding(0, 0, 0, 0);
        }
    }

    public int getStatusBarHeight(Context context){
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy ( );
    }

    @Override
    protected void onPause() {
        super.onPause();
//        MobclickAgent.onPause(this);
//        JPushInterface.onPause(this);
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onResume() {
        super.onResume();
//        MobclickAgent.onResume(this);
//        JPushInterface.onResume(this);
    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onStop() {
        super.onStop();

        EventBus.getDefault().unregister(this);
    }
    public boolean canConnect(){
        //网络访问前先检测网络是否可用
        if(!Util.isConnect(BaseActivity.this)){
            ToastUtils.showLongToast( NULL_NETWORK);
            return false;
        }
        return true;
    }

    /**
     * 初始化控件
     */
    protected abstract void initView();
    /**
     * 接口调用
     */
    protected abstract void StartApi();
    /**
     * 通过类名启动Activity
     *
     * @param pClass
     */
    protected void openActivity(Class<?> pClass) {
        openActivity(pClass, null);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param pClass
     * @param pBundle
     */
    protected void openActivity(Class<?> pClass, Bundle pBundle) {
        Intent intent = new Intent(this, pClass);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity ( intent );
    }

    /**
     * 通过Action启动Activity
     *
     * @param pAction
     */
    protected void openActivity(String pAction) {
        openActivity(pAction, null);
    }

    /**
     * 通过Action启动Activity，并且含有Bundle数据
     *
     * @param pAction
     * @param pBundle
     */
    protected void openActivity(String pAction, Bundle pBundle) {
        Intent intent = new Intent(pAction);
        if (pBundle != null) {
            intent.putExtras(pBundle);
        }
        startActivity(intent);
    }

    public void closeSelf(Activity aty){
        aty.finish();
    }


    public abstract boolean handleMessage(Message msg);

    public void onGoBackButton(View v){
        if (goback){
        if(v.getId() == R.id.titleLeftImage){
            finish();
        }}

    }
}
