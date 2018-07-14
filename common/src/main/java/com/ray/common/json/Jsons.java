package com.ray.common.json;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.ray.common.lang.Strings;
import com.ray.common.log.Tags;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by xiaoxu.yxx on 15/8/7.
 */
public final class Jsons {
    public static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
                    //.registerTypeAdapter(Date.class, new DateSerializer())
            .create();

    @Nullable
    public static String toJson(@NonNull Object bean) {
        try {
            return GSON.toJson(bean);
        } catch (Exception e) {
            Tags.App.e(e, "Jsons.toJson ex, bean=" + bean);
        }
        return null;
    }

    @Nullable
    public static <T> T fromJson(@NonNull String json, @NonNull Class<T> clazz) {
        try {
            return GSON.fromJson(json, clazz);
        } catch (Exception e) {
            Tags.App.e(e, "Jsons.fromJson ex, json=" + json + ", clazz=" + clazz);
        }
        return null;
    }

    @Nullable
    public static <T> T fromJson(@NonNull String json, @NonNull Class<T> clazz, boolean log) {
        try {
            return GSON.fromJson(json, clazz);
        } catch (Exception e) {
            if(log) {
                Tags.App.e(e, "Jsons.fromJson ex, json=" + json + ", clazz=" + clazz);
            }
        }
        return null;
    }

    @Nullable
    public static <T> T fromJson(@NonNull String json, @NonNull Type type) {
        try {
            return GSON.fromJson(json, type);
        } catch (Exception e) {
            Tags.App.e(e, "Jsons.fromJson ex, json=" + json + ", type=" + type);
        }
        return null;
    }

    public static boolean mayJson(String json) {
        if (Strings.isBlank(json)) return false;
        if (json.charAt(0) == '{' && json.charAt(json.length() - 1) == '}') return true;
        if (json.charAt(0) == '[' && json.charAt(json.length() - 1) == ']') return true;
        return false;
    }

    public static String toJson(Map<String, String> map) {
        if (map == null || map.isEmpty()) return "{}";
        StringBuilder sb = new StringBuilder(64 * map.size());
        sb.append('{');
        Iterator<Map.Entry<String, String>> it = map.entrySet().iterator();
        if (it.hasNext()) {
            append(it.next(), sb);
        }
        while (it.hasNext()) {
            sb.append(',');
            append(it.next(), sb);
        }
        sb.append('}');
        return sb.toString();
    }

    private static void append(Map.Entry<String, String> entry, StringBuilder sb) {
        String key = entry.getKey(), value = entry.getValue();
        if (value == null) value = Strings.EMPTY;
        sb.append('"').append(key).append('"');
        sb.append(':');
        sb.append('"').append(value).append('"');
    }
}
