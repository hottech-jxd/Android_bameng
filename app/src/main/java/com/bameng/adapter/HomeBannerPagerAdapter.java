package com.bameng.adapter;

import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;

import com.bameng.R;
import com.bameng.config.Constants;
import com.bameng.model.AdlistModel;
import com.bameng.utils.DensityUtils;
import com.bameng.widgets.custom.FrescoControllerListener;
import com.bameng.widgets.custom.FrescoDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.List;


/**
 * 首页无线滚动切换图片适配器
 */
public class HomeBannerPagerAdapter extends PagerAdapter {

    private List<AdlistModel> datas;

    private
    Handler mHandler;

    private Context mContext;

    public HomeBannerPagerAdapter(List<AdlistModel> datas, Context mContext, Handler mHandler) {

        this.datas = datas;
        this.mContext = mContext;
        this.mHandler = mHandler;
    }

    @Override
    public int getCount() {

        return datas.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {

        return view == object;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((View) object);
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {

        String String = datas.get(position).getItemImgUrl();
        View view = View.inflate(mContext, R.layout.banner_view, null);
        //ImageView image = (ImageView) view.findViewById(R.id.image);
        SimpleDraweeView image = (SimpleDraweeView)view.findViewById(R.id.image);
        FrescoDraweeController.loadImage(image, DensityUtils.getScreenW(mContext),String);

        container.addView(view);
//        image.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                Message message = mHandler.obtainMessage();
//                message.what = Constants.CAROUSE_URL;
//                message.obj = datas.get(position);
//                mHandler.sendMessage(message);
//            }
//        });
        return view;
    }
}
