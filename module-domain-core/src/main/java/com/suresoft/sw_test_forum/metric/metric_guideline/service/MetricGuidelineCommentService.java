package com.suresoft.sw_test_forum.metric.metric_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuidelineComment;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineCommentDto;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineDto;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.mapper.MetricGuidelineCommentMapper;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.mapper.MetricGuidelineMapper;
import com.suresoft.sw_test_forum.metric.metric_guideline.repository.MetricGuidelineCommentRepository;
import com.suresoft.sw_test_forum.metric.metric_guideline.repository.MetricGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MetricGuidelineCommentService {
    private final MetricGuidelineCommentRepository metricGuidelineCommentRepository;
    private final MetricGuidelineCommentRepositoryImpl metricGuidelineCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public MetricGuidelineCommentService(MetricGuidelineCommentRepository metricGuidelineCommentRepository,
                                         MetricGuidelineCommentRepositoryImpl metricGuidelineCommentRepositoryImpl,
                                         UserRepositoryImpl userRepositoryImpl) {
        this.metricGuidelineCommentRepository = metricGuidelineCommentRepository;
        this.metricGuidelineCommentRepositoryImpl = metricGuidelineCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * ????????? ??????
     *
     * @param metricGuidelineDto
     * @return
     */
    public MetricGuidelineDto findAllByMetricGuidelineIdxOrderByIdxDesc(MetricGuidelineDto metricGuidelineDto) {
        List<MetricGuidelineCommentDto> metricGuidelineCommentDtoList = MetricGuidelineCommentMapper.INSTANCE.toDto(metricGuidelineCommentRepository.findAllByMetricGuidelineIdxOrderByIdxDesc(metricGuidelineDto.getIdx()));

        // NewIcon ??????, createdBy ??????
        for (MetricGuidelineCommentDto metricGuidelineCommentDto : metricGuidelineCommentDtoList) {
            // ?????? ??????
            // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userRepositoryImpl.findByIdx(metricGuidelineCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("?????? ??????")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            metricGuidelineCommentDto.setNewIcon(NewIconCheck.isNew(metricGuidelineCommentDto.getCreatedDate()));
            metricGuidelineCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            metricGuidelineCommentDto.setCreatedByUser(createdByUser);
        }

        metricGuidelineDto = MetricGuidelineMapper.INSTANCE.toDtoByCommentList(metricGuidelineDto, metricGuidelineCommentDtoList);

        return metricGuidelineDto;
    }

    /**
     * ??????
     *
     * @param MetricGuidelineCommentDto
     * @return
     */
    public long insertMetricGuidelineComment(MetricGuidelineCommentDto MetricGuidelineCommentDto) {
        return metricGuidelineCommentRepository.save(MetricGuidelineCommentMapper.INSTANCE.toEntity(MetricGuidelineCommentDto)).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     * @param MetricGuidelineCommentDto
     * @return
     */
    @Transactional
    public long updateMetricGuidelineComment(long idx, MetricGuidelineCommentDto MetricGuidelineCommentDto) {
        MetricGuidelineComment persistMetricGuidelineComment = metricGuidelineCommentRepository.getById(idx);
        MetricGuidelineComment MetricGuidelineComment = MetricGuidelineCommentMapper.INSTANCE.toEntity(MetricGuidelineCommentDto);

        persistMetricGuidelineComment.update(MetricGuidelineComment);

        return metricGuidelineCommentRepository.save(persistMetricGuidelineComment).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     */
    public void deleteMetricGuidelineCommentByIdx(long idx) {
        metricGuidelineCommentRepository.deleteById(idx);
    }

    /**
     * ?????? ??????
     *
     * @param idx
     */
    public void deleteAllByMetricGuidelineIdx(long idx) {
        metricGuidelineCommentRepositoryImpl.deleteAllByMetricGuidelineIdx(idx);
    }
}