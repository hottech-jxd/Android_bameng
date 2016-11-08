package com.bameng.utils;

import android.widget.Toast;

import com.bameng.BaseApplication;


public class ToastUtils
{
    private static Toast toast;



    public static void showShortToast(String msg){
        showToast(msg, Toast.LENGTH_SHORT);
    }

    private static void showToast(String msg, int duration){
        if(toast==null) {
            toast = Toast.makeText(BaseApplication.single, msg , duration);
        }
        toast.setDuration(duration);
        toast.setText(msg);
        toast.show();
    }

    public static void showLongToast(String msg){
        showToast(msg, Toast.LENGTH_LONG);
    }
}
