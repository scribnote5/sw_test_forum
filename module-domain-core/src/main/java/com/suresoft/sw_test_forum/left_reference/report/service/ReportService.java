package com.suresoft.sw_test_forum.left_reference.report.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.left_reference.report.domain.Report;
import com.suresoft.sw_test_forum.left_reference.report.dto.ReportDto;
import com.suresoft.sw_test_forum.left_reference.report.dto.ReportSearchDto;
import com.suresoft.sw_test_forum.left_reference.report.dto.mapper.ReportMapper;
import com.suresoft.sw_test_forum.left_reference.report.repository.ReportCommentRepositoryImpl;
import com.suresoft.sw_test_forum.left_reference.report.repository.ReportRepository;
import com.suresoft.sw_test_forum.left_reference.report.repository.ReportRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ReportService {
    private final ReportRepository reportRepository;
    private final ReportRepositoryImpl reportRepositoryImpl;
    private final ReportCommentRepositoryImpl reportCommentRepositoryImpl;
    private final UserService userService;
    @Value("${module.name}")
    private String moduleName;

    public ReportService(ReportRepository reportRepository, ReportRepositoryImpl reportRepositoryImpl, ReportCommentRepositoryImpl reportCommentRepositoryImpl, UserService userService) {
        this.reportRepository = reportRepository;
        this.reportRepositoryImpl = reportRepositoryImpl;
        this.reportCommentRepositoryImpl = reportCommentRepositoryImpl;
        this.userService = userService;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<ReportDto> findAllByHighPriorityAsc() {
        List<ReportDto> reportDtoList = reportRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (ReportDto reportDto : reportDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(reportDto.getCreatedByIdx());

            reportDto.setNewIcon(NewIconCheck.isNew(reportDto.getCreatedDate()));
            reportDto.setCreatedByUser(createdByUser);
            reportDto.setCommentDtoCount(reportCommentRepositoryImpl.countAllByReportIdx(reportDto.getIdx()));
        }

        return reportDtoList;
    }

    /**
     * 우선순위가 낮은 리스트 조회
     *
     * @param pageable
     * @param reportSearchDto
     * @return
     */
    public Page<ReportDto> findReportList(Pageable pageable, ReportSearchDto reportSearchDto) {
        Page<ReportDto> reportDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        reportDtoList = reportRepositoryImpl.findAll(pageable, reportSearchDto);

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (ReportDto reportDto : reportDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(reportDto.getCreatedByIdx());

            reportDto.setNewIcon(NewIconCheck.isNew(reportDto.getCreatedDate()));
            reportDto.setCreatedByUser(createdByUser);
            reportDto.setCommentDtoCount(reportCommentRepositoryImpl.countAllByReportIdx(reportDto.getIdx()));
        }

        return reportDtoList;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenWrite() {
        List<ReportDto> highPriorityList = reportRepositoryImpl.findAllByHighPriorityAsc();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (ReportDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public ReportDto findReportAutoComplete(ReportDto ReportDto) {
        // title 설정
        for (String title : reportRepositoryImpl.findDistinctTitle()) {
            ReportDto.getAutoCompleteTitle().add(title);
        }

        return ReportDto;
    }

    /**
     * 등록
     *
     * @param reportDto
     * @return
     */
    public long insertReport(ReportDto reportDto) {
        return reportRepository.save(ReportMapper.INSTANCE.toEntity(reportDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public ReportDto findReportByIdx(long idx) {
        ReportDto reportDto = ReportMapper.INSTANCE.toDto(reportRepository.findById(idx).orElse(new Report()));

        // 권한 설정
        // Register: 로그인한 사용자 Register 접근 가능
        if (idx == 0) {
            reportDto.setAccess(true);
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(reportDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(reportDto.getLastModifiedByIdx());

            reportDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            reportDto.setCreatedByUser(createdByUser);
            reportDto.setLastModifiedByUser(lastModifiedByUser);
        }

        reportRepositoryImpl.updateViewsByIdx(idx);
        reportDto.setViews(reportDto.getViews() + 1);

        return reportDto;
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenUpdate(long idx) {
        List<ReportDto> highPriorityList = reportRepositoryImpl.findAllByHighPriorityAsc();
        Report reportPriority = reportRepositoryImpl.findAllByHighPriorityAscCheckPriority(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (ReportDto highPriority : highPriorityList) {
            if (reportPriority.getPriority() == highPriority.getPriority()) {
                priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(false, "지금 설정된 우선순위 입니다.");
            } else {
                priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
            }
        }

        return priorityDtoArray;
    }

    /**
     * 수정
     *
     * @param idx
     * @param reportDto
     * @return
     */
    @Transactional
    public long updateReport(long idx, ReportDto reportDto) {
        Report persistReport = reportRepository.getReferenceById(idx);
        Report report = ReportMapper.INSTANCE.toEntity(reportDto);

        persistReport.update(report);

        return reportRepository.save(persistReport).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteReportByIdx(long idx) {
        reportRepository.deleteById(idx);
    }
}