package com.suresoft.sw_test_forum.metric.metric_example.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.metric.metric_example.domain.MetricExampleComment;
import com.suresoft.sw_test_forum.metric.metric_example.dto.MetricExampleCommentDto;
import com.suresoft.sw_test_forum.metric.metric_example.dto.MetricExampleDto;
import com.suresoft.sw_test_forum.metric.metric_example.dto.mapper.MetricExampleCommentMapper;
import com.suresoft.sw_test_forum.metric.metric_example.dto.mapper.MetricExampleMapper;
import com.suresoft.sw_test_forum.metric.metric_example.repository.MetricExampleCommentRepository;
import com.suresoft.sw_test_forum.metric.metric_example.repository.MetricExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MetricExampleCommentService {
    private final MetricExampleCommentRepository metricExampleCommentRepository;
    private final MetricExampleCommentRepositoryImpl metricExampleCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public MetricExampleCommentService(MetricExampleCommentRepository metricExampleCommentRepository,
                                       MetricExampleCommentRepositoryImpl metricExampleCommentRepositoryImpl,
                                       UserRepositoryImpl userRepositoryImpl) {
        this.metricExampleCommentRepository = metricExampleCommentRepository;
        this.metricExampleCommentRepositoryImpl = metricExampleCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * ????????? ??????
     *
     * @param metricExampleDto
     * @return
     */
    public MetricExampleDto findAllByMetricExampleIdxOrderByIdxDesc(MetricExampleDto metricExampleDto) {
        List<MetricExampleCommentDto> metricExampleCommentDtoList = MetricExampleCommentMapper.INSTANCE.toDto(metricExampleCommentRepository.findAllByMetricExampleIdxOrderByIdxDesc(metricExampleDto.getIdx()));

        // NewIcon ??????, createdBy ??????
        for (MetricExampleCommentDto metricExampleCommentDto : metricExampleCommentDtoList) {
            // ?????? ??????
            // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userRepositoryImpl.findByIdx(metricExampleCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("?????? ??????")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            metricExampleCommentDto.setNewIcon(NewIconCheck.isNew(metricExampleCommentDto.getCreatedDate()));
            metricExampleCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            metricExampleCommentDto.setCreatedByUser(createdByUser);
        }

        metricExampleDto = MetricExampleMapper.INSTANCE.toDtoByCommentList(metricExampleDto, metricExampleCommentDtoList);

        return metricExampleDto;
    }

    /**
     * ??????
     *
     * @param MetricExampleCommentDto
     * @return
     */
    public long insertMetricExampleComment(MetricExampleCommentDto MetricExampleCommentDto) {
        return metricExampleCommentRepository.save(MetricExampleCommentMapper.INSTANCE.toEntity(MetricExampleCommentDto)).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     * @param MetricExampleCommentDto
     * @return
     */
    @Transactional
    public long updateMetricExampleComment(long idx, MetricExampleCommentDto MetricExampleCommentDto) {
        MetricExampleComment persistMetricExampleComment = metricExampleCommentRepository.getById(idx);
        MetricExampleComment MetricExampleComment = MetricExampleCommentMapper.INSTANCE.toEntity(MetricExampleCommentDto);

        persistMetricExampleComment.update(MetricExampleComment);

        return metricExampleCommentRepository.save(persistMetricExampleComment).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     */
    public void deleteMetricExampleCommentByIdx(long idx) {
        metricExampleCommentRepository.deleteById(idx);
    }

    /**
     * ?????? ??????
     *
     * @param metricExampleIdx
     */
    public void deleteAllByMetricExampleIdx(long metricExampleIdx) {
        metricExampleCommentRepositoryImpl.deleteAllByMetricExampleIdx(metricExampleIdx);
    }
}