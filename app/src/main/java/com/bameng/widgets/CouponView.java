package com.bameng.widgets;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.bameng.utils.CouponViewHelper;

/**
 * Created by Administrator on 2016/11/24.
 */

public class CouponView extends LinearLayout {
    private CouponViewHelper helper;

    public CouponView(Context context) {
        this(context, null);
    }

    public CouponView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CouponView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        helper = new CouponViewHelper(this, context, attrs, defStyle);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        helper.onSizeChanged(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        helper.onDraw(canvas);
    }

    public int getSemicircleColor() {
        return helper.getSemicircleColor();
    }

    public void setSemicircleColor(int semicircleColor) {
        helper.setSemicircleColor(semicircleColor);
    }

    public float getSemicircleGap() {
        return helper.getSemicircleGap();
    }

    public void setSemicircleGap(float semicircleGap) {
        helper.setSemicircleGap(semicircleGap);
    }

    public float getSemicircleRadius() {
        return helper.getSemicircleRadius();
    }

    public void setSemicircleRadius(float semicircleRadius) {
        helper.setSemicircleRadius(semicircleRadius);
    }

    public boolean isSemicircleTop() {
        return helper.isSemicircleTop();
    }

    public void setSemicircleTop(boolean semicircleTop) {
        helper.setSemicircleTop(semicircleTop);
    }

    public boolean isSemicircleBottom() {
        return helper.isSemicircleBottom();
    }
    public void setSemicircleBottom(boolean semicircleBottom) {
        helper.setSemicircleBottom(semicircleBottom);
    }

    public boolean isSemicircleLeft() {
        return helper.isSemicircleLeft();
    }

    public void setSemicircleLeft(boolean semicircleLeft) {
        helper.setSemicircleLeft(semicircleLeft);
    }

    public boolean isSemicircleRight() {
        return helper.isSemicircleRight();
    }

    public void setSemicircleRight(boolean semicircleRight) {
        helper.setSemicircleRight(semicircleRight);
    }

}
