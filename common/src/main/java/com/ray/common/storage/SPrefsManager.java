package com.ray.common.storage;

import android.content.Context;
import android.preference.PreferenceManager;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public final class SPrefsManager {
    private static final Map<String, PrefsProxy> PREFS_CACHE = new ConcurrentHashMap<>();
    private static PrefsProxy defaultPrefs;

    public static PrefsProxy getDefaultPrefs(Context context) {
        if (defaultPrefs == null) {
            defaultPrefs = new PrefsProxy(PreferenceManager.getDefaultSharedPreferences(context));
        }
        return defaultPrefs;
    }

    public static PrefsProxy getPreferences(Context context, String name) {
        PrefsProxy prefs = PREFS_CACHE.get(name);
        if (prefs == null) {
            prefs = new PrefsProxy(context.getSharedPreferences(name, Context.MODE_PRIVATE));
            PREFS_CACHE.put(name, prefs);
        }
        return prefs;
    }

    /**
     * 清空内存缓存
     */
    public static void clearCache() {
        PREFS_CACHE.clear();
        defaultPrefs = null;
    }

    /**
     * 清空文件
     */
    public static void clear() {
        if (defaultPrefs != null) defaultPrefs.clear();
        for (PrefsProxy p : PREFS_CACHE.values()) {
            p.clear();
        }
    }
}
