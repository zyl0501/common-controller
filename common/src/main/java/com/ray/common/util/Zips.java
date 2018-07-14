package com.ray.common.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

public class Zips {


    public static boolean unzip(byte[] data, File destDir) {
        return unzip(new ByteArrayInputStream(data), destDir);
    }


    public static boolean unzip(File src, File destDir) {
        try {
            return unzip(new FileInputStream(src), destDir);
        } catch (FileNotFoundException e) {

        }
        return false;
    }

    public static boolean unzip(InputStream src, File destDir) {
        if (src == null || destDir == null) return false;
        FileOutputStream output = null;
        ZipInputStream zipFile = null;
        try {
            zipFile = new ZipInputStream(src);
            ZipEntry zipEntry;
            while ((zipEntry = zipFile.getNextEntry()) != null) {
                File loadFile = new File(destDir, zipEntry.getName());
                if (zipEntry.isDirectory()) {
                    loadFile.mkdirs();
                } else {
                    File dir = loadFile.getParentFile();
                    if (!dir.exists()) dir.mkdirs();

                    output = new FileOutputStream(loadFile);
                    //写入每个子文件
                    byte[] buffer = new byte[1024];
                    int length;
                    while ((length = zipFile.read(buffer, 0, 1024)) > 0) {
                        output.write(buffer, 0, length);
                    }
                    IOs.close(output);
                }
            }
            return true;
        } catch (Throwable throwable) {
        } finally {
            IOs.close(output, zipFile);
        }
        return false;
    }

}
