package com.suresoft.sw_test_forum.metric.metric.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepository;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepositoryImpl;
import com.suresoft.sw_test_forum.metric.metric.domain.Metric;
import com.suresoft.sw_test_forum.metric.metric.dto.MetricDto;
import com.suresoft.sw_test_forum.metric.metric.dto.MetricSearchDto;
import com.suresoft.sw_test_forum.metric.metric.dto.mapper.MetricMapper;
import com.suresoft.sw_test_forum.metric.metric.repository.MetricCommentRepositoryImpl;
import com.suresoft.sw_test_forum.metric.metric.repository.MetricRepository;
import com.suresoft.sw_test_forum.metric.metric.repository.MetricRepositoryImpl;
import com.suresoft.sw_test_forum.metric.metric_example.dto.MetricExampleDto;
import com.suresoft.sw_test_forum.metric.metric_example.repository.MetricExampleRepository;
import com.suresoft.sw_test_forum.metric.metric_example.service.MetricExampleCommentService;
import com.suresoft.sw_test_forum.metric.metric_example.service.MetricExampleService;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineDto;
import com.suresoft.sw_test_forum.metric.metric_guideline.repository.MetricGuidelineRepository;
import com.suresoft.sw_test_forum.metric.metric_guideline.service.MetricGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.metric.metric_guideline.service.MetricGuidelineCommentService;
import com.suresoft.sw_test_forum.metric.metric_guideline.service.MetricGuidelineLikeService;
import com.suresoft.sw_test_forum.metric.metric_guideline.service.MetricGuidelineService;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MetricService {
    private final MetricRepository metricRepository;
    private final MetricRepositoryImpl metricRepositoryImpl;
    private final MetricCommentRepositoryImpl metricCommentRepositoryImpl;
    private final MetricExampleRepository metricExampleRepository;
    private final MetricGuidelineRepository metricGuidelineRepository;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final MetricExampleService metricExampleService;
    private final MetricExampleCommentService metricExampleCommentService;
    private final MetricGuidelineService metricGuidelineService;
    private final MetricGuidelineAttachedFileService metricGuidelineAttachedFileService;
    private final MetricGuidelineCommentService metricGuidelineCommentService;
    private final MetricGuidelineLikeService metricGuidelineLikeService;
    private final UserService userService;
    @Value("${module.name}")
    private String moduleName;

    public MetricService(MetricRepository metricRepository,
                         MetricRepositoryImpl metricRepositoryImpl,
                         MetricCommentRepositoryImpl metricCommentRepositoryImpl,
                         MetricExampleRepository metricExampleRepository,
                         MetricGuidelineRepository metricGuidelineRepository,
                         HashTagsRepository hashTagsRepository,
                         HashTagsRepositoryImpl hashTagsRepositoryImpl,
                         @Lazy MetricExampleService metricExampleService,
                         MetricExampleCommentService metricExampleCommentService,
                         @Lazy MetricGuidelineService metricGuidelineService,
                         MetricGuidelineAttachedFileService metricGuidelineAttachedFileService,
                         MetricGuidelineCommentService metricGuidelineCommentService,
                         MetricGuidelineLikeService metricGuidelineLikeService,
                         UserService userService) {
        this.metricRepository = metricRepository;
        this.metricRepositoryImpl = metricRepositoryImpl;
        this.metricCommentRepositoryImpl = metricCommentRepositoryImpl;
        this.metricExampleRepository = metricExampleRepository;
        this.metricGuidelineRepository = metricGuidelineRepository;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.metricExampleService = metricExampleService;
        this.metricExampleCommentService = metricExampleCommentService;
        this.metricGuidelineService = metricGuidelineService;
        this.metricGuidelineAttachedFileService = metricGuidelineAttachedFileService;
        this.metricGuidelineCommentService = metricGuidelineCommentService;
        this.metricGuidelineLikeService = metricGuidelineLikeService;
        this.userService = userService;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<MetricDto> findAllByHighPriorityAsc() {
        List<MetricDto> metricDtoList = metricRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (MetricDto metricDto : metricDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(metricDto.getCreatedByIdx());

            metricDto.setNewIcon(NewIconCheck.isNew(metricDto.getCreatedDate()));
            metricDto.setCreatedByUser(createdByUser);
            metricDto.setCommentDtoCount(metricCommentRepositoryImpl.countAllByMetricIdx(metricDto.getIdx()));
        }

        return metricDtoList;
    }

    /**
     * 우선순위가 낮은 리스트 조회
     *
     * @param pageable
     * @param metricSearchDto
     * @return
     */
    public Page<MetricDto> findAll(Pageable pageable, MetricSearchDto metricSearchDto) {
        Page<MetricDto> metricDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        metricDtoList = metricRepositoryImpl.findAll(pageable, metricSearchDto);

        // NewIcon 판별, createdBy, comment 갯수 설정
        for (MetricDto metricDto : metricDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(metricDto.getCreatedByIdx());

            metricDto.setNewIcon(NewIconCheck.isNew(metricDto.getCreatedDate()));
            metricDto.setCreatedByUser(createdByUser);
            metricDto.setCommentDtoCount(metricCommentRepositoryImpl.countAllByMetricIdx(metricDto.getIdx()));
        }

        return metricDtoList;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<MetricDto> highPriorityList = metricRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (MetricDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public MetricDto findMetricAutoComplete(MetricDto metricDto) {
        // title 설정
        for (String title : metricRepositoryImpl.findDistinctTitle()) {
            metricDto.getAutoCompleteTitle().add(title);
        }

        // hashTags 설정
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("metric")) {
            for (String hashTag : hashTags.split("#")) {
                metricDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        return metricDto;
    }

    /**
     * 등록
     *
     * @param metricDto
     * @return
     */
    public long insertMetric(MetricDto metricDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("metric")
                .content(metricDto.getHashTags())
                .build()).getIdx();

        metricDto.setHashTagsIdx(hashTagsIdx);

        return metricRepository.save(MetricMapper.INSTANCE.toEntity(metricDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public MetricDto findMetricByIdx(long idx) {
        MetricDto metricDto = new MetricDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            metricDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            metricDto = metricRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(metricDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(metricDto.getLastModifiedByIdx());

            metricDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            metricDto.setCreatedByUser(createdByUser);
            metricDto.setLastModifiedByUser(lastModifiedByUser);

            metricRepositoryImpl.updateViewsByIdx(idx);
            metricDto.setViews(metricDto.getViews() + 1);
        }

        return metricDto;
    }

    /**
     * 메트릭 규칙 규칙명 조회
     *
     * @param idx
     * @return
     */
    public String findMetricRuleByIdx(long idx) {
        return metricRepositoryImpl.findMetricByIdx(idx).getTitle();
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<MetricDto> highPriorityList = metricRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        Metric metricPriority = metricRepositoryImpl.findMetricPriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (MetricDto highPriority : highPriorityList) {
            if (metricPriority.getPriority() == highPriority.getPriority()) {
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
     * @param metricDto
     * @return
     */
    @Transactional
    public void updateMetric(long idx, MetricDto metricDto) {
        Metric persistMetric = metricRepository.getReferenceById(idx);
        Metric metric = MetricMapper.INSTANCE.toEntity(metricDto);
        persistMetric.update(metric);
        metricRepository.save(persistMetric);

        HashTags persistHashTags = hashTagsRepository.getReferenceById(metricDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("metric")
                .content(metricDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    @Transactional
    public void deleteRelatedMetricByIdx(long idx) throws Exception {
        MetricDto metricDto = metricRepositoryImpl.findByIdx(idx);
        metricDto = metricExampleService.findMetricExampleListWhenDelete(idx, metricDto);
        metricDto = metricGuidelineService.findMetricGuidelineListWhenDelete(idx, metricDto);

        // 삭제
        metricRepository.deleteById(idx);
        metricExampleRepository.deleteAllByMetricIdx(idx);
        metricGuidelineRepository.deleteAllByMetricIdx(idx);
        hashTagsRepository.deleteById(metricDto.getHashTagsIdx());

        // example 연관 데이터 삭제
        for (MetricExampleDto exampleDto : metricDto.getMetricExampleDtoList()) {
            metricExampleCommentService.deleteAllByMetricExampleIdx(exampleDto.getIdx());
        }

        // guideline 연관 데이터 삭제
        for (MetricGuidelineDto guidelineDto : metricDto.getMetricGuidelineDtoList()) {
            metricGuidelineAttachedFileService.deleteAllAttachedFile(guidelineDto.getIdx());
            metricGuidelineLikeService.deleteAllByMetricGuidelineIdx(guidelineDto.getIdx());
            metricGuidelineCommentService.deleteAllByMetricGuidelineIdx(guidelineDto.getIdx());
        }
    }
}