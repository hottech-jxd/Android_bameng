package com.bameng.widgets;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.AttributeSet;

import com.bameng.R;

/**
 * Created by Administrator on 2016/12/3.
 */

public class BmSwipeRefreshLayout extends SwipeRefreshLayout {

    public BmSwipeRefreshLayout(Context context) {
        super(context);
    }

    public BmSwipeRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);

        this.setColorSchemeColors(
                ContextCompat.getColor(getContext() , R.color.refreshcolor1),
                ContextCompat.getColor(getContext() , R.color.refreshcolor2),
                ContextCompat.getColor(getContext() , R.color.refreshcolor3),
                ContextCompat.getColor(getContext() , R.color.refreshcolor4),
                ContextCompat.getColor(getContext() , R.color.refreshcolor5)
                );
    }


}
