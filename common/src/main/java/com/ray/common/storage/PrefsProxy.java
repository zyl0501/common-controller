package com.ray.common.storage;

import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

import com.ray.common.lang.Strings;

import java.util.Map;
import java.util.Set;

public final class PrefsProxy {
    public final SharedPreferences preferences;

    public PrefsProxy(SharedPreferences preferences) {
        this.preferences = preferences;
    }

    public String getString(String key, String defValue) {
        return preferences.getString(key, defValue);
    }

    public String getString(String key) {
        return preferences.getString(key, Strings.EMPTY);
    }

    public Set<String> getStringSet(String key, Set<String> defValues) {
        return preferences.getStringSet(key, defValues);
    }

    public int getInt(String key, int defValue) {
        return preferences.getInt(key, defValue);
    }

    public long getLong(String key, long defValue) {
        return preferences.getLong(key, defValue);
    }

    public float getFloat(String key, float defValue) {
        return preferences.getFloat(key, defValue);
    }

    public boolean getBoolean(String key, boolean defValue) {
        return preferences.getBoolean(key, defValue);
    }

    public boolean contains(String key) {
        return preferences.contains(key);
    }

    public Editor edit() {
        return preferences.edit();
    }

    public void putString(Map<String, Object> map) {
        if (map == null || map.isEmpty()) return;
        Editor editor = edit();
        for (Map.Entry<String, Object> entry : map.entrySet()) {
            editor.putString(entry.getKey(), entry.getValue().toString());
        }
        editor.apply();
    }

    public void putString(String key, String value) {
        edit().putString(key, value).apply();
    }

    public void putStringSet(String key, Set<String> values) {
        edit().putStringSet(key, values).apply();
    }

    public void putInt(String key, int value) {
        edit().putInt(key, value).apply();
    }

    public void putLong(String key, long value) {
        edit().putLong(key, value).apply();
    }

    public void putFloat(String key, float value) {
        edit().putFloat(key, value).apply();
    }

    public void putBoolean(String key, boolean value) {
        edit().putBoolean(key, value).apply();
    }

    public void remove(String key) {
        edit().remove(key).apply();
    }

    public void clear() {
        edit().clear().apply();
    }
}
