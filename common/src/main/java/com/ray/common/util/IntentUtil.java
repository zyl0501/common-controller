package com.ray.common.util;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;

/**
 * Created by syy on 2017/3/22.
 * 进行页面跳转的工具
 */

public class IntentUtil {
    public static void callPhone(Context context,String phoneNumber){
        phoneNumber = phoneNumber.replaceAll("-", "");
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+phoneNumber));
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

}
