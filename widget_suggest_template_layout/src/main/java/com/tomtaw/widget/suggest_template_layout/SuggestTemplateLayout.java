package com.tomtaw.widget.suggest_template_layout;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.v4.view.GestureDetectorCompat;
import android.support.v4.view.ViewCompat;
import android.support.v4.widget.ViewDragHelper;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * 书写建议模板的layout
 */
public class SuggestTemplateLayout extends ViewGroup {
  private GestureDetectorCompat gestureDetector;
  ViewDragHelper mDragHelper;
  /**
   * 当前拖动的比例
   * view2整个显示时，为1
   * 显示一半，为0
   */
  float mDragOffset;
  /**
   * 可拖动的最大边界
   */
  int mDragRange;
  /**
   * 是否可以收拾拖动
   */
  boolean isGestureDrag;

  /**
   * 上层view占整个layout的比例
   * 会相应影像拖动的比例，
   * 即上层view的right和layout的right重合，是最大的拖动range
   */
  float mTopViewRatio = 1f;

  /**
   * 拖动时，下层view会有透明度变化
   * 0-1
   * 0表示可以完全透明，
   * 1表示不透明，即不随拖动变化
   */
  float mAlphaRange = 0;

  View view1;
  View view2;

  CallBack callBack;

  public SuggestTemplateLayout(Context context) {
    this(context, null);
  }

  public SuggestTemplateLayout(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public SuggestTemplateLayout(Context context, AttributeSet attrs, int defStyle) {
    super(context, attrs, defStyle);
    init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public SuggestTemplateLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    init();
  }

  private void init() {
    gestureDetector = new GestureDetectorCompat(getContext(), new YScrollDetector());
    mDragHelper = ViewDragHelper.create(this, 1.0f, new DragHelperCallback());
    mDragHelper.setEdgeTrackingEnabled(ViewDragHelper.EDGE_LEFT);
    isGestureDrag = true;
  }

  class YScrollDetector extends GestureDetector.SimpleOnGestureListener {
    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float dx, float dy) {
      return Math.abs(dy) <= Math.abs(dx);
    }
  }

  @Override
  protected void onFinishInflate() {
    if (getChildCount() != 2) {
      throw new RuntimeException("child view must be 2");
    }
    view1 = getChildAt(0);
    view2 = getChildAt(1);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int maxWidth = MeasureSpec.getSize(widthMeasureSpec);
    int maxHeight = MeasureSpec.getSize(heightMeasureSpec);
    setMeasuredDimension(resolveSizeAndState(maxWidth, widthMeasureSpec, 0),
        resolveSizeAndState(maxHeight, heightMeasureSpec, 0));

    int width = MeasureSpec.getSize(widthMeasureSpec);
    int half = width / 2;
    measureChild(view1, MeasureSpec.makeMeasureSpec(half, MeasureSpec.EXACTLY), heightMeasureSpec);
    measureChild(view2, MeasureSpec.makeMeasureSpec((int) (maxWidth * mTopViewRatio), MeasureSpec.EXACTLY), heightMeasureSpec);
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    view1.layout(0, getPaddingTop(), view1.getMeasuredWidth(), view1.getMeasuredHeight());

    int width = getMeasuredWidth();
    int half = width / 2;
    int maxOff = (int) (width * mTopViewRatio - half);
    int offWidth = (int) (maxOff * mDragOffset);
    mDragRange = maxOff;

    int left = half - offWidth;
    int right = left + view2.getMeasuredWidth();
    view2.layout(left, getPaddingTop(), right, view2.getMeasuredHeight());
  }

  @Override
  public void computeScroll() {
    if (mDragHelper.continueSettling(true)) {
      ViewCompat.postInvalidateOnAnimation(this);
    }
  }

  @Override
  public boolean onInterceptTouchEvent(MotionEvent ev) {
    if (!isGestureDrag) {
      return super.onInterceptTouchEvent(ev);
    }
    return mDragHelper.shouldInterceptTouchEvent(ev) && gestureDetector.onTouchEvent(ev);
  }

  @Override
  public boolean onTouchEvent(MotionEvent ev) {
    if (!isGestureDrag) {
      return super.onTouchEvent(ev);
    }
    mDragHelper.processTouchEvent(ev);
    return false;
  }

  boolean smoothSlideTo(float slideOffset) {
    final int leftBound = getPaddingLeft();
    int x = (int) (leftBound + (1 - slideOffset) * mDragRange);
    if (mDragHelper.smoothSlideViewTo(view2, x, view2.getTop())) {
      ViewCompat.postInvalidateOnAnimation(this);
      return true;
    }
    return false;
  }

  public void open() {
    smoothSlideTo(0);
  }

  public void close() {
    close(true);
  }

  public void close(boolean anim) {
    if (anim) {
      smoothSlideTo(1);
    } else {
      setOffset(1);
      if (callBack != null) {
        callBack.onClose();
      }
    }
  }

  public void setOffset(float offset) {
    this.mDragOffset = offset;
    requestLayout();
  }

  public void setTopViewRatio(float ratio) {
    this.mTopViewRatio = ratio;
    requestLayout();
  }

  public void setAlphaRange(float alpha) {
    this.mAlphaRange = alpha;
  }

  public void setGestureDrag(boolean b) {
    isGestureDrag = b;
  }

  public void setCallBack(CallBack callBack) {
    this.callBack = callBack;
  }

  private class DragHelperCallback extends ViewDragHelper.Callback {
    @Override
    public boolean tryCaptureView(View child, int pointerId) {
      return child != null && child != view1;
    }

    @Override
    public void onViewReleased(View releasedChild, float xvel, float yvel) {
      if (xvel < -mDragHelper.getMinVelocity()) {
        close();
      } else if (xvel > mDragHelper.getMinVelocity()) {
        open();
      } else {
        if (mDragOffset > 0.5f) {
          close();
        } else {
          open();
        }
      }
    }

    @Override
    public void onViewPositionChanged(View changedView, int left, int top, int dx, int dy) {
      int width = getMeasuredWidth();
      float halfF = ((float) width) / 2;
      float leftF = (float) left;
      float offset = Math.abs((halfF - leftF) / halfF);
      mDragOffset = Math.max(0, Math.min(1, offset));
      view1.setAlpha(1 - mDragOffset * (1 - mAlphaRange));
      requestLayout();
      if (callBack != null) {
        if (mDragOffset == 0) {
          callBack.onOpen();
        } else if (mDragOffset == 1) {
          callBack.onClose();
        } else {
          callBack.onChange(mDragOffset);
        }
      }
    }

    @Override
    public int clampViewPositionHorizontal(View child, int left, int dx) {
      final int leftBound = getPaddingLeft();
      final int rightBound = getWidth() / 2;
      return Math.min(Math.max(left, leftBound), rightBound);
    }

    @Override
    public int getViewHorizontalDragRange(View child) {
      return mDragRange;
    }
  }

  public interface CallBack {
    void onOpen();

    void onClose();

    void onChange(float offset);
  }
}
