package com.ray.ratingbarterminator;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.os.Build;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * 创建时间：2016/9/2
 *
 * @author zyl
 */
public class RatingBar extends View {
  private boolean isIndicator;
  private boolean halfStart;

  private int numStars;
  private float rating;

  private float starImageSize;
  private float starImageWidth;
  private float starImageHeight;
  private float starImagePadding;

  private int emptyRes;
  private int halfRes;
  private int fullRes;
  private Bitmap emptyBmp;
  private Bitmap halfBmp;
  private Bitmap fullBmp;

  private RectF drawRect;
  private Paint paint;

  public RatingBar(Context context) {
    this(context, null);
  }

  public RatingBar(Context context, AttributeSet attrs) {
    this(context, attrs, 0);
  }

  public RatingBar(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init(context, attrs);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public RatingBar(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  private void init(Context context, AttributeSet attrs) {
    Resources res = getResources();
    int defaultSize = res.getDimensionPixelSize(R.dimen.default_starImageSize);
    int defaultWidth = res.getDimensionPixelSize(R.dimen.default_starImageWidth);
    int defaultHeight = res.getDimensionPixelSize(R.dimen.default_starImageHeight);
    int defaultPadding = res.getDimensionPixelSize(R.dimen.default_starImagePadding);


    TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.RatingBar);
    starImageSize = mTypedArray.getDimension(R.styleable.RatingBar_starImageSize, defaultSize);
    starImageWidth = mTypedArray.getDimension(R.styleable.RatingBar_starImageWidth, defaultWidth);
    starImageHeight = mTypedArray.getDimension(R.styleable.RatingBar_starImageHeight, defaultHeight);
    starImagePadding = mTypedArray.getDimension(R.styleable.RatingBar_starImagePadding, defaultPadding);
    numStars = mTypedArray.getInteger(R.styleable.RatingBar_numStars, 5);
    emptyRes = mTypedArray.getResourceId(R.styleable.RatingBar_emptyRes, R.drawable.ic_widget_star_3);
    halfRes = mTypedArray.getResourceId(R.styleable.RatingBar_halfRes, R.drawable.ic_widget_star_2);
    fullRes = mTypedArray.getResourceId(R.styleable.RatingBar_fullRes, R.drawable.ic_widget_star_1);
    halfStart = mTypedArray.getBoolean(R.styleable.RatingBar_halfStart, true);
    isIndicator = mTypedArray.getBoolean(R.styleable.RatingBar_isIndicator, false);
    mTypedArray.recycle();

    drawRect = new RectF();
    paint = new Paint();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    int width = MeasureSpec.getSize(widthMeasureSpec);
    int height = MeasureSpec.getSize(heightMeasureSpec);
    int widthMode = MeasureSpec.getMode(widthMeasureSpec);
    int heightMode = MeasureSpec.getMode(heightMeasureSpec);

    if (widthMode != MeasureSpec.EXACTLY)
      width = (int) (getPaddingLeft() + getPaddingRight() +
          numStars * (starImageWidth + starImagePadding) - starImagePadding);
    if (heightMode != MeasureSpec.EXACTLY)
      height = (int) (starImageHeight + getPaddingTop() + getPaddingBottom());

    setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, 0),
        resolveSizeAndState(height, heightMeasureSpec, 0));
  }

  @Override
  protected void onDraw(Canvas canvas) {
    for (int i = 0; i < numStars; i++) {
      Bitmap bitmap;
      if (rating <= i + 0.5f) {
        if (emptyBmp == null)
          emptyBmp = BitmapFactory.decodeResource(getResources(), emptyRes);
        bitmap = emptyBmp;
      } else if (rating > i + 0.5f && rating < i + 1) {
        if (halfStart) {
          if (halfBmp == null)
            halfBmp = BitmapFactory.decodeResource(getResources(), halfRes);
          bitmap = halfBmp;
        } else {
          bitmap = emptyBmp;
        }
      } else {
        if (fullBmp == null)
          fullBmp = BitmapFactory.decodeResource(getResources(), fullRes);
        bitmap = fullBmp;
      }
      float left = (float) getPaddingLeft() + (float) i * (starImagePadding + starImageWidth);
      float top = (getHeight() - starImageHeight) / 2;
      float right = left + starImageWidth;
      float bottom = top + starImageHeight;
      drawRect.set(left, top, right, bottom);
      canvas.drawBitmap(bitmap, null, drawRect, paint);
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event) {
    if (isIndicator)
      return super.onTouchEvent(event);
    int action = event.getActionMasked();
    float x;
    float newRating;
    switch (action) {
      case MotionEvent.ACTION_DOWN:
        x = event.getX();
        newRating = calRating(x);
        if (needNotify(rating, newRating)) {
          rating = newRating;
          invalidate();
        }
        return true;
      case MotionEvent.ACTION_MOVE:
        x = event.getX();
        newRating = calRating(x);
        if (needNotify(rating, newRating)) {
          rating = newRating;
          invalidate();
        }
        return true;
      case MotionEvent.ACTION_UP:
      case MotionEvent.ACTION_CANCEL:
        return true;
    }
    return super.onTouchEvent(event);
  }

  private float calRating(float x) {
    float newRating = (x - (float) getPaddingLeft())
        / (starImageWidth + starImagePadding);
    //没有半星的情况
    //如：点击的是2.2f位置，手指在3星位置，所以+1显示
    if (!halfStart) {
      newRating = (int) newRating + 1;
    }
    return newRating;
  }

  private boolean needNotify(float oldRate, float newRate) {
    return (int) oldRate != (int) newRate
        || (int) (oldRate + 0.5f) != (int) (newRate + 0.5f)
        || (int) (oldRate - 0.5f) != (int) (newRate - 0.5f);
  }

  public void setRating(float newRating) {
    boolean needNotify = needNotify(rating, newRating);
    rating = newRating;
    if (needNotify)
      invalidate();
  }

  public float getRating() {
    return rating;
  }

  @Override
  public void onRestoreInstanceState(Parcelable state) {
    SavedState savedState = (SavedState) state;
    super.onRestoreInstanceState(savedState.getSuperState());
    rating = savedState.rating;
    invalidate();
  }

  @Override
  public Parcelable onSaveInstanceState() {
    Parcelable superState = super.onSaveInstanceState();
    SavedState savedState = new SavedState(superState);
    savedState.rating = rating;
    return savedState;
  }

  static class SavedState extends BaseSavedState {
    float rating;

    public SavedState(Parcelable superState) {
      super(superState);
    }

    private SavedState(Parcel in) {
      super(in);
      rating = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
      super.writeToParcel(dest, flags);
      dest.writeFloat(rating);
    }

    public static final Parcelable.Creator<SavedState> CREATOR = new Parcelable.Creator<SavedState>() {
      @Override
      public SavedState createFromParcel(Parcel in) {
        return new SavedState(in);
      }

      @Override
      public SavedState[] newArray(int size) {
        return new SavedState[size];
      }
    };
  }
}
