package com.tomtaw.third_party.utils;

import android.content.Context;
import android.content.pm.PackageManager;

public class PackageUtils {

   public static boolean checkApkExist(Context context, String packageName) {
        if (packageName == null || "".equals(packageName)) {
            return false;
        }
        try {
            context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_UNINSTALLED_PACKAGES);
            return true;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }

}
