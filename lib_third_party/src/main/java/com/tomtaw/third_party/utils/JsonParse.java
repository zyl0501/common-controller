package com.tomtaw.third_party.utils;

import com.ray.common.json.Jsons;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * Created by zyl on 2017/4/28.
 */

public class JsonParse {

    public static <T> T parseResult(InputStream inputStream, Class<T> clz){
        byte[] responseBody = getBytesByInputStream(inputStream);
        String jsonString = getStringByBytes(responseBody);
        return Jsons.fromJson(jsonString, clz);
    }

    public static String getStringByInputStream(InputStream inputStream){
        byte[] responseBody = getBytesByInputStream(inputStream);
        return getStringByBytes(responseBody);
    }

    //从InputStream中读取数据，转换成byte数组，最后关闭InputStream
    private static byte[] getBytesByInputStream(InputStream is) {
        byte[] bytes = null;
        BufferedInputStream bis = new BufferedInputStream(is);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        BufferedOutputStream bos = new BufferedOutputStream(baos);
        byte[] buffer = new byte[1024 * 8];
        int length = 0;
        try {
            while ((length = bis.read(buffer)) > 0) {
                bos.write(buffer, 0, length);
            }
            bos.flush();
            bytes = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                bis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return bytes;
    }

    //根据字节数组构建UTF-8字符串
    private static String getStringByBytes(byte[] bytes) {
        String str = "";
        try {
            str = new String(bytes, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return str;
    }
}
