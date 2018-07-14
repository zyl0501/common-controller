package com.flyco.tablayout;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;


/**
 * Created by zyl on 2016/7/15.
 */
public class TabView extends android.support.v7.widget.AppCompatTextView {
  private static final int INVALID = -1;

  int lineHeight;
  int lineColor;
  int textWidthCache = INVALID;
  Paint linePaint;

  public TabView(Context context) {
    super(context);
    init(null);
  }

  public TabView(Context context, AttributeSet attrs) {
    super(context, attrs);
    init(attrs);
  }

  public TabView(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(attrs);
  }

  private void init(AttributeSet attrs) {
    if (attrs != null) {
      Context context = getContext();
      TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.TabView_Line);
      lineColor = t.getColor(R.styleable.TabView_Line_line_color, getResources().getColor(R.color.tl_default_line_color));
      t.recycle();
    } else {
      lineColor = getResources().getColor(R.color.tl_default_line_color);
    }
    if (linePaint == null) {
      linePaint = new Paint();
      linePaint.setColor(lineColor);
      lineHeight = dp2px(getContext(), 3);
    }
  }

  private static int dp2px(Context context, float dpValue) {
    final float scale = context.getResources().getDisplayMetrics().density;
    return (int) (dpValue * scale + 0.5f);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
    super.onLayout(changed, left, top, right, bottom);
  }

  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);
    canvas.save();
    //single line will cause canvas translate ScrollX
    canvas.translate(getScrollX(), 0);
    float width = canvas.getWidth();
    float textWidth = textWidthCache == INVALID ? getTextWidth(getLayout().getPaint(), getText()) : textWidthCache;
    float left = (width - textWidth) / 2;
    float top = canvas.getHeight() - lineHeight;
    float right = left + textWidth;
    float bottom = canvas.getHeight();
    linePaint.setColor(isSelected() || isPressed() ? lineColor : Color.TRANSPARENT);
    canvas.drawRect(left, top, right, bottom, linePaint);
    canvas.restore();
  }

  private int getTextWidth(Paint paint, CharSequence str) {
    int iRet = 0;
    if (str != null && str.length() > 0) {
      int len = str.length();
      float[] widths = new float[len];
      paint.getTextWidths(str.toString(), widths);
      for (int j = 0; j < len; j++) {
        iRet += (int) Math.ceil(widths[j]);
      }
    }
    return iRet;
  }

  int count = 0;

  @Override
  public void setSelected(boolean selected) {
    super.setSelected(selected);
  }
}
