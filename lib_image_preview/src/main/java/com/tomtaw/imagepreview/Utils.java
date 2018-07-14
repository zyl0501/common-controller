package com.tomtaw.imagepreview;

import android.text.TextUtils;

import java.util.Arrays;

/**
 * @author zyl
 * @date Created on 2018/1/3
 */
class Utils {

    static String[] remove(String[] ss, String s) {
        if (ss != null) {
            int i = 0;
            int size = ss.length;
            int index = -1;
            for (; i < size; i++) {
                if (TextUtils.equals(ss[i], s)) {
                    index = i;
                    break;
                }
            }
            return remove(ss, index);
        }
        return null;
    }

    static String[] remove(String[] ss, int index) {
        if (ss != null) {
            int size = ss.length;
            if (index != -1) {
                int i = index;
                for (; i < size - 1; i++) {
                    ss[i] = ss[i + 1];
                }
                return Arrays.copyOf(ss, size - 1);
            } else {
                return ss;
            }
        } else {
            return null;
        }
    }
}
