package com.ray.common.util;

import android.text.TextUtils;

import com.ray.common.Constants;
import com.ray.common.lang.Strings;
import com.ray.common.log.Logs;

import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.FileChannel;

import static com.ray.common.Constants.EMPTY_BYTES;

public final class IOs {

    public static boolean isFileExist(String filePath) {
        if (TextUtils.isEmpty(filePath)) {
            return false;
        } else {
            File file = new File(filePath);
            return file.exists() && file.isFile();
        }
    }

    public static String getFileName(File file) {
        String fileName = file.getName();
        int index = fileName.lastIndexOf('.');
        if (index == -1) return fileName;
        return fileName.substring(0, index);
    }

    public static String getFileSuffix(File file) {
        String fileName = file.getName();
        int index = fileName.lastIndexOf('.');
        if (index == -1) return Strings.EMPTY;
        return fileName.substring(index + 1);
    }

    public static String readFile(File file) {
        byte[] bytes = loadFile(file);
        if (bytes.length == 0) return Strings.EMPTY;
        return new String(bytes, Constants.UTF_8);
    }

    public static boolean writeFile(File file, InputStream in) {
        if (file == null || in == null) return false;
        FileOutputStream out = null;
        File tmp = null;
        try {
            int size = in.available();
            if (size <= 0) return false;
            tmp = new File(file.getAbsolutePath() + ".tmp");
            createFile(tmp);
            out = new FileOutputStream(tmp);
            byte[] buffer = new byte[10240];
            int offset;
            while ((offset = in.read(buffer)) != -1) {
                out.write(buffer, 0, offset);
            }
            if (tmp.renameTo(file)) return true;
        } catch (Throwable throwable) {
            Logs.e("IOS", "write file ex, file=" + file.getAbsolutePath());
        } finally {
            close(out, in);
            if (tmp != null && tmp.exists()) {
                tmp.delete();
            }
        }
        return false;
    }

    public static byte[] loadFile(File file) {
        if (file == null || !file.exists()) return EMPTY_BYTES;
        int size = (int) file.length();
        if (size <= 0) return EMPTY_BYTES;
        FileInputStream inputStream = null;
        try {
            byte[] buffer = new byte[size];
            inputStream = new FileInputStream(file);
            inputStream.read(buffer);
            return buffer;
        } catch (Throwable throwable) {
            Logs.e("IOS", throwable, "load file ex, file=" + file.getAbsolutePath());
        } finally {
            close(inputStream);
        }
        return EMPTY_BYTES;
    }

    public static boolean renameFile(File srcFile, File destFile){
        try {
            if( srcFile == null || destFile == null
                    || !srcFile.exists()
                    || srcFile.isDirectory()
                    || srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())){
                return false;
            }
        } catch (IOException e) {
            return false;
        }
        File parentFile = destFile.getParentFile();
        if (parentFile != null) {
            if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
                return false;
            }
        }
        if (destFile.exists() && !destFile.canWrite() || destFile.isDirectory()) {
            return false;
        }

        return srcFile.renameTo(destFile);
    }

    public static boolean copyFile(File srcFile, File destFile) {
        try {
            if (srcFile == null || destFile == null
                    || !srcFile.exists()
                    || srcFile.isDirectory()
                    || srcFile.getCanonicalPath().equals(destFile.getCanonicalPath())
                    ) {
                return false;
            }
        } catch (IOException e) {
            return false;
        }

        File parentFile = destFile.getParentFile();
        if (parentFile != null) {
            if (!parentFile.mkdirs() && !parentFile.isDirectory()) {
                return false;
            }
        }
        if (destFile.exists() && !destFile.canWrite() || destFile.isDirectory()) {
            return false;
        }
        try {
            return doCopyFile(srcFile, destFile, false);
        } catch (Exception e) {
            return false;
        }
    }


    private static boolean doCopyFile(File srcFile, File destFile, boolean preserveFileDate) throws IOException {
        FileInputStream fis = null;
        FileOutputStream fos = null;
        FileChannel input = null;
        FileChannel output = null;
        try {
            fis = new FileInputStream(srcFile);
            fos = new FileOutputStream(destFile);
            input = fis.getChannel();
            output = fos.getChannel();
            long size = input.size();
            long pos = 0;
            long count;
            long FILE_COPY_BUFFER_SIZE = 1024 * 30;
            while (pos < size) {
                count = size - pos > FILE_COPY_BUFFER_SIZE ? FILE_COPY_BUFFER_SIZE : size - pos;
                pos += output.transferFrom(input, pos, count);
            }
        } finally {
            IOs.close(output, fos, input, fis);
        }

        if (srcFile.length() != destFile.length()) {
            return false;
        }
        if (preserveFileDate) {
            destFile.setLastModified(srcFile.lastModified());
        }
        return true;
    }

    public static void createFile(File file) throws IOException {
        if (!file.exists()) {
            File dir = file.getParentFile();
            if (!dir.exists()) dir.mkdirs();
            file.createNewFile();
        }
    }

    public static void close(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Throwable throwable) {
            }
        }
    }

    public static void close(Closeable... closeables) {
        for (Closeable closeable : closeables) {
            if (closeable == null) continue;
            try {
                closeable.close();
            } catch (Throwable throwable) {
            }
        }
    }
}
