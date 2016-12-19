package com.bameng.widgets;

import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.bameng.R;
import com.bameng.utils.DensityUtils;

import me.relex.photodraweeview.PhotoDraweeView;

/**
 * Created by Administrator on 2016/12/16.
 */

public class PhoteZoomView {
    Context mContext;
    Dialog dialog;
    PhotoDraweeView photoDraweeView;
    ImageView ivClose;
    View mainView;

    public PhoteZoomView(Context context) {
        this.mContext = context;
        initView(context);
    }

    private void initView(final Context context) {
        if (dialog == null) {
            mainView = LayoutInflater.from(context).inflate(R.layout.pop_photozoomview, null);
            dialog = new Dialog(context , R.style.BMPopDialog );
            dialog.setContentView(mainView);
            Window window = dialog.getWindow();
            //window.setGravity(Gravity.);  //此处可以设置dialog显示的位置

            //设置视图充满屏幕宽度
            WindowManager.LayoutParams lp = window.getAttributes();
            int[] size = DensityUtils.getSize(mContext);
            lp.width = size[0]; //设置宽度
            lp.height = size[1];
            window.setAttributes(lp);

            photoDraweeView = (PhotoDraweeView) mainView.findViewById(R.id.photoDraweeView);


            ivClose = (ImageView) mainView.findViewById(R.id.ivClose);

        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });

    }

    public void show(String url) {
        photoDraweeView.setPhotoUri(Uri.parse(url));
        dialog.show();
    }
}
