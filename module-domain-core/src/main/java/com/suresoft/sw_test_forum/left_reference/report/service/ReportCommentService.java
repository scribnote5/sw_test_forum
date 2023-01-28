package com.suresoft.sw_test_forum.left_reference.report.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.report.domain.ReportComment;
import com.suresoft.sw_test_forum.left_reference.report.dto.ReportCommentDto;
import com.suresoft.sw_test_forum.left_reference.report.dto.ReportDto;
import com.suresoft.sw_test_forum.left_reference.report.dto.mapper.ReportCommentMapper;
import com.suresoft.sw_test_forum.left_reference.report.dto.mapper.ReportMapper;
import com.suresoft.sw_test_forum.left_reference.report.repository.ReportCommentRepository;
import com.suresoft.sw_test_forum.left_reference.report.repository.ReportCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ReportCommentService {
    private final ReportCommentRepository ReportCommentRepository;
    private final ReportCommentRepositoryImpl ReportCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public ReportCommentService(ReportCommentRepository ReportCommentRepository, ReportCommentRepositoryImpl ReportCommentRepositoryImpl, UserRepositoryImpl userRepositoryImpl) {
        this.ReportCommentRepository = ReportCommentRepository;
        this.ReportCommentRepositoryImpl = ReportCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param reportDto
     * @return
     */
    public ReportDto findAllByReportIdxOrderByIdxDesc(ReportDto reportDto) {
        List<ReportCommentDto> reportCommentDtoList = ReportCommentMapper.INSTANCE.toDto(ReportCommentRepository.findAllByReportIdxOrderByIdxDesc(reportDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (ReportCommentDto reportCommentDto : reportCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(reportCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            reportCommentDto.setNewIcon(NewIconCheck.isNew(reportCommentDto.getCreatedDate()));
            reportCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            reportCommentDto.setCreatedByUser(createdByUser);
        }

        reportDto = ReportMapper.INSTANCE.toDtoByCommentList(reportDto, reportCommentDtoList);

        return reportDto;
    }

    /**
     * 등록
     *
     * @param ReportCommentDto
     * @return
     */
    public long insertReportComment(ReportCommentDto ReportCommentDto) {
        return ReportCommentRepository.save(ReportCommentMapper.INSTANCE.toEntity(ReportCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param ReportCommentDto
     * @return
     */
    @Transactional
    public long updateReportComment(long idx, ReportCommentDto ReportCommentDto) {
        ReportComment persistReportComment = ReportCommentRepository.getReferenceById(idx);
        ReportComment ReportComment = ReportCommentMapper.INSTANCE.toEntity(ReportCommentDto);

        persistReportComment.update(ReportComment);

        return ReportCommentRepository.save(persistReportComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteReportCommentByIdx(long idx) {
        ReportCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param reportIdx
     */
    public void deleteAllByReportIdx(long reportIdx) {
        ReportCommentRepositoryImpl.deleteAllByReportIdx(reportIdx);
    }
}