package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto;

import com.suresoft.sw_test_forum.common.dto.CommonDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class MisraCGuidelineLikeDto extends CommonDto {
    private long misraCGuidelineIdx;

    private boolean isLike;

    private long likeCount;
}