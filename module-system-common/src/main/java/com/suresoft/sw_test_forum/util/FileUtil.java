package com.suresoft.sw_test_forum.util;

import java.text.DecimalFormat;

public class FileUtil {
    public static String getFileExtension(String fileName) {
        return (fileName.substring(fileName.lastIndexOf("."))).toLowerCase();
    }

    public static String convertFileSizeUnit(long fileSize) {
        String retFormat = "";
        String[] units = {"Byte", "KB", "MB", "GB", "TB", "PB"};
        DecimalFormat df = new DecimalFormat("#,###.##");

        if (fileSize != 0) {
            int idx = (int) Math.floor(Math.log(fileSize) / Math.log(1024));
            double ret = ((fileSize / Math.pow(1024, Math.floor(idx))));
            retFormat = df.format(ret) + "" + units[idx];
        } else {
            retFormat += " " + units[0];
        }

        return retFormat;
    }
}
