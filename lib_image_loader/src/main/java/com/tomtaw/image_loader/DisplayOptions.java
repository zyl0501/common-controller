package com.tomtaw.image_loader;

/**
 * Created by xiaoxu.yxx on 2015/7/30.
 */
public final class DisplayOptions {
  public int loadingResId = 0;
  public int defaultResId = 0;
  public int errorResId = 0;
  public int maxWidth = 0;
  public int maxHeight = 0;
  public boolean cacheInMemory = true;
  public boolean cacheOnDisk = true;

  public DisplayOptions setLoadingResId(int loadingResId) {
    this.loadingResId = loadingResId;
    return this;
  }

  public DisplayOptions setDefaultResId(int defaultResId) {
    this.defaultResId = defaultResId;
    return this;
  }

  public DisplayOptions setErrorResId(int errorResId) {
    this.errorResId = errorResId;
    return this;
  }

  public DisplayOptions setMaxWidth(int maxWidth) {
    this.maxWidth = maxWidth;
    return this;
  }

  public DisplayOptions setMaxHeight(int maxHeight) {
    this.maxHeight = maxHeight;
    return this;
  }

  public DisplayOptions setCacheInMemory(boolean cacheInMemory) {
    this.cacheInMemory = cacheInMemory;
    return this;
  }

  public DisplayOptions setCacheOnDisk(boolean cacheOnDisk) {
    this.cacheOnDisk = cacheOnDisk;
    return this;
  }

  public static DisplayOptions getDefaultOps() {
    return new DisplayOptions();
  }

}
