package com.bameng.widgets;

import android.app.Dialog;
import android.content.Context;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.utils.DensityUtils;

import butterknife.OnClick;

/**
 * Created by Administrator on 2016/11/29.
 */

public class BMShareView implements View.OnClickListener{
    Context mContext;
    Dialog dialog;
    View mainView;
    LinearLayout layShareCustomer;
    LinearLayout layShareMY;
    View.OnClickListener onClickListener;

    public BMShareView(Context context, View.OnClickListener onClickListener){
        this.mContext= context;
        this.onClickListener = onClickListener;
        initView();
    }
    private void initView() {
        if (dialog == null) {
            mainView = LayoutInflater.from(mContext).inflate(R.layout.pop_bmshare, null);
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

        layShareCustomer = (LinearLayout) mainView.findViewById(R.id.layShareCustomer);
        layShareMY = (LinearLayout) mainView.findViewById(R.id.layShareMY);
        layShareCustomer.setOnClickListener(this);
        layShareMY.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.layShareCustomer){
            dialog.dismiss();
            if(onClickListener!=null) {
                onClickListener.onClick(view);
            }
        }else if(view.getId()==R.id.layShareMY){
            dialog.dismiss();
            if(onClickListener!=null) {
                onClickListener.onClick(view);
            }
        }
    }

    public void show(){
        dialog.show();
    }
}
