package com.suresoft.sw_test_forum.metric.metric_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.domain.ProjectInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.metric.metric.dto.MetricDto;
import com.suresoft.sw_test_forum.metric.metric.dto.mapper.MetricMapper;
import com.suresoft.sw_test_forum.metric.metric.service.MetricService;
import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuideline;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineDto;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineSearchDto;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.mapper.MetricGuidelineMapper;
import com.suresoft.sw_test_forum.metric.metric_guideline.repository.MetricGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.metric.metric_guideline.repository.MetricGuidelineRepository;
import com.suresoft.sw_test_forum.metric.metric_guideline.repository.MetricGuidelineRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class MetricGuidelineService {
    private final MetricGuidelineRepository metricGuidelineRepository;
    private final MetricGuidelineRepositoryImpl metricGuidelineRepositoryImpl;
    private final MetricGuidelineCommentRepositoryImpl metricGuidelineCommentRepositoryImpl;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final ProjectInformationRepository projectInformationRepository;
    private final ProjectInformationRepositoryImpl projectInformationRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final MetricService metricService;
    @Value("${module.name}")
    private String moduleName;

    public MetricGuidelineService(MetricGuidelineRepository metricGuidelineRepository,
                                  MetricGuidelineRepositoryImpl metricGuidelineRepositoryImpl,
                                  MetricGuidelineCommentRepositoryImpl metricGuidelineCommentRepositoryImpl,
                                  HashTagsRepository hashTagsRepository,
                                  HashTagsRepositoryImpl hashTagsRepositoryImpl,
                                  ProjectInformationRepository projectInformationRepository,
                                  ProjectInformationRepositoryImpl projectInformationRepositoryImpl,
                                  ToolInformationRepository toolInformationRepository,
                                  ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                  CompilerInformationRepository compilerInformationRepository,
                                  CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                  UserService userService,
                                  MetricService metricService) {
        this.metricGuidelineRepository = metricGuidelineRepository;
        this.metricGuidelineRepositoryImpl = metricGuidelineRepositoryImpl;
        this.metricGuidelineCommentRepositoryImpl = metricGuidelineCommentRepositoryImpl;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.projectInformationRepository = projectInformationRepository;
        this.projectInformationRepositoryImpl = projectInformationRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.metricService = metricService;
    }

    /**
     * 리스트 조회
     *
     * @return
     */
    public Page<MetricGuidelineDto> findMetricGuidelineList(Pageable pageable, MetricGuidelineSearchDto metricGuidelineSearchDto) {
        Page<MetricGuidelineDto> metricGuidelineDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        metricGuidelineDtoList = metricGuidelineRepositoryImpl.findAll(pageable, metricGuidelineSearchDto);

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (MetricGuidelineDto metricGuidelineDto : metricGuidelineDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(metricGuidelineDto.getCreatedByIdx());

            metricGuidelineDto.setNewIcon(NewIconCheck.isNew(metricGuidelineDto.getCreatedDate()));
            metricGuidelineDto.setCreatedByUser(createdByUser);
            metricGuidelineDto.setCommentDtoCount(metricGuidelineCommentRepositoryImpl.countAllByMetricGuidelineCIdx(metricGuidelineDto.getIdx()));

            // 메트릭 예제 코드 리스트를 조회하는 경우(메트릭 보기 페이지에서 이동하지 않음)
            if (metricGuidelineSearchDto.getMetricIdx() == 0) {
                metricGuidelineDto.setMetricRule(metricService.findMetricRuleByIdx(metricGuidelineDto.getMetricIdx()));
            }
        }

        return metricGuidelineDtoList;
    }

    /**
     * 메트릭 읽기 페이지 일 때, 리스트 조회
     *
     * @param metricIdx
     * @param metricDto
     * @return
     */
    public MetricDto findMetricGuidelineList(long metricIdx, MetricDto metricDto) {
        List<MetricGuidelineDto> metricGuidelineDtoList = metricGuidelineRepositoryImpl.findAll(metricIdx);
        // 좋아요 내림차순으로 정렬
        class MetricGuidelineDtoComparator implements Comparator<MetricGuidelineDto> {
            @Override
            public int compare(MetricGuidelineDto o1, MetricGuidelineDto o2) {
                return (int) o2.getLikeCountInList() - (int) o1.getLikeCountInList();
            }
        }
        Collections.sort(metricGuidelineDtoList, new MetricGuidelineDtoComparator());

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (MetricGuidelineDto metricGuidelineDto : metricGuidelineDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(metricGuidelineDto.getCreatedByIdx());

            metricGuidelineDto.setNewIcon(NewIconCheck.isNew(metricGuidelineDto.getCreatedDate()));
            metricGuidelineDto.setCreatedByUser(createdByUser);
            metricGuidelineDto.setCommentDtoCount(metricGuidelineCommentRepositoryImpl.countAllByMetricGuidelineCIdx(metricGuidelineDto.getIdx()));
        }

        metricDto = MetricMapper.INSTANCE.toDtoByGuideline(metricDto, metricGuidelineDtoList);

        return metricDto;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public MetricGuidelineDto findMetricGuidelineAutoComplete(MetricGuidelineDto metricGuidelineDto) {
        // title 설정
        for (String title : metricGuidelineRepositoryImpl.findDistinctTitle()) {
            metricGuidelineDto.getAutoCompleteTitle().add(title);
        }

        // hashTags 설정
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("metric_guideline")) {
            for (String hashTag : hashTags.split("#")) {
                metricGuidelineDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // projectName 설정
        for (String projectName : projectInformationRepositoryImpl.findDistinctProjectNameByTableName("metric_guideline")) {
            metricGuidelineDto.getAutoCompleteProjectName().add(projectName);
        }

        // toolName 설정
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("metric_guideline")) {
            metricGuidelineDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote 설정
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("metric_guideline")) {
            metricGuidelineDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName 설정
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("metric_guideline")) {
            metricGuidelineDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote 설정
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("metric_guideline")) {
            metricGuidelineDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return metricGuidelineDto;
    }

    /**
     * 등록
     *
     * @param metricGuidelineDto
     * @return
     */
    public long insertMetricGuideline(MetricGuidelineDto metricGuidelineDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("metric_guideline")
                .content(metricGuidelineDto.getHashTags())
                .build()).getIdx();

        long projectInformationIdx = projectInformationRepository.save(ProjectInformation.builder()
                .tableName("metric_guideline")
                .projectName(metricGuidelineDto.getProjectName())
                .build()).getIdx();

        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("metric_guideline")
                .toolName(metricGuidelineDto.getToolName())
                .toolNote(metricGuidelineDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("metric_guideline")
                .compilerName(metricGuidelineDto.getCompilerName())
                .compilerNote(metricGuidelineDto.getCompilerNote())
                .build()).getIdx();

        metricGuidelineDto.setHashTagsIdx(hashTagsIdx);
        metricGuidelineDto.setProjectInformationIdx(projectInformationIdx);
        metricGuidelineDto.setToolInformationIdx(toolInformationIdx);
        metricGuidelineDto.setCompilerInformationIdx(compilerInformationIdx);

        return metricGuidelineRepository.save(MetricGuidelineMapper.INSTANCE.toEntity(metricGuidelineDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public MetricGuidelineDto findMetricGuidelineByIdx(long idx) {
        MetricGuidelineDto metricGuidelineDto = new MetricGuidelineDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            metricGuidelineDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            metricGuidelineDto = metricGuidelineRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(metricGuidelineDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(metricGuidelineDto.getLastModifiedByIdx());

            metricGuidelineDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            metricGuidelineDto.setCreatedByUser(createdByUser);
            metricGuidelineDto.setLastModifiedByUser(lastModifiedByUser);

            metricGuidelineRepositoryImpl.updateViewsByIdx(idx);
            metricGuidelineDto.setViews(metricGuidelineDto.getViews() + 1);
        }

        return metricGuidelineDto;
    }

    /**
     * 수정
     *
     * @param idx
     * @param metricGuidelineDto
     */
    @Transactional
    public void updateMetricGuideline(long idx, MetricGuidelineDto metricGuidelineDto) {
        MetricGuideline persistMetricGuideline = metricGuidelineRepository.getReferenceById(idx);
        MetricGuideline metricGuideline = MetricGuidelineMapper.INSTANCE.toEntity(metricGuidelineDto);
        persistMetricGuideline.update(metricGuideline);
        metricGuidelineRepository.save(persistMetricGuideline);

        HashTags persistHashTags = hashTagsRepository.getReferenceById(metricGuidelineDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("metric_guideline")
                .content(metricGuidelineDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        ProjectInformation persistProjectInformation = projectInformationRepository.getReferenceById(metricGuidelineDto.getProjectInformationIdx());
        persistProjectInformation.update(ProjectInformation.builder()
                .tableName("metric_guideline")
                .projectName(metricGuidelineDto.getProjectName())
                .build());
        projectInformationRepository.save(persistProjectInformation);

        ToolInformation persistToolInformation = toolInformationRepository.getReferenceById(metricGuidelineDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("metric_guideline")
                .toolName(metricGuidelineDto.getToolName())
                .toolNote(metricGuidelineDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getReferenceById(metricGuidelineDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("metric_guideline")
                .compilerName(metricGuidelineDto.getCompilerName())
                .compilerNote(metricGuidelineDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteMetricGuidelineByIdx(long idx) {
        MetricGuidelineDto metricGuidelineDto = metricGuidelineRepositoryImpl.findByIdx(idx);

        metricGuidelineRepository.deleteById(idx);
        hashTagsRepository.deleteById(metricGuidelineDto.getHashTagsIdx());
        projectInformationRepository.deleteById(metricGuidelineDto.getProjectInformationIdx());
        toolInformationRepository.deleteById(metricGuidelineDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(metricGuidelineDto.getCompilerInformationIdx());
    }

    /**
     * 메트릭 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param metricIdx
     * @param metricDto
     * @return
     */
    public MetricDto findMetricGuidelineListWhenDelete(long metricIdx, MetricDto metricDto) {
        List<MetricGuidelineDto> metricGuidelineDtoList = metricGuidelineRepositoryImpl.findAllWhenDelete(metricIdx);
        metricDto = MetricMapper.INSTANCE.toDtoByGuideline(metricDto, metricGuidelineDtoList);

        return metricDto;
    }
}