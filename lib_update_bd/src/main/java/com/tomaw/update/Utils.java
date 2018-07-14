package com.tomaw.update;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.DisplayMetrics;
import android.view.WindowManager;

/**
 * Created by zyl on 2017/6/28.
 */

public class Utils {
    /*
     * 获取程序 图标
     */
    public static Drawable getAppIcon(Context context){
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
            return info.loadIcon(pm);
        } catch (PackageManager.NameNotFoundException e) {
        }
        return null;
    }
    /*
     * 获取程序 图标
     */
    public static int getAppIconRes(Context context){
        try {
            final PackageManager pm=context.getPackageManager();
            final ApplicationInfo applicationInfo=pm.getApplicationInfo(context.getPackageName(),PackageManager.GET_META_DATA);
            return applicationInfo.icon;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return -1;
    }



    /*
 *获取程序的版本号
 */
    public static String getAppVersion(Context context){

        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packinfo = pm.getPackageInfo(context.getPackageName(), 0);
            return packinfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return null;
    }

    /*
     * 获取程序的名字
     */
    public static String getAppName(Context context){
        try {
            PackageManager pm = context.getPackageManager();
            ApplicationInfo info = pm.getApplicationInfo(context.getPackageName(), 0);
            return info.loadLabel(pm).toString();
        } catch (PackageManager.NameNotFoundException e) {
        }
        return null;
    }

    public static DisplayMetrics getScreenHeightAndWidth(Context context) {
        WindowManager manager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics;
    }

    /**
     * make true current connect service is wifi
     * 判断当前网络是不是wifi情况
     *
     * @param mContext
     * @return
     */
    public static boolean isWifi(Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
            .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null
            && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }

    public static Bitmap drawableToBitmap(Drawable drawable) {
        Bitmap bitmap = Bitmap.createBitmap(
            drawable.getIntrinsicWidth(),
            drawable.getIntrinsicHeight(),
            drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888
                : Bitmap.Config.RGB_565);
        Canvas canvas = new Canvas(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return bitmap;
    }
}
