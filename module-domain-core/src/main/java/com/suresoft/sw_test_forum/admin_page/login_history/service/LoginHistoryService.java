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
import org.springframework.data.domain.Sort;
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
     * ????????? ??????
     *
     * @param pageable
     * @param loginHistorySearchDto
     * @return
     */
    public Page<LoginHistoryDto> findLoginHistoryList(Pageable pageable, LoginHistorySearchDto loginHistorySearchDto) {
        Page<LoginHistoryDto> loginHistoryDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        loginHistoryDtoList = loginHistoryRepositoryImpl.findAll(pageable, loginHistorySearchDto);

        // NewIcon ??????, createdBy ??????
        for (LoginHistoryDto loginHistoryDto : loginHistoryDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(loginHistoryDto.getCreatedByIdx());

            loginHistoryDto.setNewIcon(NewIconCheck.isNew(loginHistoryDto.getCreatedDate()));
            loginHistoryDto.setCreatedByUser(createdByUser);
        }

        return loginHistoryDtoList;
    }

    /**
     * ????????? ????????? 10??? ????????? ??????
     *
     * @return
     */
    public List<LoginHistoryDto> findTop10() {
        return loginHistoryRepositoryImpl.findTop10();
    }

    /**
     * ????????? ?????? ?????? ???, ??????
     *
     * @param username
     */
    public void saveFailLoginHistory(String username) {
        // ip ??????: ip ????????? ?????? ?????? HttpServletRequest ??????
        String ip = IpUtil.getClientIP(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());
        // GeoLite2??? ????????? location ??????
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
                .detailedMessage("????????? ??????")
                .loginResultType(LoginResultType.FAIL).build());
    }

    /**
     * ????????? ?????? ?????? ???, ??????
     *
     * @param username
     */
    public void saveLoginHistory(String username) {
        // ip ??????: ip ????????? ?????? ?????? HttpServletRequest ??????
        String ip = IpUtil.getClientIP(((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest());
        // GeoLite2??? ????????? location ??????
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
                .detailedMessage("????????? ??????")
                .loginResultType(LoginResultType.SUCCESS).build());
    }

    /**
     * ??????
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
     * ?????? ?????? ????????? ?????? ??????
     *
     * @param days
     * @return
     */
    public long countLoginHistoryByDays(long days) {
        return loginHistoryRepository.countAllByCreatedDateBetweenAndLoginResultTypeIs(LocalDateTime.now().minusDays(days), LocalDateTime.now(), LoginResultType.SUCCESS);
    }
}