package com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto;

import com.suresoft.sw_test_forum.common.dto.CommonDto;
import com.suresoft.sw_test_forum.common.validation.Editor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StaticToolCommentDto extends CommonDto {
    private long staticToolIdx;

    @Editor(max = 16777215, message = "'에디터'는 16777215Bytes(16MB)보다 작아야 합니다.")
    private String content;
}