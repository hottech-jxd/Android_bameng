package com.bameng.widgets;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;

import com.bameng.R;
import com.theartofdev.edmodo.cropper.CropImageView;

public class ImageCropperView {
	 private static final int ROTATE_NINETY_DEGREES = 90;
//	public interface OnCropperBackListener{
//		void OnCropperBack(Bitmap bitmap);
//	}
	private CropperView.OnCropperBackListener listener;
	private WindowManager mWindowManager;
	private Context mContext;
	private CropImageView cropImageView;
	private View mView;
	private boolean changSize;

	public ImageCropperView(Context context, CropperView.OnCropperBackListener listener){
		this.mContext = context;
		this.listener = listener;
	}
	private void dismiss() {
		if (mView != null && mView.getParent() != null) {
			mWindowManager.removeView(mView);
		}
	}

//	public void cropper(Uri uri){
//		if(source == null) return;
//
//	}

	/**
	 *
	 * @param source
	 */
	public void cropper( Bitmap bitmap ){
		if(bitmap == null) return;
		changSize = false;
//		int widthMul = source.getWidth()/640;
//		int heightMul = source.getHeight()/640;
//		int mul = Math.min(widthMul, heightMul);
//		mul = mul == 0 ? 1 : mul;
//		this.mOutWidth = source.getWidth()	/ mul;
//		this.mOutHeight = source.getHeight() / mul;
		if (mWindowManager == null)
			mWindowManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
		if(mView == null){
			mView = LayoutInflater.from(mContext).inflate(R.layout.cropper, null);
			cropImageView = (CropImageView) mView.findViewById(R.id.CropImageView);
			cropImageView.setFixedAspectRatio(true);
			//
			 cropImageView.setAspectRatio(10, 10);

			 mView.findViewById(R.id.btnSure).setOnClickListener(new View.OnClickListener() {
				 			@Override
				 			public void onClick(View v) {
				 				dismiss();
				 				 if(listener != null){
				 					listener.OnCropperBack(cropImageView.getCroppedImage());
				 				 }

				 			}
				 		});
			 mView.findViewById(R.id.btnCancel).setOnClickListener(new View.OnClickListener() {

				 			@Override
				 			public void onClick(View v) {
				 				dismiss();
				 				 if(listener != null)
				 					 listener.OnCropperBack(null);

				 			}
				 		});
			 mView.findViewById(R.id.btnRotate).setOnClickListener(new View.OnClickListener() {

				 			@Override
				 			public void onClick(View v) {
				 				changSize = !changSize;
				 				//旋转
				 				cropImageView.rotateImage(ROTATE_NINETY_DEGREES);
				 			}
				 		});
			 mView.setFocusableInTouchMode(true);
			 mView.setOnKeyListener(new View.OnKeyListener() {
					@Override
					public boolean onKey(View v, int keyCode, KeyEvent event) {
						if(keyCode == KeyEvent.KEYCODE_BACK ){
							System.out.println(">>>>>>KEYCODE_BACK");
							dismiss();
							 if(listener != null)
								 listener.OnCropperBack(null);

						}
						return false;
					}
				});
		}

		cropImageView.setImageBitmap( bitmap );

		if (mView.getParent() == null) {
			WindowManager.LayoutParams lp = new WindowManager.LayoutParams(
					LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT,
					WindowManager.LayoutParams.TYPE_APPLICATION,
					WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN,
					PixelFormat.TRANSLUCENT);
			mWindowManager.addView(mView, lp);
		}
	}

}
