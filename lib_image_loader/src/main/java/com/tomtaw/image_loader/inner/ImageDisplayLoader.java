package com.tomtaw.image_loader.inner;

import android.graphics.Bitmap;
import android.widget.ImageView;

import com.tomtaw.image_loader.DisplayOptions;
import com.tomtaw.image_loader.ImageLoadListener;

import java.io.File;

/**
 * Created by yxx on 2015/8/13.
 *
 * @author ohun@live.cn
 */
public interface ImageDisplayLoader {

  void display(ImageView imageView, int resourceId);

  void display(ImageView imageView, int resourceId,  int defaultRes);

  void display(ImageView imageView, String url);

  void display(ImageView imageView, File url);

  void display(ImageView imageView, String url, int defaultRes);

  void display(ImageView imageView, File url, int defaultRes);

  void display(ImageView imageView, String url, ImageLoadListener listener);

  void display(DisplayOptions opts, ImageView imageView, String url, ImageLoadListener listener);

  void display(DisplayOptions opts, ImageView imageView, String url);

  void load(String url, ImageLoadListener listener);

  Bitmap syncLoad(String url);
}
