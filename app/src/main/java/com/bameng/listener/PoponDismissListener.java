package com.bameng.listener;

import android.app.Activity;
import android.widget.PopupWindow;


import com.bameng.utils.WindowUtils;

import java.lang.ref.WeakReference;

/**
 * popwin 关闭后取消遮罩层监听器
 */
public class PoponDismissListener implements PopupWindow.OnDismissListener {
    private WeakReference<Activity> ref;// aty;
    public PoponDismissListener ( Activity aty ){
        //this.aty = aty;
        ref = new WeakReference<>(aty);
    }
    @Override
    public void onDismiss ( ) {
        if( ref.get() ==null )return;
        WindowUtils.backgroundAlpha ( ref.get() , 1.0f );
    }
}
