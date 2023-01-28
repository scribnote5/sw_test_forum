package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto;

import com.suresoft.sw_test_forum.common.domain.enums.GuidelineResult;
import com.suresoft.sw_test_forum.common.dto.CommonDto;
import com.suresoft.sw_test_forum.common.validation.Editor;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuidelineAttachedFile;
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
@NoArgsConstructor
@ToString
public class StyleCopGuidelineDto extends CommonDto {
    private long styleCopIdx;

    @NotBlank(message = "'제목'은 공란이 될 수 없습니다.")
    @Size(max = 255, message = "'제목'의 길이는 255 보다 작아야 합니다.")
    private String title;

    @Size(max = 255, message = "'해시태그'의 길이는 255 보다 작아야 합니다.")
    private String hashTags;

    @NotNull(message = "'해시태그 idx'는 NULL이 될 수 없습니다.")
    private long hashTagsIdx;

    @Size(max = 255, message = "'프로젝트 이름'의 길이는 255 보다 작아야 합니다.")
    private String projectName;

    @NotNull(message = "'프로젝트 정보 idx'는 NULL이 될 수 없습니다.")
    private long projectInformationIdx;

    @NotNull(message = "'가이드라인 결과'는 NULL이 될 수 없습니다.")
    private GuidelineResult guidelineResult;

    @Size(max = 255, message = "'가이드라인 결과 노트'의 길이는 255 보다 작아야 합니다.")
    private String guidelineResultNote;

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
    Set<String> autoCompleteHashTags = new HashSet<>();
    Set<String> autoCompleteProjectName = new HashSet<>();
    Set<String> autoCompleteToolName = new HashSet<>();
    Set<String> autoCompleteToolNote = new HashSet<>();
    Set<String> autoCompleteCompilerName = new HashSet<>();
    Set<String> autoCompleteCompilerNote = new HashSet<>();

    /* 규칙 정보: number + title */
    private String styleCopRule;

    /* 첨부 파일 */
    private List<StyleCopGuidelineAttachedFile> attachedFileList = new ArrayList<>();

    /* 댓글 갯수 */
    private long commentDtoCount;

    /* 댓글 */
    private List<StyleCopGuidelineCommentDto> commentDtoList = new ArrayList<>();

    /* 좋아요 */
    private StyleCopGuidelineLikeDto likeDto = new StyleCopGuidelineLikeDto();

    /* 리스트 페이지에서, 좋아요 갯수 */
    private long likeCountInList;
}
