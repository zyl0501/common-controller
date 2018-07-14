package com.tomtaw.lib_badge;

import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by zyl on 2016/8/11.
 */
public class BadgeNumber {
  private int badgeId;//badge number真正的类型
  private String suffix;//badge id的后缀，用于列表
  private int count;//badge number的count
  private
  @BadgeMode
  int displayMode;//当前badge number在父节点上的显示方式
  /**
   * 附加的数据
   */
  private Object payLoad;

  public BadgeNumber() {
  }

  public BadgeNumber(int badgeId) {
    this.badgeId = badgeId;
  }

  public int getCount() {
    return count;
  }

  public void setCount(int count) {
    this.count = count;
  }

  public String getSuffix() {
    return suffix;
  }

  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }

  public
  @BadgeMode
  int getDisplayMode() {
    return displayMode;
  }

  public void setDisplayMode(int displayMode) {
    this.displayMode = displayMode;
  }

  public int getBadgeId() {
    return badgeId;
  }

  public void setBadgeId(int badgeId) {
    this.badgeId = badgeId;
  }

  public Object getPayLoad() {
    return payLoad;
  }

  public void setPayLoad(Object payLoad) {
    this.payLoad = payLoad;
  }

  @IntDef({
      BadgeMode.NUMBER,
      BadgeMode.DOT,
  })
  @Retention(RetentionPolicy.SOURCE)
  public @interface BadgeMode {
    /**
     * 数字
     */
    short NUMBER = 1;

    /**
     * 红点
     */
    short DOT = 2;

  }
}
