package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto;

import com.suresoft.sw_test_forum.common.dto.CommonDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class FxCopGuidelineLikeDto extends CommonDto {
    private long fxCopGuidelineIdx;

    private boolean isLike;

    private long likeCount;
}