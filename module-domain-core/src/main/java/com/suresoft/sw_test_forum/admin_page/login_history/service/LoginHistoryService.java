package com.suresoft.sw_test_forum.admin_page.login_history.service;

import com.suresoft.sw_test_forum.admin_page.login_history.domain.LoginHistory;
import com.suresoft.sw_test_forum.admin_page.login_history.domain.enums.LoginResultType;
import com.suresoft.sw_test_forum.admin_page.login_history.dto.LoginHistoryDto;
import com.suresoft.sw_test_forum.admin_page.login_history.dto.LoginHistorySearchDto;
import com.suresoft.sw_test_forum.admin_page.login_history.repository.LoginHistoryRepository;
import com.suresoft.sw_test_forum.admin_page.login_history.repository.LoginHistoryRepositoryImpl;
import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.util.AuditMessageUtil;
import com.suresoft.sw_test_forum.util.IpUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class LoginHistoryService {
    private final LoginHistoryRepository loginHistoryRepository;
    private final LoginHistoryRepositoryImpl loginHistoryRepositoryImpl;
    private final GeoLocationService geoLocationService;
    private final UserService userService;

    public LoginHistoryService(LoginHistoryRepository loginHistoryRepository,
                               LoginHistoryRepositoryImpl loginHistoryRepositoryImpl,
                               GeoLocationService geoLocationService,
                               UserService userService) {
        this.loginHistoryRepository = loginHistoryRepository;
        this.loginHistoryRepositoryImpl = loginHistoryRepositoryImpl;
        this.geoLocationService = geoLocationService;
        this.userService = userService;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param loginHistorySearchDto
     * @return
     */
    public Page<LoginHistoryDto> findLoginHistoryList(Pageable pageable, LoginHistorySearchDto loginHistorySearchDto) {
        Page<LoginHistoryDto> loginHistoryDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        loginHistoryDtoList = loginHistoryRepositoryImpl.findAll(pageable, loginHistorySearchDto);

        // NewIcon 판별, createdBy 설정
        for (LoginHistoryDto loginHistoryDto : loginHistoryDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(loginHistoryDto.getCreatedByIdx());

            loginHistoryDto.setNewIcon(NewIconCheck.isNew(loginHistoryDto.getCreatedDate()));
            loginHistoryDto.setCreatedByUser(createdByUser);
        }

        return loginHistoryDtoList;
    }

    /**
     * 최근에 등록된 10개 리스트 조회
     *
     * @return
     */
    public List<LoginHistoryDto> findTop10() {
        return loginHistoryRepositoryImpl.findTop10();
    }

    /**
     * 로그인 실패 했을 때, 등록
     *
     * @param username
     */
    public void saveFailLoginHistory(String username) {
        // ip 주소: ip 주소를 받기 위한 HttpServletRequest 객체
        String ip = IpUtil.getClientIP(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());
        // GeoLite2를 사용한 location 조회
        String location = geoLocationService.getLocationByIp(ip);

        loginHistoryRepository.save(LoginHistory.builder()
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .createdByIdx(1)
                .lastModifiedByIdx(1)
                .activeStatus(ActiveStatus.ACTIVE)

                .loginUsername(username)
                .ip(ip)
                .location(location)
                .message(AuditMessageUtil.getLoginFailureMessage(username))
                .detailedMessage("로그인 실패")
                .loginResultType(LoginResultType.FAIL).build());
    }

    /**
     * 로그인 성공 했을 때, 등록
     *
     * @param username
     */
    public void saveLoginHistory(String username) {
        // ip 주소: ip 주소를 받기 위한 HttpServletRequest 객체
        String ip = IpUtil.getClientIP(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());
        // GeoLite2를 사용한 location 조회
        String location = geoLocationService.getLocationByIp(ip);

        loginHistoryRepository.save(LoginHistory.builder()
                .createdDate(LocalDateTime.now())
                .lastModifiedDate(LocalDateTime.now())
                .createdByIdx(1)
                .lastModifiedByIdx(1)
                .activeStatus(ActiveStatus.ACTIVE)

                .loginUsername(username)
                .ip(ip)
                .location(location)
                .message(AuditMessageUtil.getLoginSuccessMessage(username))
                .detailedMessage("로그인 성공")
                .loginResultType(LoginResultType.SUCCESS).build());
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public LoginHistoryDto findLoginHistoryByIdx(long idx) {
        LoginHistoryDto loginHistoryDto = loginHistoryRepositoryImpl.findByIdx(idx);

        User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(loginHistoryDto.getCreatedByIdx());
        User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(loginHistoryDto.getLastModifiedByIdx());

        loginHistoryDto.setCreatedByUser(createdByUser);
        loginHistoryDto.setLastModifiedByUser(lastModifiedByUser);

        loginHistoryRepositoryImpl.updateViewsByIdx(idx);
        loginHistoryDto.setViews(loginHistoryDto.getViews() + 1);

        return loginHistoryDto;
    }

    /**
     * 기간 내의 리스트 개수 조회
     *
     * @param days
     * @return
     */
    public long countLoginHistoryByDays(long days) {
        return loginHistoryRepository.countAllByCreatedDateBetweenAndLoginResultTypeIs(LocalDateTime.now().minusDays(days), LocalDateTime.now(), LoginResultType.SUCCESS);
    }
}