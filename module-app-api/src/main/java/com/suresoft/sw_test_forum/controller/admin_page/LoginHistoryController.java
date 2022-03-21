package com.suresoft.sw_test_forum.controller.admin_page;

import com.suresoft.sw_test_forum.admin_page.user.dto.TokenDto;
import com.suresoft.sw_test_forum.admin_page.login_history.domain.enums.LoginResultType;
import com.suresoft.sw_test_forum.admin_page.login_history.dto.LoginHistoryDto;
import com.suresoft.sw_test_forum.admin_page.login_history.dto.LoginHistorySearchDto;
import com.suresoft.sw_test_forum.admin_page.login_history.service.LoginHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/login-historys")
public class LoginHistoryController {
    private final LoginHistoryService loginHistoryService;

    public LoginHistoryController(LoginHistoryService loginHistoryService) {
        this.loginHistoryService = loginHistoryService;
    }

    /**
     * 리스트 페이지에서, 조회
     *
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getLoginHistoryList(Pageable pageable, LoginHistorySearchDto loginHistorySearchDto) {
        Page<LoginHistoryDto> loginHistoryDtoList = loginHistoryService.findLoginHistoryList(pageable, loginHistorySearchDto);

        return new ResponseEntity(loginHistoryDtoList, HttpStatus.OK);
    }

    /**
     * 로그인 페이지에서, 등록
     *
     * @param loginHistoryDto
     * @return
     */
    @PostMapping
    public ResponseEntity<TokenDto> postLoginHistory(@RequestBody LoginHistoryDto loginHistoryDto) {
        if (loginHistoryDto.getLoginResultType() == LoginResultType.SUCCESS) {
            loginHistoryService.saveLoginHistory(loginHistoryDto.getLoginUsername());
        } else {
            loginHistoryService.saveFailLoginHistory(loginHistoryDto.getLoginUsername());
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/read/{idx}"})
    public ResponseEntity getLoginHistoryWhenRead(@PathVariable("idx") long idx) {
        LoginHistoryDto loginHistoryDto = loginHistoryService.findLoginHistoryByIdx(idx);

        return new ResponseEntity(loginHistoryDto, HttpStatus.OK);
    }
}