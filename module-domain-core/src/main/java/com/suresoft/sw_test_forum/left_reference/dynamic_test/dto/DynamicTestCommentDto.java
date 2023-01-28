package com.suresoft.sw_test_forum.left_reference.dynamic_test.dto;

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
public class DynamicTestCommentDto extends CommonDto {
    private long dynamicTestIdx;

    @Editor(max = 16777215, message = "'에디터'는 16777215Bytes(16MB)보다 작아야 합니다.")
    private String content;
}