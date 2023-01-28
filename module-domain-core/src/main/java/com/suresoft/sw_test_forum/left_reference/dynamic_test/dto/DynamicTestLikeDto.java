package com.suresoft.sw_test_forum.left_reference.dynamic_test.dto;

import com.suresoft.sw_test_forum.common.dto.CommonDto;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DynamicTestLikeDto extends CommonDto {
    private long dynamicTestIdx;

    private boolean isLike;

    private long likeCount;
}