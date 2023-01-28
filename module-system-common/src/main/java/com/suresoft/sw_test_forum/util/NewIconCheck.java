package com.suresoft.sw_test_forum.util;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

public class NewIconCheck {
    public static Boolean isNew(LocalDateTime pastLocalDateTime) {
        LocalDateTime currentTime = LocalDateTime.now();
        boolean result;

        // 현재 시간과 비교하여 48시간 이내의 게시글의 경유 newIcon 생성
        if (ChronoUnit.HOURS.between(currentTime, pastLocalDateTime) >= -48) {
            result = true;
        } else {
            result = false;
        }

        return result;
    }
}