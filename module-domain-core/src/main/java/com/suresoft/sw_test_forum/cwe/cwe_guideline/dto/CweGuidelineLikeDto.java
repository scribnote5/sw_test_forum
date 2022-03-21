package com.suresoft.sw_test_forum.cwe.cwe_guideline.dto;

import com.suresoft.sw_test_forum.common.dto.CommonDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class CweGuidelineLikeDto extends CommonDto {
    private long cweGuidelineIdx;

    private boolean isLike;

    private long likeCount;
}