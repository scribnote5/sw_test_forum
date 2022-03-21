package com.suresoft.sw_test_forum.admin_page.data_history.dto;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.common.dto.CommonDto;
import com.suresoft.sw_test_forum.common.validation.Editor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DataHistoryDto extends CommonDto {
    private long auditIdx;

    @Size(max = 255, message = "'audit 게시판'의 길이는 255 보다 작아야 합니다.")
    private String auditBoard;

    @Size(max = 255, message = "'audit 유형'의 길이는 255 보다 작아야 합니다.")
    private AuditType auditType;

    @Size(max = 255, message = "'메시지'의 길이는 255 보다 작아야 합니다.")
    private String message;

    @Size(max = 255, message = "'상세 메시지'의 길이는 255 보다 작아야 합니다.")
    private String detailedMessage;

    @Editor(max = 16777215, message = "'에디터' 크기는 16777215Bytes(16MB) 보다 작아야 합니다.")
    private String content;
}
