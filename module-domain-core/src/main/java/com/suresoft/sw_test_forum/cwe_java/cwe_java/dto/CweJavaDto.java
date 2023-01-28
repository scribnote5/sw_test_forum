package com.suresoft.sw_test_forum.cwe_java.cwe_java.dto;

import com.suresoft.sw_test_forum.common.domain.enums.Frequency;
import com.suresoft.sw_test_forum.common.dto.CommonDto;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.validation.Editor;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.CweJavaAttachedFile;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.CweJavaExampleDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineDto;
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
public class CweJavaDto extends CommonDto {
    @NotBlank(message = "'규칙명'은 공란이 될 수 없습니다.")
    @Size(max = 255, message = "'규칙명'의 길이는 255 보다 작아야 합니다.")
    private String title;

    @Max(value = 6, message = "'우선순위'는 6 보다 작아야 합니다.")
    @Min(value = 1, message = "'우선순위'는 1 보다 커야 합니다.")
    private long priority;

    @NotNull(message = "'위배 빈도수'는 NULL이 될 수 없습니다.")
    private Frequency frequency;

    @Size(max = 255, message = "'해시태그'의 길이는 255 보다 작아야 합니다.")
    private String hashTags;

    @NotNull(message = "'해시태그 idx'는 NULL이 될 수 없습니다.")
    private long hashTagsIdx;

    @Size(max = 255, message = "'STATIC 규칙명'의 길이는 255 보다 작아야 합니다.")
    private String staticTitle;

    @Size(max = 255, message = "'Sparrow 규칙명'의 길이는 255 보다 작아야 합니다.")
    private String sparrowTitle;

    @Editor(max = 16777215, message = "'에디터' 크기는 16777215Bytes(16MB) 보다 작아야 합니다.")
    private String content;

    /* autoComplete */
    Set<String> autoCompleteTitle = new HashSet<>();
    Set<String> autoCompleteHashTags = new HashSet<>();

    /* 우선순위 현황 */
    private List<PriorityDto> priorityDtoList = new ArrayList<>();

    /* 예제 */
    private List<CweJavaExampleDto> cweJavaExampleDtoList = new ArrayList<>();

    /* 가이드라인 */
    private List<CweJavaGuidelineDto> cweJavaGuidelineDtoList = new ArrayList<>();

    /* 첨부 파일 */
    private List<CweJavaAttachedFile> attachedFileList = new ArrayList<>();

    /* 댓글 갯수 */
    private long commentDtoCount;

    /* 댓글 */
    private List<CweJavaCommentDto> commentDtoList = new ArrayList<>();
}