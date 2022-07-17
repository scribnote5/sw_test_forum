package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto;

import com.suresoft.sw_test_forum.common.dto.CommonDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class JavaCodeConventionsGuidelineLikeDto extends CommonDto {
    private long javaCodeConventionsGuidelineIdx;

    private boolean isLike;

    private long likeCount;
}