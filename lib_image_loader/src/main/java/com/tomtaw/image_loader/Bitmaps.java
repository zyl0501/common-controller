package com.tomtaw.image_loader;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.WindowManager;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.net.URLConnection;

import static android.graphics.Bitmap.CompressFormat.JPEG;

/**
 * Created by yxx on 2015/8/12.
 *
 * @author ohun@live.cn
 */
public final class Bitmaps {
  private static final String TAG = "Bitmaps";
  private static byte[] EMPTY_BYTES = new byte[0];

  /**
   * Convert Image String(base64) to Drawable Object
   *
   * @return
   */
  public static Drawable toDrawable(Resources res, String imageStr) {
    if (imageStr == null) {
      return null;
    }
    try {
      byte[] b = Base64.decode(imageStr, Base64.DEFAULT);
      ByteArrayInputStream in = new ByteArrayInputStream(b);
      Bitmap bm = BitmapFactory.decodeStream(in);
      return new BitmapDrawable(res, bm);
    } catch (IllegalArgumentException e) {
      return null;
    }
  }

  public static Bitmap scaleBitmap(Bitmap bitmap, float scale) {
    Matrix matrix = new Matrix();
    matrix.postScale(scale, scale); //长和宽放大缩小的比例
    Bitmap resizeBmp = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
    return resizeBmp;
  }

  public Bitmap decode(byte[] data) {
    if (data == null) return null;
    return BitmapFactory.decodeByteArray(data, 0, data.length);
  }

  public static byte[] compress(Context context, String filePath, int maxBytes) {
    WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
    android.view.Display display = wm.getDefaultDisplay();
    DisplayMetrics mDisplayMetrics = new DisplayMetrics();
    display.getMetrics(mDisplayMetrics);
    int W = mDisplayMetrics.widthPixels;
    int H = mDisplayMetrics.heightPixels;
    return compress(filePath, W, H, maxBytes);
  }


  /**
   * 基于质量的压缩算法， 此方法未 解决压缩后图像失真问题
   * <br> 可先调用比例压缩适当压缩图片后，再调用此方法可解决上述问题
   *
   * @param maxBytes 压缩后的图像最大大小 单位为byte
   * @return
   */
  public final static byte[] compress(Bitmap bitmap, int maxBytes) {
    int width = bitmap.getWidth();
    int height = bitmap.getHeight();
    if (maxBytes < 160 && height / width >= 3) {
      maxBytes = 160;
    }
    int size = width * height / 1024 / 8;
    int quality = getQuality(size);
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    try {
      bitmap.compress(JPEG, quality, out);
      while (quality > 10 && out.size() / 1024 > maxBytes) {
        out.reset();
        if (!bitmap.compress(JPEG, quality -= 10, out)) break;
      }
    } catch (Throwable e) {
      if(BuildConfig.DEBUG) {
        Log.e(TAG, "compress ex", e);
      }
    }
    return out.toByteArray();

  }


  /**
   * @param filePath
   * @param maxWidth
   * @param maxHeight
   * @param maxBytes
   * @return
   */
  public static byte[] compress(String filePath, int maxWidth, int maxHeight, int maxBytes) {
    Options options = new Options();
    options.inSampleSize = 1;
    options.inJustDecodeBounds = true;
    options.inBitmap = BitmapFactory.decodeFile(filePath, options);

    int actualWidth = options.outWidth;
    int actualHeight = options.outHeight;

    int desiredWidth = getResizedDimension(maxWidth, maxHeight, actualWidth, actualHeight);
    int desiredHeight = getResizedDimension(maxHeight, maxWidth, actualHeight, actualWidth);

    options.inJustDecodeBounds = false;
    options.inSampleSize = findBestSampleSize(actualWidth, actualHeight, desiredWidth, desiredHeight);
    Bitmap destBitmap;
    try {
      destBitmap = BitmapFactory.decodeFile(filePath, options);
      if (destBitmap.getWidth() > desiredWidth || destBitmap.getHeight() > desiredHeight) {
        destBitmap = Bitmap.createScaledBitmap(destBitmap, desiredWidth, desiredHeight, true);
      }
    } catch (Throwable e) {
      if(BuildConfig.DEBUG) {
        Log.e(TAG, "compress ex", e);
      }
      return EMPTY_BYTES;
    }
    return compress(destBitmap, maxBytes);
  }

