package com.ray.chart;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * 创建时间：2016/10/25
 *
 * @author zyl
 */
public class PieChart extends ViewGroup {


  PieDataSet pieDataSet;
  RectF arcRect;

  List<Float> percentList;
  Paint paint;
  Paint linePaint;

  float innerCircleRadius;
  boolean showInnerCircle = true;
  float circlePart = 0.5f;

  /**
   * 当只有一种数据时，强行使label位置改变
   * 否则是在360/2的地方，影响视觉
   */
  int singleFillAngle = 90;

  private Path mPathBuffer = new Path();

  public PieChart(Context context) {
    this(context, null);
  }

  public PieChart(Context context, AttributeSet attrs) {
    this(context, attrs, -1);
  }

  public PieChart(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    init();
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public PieChart(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    int cellWidthSpec = MeasureSpec.makeMeasureSpec(getMeasuredWidth(), MeasureSpec.AT_MOST);
    int cellHeightSpec = MeasureSpec.makeMeasureSpec(getMeasuredHeight(), MeasureSpec.AT_MOST);
    int childCount = getChildCount();
    for (int i = 0; i < childCount; i++) {
      View child = getChildAt(i);
      child.measure(cellWidthSpec, cellHeightSpec);
    }
  }

  @Override
  protected void onLayout(boolean changed, int l, int t, int r, int b) {
    Log.e("raytest", "onLayout");
    if (isEmpty()) {
      return;
    }
    float total = 0;
    for (PieData pieData : pieDataSet.pieDatas) {
      total += pieData.value;
    }

    float sweepAngle = 0;
    int index = 0;
    int width = getMeasuredWidth();
    int height = getMeasuredHeight();
    int centerX = width / 2;
    int centerY = height / 2;
    float radius = width * circlePart / 2;
    float pieWidth = showInnerCircle ? radius - innerCircleRadius : radius;
    float pieCenterRadius = radius - pieWidth / 3;
    float curEndAngle = 0;

    boolean isSingleFill = pieDataSet.pieDatas.size() == 1;
    for (PieData pieData : pieDataSet.pieDatas) {

      float itemSweep = pieData.value / total * 360;
      if (isSingleFill) {
        itemSweep = singleFillAngle;
      }

      float calAngle = sweepAngle + itemSweep / 2;
      sweepAngle += itemSweep;
      index++;
      int endX, endY;

      if (calAngle == 180) {
        calAngle -= itemSweep / 4;
      }
      View child;
      if (calAngle <= 180) {
        child = pieData.getRightView();
        pieData.getLeftView().setVisibility(GONE);
      } else {
        child = pieData.getLeftView();
        pieData.getRightView().setVisibility(GONE);
      }
      if (child == null) continue;
      child.setVisibility(VISIBLE);
      int viewWidth = child.getMeasuredWidth();
      int viewHeight = child.getMeasuredHeight();

      if (calAngle < 90) {
//        calAngle = 90 - sweepAngle / 2;
        endY = (int) (Math.abs(Math.cos(Math.toRadians(calAngle))) * (pieCenterRadius + 80));
        endX = (int) (Math.abs(Math.sin(Math.toRadians(calAngle))) * (pieCenterRadius + 80));
        endX += centerX;
        endY = centerY - endY;
      } else if (calAngle == 90) {
        endX = (int) (centerX + pieCenterRadius + 80);
        endY = centerY;
      } else if (calAngle > 90 && calAngle < 180) {
//        calAngle = (sweepAngle - 90) / 2;
        endY = (int) (Math.abs(Math.cos(Math.toRadians(calAngle))) * (pieCenterRadius + 80));
        endX = (int) (Math.abs(Math.sin(Math.toRadians(calAngle))) * (pieCenterRadius + 80));
        endX += centerX;
        endY = centerY + endY;
      } else if (calAngle == 180) {
        endX = centerX;
        endY = (int) (centerY + pieCenterRadius + 80);
      } else if (calAngle > 180 && calAngle < 270) {
//        calAngle = 90 - (sweepAngle - 180) / 2;
        endY = (int) (Math.abs(Math.cos(Math.toRadians(calAngle))) * (pieCenterRadius + 80));
        endX = (int) (Math.abs(Math.sin(Math.toRadians(calAngle))) * (pieCenterRadius + 80));
        endX = centerX - endX - viewWidth;
        endY = centerY + endY;
      } else if (calAngle == 270) {
        endX = (int) (centerX - pieCenterRadius - 80 - viewWidth);
        endY = centerY;
      } else if (calAngle > 270 && calAngle < 360) {
//        calAngle = 90 - (sweepAngle - 270) / 2;
        endY = (int) (Math.abs(Math.cos(Math.toRadians(calAngle))) * (pieCenterRadius + 80));
        endX = (int) (Math.abs(Math.sin(Math.toRadians(calAngle))) * (pieCenterRadius + 80));
        endX = centerX - endX - viewWidth;
        endY = centerY - endY;
      } else {//360度
        endX = centerX;
        endY = (int) (centerY - pieCenterRadius - 80 - viewHeight);
      }
      child.layout(endX, endY - viewHeight / 2, endX + viewWidth, endY + viewHeight / 2);
      Log.e("raytest", endX + ", " + endY + ", " + (endX + viewWidth) + ", " + (endY + viewHeight));
      curEndAngle += sweepAngle;
    }
  }

  private void init() {
    setWillNotDraw(false);
    pieDataSet = new PieDataSet();
    arcRect = new RectF();
    percentList = new ArrayList<>();
    paint = new Paint();
    paint.setAntiAlias(true);
    paint.setStyle(Paint.Style.FILL);
    paint.setStrokeWidth(0);
    paint.setStrokeCap(Paint.Cap.SQUARE);

    linePaint = new Paint();
    linePaint.setStyle(Paint.Style.STROKE);
    linePaint.setStrokeWidth(1.5f);
    linePaint.setColor(Color.parseColor("#66000000"));
  }

  @Override
  protected void onDraw(Canvas canvas) {
    Log.e("raytest", "onDraw");
    drawPieArc(canvas);
    drawInnerCircle(canvas);
    drawItemLabelLine(canvas);
  }

  private void drawPieArc(Canvas canvas) {
    float width = canvas.getWidth() * circlePart;
    float height = width;
    arcRect.set(0, 0, width, height);
    if (isEmpty()) {
      canvas.save();
      canvas.translate((canvas.getWidth() - width) / 2, (canvas.getHeight() - height) / 2);
      paint.setColor(Color.GRAY);
      canvas.drawOval(arcRect, paint);
      canvas.restore();
      return;
    }

    float total = 0;
    for (PieData pieData : pieDataSet.pieDatas) {
      total += pieData.value;
    }

    float startAngle = -90;
    float sweepAngle;
    int index = 0;
    canvas.save();
    canvas.translate((canvas.getWidth() - width) / 2, (canvas.getHeight() - height) / 2);
    for (PieData pieData : pieDataSet.pieDatas) {
      paint.setColor(pieData.getColor());
      sweepAngle = pieData.value / total * 360;
      canvas.drawArc(arcRect, startAngle, sweepAngle, true, paint);
      startAngle += sweepAngle;
      index++;
    }
    canvas.restore();
  }

  private void drawInnerCircle(Canvas canvas) {
    if (!showInnerCircle) return;
    float width = canvas.getWidth() * circlePart;
    float height = canvas.getHeight() * circlePart;
    float centerX = width / 2;
    float centerY = height / 2;
    float radius = innerCircleRadius;
    arcRect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
    paint.setColor(Color.WHITE);
    canvas.save();
    canvas.translate((canvas.getWidth() - width) / 2, (canvas.getHeight() - height) / 2);
    canvas.drawOval(arcRect, paint);
    canvas.restore();
  }

  private void drawItemLabelLine(Canvas canvas) {
    if (isEmpty()) {
      return;
    }

    float total = 0;
    for (PieData pieData : pieDataSet.pieDatas) {
      total += pieData.value;
    }
    float width = canvas.getWidth() * circlePart;
    float height = width;
    float centerX = width / 2;
    float centerY = height / 2;
    float radius = width / 2;
    float pieWidth = showInnerCircle ? radius - innerCircleRadius : radius;
    float pieCenterRadius = radius - pieWidth / 3;
    float sweepAngle;

    boolean isSingleFill = pieDataSet.pieDatas.size() == 1;
    canvas.save();
    canvas.translate((canvas.getWidth() - width) / 2, (canvas.getHeight() - height) / 2);
    float curAngle = 0;
    for (PieData pieData : pieDataSet.pieDatas) {
      sweepAngle = pieData.value / total * 360;
      if (isSingleFill) {
        sweepAngle = singleFillAngle;
      }
      float halfSweepAngle = sweepAngle / 2;
      float sweep1 = halfSweepAngle;
      float sweep2 = halfSweepAngle;
      if (curAngle + halfSweepAngle == 180) {
        //180度的时候做特殊调整
        sweep1 = halfSweepAngle / 2;
        sweep2 = halfSweepAngle * 3 / 2;
      }
      canvas.rotate(sweep1, centerX, centerY);
      canvas.drawLine(centerX, centerY - pieCenterRadius, centerX, centerY - pieCenterRadius - 80, linePaint);
      canvas.rotate(sweep2, centerX, centerY);

      curAngle += sweepAngle;
    }
    canvas.restore();

  }

  private boolean isEmpty() {
    return pieDataSet == null || pieDataSet.pieDatas == null || pieDataSet.pieDatas.size() <= 0;
  }

  public void setData(PieDataSet pieDataSet) {
    removeAllViews();
    this.pieDataSet = pieDataSet;
    List<PieData> pieDatas = pieDataSet.pieDatas;
    for (PieData pieData : pieDatas) {
      View view = pieData.getLeftView();
      if (view != null) addView(view);
      view = pieData.getRightView();
      if (view != null) addView(view);
    }
    invalidate();
  }

  public void setInnerCircleRadius(float innerCircleRadius) {
    this.innerCircleRadius = innerCircleRadius;
  }
}
