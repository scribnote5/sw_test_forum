package com.suresoft.sw_test_forum.metric.metric.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.metric.metric.domain.MetricComment;
import com.suresoft.sw_test_forum.metric.metric.dto.MetricCommentDto;
import com.suresoft.sw_test_forum.metric.metric.dto.MetricDto;
import com.suresoft.sw_test_forum.metric.metric.dto.mapper.MetricCommentMapper;
import com.suresoft.sw_test_forum.metric.metric.dto.mapper.MetricMapper;
import com.suresoft.sw_test_forum.metric.metric.repository.MetricCommentRepository;
import com.suresoft.sw_test_forum.metric.metric.repository.MetricCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MetricCommentService {
    private final MetricCommentRepository metricCommentRepository;
    private final MetricCommentRepositoryImpl metricCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public MetricCommentService(MetricCommentRepository metricCommentRepository,
                                MetricCommentRepositoryImpl metricCommentRepositoryImpl,
                                UserRepositoryImpl userRepositoryImpl) {
        this.metricCommentRepository = metricCommentRepository;
        this.metricCommentRepositoryImpl = metricCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param metricDto
     * @return
     */
    public MetricDto findAllByMetricIdxOrderByIdxDesc(MetricDto metricDto) {
        List<MetricCommentDto> metricCommentDtoList = MetricCommentMapper.INSTANCE.toDto(metricCommentRepository.findAllByMetricIdxOrderByIdxDesc(metricDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (MetricCommentDto metricCommentDto : metricCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(metricCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            metricCommentDto.setNewIcon(NewIconCheck.isNew(metricCommentDto.getCreatedDate()));
            metricCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            metricCommentDto.setCreatedByUser(createdByUser);
        }

        metricDto = MetricMapper.INSTANCE.toDtoByCommentList(metricDto, metricCommentDtoList);

        return metricDto;
    }

    /**
     * 등록
     *
     * @param MetricCommentDto
     * @return
     */
    public long insertMetricComment(MetricCommentDto MetricCommentDto) {
        return metricCommentRepository.save(MetricCommentMapper.INSTANCE.toEntity(MetricCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param MetricCommentDto
     * @return
     */
    @Transactional
    public long updateMetricComment(long idx, MetricCommentDto MetricCommentDto) {
        MetricComment persistMetricComment = metricCommentRepository.getReferenceById(idx);
        MetricComment MetricComment = MetricCommentMapper.INSTANCE.toEntity(MetricCommentDto);

        persistMetricComment.update(MetricComment);

        return metricCommentRepository.save(persistMetricComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteMetricCommentByIdx(long idx) {
        metricCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param metricIdx
     */
    public void deleteAllByMetricIdx(long metricIdx) {
        metricCommentRepositoryImpl.deleteAllByMetricIdx(metricIdx);
    }
}