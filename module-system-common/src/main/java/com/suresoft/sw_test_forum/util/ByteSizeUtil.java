package com.suresoft.sw_test_forum.util;

public class ByteSizeUtil {
    public static long getByteSize(String str) {
        long byteSize = 0;

        try {
            byteSize = str.getBytes("UTF-8").length;
        } catch (NullPointerException e) {
            byteSize = 0;
        } catch (Exception e) {
            byteSize = 0;
            e.printStackTrace();
        }

        return byteSize;
    }
}
