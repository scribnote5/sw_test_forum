package com.suresoft.sw_test_forum.common.dto;

import lombok.*;

@Getter
@Setter
@ToString
@Builder
@AllArgsConstructor
public class PriorityDto {
    private boolean isDisabled;

    private String note;

}