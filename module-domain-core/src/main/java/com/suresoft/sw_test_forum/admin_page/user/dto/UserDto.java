package com.suresoft.sw_test_forum.admin_page.user.dto;

import com.suresoft.sw_test_forum.admin_page.user.domain.UserAttachedFile;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.UserStatus;
import com.suresoft.sw_test_forum.common.dto.CommonDto;
import com.suresoft.sw_test_forum.common.validation.Editor;
import com.suresoft.sw_test_forum.common.validation.Password;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class UserDto extends CommonDto {
    /* 필수 정보 */
    @Size(min = 4, max = 20, message = "'아이디'의 길이는 4 보다 크고 20 보다 작아야 합니다.")
    private String username;

    @Password(min = 6, max = 16, message = "'비밀번호'의 길이는 6 보다 크고 16 보다 작여야 합니다.")
    private String password;

    @NotBlank(message = "'이름'은 공란이 될 수 없습니다.")
    private String name;

    @NotBlank(message = "'부서'는 공란이 될 수 없습니다.")
    private String department;

    @NotNull(message = "'직위'는 공란이 될 수 없습니다.")
    private Position position;

    @NotNull(message = "'사용자 상태'는 공란이 될 수 없습니다.")
    private UserStatus userStatus;

    @NotNull(message = "'권한'은 공란이 될 수 없습니다.")
    private AuthorityType authorityType;

    /* 연락처 */
    @Size(max = 255, message = "'연락처'는 255 보다 작아야 합니다.")
    private String contact;

    @Size(max = 255, message = "'이메일'은 255 보다 작아야 합니다.")
    private String email;

    @Size(max = 255, message = "'개인 이메일'은 255 보다 작아야 합니다.")
    private String privateEmail;

    /* 자기소개 */
    @Editor(max = 16777215, message = "'에디터' 크기는 16777215Bytes(16MB) 보다 작아야 합니다.")
    private String introduction;

    /* 기타 정보 */
    private long contribution;

    /* autoComplete */
    Set<String> autoCompleteDepartment = new HashSet<>();

    /* 사용자 정보에 접근하는 사용자의 권한 */
    private AuthorityType accessorAuthorityType;

    /* 첨부 파일 */
    private List<UserAttachedFile> attachedFileList = new ArrayList<>();
}