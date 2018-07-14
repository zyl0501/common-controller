package com.ray.common.util;

import android.text.TextUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Date;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

public class Files {
    /**
     * 文件拷贝
     *
     * @param fileFrom
     * @param fileTo
     * @return
     * @throws Exception
     */
    public static long copy(File fileFrom, File fileTo) throws Exception {
        long time = new Date().getTime();
        int length = 2097152;
        FileInputStream in = new FileInputStream(fileFrom);
        FileOutputStream out = new FileOutputStream(fileTo);
        FileChannel inC = in.getChannel();
        FileChannel outC = out.getChannel();
        ByteBuffer b = null;
        while (true) {
            if (inC.position() == inC.size()) {
                inC.close();
                outC.close();
                return new Date().getTime() - time;
            }
            if ((inC.size() - inC.position()) < length) {
                length = (int) (inC.size() - inC.position());
            } else
                length = 2097152;
            b = ByteBuffer.allocateDirect(length);
            inC.read(b);
            b.flip();
            outC.write(b);
            outC.force(false);
        }
    }

    /**
     * 获取带后缀名的文件名，如果已经有了，则不变
     * @param file
     * @return
     */
    public static String fillExtensionName(File file){
        String absolutePath = file.getAbsolutePath();
        if(TextUtils.isEmpty(getFileExtension(absolutePath))) {
            String type = FileType.getFileType(absolutePath);
            return getNameWithoutExtension(absolutePath) + "." + type;
        }else{
            return file.getName();
        }
    }

    public static String getFileExtension(String fullName) {
        checkNotNull(fullName);
        String fileName = new File(fullName).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? "" : fileName.substring(dotIndex + 1);
    }

    public static String getNameWithoutExtension(String file) {
        checkNotNull(file);
        String fileName = new File(file).getName();
        int dotIndex = fileName.lastIndexOf('.');
        return (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
    }

    // storage, G M K B
    public static String convertStorage(long size) {
        long kb = 1024;
        long mb = kb * 1024;
        long gb = mb * 1024;

        if (size >= gb) {
            return String.format("%.1f GB", (float) size / gb);
        } else if (size >= mb) {
            float f = (float) size / mb;
            return String.format(f > 100 ? "%.0f MB" : "%.1f MB", f);
        } else if (size >= kb) {
            float f = (float) size / kb;
            return String.format(f > 100 ? "%.0f KB" : "%.1f KB", f);
        } else
            return String.format("%d B", size);
    }
}
