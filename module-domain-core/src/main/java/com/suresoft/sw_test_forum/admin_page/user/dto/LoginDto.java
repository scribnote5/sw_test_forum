package com.suresoft.sw_test_forum.admin_page.user.dto;

import com.suresoft.sw_test_forum.common.validation.ModuleName;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class LoginDto {
    @NotNull
    @Size(min = 4, max = 16, message = "'비밀번호'의 길이는 6 보다 크고 16 보다 작아야 합니다.")
    private String username;

    @NotNull
    @Size(min = 6, max = 16, message = "'비밀번호'의 길이는 6 보다 크고 16 보다 작아야 합니다.")
    private String password;

    @ModuleName(message = "'모듈명'이 유효하지 않습니다.")
    private String moduleName;
}
