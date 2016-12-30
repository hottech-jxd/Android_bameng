package com.bameng.widgets;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.utils.DensityUtils;
import android.support.v4.content.ContextCompat;

import java.util.List;

/**
 * Created by Administrator on 2016/11/29.
 */
public class BMPopWindow implements View.OnClickListener{
    Context mContext;
    Dialog dialog;
    LinearLayout mainView;
    View.OnClickListener onClickListener;
    List<String> menus;

    public BMPopWindow(Context context, List<String> menus , View.OnClickListener onClickListener){
        this.mContext= context;
        this.onClickListener = onClickListener;
        this.menus = menus;
        initView();
    }

    private void initView() {
        if (dialog == null) {
            mainView = new LinearLayout(mContext);
            mainView.setBackgroundColor(Color.WHITE);
            mainView.setOrientation(LinearLayout.VERTICAL);
            dialog = new Dialog(mContext, R.style.PopDialog);
            dialog.setContentView(mainView);
            Window window = dialog.getWindow();
            window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置

            //设置视图充满屏幕宽度
            WindowManager.LayoutParams lp = window.getAttributes();
            int[] size = DensityUtils.getSize(mContext);
            lp.width = size[0]; //设置宽度
            // lp.height = (int) (size[1]*0.8);
            window.setAttributes(lp);
        }

        mainView.setFocusableInTouchMode(true);
        mainView.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK) {
                    dialog.dismiss();
                }
                return false;
            }
        });

        if(menus==null) return;
        mainView.removeAllViews();
        for(int i=0;i<menus.size();i++){
            String menu = menus.get(i);
            TextView tv = new TextView(mContext);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            tv.setLayoutParams(layoutParams);
            int pad= DensityUtils.dip2px(mContext,15);
            tv.setPadding(0,pad,0,pad);
            tv.setGravity(Gravity.CENTER);
            tv.setText( menu );
            tv.setOnClickListener(this);
            tv.setBackgroundResource(R.drawable.item_click_selector);
            mainView.addView(tv);

            if( i < menus.size()-1 ) {
                View spaceView = new View(mContext);
                layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,1);
                spaceView.setLayoutParams(layoutParams);
                spaceView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.line_gray));
                mainView.addView(spaceView);
            }
        }

        View spaceView = new View(mContext);
        int hpx  =DensityUtils.dip2px(mContext,1);
        LinearLayout.LayoutParams layoutParams= new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, hpx);
        spaceView.setLayoutParams(layoutParams);
        spaceView.setBackgroundColor(ContextCompat.getColor(mContext, R.color.line_gray));
        mainView.addView(spaceView);

        TextView tv = new TextView(mContext);
        layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setLayoutParams(layoutParams);
        int pad= DensityUtils.dip2px(mContext,15);
        tv.setPadding(0,pad,0,pad);
        tv.setId(tv.hashCode());
        tv.setGravity(Gravity.CENTER);
        tv.setText( "取消" );
        tv.setOnClickListener(this);
        tv.setBackgroundResource(R.drawable.item_click_selector);
        mainView.addView(tv);

    }

    @Override
    public void onClick(View view) {
        if(view instanceof  TextView){
            dialog.dismiss();
            if( ((TextView) view).getText().equals("取消") ) return;

            if(onClickListener!=null) {
                onClickListener.onClick(view);
            }
        }else {
            dialog.dismiss();
        }
    }

    public void show( ){
        dialog.show();
    }
}
