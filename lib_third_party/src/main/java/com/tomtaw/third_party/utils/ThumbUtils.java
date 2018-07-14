package com.tomtaw.third_party.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;

/**
 * Created by zyl on 2017/9/25.
 */

public class ThumbUtils {
    public static byte[] getThumb(Context context, int res, int size){
        Bitmap bmp = BitmapFactory.decodeResource(context.getResources(), res);
        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, size, size, true);
        bmp.recycle();
        return bmpToByteArray(thumbBmp, true);
    }

    private static byte[] bmpToByteArray(final Bitmap bmp, final boolean needRecycle) {
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
}
