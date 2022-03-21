package com.suresoft.sw_test_forum.admin_page.login_history.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class LoginHistorySearchDto {
    private String searchType = "";
    private String searchKeyword = "";
}