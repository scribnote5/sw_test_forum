package com.suresoft.sw_test_forum.admin_page.login_history.dto;

import com.suresoft.sw_test_forum.admin_page.login_history.domain.enums.LoginResultType;
import com.suresoft.sw_test_forum.common.dto.CommonDto;
import lombok.*;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class LoginHistoryDto extends CommonDto {
    @Size(max = 255, message = "'아이디'의 길이는 255 보다 작아야 합니다.")
    private String loginUsername;

    @Size(max = 255, message = "'ip 주소'의 길이는 255 보다 작아야 합니다.")
    private String ip;

    @Size(max = 255, message = "'위치'의 길이는 255 보다 작아야 합니다.")
    private String location;

    @Size(max = 255, message = "'메시지'의 길이는 255 보다 작아야 합니다.")
    private String message;

    @Size(max = 255, message = "'상세 메시지'의 길이는 255 보다 작아야 합니다.")
    private String detailedMessage;

    @NotNull(message = "'로그인 접속 결과'는 NULL이 될 수 없습니다.")
    private LoginResultType loginResultType;
}