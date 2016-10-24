package com.bameng.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bameng.R;
import com.bameng.model.NewsModel;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by 47483 on 2016.10.24.
 */

public class HomeNewsAdapter extends BaseAdapter {

    private List<NewsModel> newsModels;
    private Context mContext;
    private Activity aty;
    private
    Handler mHandler;

    @Override
    public int getCount() {
        return newsModels.size();
    }

    @Override
    public Object getItem(int position) {
        return (null==newsModels || newsModels.isEmpty())?null:newsModels.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        Resources resources = mContext.getResources();
        if (convertView == null)
        {
            convertView = View.inflate(mContext, R.layout.news_item, null);
            holder = new ViewHolder(convertView);
            convertView.setTag(holder);
            return convertView;
        }
        else
        {
            return null;
        }
    }
    class ViewHolder
    {
        public ViewHolder(View view)
        {
            ButterKnife.bind(this, view);
        }
    }
}
