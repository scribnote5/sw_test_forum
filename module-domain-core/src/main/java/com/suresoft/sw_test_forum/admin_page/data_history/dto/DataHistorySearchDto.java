package com.suresoft.sw_test_forum.admin_page.data_history.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DataHistorySearchDto {
    private String searchType = "";
    private String searchKeyword = "";
}
