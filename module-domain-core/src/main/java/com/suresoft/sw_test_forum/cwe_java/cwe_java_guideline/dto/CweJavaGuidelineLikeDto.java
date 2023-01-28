package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto;

import com.suresoft.sw_test_forum.common.dto.CommonDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CweJavaGuidelineLikeDto extends CommonDto {
    private long cweJavaGuidelineIdx;

    private boolean isLike;

    private long likeCount;
}