package com.ray.common;

import android.content.Context;

public class Common {
  private Context context;

  static volatile Common defaultInstance;

  private Common() {
  }

  public static Common I() {
    if (defaultInstance == null) {
      synchronized (Common.class) {
        if (defaultInstance == null) {
          defaultInstance = new Common();
        }
      }
    }
    return defaultInstance;
  }

  public void init(Context context) {
    this.context = context;
  }

  public static Context ctx(){
    return I().context;
  }
}
