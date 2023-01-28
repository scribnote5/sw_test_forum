package com.suresoft.sw_test_forum.common.dto;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
public class CommonDto {
    private long idx;

    private LocalDateTime createdDate;

    private LocalDateTime lastModifiedDate;

    private long createdByIdx;

    private User createdByUser;

    private long lastModifiedByIdx;

    private User lastModifiedByUser;

    @NotNull(message = "'게시 여부'는 NULL이 될 수 없습니다.")
    private ActiveStatus activeStatus;

    private long views = 0;

    private boolean isAccess;

    private boolean isNewIcon;
}