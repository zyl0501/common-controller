package com.ray.chart;

import android.view.View;

/**
 * 创建时间：2016/10/25
 *
 * @author zyl
 */
public class PieData {
  protected float value;
  protected View[] labelViews;
  protected int color;

  public PieData() {
  }

  public float getValue() {
    return value;
  }

  public void setValue(float value) {
    this.value = value;
  }

  public View[] getLabelViews() {
    return labelViews;
  }

  public void setLabelViews(View[] labelViews) {
    this.labelViews = labelViews;
  }

  public View getLeftView() {
    if (labelViews != null && labelViews.length >= 1) {
      return labelViews[0];
    }
    return null;
  }

  public View getRightView() {
    if (labelViews != null && labelViews.length >= 2) {
      return labelViews[1];
    }
    return null;
  }

  public void setLeftView(View view){
    if(labelViews==null){
      labelViews=new View[2];
    }
    labelViews[0]=view;
  }

  public void setRightView(View view){
    if(labelViews==null){
      labelViews=new View[2];
    }
    labelViews[1]=view;
  }

  public int getColor() {
    return color;
  }

  public void setColor(int color) {
    this.color = color;
  }
}
