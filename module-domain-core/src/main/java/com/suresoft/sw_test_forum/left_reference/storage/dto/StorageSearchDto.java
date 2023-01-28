package com.suresoft.sw_test_forum.left_reference.storage.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class StorageSearchDto {
    private String searchType = "";
    private String searchKeyword = "";
}