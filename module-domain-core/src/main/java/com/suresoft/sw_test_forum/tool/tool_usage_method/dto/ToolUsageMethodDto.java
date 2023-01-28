package com.suresoft.sw_test_forum.tool.tool_usage_method.dto;

import com.suresoft.sw_test_forum.common.domain.enums.TargetToolName;
import com.suresoft.sw_test_forum.common.domain.enums.ToolCategory;
import com.suresoft.sw_test_forum.common.dto.CommonDto;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.validation.Editor;
import com.suresoft.sw_test_forum.tool.tool_usage_method.domain.ToolUsageMethodAttachedFile;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.validation.constraints.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@ToString
@NoArgsConstructor
public class ToolUsageMethodDto extends CommonDto {
    @NotBlank(message = "'제목'은 공란이 될 수 없습니다.")
    @Size(max = 255, message = "'제목'의 길이는 255 보다 작아야 합니다.")
    private String title;

    @Max(value = 6, message = "'우선순위'는 6 보다 작아야 합니다.")
    @Min(value = 1, message = "'우선순위'는 1 보다 커야 합니다.")
    private long priority;

    @Size(max = 255, message = "'해시태그'의 길이는 255 보다 작아야 합니다.")
    private String hashTags;

    @NotNull(message = "'해시태그 idx'는 NULL이 될 수 없습니다.")
    private long hashTagsIdx;

    @NotNull(message = "'도구 유형'는 NULL이 될 수 없습니다.")
    private ToolCategory toolCategory;

    @NotNull(message = "'대상 도구'는 NULL이 될 수 없습니다.")
    private TargetToolName targetToolName;

    @Editor(max = 16777215, message = "'에디터' 크기는 16777215Bytes(16MB) 보다 작아야 합니다.")
    private String content;

    /* autoComplete */
    Set<String> autoCompleteTitle = new HashSet<>();
    Set<String> autoCompleteHashTags = new HashSet<>();

    /* 우선순위 현황 */
    private List<PriorityDto> priorityDtoList = new ArrayList<>();

    /* 첨부 파일 */
    private List<ToolUsageMethodAttachedFile> attachedFileList = new ArrayList<>();

    /* 댓글 갯수 */
    private long commentDtoCount;

    /* 댓글 */
    private List<ToolUsageMethodCommentDto> commentDtoList = new ArrayList<>();
}