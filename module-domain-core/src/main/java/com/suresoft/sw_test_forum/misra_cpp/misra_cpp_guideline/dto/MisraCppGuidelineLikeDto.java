package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto;

import com.suresoft.sw_test_forum.common.dto.CommonDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MisraCppGuidelineLikeDto extends CommonDto {
    private long misraCppGuidelineIdx;

    private boolean isLike;

    private long likeCount;
}