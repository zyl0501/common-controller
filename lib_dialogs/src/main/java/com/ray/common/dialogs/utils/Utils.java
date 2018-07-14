package com.ray.common.dialogs.utils;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * 创建时间：2016/12/20
 *
 * @author zyl
 */
public class Utils {
  public static DisplayMetrics getScreenHeightAndWidth(Context context) {
    WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    DisplayMetrics metrics = new DisplayMetrics();
    manager.getDefaultDisplay().getMetrics(metrics);
    return metrics;
  }
}
