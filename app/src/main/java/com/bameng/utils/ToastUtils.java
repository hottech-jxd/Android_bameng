package com.bameng.utils;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import com.bameng.BaseApplication;

import static android.R.attr.duration;


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

    public static void showToast(Context context , String msg , int resId , int duration){
        Toast t =  Toast.makeText(BaseApplication.single, msg , duration);
        t.setGravity(Gravity.CENTER ,0,0);
        View v = new View(context );
        v.setBackgroundResource(resId);
        t.setView(v);
        t.setText(msg);
        t.show();
    }
}
