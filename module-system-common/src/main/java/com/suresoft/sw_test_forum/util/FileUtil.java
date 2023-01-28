package com.suresoft.sw_test_forum.util;

import java.text.DecimalFormat;

public class FileUtil {
    public static String getFileExtension(String fileName) {
        String fileExtension = null;

        try {
            fileExtension = (fileName.substring(fileName.lastIndexOf("."))).toLowerCase();
        } catch(Exception e) {
            System.err.println("파일 확장자가 없습니다.");
            fileExtension = "";
        }
        return fileExtension;
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
