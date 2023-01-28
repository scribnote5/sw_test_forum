package com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto;

import com.suresoft.sw_test_forum.common.domain.enums.TargetToolName;
import com.suresoft.sw_test_forum.common.domain.enums.ToolCategory;
import com.suresoft.sw_test_forum.common.dto.CommonDto;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.validation.Editor;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain.ToolTroubleShootingAttachedFile;
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
public class ToolTroubleShootingDto extends CommonDto {
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

    @Size(max = 255, message = "'도구 정보 이름'의 길이는 255 보다 작아야 합니다.")
    private String toolName;

    @Size(max = 255, message = "'도구 정보 비고'의 길이는 255 보다 작아야 합니다.")
    private String toolNote;

    @NotNull(message = "'도구 정보 idx'는 NULL이 될 수 없습니다.")
    private long toolInformationIdx;

    @Size(max = 255, message = "'IDE 이름'의 길이는 255 보다 작아야 합니다.")
    private String ideName;

    @NotNull(message = "'IDE 정보 idx'는 NULL이 될 수 없습니다.")
    private long ideInformationIdx;

    @Size(max = 255, message = "'컴파일러 이름'의 길이는 255 보다 작아야 합니다.")
    private String compilerName;

    @Size(max = 255, message = "'컴파일러 비고'의 길이는 255 보다 작아야 합니다.")
    private String compilerNote;

    @NotNull(message = "'컴파일러 정보 idx'는 NULL이 될 수 없습니다.")
    private long compilerInformationIdx;

    @Editor(max = 16777215, message = "'에디터' 크기는 16777215Bytes(16MB) 보다 작아야 합니다.")
    private String content;

    /* autoComplete */
    Set<String> autoCompleteTitle = new HashSet<>();
    Set<String> autoCompleteHashTags = new HashSet<>();
    Set<String> autoCompleteIdeName = new HashSet<>();
    Set<String> autoCompleteToolName = new HashSet<>();
    Set<String> autoCompleteToolNote = new HashSet<>();
    Set<String> autoCompleteCompilerName = new HashSet<>();
    Set<String> autoCompleteCompilerNote = new HashSet<>();

    /* 우선순위 현황 */
    private List<PriorityDto> priorityDtoList = new ArrayList<>();

    /* 첨부 파일 */
    private List<ToolTroubleShootingAttachedFile> attachedFileList = new ArrayList<>();

    /* 댓글 갯수 */
    private long commentDtoCount;

    /* 댓글 */
    private List<ToolTroubleShootingCommentDto> commentDtoList = new ArrayList<>();
}