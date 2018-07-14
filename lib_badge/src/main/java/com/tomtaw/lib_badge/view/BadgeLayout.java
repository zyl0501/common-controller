package com.tomtaw.lib_badge.view;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tomtaw.lib_badge.R;


/**
 * Created by zyl on 2016/8/12.
 */
public class BadgeLayout extends FrameLayout {
  private final static int INVALID_ID = -1;
  /**
   * 红点横向超出部分
   */
  int overH;
  /**
   * 红点纵向超出部分
   */
  int overV;

  int numberRes;
  int dotRes;
  int contentResId = INVALID_ID;
  TextView numberView;
  View dotView;

  View contentView;

  boolean isNumber = true;
  int count = 0;
  boolean isShowBadge = false;

  public BadgeLayout(Context context) {
    this(context, null);
  }

  public BadgeLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public BadgeLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public BadgeLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  void init(Context context, AttributeSet attrs) {
    TypedArray t = context.obtainStyledAttributes(attrs, R.styleable.BadgeLayout);
    numberRes = t.getResourceId(R.styleable.BadgeLayout_numberLayout, R.layout.widget_badge_number);
    dotRes = t.getResourceId(R.styleable.BadgeLayout_dotLayout, R.layout.widget_badge_dot);
    contentResId = t.getResourceId(R.styleable.BadgeLayout_contentRefId, INVALID_ID);
    overH = t.getDimensionPixelOffset(R.styleable.BadgeLayout_overH, 0);
    overV = t.getDimensionPixelOffset(R.styleable.BadgeLayout_overV, 0);
    t.recycle();
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    if (contentResId == INVALID_ID) {
      contentView = getChildAt(0);
    } else {
      contentView = findViewById(contentResId);
    }

    LayoutInflater inflater = LayoutInflater.from(getContext());
    numberView = (TextView) inflater.inflate(numberRes, this, false);
    dotView = inflater.inflate(dotRes, this, false);
    numberView.setVisibility(View.GONE);
    dotView.setVisibility(View.GONE);
    addView(numberView);
    addView(dotView);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    super.onLayout(changed, l, t, r, b);
    int centerX = (r - l) / 2;
    int centerY = (b - t) / 2;
    int width = contentView.getMeasuredWidth();
    int height = contentView.getMeasuredHeight();
    int halfWidth = width / 2;
    int halfHeight = height / 2;
    contentView.layout(centerX - halfWidth, centerY - halfHeight, centerX + halfWidth, centerY + halfHeight);

    int contentTop = centerY - halfHeight;
    int contentRight = centerX + halfWidth;
    width = numberView.getMeasuredWidth();
    height = numberView.getMeasuredHeight();
    int maxTop = height / 2;
    int maxRight = r - l - width / 2;
    centerY = Math.max(contentTop, maxTop)+overV;
    centerX = Math.min(contentRight, maxRight)+overH;

    halfWidth = width / 2;
    halfHeight = height / 2;
    numberView.layout(centerX - halfWidth, centerY - halfHeight, centerX + halfWidth, centerY + halfHeight);

    width = dotView.getMeasuredWidth();
    height = dotView.getMeasuredHeight();
    halfWidth = width / 2;
    halfHeight = height / 2;
    maxTop = height / 2;
    maxRight = r - l - width / 2;
    centerY = Math.max(contentTop, maxTop)+overV;
    centerX = Math.min(contentRight, maxRight)+overH;
    dotView.layout(centerX - halfWidth, centerY - halfHeight, centerX + halfWidth, centerY + halfHeight);
  }

  public void setCount(int count) {
    this.count = count;
    numberView.setText(String.valueOf(count));
    updateUI();
  }

  public void setMode(boolean isNumber) {
    this.isNumber = isNumber;
    updateUI();
  }

  private void updateUI() {
    showBadge(isShowBadge);
  }

  public void showBadge(boolean show) {
    this.isShowBadge = show;
    if (show) {
      if (isNumber) {
        numberView.setVisibility(View.VISIBLE);
        bringChildToFront(numberView);
        dotView.setVisibility(View.GONE);
      } else {
        dotView.setVisibility(View.VISIBLE);
        numberView.setVisibility(View.GONE);
        bringChildToFront(dotView);
      }
    } else {
      dotView.setVisibility(View.GONE);
      numberView.setVisibility(View.GONE);
    }
  }

  public void clearBadge() {
    count = 0;
    isShowBadge = false;
    updateUI();
  }
}
