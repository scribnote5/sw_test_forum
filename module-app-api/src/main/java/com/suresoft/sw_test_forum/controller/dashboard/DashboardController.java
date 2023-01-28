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
import com.suresoft.sw_test_forum.cwe_java.cwe_java.service.CweJavaService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service.CweJavaGuidelineService;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.service.FxCopService;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.service.FxCopGuidelineService;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.service.JavaCodeConventionsService;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.service.JavaCodeConventionsGuidelineService;
import com.suresoft.sw_test_forum.misra_c.misra_c.service.MisraCService;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.service.MisraCGuidelineService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.service.MisraCppService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.service.MisraCppGuidelineService;
import com.suresoft.sw_test_forum.style_cop.style_cop.service.StyleCopService;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.service.StyleCopGuidelineService;
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
    private final JavaCodeConventionsService javaCodeConventionsService;
    private final CweJavaService cweJavaService;
    private final StyleCopService styleCopService;
    private final FxCopService fxCopService;
    private final MisraCGuidelineService misraCGuidelineService;
    private final MisraCppGuidelineService misraCppGuidelineService;
    private final CweGuidelineService cweGuidelineService;
    private final JavaCodeConventionsGuidelineService javaCodeConventionsGuidelineService;
    private final CweJavaGuidelineService cweJavaGuidelineService;
    private final StyleCopGuidelineService styleCopGuidelineService;
    private final FxCopGuidelineService fxCopGuidelineService;

    public DashboardController(UserService userService,
                               DataHistoryService dataHistoryService,
                               LoginHistoryService loginHistoryService,
                               NoticeService noticeService,
                               SettingService settingService,
                               MisraCService misraCService,
                               MisraCppService misraCppService,
                               CweService cweService,
                               JavaCodeConventionsService javaCodeConventionsService,
                               CweJavaService cweJavaService,
                               StyleCopService styleCopService,
                               FxCopService fxCopService,
                               MisraCGuidelineService misraCGuidelineService,
                               MisraCppGuidelineService misraCppGuidelineService,
                               CweGuidelineService cweGuidelineService,
                               JavaCodeConventionsGuidelineService javaCodeConventionsGuidelineService,
                               CweJavaGuidelineService cweJavaGuidelineService,
                               StyleCopGuidelineService styleCopGuidelineService,
                               FxCopGuidelineService fxCopGuidelineService) {
        this.userService = userService;
        this.dataHistoryService = dataHistoryService;
        this.loginHistoryService = loginHistoryService;
        this.noticeService = noticeService;
        this.settingService = settingService;
        this.misraCService = misraCService;
        this.misraCppService = misraCppService;
        this.cweService = cweService;
        this.javaCodeConventionsService = javaCodeConventionsService;
        this.cweJavaService = cweJavaService;
        this.styleCopService = styleCopService;
        this.fxCopService = fxCopService;
        this.misraCGuidelineService = misraCGuidelineService;
        this.misraCppGuidelineService = misraCppGuidelineService;
        this.cweGuidelineService = cweGuidelineService;
        this.javaCodeConventionsGuidelineService = javaCodeConventionsGuidelineService;
        this.cweJavaGuidelineService = cweJavaGuidelineService;
        this.styleCopGuidelineService = styleCopGuidelineService;
        this.fxCopGuidelineService = fxCopGuidelineService;
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
                .javaCodeConventionsPostsCount(javaCodeConventionsService.countPosts())
                .cweJavaPostsCount(cweJavaService.countPosts())
                .styleCopPostsCount(styleCopService.countPosts())
                .fxCopPostsCount(fxCopService.countPosts())
                .misraCGuidelinePostsCount(misraCGuidelineService.countPosts())
                .misraCppGuidelinePostsCount(misraCppGuidelineService.countPosts())
                .cweGuidelinePostsCount(cweGuidelineService.countPosts())
                .javaCodeConventionsGuidelinePostsCount(javaCodeConventionsGuidelineService.countPosts())
                .cweJavaGuidelinePostsCount(cweJavaGuidelineService.countPosts())
                .styleCopGuidelinePostsCount(styleCopGuidelineService.countPosts())
                .fxCopGuidelinePostsCount(fxCopGuidelineService.countPosts())

                .misraCDtoList(misraCService.findTop10ByViews())
                .misraCppDtoList(misraCppService.findTop10ByViews())
                .cweDtoList(cweService.findTop10ByViews())
                .styleCopDtoList(styleCopService.findTop10ByViews())

                .misraCGuidelineDtoList(misraCGuidelineService.findTop10ByViewsAndGuidelinelike())
                .misraCppGuidelineDtoList(misraCppGuidelineService.findTop10ByViewsAndGuidelinelike())
                .cweGuidelineDtoList(cweGuidelineService.findTop10ByViewsAndGuidelinelike())
                .styleCopGuidelineDtoList(styleCopGuidelineService.findTop10ByViewsAndGuidelinelike())

                .settingDto(settingService.findSetting())
                .noticeDtoList(noticeService.findTop10())
                .loginHistoryDtoList(loginHistoryService.findTop10())
                .dataHistoryDtoList(dataHistoryService.findTop10())
                .build();

        return new ResponseEntity(dashboardDto, HttpStatus.OK);
    }
}