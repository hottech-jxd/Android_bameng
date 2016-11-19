package com.bameng.widgets;


import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bameng.R;
import com.bameng.utils.DensityUtils;

import java.util.HashMap;

public class UserInfoView {
	public enum Type{
		Name, Realname,Sex,ShopStatus
	}
	public HashMap<Type, String> titleNames = new HashMap<Type, String>();
	public interface OnUserInfoBackListener{
		void onUserInfoBack(Type type , String value );
	}
	private OnUserInfoBackListener listener;
	private View mainView;
	private TextView txtTitle;
	private Context mContext;
	private TextView btnSure;
	private Dialog dialog;
	private RelativeLayout layMain;
	private LinearLayout layedt;
	private LinearLayout laysex;
	private LinearLayout layShopStatus;
	private EditText edtName;
	TextView noInShop;
	TextView inShop;

	public UserInfoView(Context context){
		this.mContext = context;
		initView(context);
	}

	public void setOnUserInfoBackListener(OnUserInfoBackListener listener){
		this.listener = listener;
	}

	private void initView(final Context context) {
		if(dialog == null){
			mainView = LayoutInflater.from(context).inflate(R.layout.pop_userinfo, null);
			dialog = new Dialog(context, R.style.PopDialog);
			dialog.setContentView(mainView);
			 Window window = dialog.getWindow();
			 window.setGravity(Gravity.BOTTOM);  //此处可以设置dialog显示的位置

			 //设置视图充满屏幕宽度
			 WindowManager.LayoutParams lp = window.getAttributes();
			 int[] size  = DensityUtils.getSize(mContext);
			 lp.width = size[0]; //设置宽度
			// lp.height = (int) (size[1]*0.8);
			 window.setAttributes(lp);
		}
		titleNames.put(Type.Name, "昵称");
		titleNames.put(Type.Realname, "姓名");
		edtName = (EditText) mainView.findViewById(R.id.edtName);
		txtTitle = (TextView) mainView.findViewById(R.id.txtTitle);


		btnSure = (TextView) mainView.findViewById(R.id.btnSure);
		btnSure.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {




				dialog.dismiss();

				if(listener!=null){
					listener.onUserInfoBack( curType , edtName.getText().toString() );
				}

			}
		});
		// init support
		mainView.findViewById(R.id.btnCancel1).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				 if(listener != null)
					 listener.onUserInfoBack(null,null);
				 dialog.dismiss();
			}
		});
		mainView.findViewById(R.id.btn_Cancel2).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if(listener != null)
					listener.onUserInfoBack(null,null);
				dialog.dismiss();
			}
		});

		mainView.findViewById(R.id.sex_male).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
				if(listener!=null){
					listener.onUserInfoBack( curType , "M");
				}
			}
		});

		mainView.findViewById(R.id.sex_female).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
				if(listener!=null){
					listener.onUserInfoBack( curType , "F");
				}
			}
		});

		mainView.findViewById(R.id.btn_Cancel3).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if(listener!=null){
					listener.onUserInfoBack(null,null);
				}
				dialog.dismiss();
			}
		});

		mainView.findViewById(R.id.noInShop).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
				if(listener!=null){
					listener.onUserInfoBack( curType , "未进店");
				}
			}
		});
		mainView.findViewById(R.id.inShop).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				dialog.dismiss();
				if(listener!=null){
					listener.onUserInfoBack( curType , "已进店");
				}
			}
		});

		mainView.setFocusableInTouchMode(true);
		mainView.setOnKeyListener(new View.OnKeyListener() {
			@Override
			public boolean onKey(View v, int keyCode, KeyEvent event) {
				if(keyCode == KeyEvent.KEYCODE_BACK ){
					 if(listener != null)
						 listener.onUserInfoBack(null,null);
					 dialog.dismiss();
				}
				return false;
			}
		});
		layMain = (RelativeLayout) mainView.findViewById(R.id.layMain);
		layedt  = (LinearLayout) mainView.findViewById(R.id.layedt);
		laysex  = (LinearLayout) mainView.findViewById(R.id.laysex);
		layShopStatus=(LinearLayout)mainView.findViewById(R.id.layShopStatus);
		noInShop = (TextView) mainView.findViewById(R.id.noInShop);
		inShop = (TextView) mainView.findViewById(R.id.inShop);
	}
	private Handler handler = new Handler();
	private Type curType;
	public void show(final Type type, String selectIds){
		curType = type;
		txtTitle.setText(titleNames.get(type)+":");
		layedt.setVisibility(type == Type.Name||type == Type.Realname ? View.VISIBLE:View.GONE);
		laysex.setVisibility(type == Type.Sex ? View.VISIBLE:View.GONE);
		layShopStatus.setVisibility(type == Type.ShopStatus? View.VISIBLE:View.GONE);



		dialog.show();
			if (type == Type.Sex){
			return;
			}else if(type== Type.ShopStatus) {
				inShop.setTextColor( selectIds.equals("已进店") ? Color.parseColor("#ff8888") : Color.parseColor("#000000") );
				noInShop.setTextColor( selectIds.equals("未进店") ? Color.parseColor("#ff8888") : Color.parseColor("#000000") );
				return;
			}else {
				edtName.requestFocus();
				edtName.requestFocusFromTouch();

				final InputMethodManager imm = (InputMethodManager) mContext.getSystemService(Context.INPUT_METHOD_SERVICE);
				handler.postDelayed(new Runnable() {
					@Override
					public void run() {
						imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
					}
				}, 10);
				edtName.setText(selectIds);
			}


		//set height
		LinearLayout.LayoutParams params =  (LayoutParams) layMain.getLayoutParams();
    	//reset
    	params.height = LinearLayout.LayoutParams.WRAP_CONTENT;//ownHeight > height ? height :ownHeight;
    	layMain.setLayoutParams(params);

		ViewTreeObserver vto2 = layMain.getViewTreeObserver();
		vto2.addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
		    @Override
		    public void onGlobalLayout() {
		    	layMain.getViewTreeObserver().removeGlobalOnLayoutListener(this);
		    	LinearLayout.LayoutParams params =  (LayoutParams) layMain.getLayoutParams();
				int ownHeight = layMain.getHeight();
				int height = (int) (DensityUtils.getSize(mContext)[1] * 0.75);
				params.height = ownHeight > height ? height :ownHeight;
				layMain.setLayoutParams(params);
		    }
		});


	}
}
