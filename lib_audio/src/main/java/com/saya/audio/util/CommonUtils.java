package com.saya.audio.util;

import android.content.Context;
import android.os.Environment;
import android.util.DisplayMetrics;

import com.saya.audio.config.SysConstant;

import java.io.File;

/**
 * Created by saya on 2016/7/19.
 */
public class CommonUtils {

    /**
     * @Description 判断存储卡是否存在
     * @return
     */
    public static boolean checkSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
                android.os.Environment.MEDIA_MOUNTED)) {
            return true;
        }

        return false;
    }

    public static String getFileSavePath(Context context,String dir,String filenamne) {

        String path;
        if (CommonUtils.checkSDCard()) {
            path = context.getExternalFilesDir(dir).toString()
                    + File.separator+ filenamne;

        } else {
            path = context.getExternalFilesDir(dir).toString() + File.separator+ filenamne;
        }

        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()){
                return "error";
            }
        }
        return path;
    }

    public static String getImageSavePath(int userId,String fileExtension) {
        String path = getSavePath(SysConstant.FILE_SAVE_TYPE_IMAGE) + userId
                + "_" + String.valueOf(System.currentTimeMillis())
                + "."+ fileExtension;
        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()){
                return "error";
            }
        }
        return path;
    }



    public static String getAudioSavePath(int userId) {

        String path = getSavePath(SysConstant.FILE_SAVE_TYPE_AUDIO) + userId
                + "_" + String.valueOf(System.currentTimeMillis())
                + ".spx";
        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            if (!parent.mkdirs()){
                return "error";
            }
        }
        return path;
    }

    public static String getSavePath(int type) {
        String path;
        String floder = (type == SysConstant.FILE_SAVE_TYPE_IMAGE) ? "images"
                : "audio";
        if (CommonUtils.checkSDCard()) {
            path = Environment.getExternalStorageDirectory().toString()
                    + File.separator + "saya" + File.separator + floder
                    + File.separator;

        } else {
            path = Environment.getDataDirectory().toString() + File.separator
                    + "saya" + File.separator + floder + File.separator;
        }
        return path;
    }

    // check again
    public static int getAudioBkSize(int sec, Context context) {
        int size = getElementSzie(context) * 2;
        if (sec <= 0) {
            return -1;
        } else if (sec <= 2) {
            return size;
        } else if (sec <= 8) {
            return (int) (size + ((float) ((sec - 2) / 6.0)) * size);
        } else if (sec <= 60) {
            return (int) (2 * size + ((float) ((sec - 8) / 52.0)) * size);
        } else {
            return -1;
        }
    }

    public static int getElementSzie(Context context) {
        if (context != null) {
            DisplayMetrics dm = context.getResources().getDisplayMetrics();
            int screenHeight = px2dip(dm.heightPixels, context);
            int screenWidth = px2dip(dm.widthPixels, context);
            int size = screenWidth / 6;
            if (screenWidth >= 800) {
                size = 60;
            } else if (screenWidth >= 650) {
                size = 55;
            } else if (screenWidth >= 600) {
                size = 50;
            } else if (screenHeight <= 400) {
                size = 20;
            } else if (screenHeight <= 480) {
                size = 25;
            } else if (screenHeight <= 520) {
                size = 30;
            } else if (screenHeight <= 570) {
                size = 35;
            } else if (screenHeight <= 640) {
                if (dm.heightPixels <= 960) {
                    size = 50;
                } else if (dm.heightPixels <= 1000) {
                    size = 45;
                }
            }
            return size;
        }
        return 40;
    }

    private static int px2dip(float pxValue, Context context) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

}
