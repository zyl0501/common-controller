package com.tomtaw.widget.ratio_image;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;


public class AspectRatioImageView extends AppCompatImageView {
    /**
     * 图片的宽高比
     * width / height
     */
    float ratio = 1f;

    public AspectRatioImageView(Context context) {
        super(context);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AspectRatioImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.RatioImageView);
        ratio = t.getFloat(R.styleable.RatioImageView_ratio, 1f);
        t.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = (int) ((float) width / ratio);
        setMeasuredDimension(width, height);
    }

}