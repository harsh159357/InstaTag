package com.harsh.instatag;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

public class InstaTagImageView extends AppCompatImageView {
    public InstaTagImageView(Context context) {
        super(context);
    }

    public InstaTagImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public InstaTagImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(getMeasuredWidth(), getMeasuredWidth());
    }
}