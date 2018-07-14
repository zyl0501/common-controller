package com.ray.common.util;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.pm.Signature;
import android.net.Uri;
import android.os.Bundle;

import com.ray.common.log.Tags;

import java.util.List;

public final class Apks {
    public static boolean isDebugable(Context context) {
        try {
            ApplicationInfo info = context.getApplicationInfo();
            return (info.flags & ApplicationInfo.FLAG_DEBUGGABLE) != 0;
        } catch (Exception e) {
        }
        return false;
    }

    public static Bundle getMateData(Context ctx) {
        PackageManager pm = ctx.getPackageManager();
        try {
            ApplicationInfo applicationInfo = pm.getApplicationInfo(ctx.getPackageName(), PackageManager.GET_META_DATA);
            return applicationInfo.metaData;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return null;
    }


    public static PackageInfo getPackageInfo(Context context) {
        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
            return packInfo;
        } catch (PackageManager.NameNotFoundException e) {
        }
        return null;
    }


    public static void openBrowse(Context context, String url) {
        PackageManager packageManager = context.getPackageManager();
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.BROWSABLE");
        intent.addCategory("android.intent.category.DEFAULT");
        intent.setDataAndType(Uri.parse("http://"), null);

        //找出手机当前安装的所有浏览器程序
        List<ResolveInfo> list = packageManager.queryIntentActivities(intent,
                PackageManager.GET_INTENT_FILTERS);
        if (list.isEmpty()) return;

        intent = packageManager.getLaunchIntentForPackage(list.get(0).activityInfo.packageName);
        intent.setAction(Intent.ACTION_VIEW);
        intent.addCategory(Intent.CATEGORY_DEFAULT);
        intent.setData(Uri.parse(url));
        context.startActivity(intent);
    }


    /**
     * 安装apk文件
     */
    public static void installApk(Context context, Uri apk) {
        // 通过Intent安装APK文件
        Intent intents = new Intent();
        intents.setAction("android.intent.action.VIEW");
        intents.addCategory("android.intent.category.DEFAULT");
        intents.setType("application/vnd.android.package-archive");
        intents.setData(apk);
        intents.setDataAndType(apk, "application/vnd.android.package-archive");
        intents.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        try {
            context.startActivity(intents);
        } catch (Exception e) {
            Tags.App.e(e, "install apk failure");
        }
    }

    /**
     * 获取签名
     * @param context
     * @return
     */
    public static String getSignature(Context context) {
        try {
            /** 通过包管理器获得指定包名包含签名的包信息 **/
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), PackageManager.GET_SIGNATURES);
            /******* 通过返回的包信息获得签名数组 *******/
            Signature[] signatures = packageInfo.signatures;
            /******* 循环遍历签名数组拼接应用签名 *******/
            return signatures[0].toCharsString();
            /************** 得到应用签名 **************/
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;

    }
}
