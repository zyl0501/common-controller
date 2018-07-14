package com.tomtaw.image_loader.inner;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

public interface RequestManager {
  ImageDisplayLoader with(Fragment fragment);
  ImageDisplayLoader with(android.app.Fragment fragment);
  ImageDisplayLoader with(Activity activity);
  ImageDisplayLoader with(Context context);
}
