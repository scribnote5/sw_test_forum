package com.suresoft.sw_test_forum.misra_c.misra_c_example.dto;

import com.suresoft.sw_test_forum.common.dto.CommonDto;
import com.suresoft.sw_test_forum.common.validation.Editor;
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
@NoArgsConstructor
@ToString
public class MisraCExampleDto extends CommonDto {
    private long misraCIdx;

    @NotBlank(message = "'제목'은 공란이 될 수 없습니다.")
    @Size(max = 255, message = "'제목'의 길이는 255 보다 작아야 합니다.")
    private String title;

    @Max(value = 4, message = "'우선순위'는 4 보다 작아야 합니다.")
    @Min(value = 1, message = "'우선순위'는 1 보다 커야 합니다.")
    private long priority;

    @Size(max = 255, message = "'도구 정보 이름'의 길이는 255 보다 작아야 합니다.")
    private String toolName;

    @Size(max = 255, message = "'도구 정보 비고'의 길이는 255 보다 작아야 합니다.")
    private String toolNote;

    @NotNull(message = "'도구 정보 idx'는 NULL이 될 수 없습니다.")
    private long toolInformationIdx;

    @Size(max = 255, message = "'컴파일러 이름'의 길이는 255 보다 작아야 합니다.")
    private String compilerName;

    @Size(max = 255, message = "'컴파일러 비고'의 길이는 255 보다 작아야 합니다.")
    private String compilerNote;

    @NotNull(message = "'컴파일러 정보 idx'는 NULL이 될 수 없습니다.")
    private long compilerInformationIdx;

    @Editor(max = 16777215, message = "'에디터'는 16777215Bytes(16MB)보다 작아야 합니다.")
    private String content;

    @Editor(max = 16777215, message = "'에디터'는 16777215Bytes(16MB)보다 작아야 합니다.")
    private String nonCompliantExample;

    @Editor(max = 16777215, message = "'에디터'는 16777215Bytes(16MB)보다 작아야 합니다.")
    private String compliantExample;

    @Size(max = 255, message = "'Bad Case 위치 리스트'의 길이는 255 보다 작아야 합니다.")
    private String badCasePositionList;

    @Size(max = 255, message = "'Good Case 위치 리스트'의 길이는 255 보다 작아야 합니다.")
    private String goodCasePositionList;

    /* autoComplete */
    Set<String> autoCompleteTitle = new HashSet<>();
    Set<String> autoCompleteToolName = new HashSet<>();
    Set<String> autoCompleteToolNote = new HashSet<>();
    Set<String> autoCompleteCompilerName = new HashSet<>();
    Set<String> autoCompleteCompilerNote = new HashSet<>();

    /* 규칙 정보: number + title */
    private String misraCRule;

    /* 댓글 갯수 */
    private long commentDtoCount;

    /* 댓글 */
    private List<MisraCExampleCommentDto> commentDtoList = new ArrayList<>();
}
