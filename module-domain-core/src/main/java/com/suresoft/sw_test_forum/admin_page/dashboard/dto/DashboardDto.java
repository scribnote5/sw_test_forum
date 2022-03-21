package com.suresoft.sw_test_forum.admin_page.dashboard.dto;

import com.suresoft.sw_test_forum.admin_page.data_history.dto.DataHistoryDto;
import com.suresoft.sw_test_forum.admin_page.login_history.dto.LoginHistoryDto;
import com.suresoft.sw_test_forum.admin_page.notice.dto.NoticeDto;
import com.suresoft.sw_test_forum.admin_page.setting.dto.SettingDto;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.dto.CommonDto;
import com.suresoft.sw_test_forum.util.DiskUtil;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@ToString
public class DashboardDto extends CommonDto {
    // 사용자 수
    private long usersCount;
    // 게시글 수
    private long postsCount;

    // 통계: 접속 회원 수
    private long loginUsersCountBeforeYesterday;
    private long loginUsersCountBeforeWeek;
    private long loginUsersCountBeforeMonth;

    // 통계: 등록된 게시글 수
    private long postsCountBeforeYesterday;
    private long postsCountBeforeWeek;
    private long postsCountBeforeMonth;

    // 메모리 공간
    private List<DiskUtil> diskUtilList;

    // 등록된 정적시험 규칙 개수 및 가이드라인 개수
    private long misraCPostsCount;
    private long misraCppPostsCount;
    private long cwePostsCount;
    private long misraCGuidelinePostsCount;
    private long misraCppGuidelinePostsCount;
    private long cweGuidelinePostsCount;

    // 설정
    private SettingDto settingDto;
    // 공지사항
    private List<NoticeDto> noticeDtoList;
    // 로그인 기록
    private List<LoginHistoryDto> loginHistoryDtoList;
    // 데이터 기록
    private List<DataHistoryDto> dataHistoryDtoList;

    @Builder
    public DashboardDto(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                        long usersCount,
                        long postsCount,
                        long loginUsersCountBeforeYesterday,
                        long loginUsersCountBeforeWeek,
                        long loginUsersCountBeforeMonth,
                        long postsCountBeforeYesterday,
                        long postsCountBeforeWeek,
                        long postsCountBeforeMonth,
                        List<DiskUtil> diskUtilList,
                        long misraCPostsCount,
                        long misraCppPostsCount,
                        long cwePostsCount,
                        long misraCGuidelinePostsCount,
                        long misraCppGuidelinePostsCount,
                        long cweGuidelinePostsCount,
                        SettingDto settingDto,
                        List<NoticeDto> noticeDtoList,
                        List<LoginHistoryDto> loginHistoryDtoList,
                        List<DataHistoryDto> dataHistoryDtoList) {
        setIdx(idx);
        setCreatedDate(createdDate);
        setLastModifiedDate(lastModifiedDate);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.usersCount = usersCount;
        this.postsCount = postsCount;
        this.loginUsersCountBeforeYesterday = loginUsersCountBeforeYesterday;
        this.loginUsersCountBeforeWeek = loginUsersCountBeforeWeek;
        this.loginUsersCountBeforeMonth = loginUsersCountBeforeMonth;
        this.postsCountBeforeYesterday = postsCountBeforeYesterday;
        this.postsCountBeforeWeek = postsCountBeforeWeek;
        this.postsCountBeforeMonth = postsCountBeforeMonth;
        this.diskUtilList = diskUtilList;
        this.misraCPostsCount = misraCPostsCount;
        this.misraCppPostsCount = misraCppPostsCount;
        this.cwePostsCount = cwePostsCount;
        this.misraCGuidelinePostsCount = misraCGuidelinePostsCount;
        this.misraCppGuidelinePostsCount = misraCppGuidelinePostsCount;
        this.cweGuidelinePostsCount = cweGuidelinePostsCount;
        this.settingDto = settingDto;
        this.noticeDtoList = noticeDtoList;
        this.loginHistoryDtoList = loginHistoryDtoList;
        this.dataHistoryDtoList = dataHistoryDtoList;
    }
}