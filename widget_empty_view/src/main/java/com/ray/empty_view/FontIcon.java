package com.ray.empty_view;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Typeface;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by yxx on 2015/9/15.
 *
 * @author ohun@live.cn
 *         <p/>
 *         用iconfont图标代替常规的ImageView
 *         即在TextView的基础上，所有text都用 strings_icon_font.xml 指定的字符串资源
 */
public class FontIcon extends TextView {
  private static Typeface typeface;

  private Typeface loadTypeface(Context context) {
    try {
      return Typeface.createFromAsset(context.getApplicationContext().getAssets(), "font/iconfont.ttf");
    } catch (Throwable e) {
      e.printStackTrace();
    }
    return Typeface.DEFAULT;
  }

  public FontIcon(Context context) {
    super(context);
    this.setTypeface(getTypeFace(context));
    this.setIncludeFontPadding(true);
  }

  public FontIcon(Context context, AttributeSet attrs) {
    super(context, attrs);
    this.setTypeface(getTypeFace(context));
    this.setIncludeFontPadding(true);
  }

  public FontIcon(Context context, AttributeSet attrs, int defStyleAttr) {
    super(context, attrs, defStyleAttr);
    this.setTypeface(getTypeFace(context));
    this.setIncludeFontPadding(true);
  }

  @TargetApi(Build.VERSION_CODES.LOLLIPOP)
  public FontIcon(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
    super(context, attrs, defStyleAttr, defStyleRes);
    this.setTypeface(getTypeFace(context));
    this.setIncludeFontPadding(true);
  }

  private Typeface getTypeFace(Context context) {
    if (typeface == null) {
      typeface = loadTypeface(context);
    }
    return typeface;
  }
}
