package com.bameng.widgets;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.utils.DensityUtils;
import com.huotu.android.library.libedittext.EditText;

/**
 * Created by Administrator on 2017/8/7.
 */

public class InputDialogView {
    public interface OnInputBackListener{
        void onInputBack( String value );
    }
    private OnInputBackListener listener;
    private View mainView;
    private Context mContext;
    private Dialog dialog;
    private EditText editText;

    public InputDialogView(Context context){
        this.mContext = context;
        initView(context);
    }

    public void setOnUserInfoBackListener(OnInputBackListener listener){
        this.listener = listener;
    }

    public void show(){
        dialog.show();
        editText.setText("");



    }

    private void initView(final Context context) {

        if(dialog == null){
            mainView = LayoutInflater.from(context).inflate(R.layout.layout_inputdialog , null);
            dialog = new Dialog(context, R.style.PopDialog);
            dialog.setContentView(mainView);
            Window window = dialog.getWindow();
            window.setGravity(Gravity.CENTER);  //此处可以设置dialog显示的位置

            //设置视图充满屏幕宽度
            WindowManager.LayoutParams lp = window.getAttributes();
            int[] size  = DensityUtils.getSize(mContext);
            lp.width = size[0]*80/100; //设置宽度
            // lp.height = (int) (size[1]*0.8);
            window.setAttributes(lp);

            editText =(EditText) mainView.findViewById(R.id.pop_input);
        }

        TextView tvok =(TextView) mainView.findViewById(R.id.btnOk);
        tvok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                if(TextUtils.isEmpty( editText.getText().toString() )){
                    editText.setError("请输入内容");
                    return;
                }
                if(listener!=null){
                    listener.onInputBack(  editText.getText().toString() );
                }
            }
        });
        TextView tvCancel =(TextView)mainView.findViewById(R.id.btnCancel);
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){
                    dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
                }
            }
        });
    }
}
