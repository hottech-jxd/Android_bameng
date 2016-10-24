package com.bameng.utils;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.ShapeDrawable;
import android.graphics.drawable.StateListDrawable;
import android.graphics.drawable.shapes.RoundRectShape;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;


import com.bameng.BaseApplication;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class SystemTools {

    public static byte[] readInputStream(InputStream inStream) {

        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        try {
            while ((len = inStream.read(buffer)) != -1) {
                outStream.write(buffer, 0, len);
            }
            return outStream.toByteArray();
        } catch (IOException e) {
            Log.e( "error" , e.getMessage());
        } finally {
            try {
                inStream.close();
            } catch (IOException e) {
                Log.e("error",e.getMessage());
            }
        }
        return null;
    }

    /**
     * 指定格式返回当前系统时间
     */
    public static String getDataTime(String format) {
        SimpleDateFormat df = new SimpleDateFormat(format);
        return df.format(new Date());
    }

    /**
     * @param time yyy-MM-dd HH:mm
     * @return
     */
    public static long getLongTime(String time) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyy-MM-dd HH:mm", Locale.CHINA);
        Date date;
        try {
            date = sdf.parse(time);
            Calendar c = Calendar.getInstance();
            TimeZone tz = TimeZone.getTimeZone("GMT");
            c.setTimeZone(tz);
            c.setTime(date);

            return c.getTimeInMillis();
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取手机系统SDK版本
     *
     * @return 如API 17 则返回 17
     */
    public static int getSDKVersion() {
        return android.os.Build.VERSION.SDK_INT;
    }

    /**
     * 获取系统版本
     *
     * @return 形如2.3.3
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

    @SuppressLint("NewApi")
    public static void loadBackground(TextView view, Drawable drawable, String text) {
        if (SystemTools.getSDKVersion() >= 16) {
            view.setText(text);
            view.setBackground(drawable);
        } else {
            view.setText(text);
            view.setBackgroundDrawable(drawable);
        }
    }

    @SuppressLint("NewApi")
    public static void loadBackground(View view, Drawable drawable) {
        if (SystemTools.getSDKVersion() >= 16) {
            view.setBackground(drawable);
        } else {
            view.setBackgroundDrawable(drawable);
        }
    }


    //获取系统颜色
    public static int obtainColor(String colorStr) {
        String[] colors = colorStr.split(",");
        return Color.argb(Integer.parseInt(colors[0]), Integer.parseInt(colors[1]), Integer.parseInt(colors[2]), Integer.parseInt(colors[3]));
    }

    public static int obtainAlphaColor(String colorStr) {
        String[] colors = colorStr.split(",");
        return Color.argb(12, Integer.parseInt(colors[1]), Integer.parseInt(colors[2]), Integer.parseInt(colors[3]));
    }

    /**
     * 解析 color 字符串
     * @param color
     * @return
     */
    public static int parseColor(String color){
        if( TextUtils.isEmpty( color)) return Color.WHITE;
        try {
            return Color.parseColor(color);
        }catch (Exception ex){
            Log.e( "error", ex.getMessage());
            return Color.WHITE;
        }
    }

    /**
     * 图片渐变
     *
     * @param view
     */
    public static void setFlickerAnimation(ImageView view) {
        final Animation animation = new AlphaAnimation(0, 1); // Change alpha from fully visible to invisible
        animation.setDuration(500); // duration - half a second
        animation.setRepeatCount(3); // Repeat animation infinitely
        animation.setFillAfter(true);
        view.setAnimation(animation);
        animation.startNow();
    }

    public static void setRotateAnimation(ImageView view) {
        final Animation animation = new RotateAnimation(0f, 360f, Animation.RELATIVE_TO_SELF,
                0.5f, Animation.RELATIVE_TO_SELF, 0.5f); // Change alpha from fully visible to invisible
        animation.setDuration(1000); // duration - half a second
        animation.setRepeatCount(Animation.INFINITE); // Repeat animation infinitely
        animation.setFillAfter(true);
        view.setAnimation(animation);
        animation.startNow();
    }



    public static Bitmap readBitmapFromSD(String iconName) {
        String dir = Environment.getExternalStorageDirectory() + File.separator + "buyer" + File.separator + "icon" + File.separator;
        String iconPath = dir + iconName + ".png";
        File iconFile = new File(iconPath);
        //若该文件存在
        if (iconFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(iconPath);
            return bitmap;
        }
        return null;
    }

    public static void setWindowsStyle(View view  , float radius  , int backgroundColor ){
        //popTitle.setBackgroundColor(SystemTools.obtainColor( ((BaseApplication) context.getApplication()).obtainMainColor() ));
        //popContext.setBackgroundColor( Color.WHITE );
        //tipsMsg.setTextColor( Color.BLACK );

        StateListDrawable stateListDrawable =new StateListDrawable();
        float[] outR = { radius,radius,radius,radius,radius,radius,radius,radius };
        RectF inRect = new RectF(1f,1f,1f,1f);
        float[] inR = {8f,8f,8f,8f,8f,8f,8f,8f};
//        RoundRectShape roundRectShape1 = new RoundRectShape(outR , inRect , inR);
        RoundRectShape roundRectShape1 = new RoundRectShape(outR , null , null);
        ShapeDrawable shapeDrawable1 = new ShapeDrawable(roundRectShape1);
        shapeDrawable1.setPadding(8,8,8,8);
        shapeDrawable1.getPaint().setColor( backgroundColor );
        shapeDrawable1.getPaint().setAntiAlias(true);
        shapeDrawable1.getPaint().setStyle(Paint.Style.FILL );

//        RoundRectShape roundRectShape2 = new RoundRectShape(outR, inRect, inR);
        RoundRectShape roundRectShape2 = new RoundRectShape(outR, null, null);
        ShapeDrawable shapeDrawable2 = new ShapeDrawable(roundRectShape2);
        shapeDrawable2.setPadding(8,8,8,8);
        shapeDrawable2.getPaint().setColor( backgroundColor );
        shapeDrawable2.getPaint().setAntiAlias(true);
        shapeDrawable2.getPaint().setStyle(Paint.Style.FILL);
        shapeDrawable2.setAlpha(50);

        int[] normalState = new int[] {};
        int[] pressState = new int[]{android.R.attr.state_pressed};
        int[] selectedState = new int[]{android.R.attr.state_selected};
        stateListDrawable.addState( normalState , shapeDrawable1 );
        stateListDrawable.addState( pressState , shapeDrawable2 );
        stateListDrawable.addState(selectedState, shapeDrawable1);

        //view.setTextColor(Color.BLACK);
        //.setTextColor(Color.BLACK);
        SystemTools.loadBackground(view, stateListDrawable);
        //SystemTools.loadBackground( btnCancel , stateListDrawable);
    }

}
