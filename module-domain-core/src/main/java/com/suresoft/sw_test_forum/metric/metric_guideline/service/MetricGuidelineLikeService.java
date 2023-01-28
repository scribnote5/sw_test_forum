package com.suresoft.sw_test_forum.metric.metric_guideline.service;

import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuidelineLike;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineDto;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineLikeDto;
import com.suresoft.sw_test_forum.metric.metric_guideline.repository.MetricGuidelineLikeRepository;
import com.suresoft.sw_test_forum.metric.metric_guideline.repository.MetricGuidelineLikeRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.stereotype.Service;

@Service
public class MetricGuidelineLikeService {
    private final MetricGuidelineLikeRepository metricGuidelineLikeRepository;
    private final MetricGuidelineLikeRepositoryImpl metricGuidelineLikeRepositoryImpl;

    public MetricGuidelineLikeService(MetricGuidelineLikeRepository metricGuidelineLikeRepository,
                                      MetricGuidelineLikeRepositoryImpl metricGuidelineLikeRepositoryImpl) {
        this.metricGuidelineLikeRepository = metricGuidelineLikeRepository;
        this.metricGuidelineLikeRepositoryImpl = metricGuidelineLikeRepositoryImpl;
    }

    /**
     * 좋아요 조회
     *
     * @param metricGuidelineDto
     * @return
     */
    public MetricGuidelineDto findMetricGuidelineLike(MetricGuidelineDto metricGuidelineDto) {
        MetricGuidelineLikeDto metricGuidelineLikeDto = metricGuidelineLikeRepositoryImpl.findByUsernameAndMetricGuidelineIdx(AuthorityUtil.getCurrentUsername(), metricGuidelineDto.getIdx());
        boolean isEmptyMetricGuidelineLikeDto = EmptyUtil.isEmpty(metricGuidelineLikeDto);

        // metricGuidelineLikeDto가 empty 인 경우, 객체 생성
        if (isEmptyMetricGuidelineLikeDto) {
            metricGuidelineLikeDto = new MetricGuidelineLikeDto();
        }

        metricGuidelineLikeDto.setMetricGuidelineIdx(metricGuidelineDto.getIdx());
        metricGuidelineLikeDto.setLike(!isEmptyMetricGuidelineLikeDto);
        // 좋아요 갯수 조회
        metricGuidelineLikeDto.setLikeCount(metricGuidelineLikeRepository.countByMetricGuidelineIdx(metricGuidelineDto.getIdx()));
        metricGuidelineDto.setLikeDto(metricGuidelineLikeDto);

        return metricGuidelineDto;
    }

    /**
     * 등록
     *
     * @param metricGuidelineIdx
     * @return
     */
    public long insertMetricGuidelineLike(long metricGuidelineIdx) {
        return metricGuidelineLikeRepository.save(MetricGuidelineLike.builder()
                .activeStatus(ActiveStatus.ACTIVE)
                .metricGuidelineIdx(metricGuidelineIdx)
                .build()).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteMetricGuidelineLikeByIdx(long idx) {
        metricGuidelineLikeRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param idx
     */
    public void deleteAllByMetricGuidelineIdx(long idx) {
        metricGuidelineLikeRepositoryImpl.deleteAllByMetricGuidelineIdx(idx);
    }
}