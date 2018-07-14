package com.ray.common.lang;

public final class Arrays {
    public static final Object[] EMPTY_OBJECT_ARRAY = new Object[0];

    public static long[] toPrimitiveLongs(String[] array) {
        if (array == null || array.length == 0) return new long[0];
        long[] longs = new long[array.length];
        for (int i = 0; i < array.length; i++) {
            longs[i] = Strings.toLong(array[i], 0);
        }
        return longs;
    }

    public static Long[] toLongs(String[] array) {
        if (array == null || array.length == 0) return new Long[0];
        Long[] longs = new Long[array.length];
        for (int i = 0; i < array.length; i++) {
            longs[i] = Strings.toLong(array[i], 0);
        }
        return longs;
    }
}
