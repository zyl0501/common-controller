package com.tomtaw.image_loader.glide;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.Fragment;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.tomtaw.image_loader.ConfigOptions;
import com.tomtaw.image_loader.DisplayOptions;
import com.tomtaw.image_loader.ImageLoadListener;
import com.tomtaw.image_loader.inner.ImageDisplayLoader;
import com.tomtaw.image_loader.inner.RequestManager;

import java.io.File;

/**
 * Created by zyl on 2017/3/15.
 */
public class GlideRequestManager implements RequestManager {
  GlideLoader loader;

  public GlideRequestManager(ConfigOptions options) {
    loader = new GlideLoader();
  }

  @Override
  public ImageDisplayLoader with(Fragment fragment) {
    loader.requestManager = Glide.with(fragment);
    return loader;
  }

  @Override
  public ImageDisplayLoader with(android.app.Fragment fragment) {
    loader.requestManager = Glide.with(fragment);
    return loader;
  }

  @Override
  public ImageDisplayLoader with(Activity activity) {
    loader.requestManager = Glide.with(activity);
    return loader;
  }

  @Override
  public ImageDisplayLoader with(Context context) {
    loader.requestManager = Glide.with(context);
    return loader;
  }

  private class GlideLoader implements ImageDisplayLoader {

    com.bumptech.glide.RequestManager requestManager;

    @Override
    public void display(ImageView imageView, int resourceId) {
      if(requestManager == null) return;
      requestManager.load(resourceId).into(imageView);
    }

    @Override
    public void display(ImageView imageView, int resourceId, int defaultRes) {
      if(requestManager == null) return;
      requestManager.load(resourceId).placeholder(defaultRes).into(imageView);
    }

    @Override
    public void display(final ImageView imageView, String url) {
      if(requestManager == null) return;
      requestManager
          .load(url)
          .into(imageView);
    }

    @Override
    public void display(ImageView imageView, File url) {
      if(requestManager == null) return;
      requestManager.load(url).into(imageView);
    }

    @Override
    public void display(ImageView imageView, String url, int defaultRes) {
      if(requestManager == null) return;
      requestManager.load(url).placeholder(defaultRes).dontAnimate().into(imageView);
    }

    @Override
    public void display(ImageView imageView, File url, int defaultRes) {
      if(requestManager == null) return;
      requestManager.load(url).placeholder(defaultRes).dontAnimate().into(imageView);
    }

    @Override
    public void display(ImageView imageView, String url, ImageLoadListener listener) {
    }

    @Override
    public void display(DisplayOptions opts, ImageView imageView, String url, ImageLoadListener listener) {
      if(requestManager == null) return;
      requestManager.load(url).placeholder(opts.defaultResId).into(imageView);
    }

    @Override
    public void display(DisplayOptions opts, ImageView imageView, String url) {
    }

    @Override
    public void load(String url, ImageLoadListener listener) {
    }

    @Override
    public Bitmap syncLoad(String url) {
      return null;
    }
  }
}
