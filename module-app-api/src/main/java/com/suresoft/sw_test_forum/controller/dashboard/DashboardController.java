package com.suresoft.sw_test_forum.controller.dashboard;

import com.suresoft.sw_test_forum.admin_page.dashboard.dto.DashboardDto;
import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.admin_page.data_history.service.DataHistoryService;
import com.suresoft.sw_test_forum.admin_page.login_history.service.LoginHistoryService;
import com.suresoft.sw_test_forum.admin_page.notice.service.NoticeService;
import com.suresoft.sw_test_forum.admin_page.setting.service.SettingService;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.cwe.cwe.service.CweService;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.service.CweGuidelineService;
import com.suresoft.sw_test_forum.misra_c.misra_c.service.MisraCService;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.service.MisraCGuidelineService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.service.MisraCppService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.service.MisraCppGuidelineService;
import com.suresoft.sw_test_forum.util.DiskUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/dashboards")
public class DashboardController {
    private final UserService userService;
    private final DataHistoryService dataHistoryService;
    private final LoginHistoryService loginHistoryService;
    private final NoticeService noticeService;
    private final SettingService settingService;
    private final MisraCService misraCService;
    private final MisraCppService misraCppService;
    private final CweService cweService;
    private final MisraCGuidelineService misraCGuidelineService;
    private final MisraCppGuidelineService misraCppGuidelineService;
    private final CweGuidelineService cweGuidelineService;

    public DashboardController(UserService userService,
                               DataHistoryService dataHistoryService,
                               LoginHistoryService loginHistoryService,
                               NoticeService noticeService,
                               SettingService settingService,
                               MisraCService misraCService,
                               MisraCppService misraCppService,
                               CweService cweService,
                               MisraCGuidelineService misraCGuidelineService,
                               MisraCppGuidelineService misraCppGuidelineService,
                               CweGuidelineService cweGuidelineService) {
        this.userService = userService;
        this.dataHistoryService = dataHistoryService;
        this.loginHistoryService = loginHistoryService;
        this.noticeService = noticeService;
        this.settingService = settingService;
        this.misraCService = misraCService;
        this.misraCppService = misraCppService;
        this.cweService = cweService;
        this.misraCGuidelineService = misraCGuidelineService;
        this.misraCppGuidelineService = misraCppGuidelineService;
        this.cweGuidelineService = cweGuidelineService;
    }

    /**
     * 대시보드 페이지에서, 데이터 조회
     *
     * @return
     */
    @GetMapping
    public ResponseEntity getCweListByPriorityAsc() {
        DashboardDto dashboardDto = DashboardDto.builder()
                .usersCount(userService.countUser())
                .postsCount((dataHistoryService.countDataHistoryByAuditType(AuditType.INSERT) - dataHistoryService.countDataHistoryByAuditType(AuditType.DELETE)))
                .loginUsersCountBeforeYesterday(loginHistoryService.countLoginHistoryByDays(1))
                .loginUsersCountBeforeWeek(loginHistoryService.countLoginHistoryByDays(7))
                .loginUsersCountBeforeMonth(loginHistoryService.countLoginHistoryByDays(28))
                .postsCountBeforeYesterday(dataHistoryService.countDataHistoryByDays(1))
                .postsCountBeforeWeek(dataHistoryService.countDataHistoryByDays(7))
                .postsCountBeforeMonth(dataHistoryService.countDataHistoryByDays(28))
                .diskUtilList(DiskUtil.getDiskUtil())
                .misraCPostsCount(misraCService.countPosts())
                .misraCppPostsCount(misraCppService.countPosts())
                .cwePostsCount(cweService.countPosts())
                .misraCGuidelinePostsCount(misraCGuidelineService.countPosts())
                .misraCppGuidelinePostsCount(misraCppGuidelineService.countPosts())
                .cweGuidelinePostsCount(cweGuidelineService.countPosts())
                .settingDto(settingService.findSetting())
                .noticeDtoList(noticeService.findTop10())
                .loginHistoryDtoList(loginHistoryService.findTop10())
                .dataHistoryDtoList(dataHistoryService.findTop10())
                .build();

        return new ResponseEntity(dashboardDto, HttpStatus.OK);
    }
}