  public final static int calculateInSampleSize2(Options options, int rqsW, int rqsH) {
    final float h = options.outHeight;
    final float w = options.outWidth;
    if (rqsW == 0 || rqsH == 0) return 1;
    float width = w, height = h;
    float ratio = width / height;
    if (width > rqsW) {
      width = rqsW;
      height = rqsW / ratio;
    }
    if (height > rqsH) {
      height = rqsH;
      width = rqsH * ratio;
    }
    return Math.round(w / width);
  }

  private static int getQuality(int size) {
    int quality = 100;
    if (size > 300) {
      quality = 10;
    } else if (size > 260) {
      quality = 20;
    } else if (size > 220) {
      quality = 30;
    } else if (size > 180) {
      quality = 40;
    } else if (size > 140) {
      quality = 50;
    } else if (size >= 110) {
      quality = 60;
    } else if (size >= 100) {
      quality = 65;
    } else if (size >= 90) {
      quality = 70;
    } else if (size >= 80) {
      quality = 75;
    } else if (size >= 70) {
      quality = 80;
    } else if (size >= 60) {
      quality = 85;
    } else if (size >= 50) {
      quality = 90;
    }
    return quality;
  }

  private static int findBestSampleSize(int actualWidth, int actualHeight,
                                        int desiredWidth, int desiredHeight) {
    double wr = (double) actualWidth / desiredWidth;
    double hr = (double) actualHeight / desiredHeight;
    double ratio = Math.min(wr, hr);
    float n = 1.0f;
    while ((n * 2) <= ratio) {
      n *= 2;
    }
    return (int) n;
  }

  private static int getResizedDimension(int maxPrimary, int maxSecondary,
                                         int actualPrimary, int actualSecondary) {
    // If no dominant value at all, just return the actual.
    if (maxPrimary == 0 && maxSecondary == 0) {
      return actualPrimary;
    }

    // If primary is unspecified, scale primary to match secondary's scaling
    // ratio.
    if (maxPrimary == 0) {
      double ratio = (double) maxSecondary / (double) actualSecondary;
      return (int) (actualPrimary * ratio);
    }

    if (maxSecondary == 0) {
      return maxPrimary;
    }

    double ratio = (double) actualSecondary / (double) actualPrimary;
    int resized = maxPrimary;
    if (resized * ratio > maxSecondary) {
      resized = (int) (maxSecondary / ratio);
    }
    return resized;
  }

  public final static int calculateInSampleSize(Options options, int rqsW, int rqsH) {
    final int height = options.outHeight;
    final int width = options.outWidth;
    int inSampleSize = 1;
    if (rqsW == 0 || rqsH == 0) return 1;
    if (height > rqsH || width > rqsW) {
      final int heightRatio = Math.round((float) height / (float) rqsH);
      final int widthRatio = Math.round((float) width / (float) rqsW);
      inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
    }
    return inSampleSize;
  }

  public static boolean isLargeImage(Options options) {

    return (options.outHeight > 0 && options.outWidth > 0) ? (options.outHeight
        / options.outWidth >= 3)
        : false;
  }

  public static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
    ByteArrayOutputStream output = new ByteArrayOutputStream();
    bmp.compress(Bitmap.CompressFormat.PNG, 100, output);
    if (needRecycle) {
      bmp.recycle();
    }

    byte[] result = output.toByteArray();
    try {
      output.close();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return result;
  }

  /**
   * 判断文件是否是图片
   *
   * @return 如果文件不存在，直接返回false
   */
  public static boolean isImg(@NonNull File file) {
    if(file == null || !file.exists()) return false;

    String path = file.getAbsolutePath();
    String mimeType = URLConnection.guessContentTypeFromName(path);
    return mimeType != null && mimeType.startsWith("image");
  }
}
