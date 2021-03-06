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
     * ????????? ??????
     *
     * @return
     */
    public Page<MetricGuidelineDto> findMetricGuidelineList(Pageable pageable, MetricGuidelineSearchDto metricGuidelineSearchDto) {
        Page<MetricGuidelineDto> metricGuidelineDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        metricGuidelineDtoList = metricGuidelineRepositoryImpl.findAll(pageable, metricGuidelineSearchDto);

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (MetricGuidelineDto metricGuidelineDto : metricGuidelineDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(metricGuidelineDto.getCreatedByIdx());

            metricGuidelineDto.setNewIcon(NewIconCheck.isNew(metricGuidelineDto.getCreatedDate()));
            metricGuidelineDto.setCreatedByUser(createdByUser);
            metricGuidelineDto.setCommentDtoCount(metricGuidelineCommentRepositoryImpl.countAllByMetricGuidelineCIdx(metricGuidelineDto.getIdx()));

            // ????????? ?????? ?????? ???????????? ???????????? ??????(????????? ?????? ??????????????? ???????????? ??????)
            if (metricGuidelineSearchDto.getMetricIdx() == 0) {
                metricGuidelineDto.setMetricRule(metricService.findMetricRuleByIdx(metricGuidelineDto.getMetricIdx()));
            }
        }

        return metricGuidelineDtoList;
    }

    /**
     * ????????? ?????? ????????? ??? ???, ????????? ??????
     *
     * @param metricIdx
     * @param metricDto
     * @return
     */
    public MetricDto findMetricGuidelineList(long metricIdx, MetricDto metricDto) {
        List<MetricGuidelineDto> metricGuidelineDtoList = metricGuidelineRepositoryImpl.findAll(metricIdx);
        // ????????? ?????????????????? ??????
        class MetricGuidelineDtoComparator implements Comparator<MetricGuidelineDto> {
            @Override
            public int compare(MetricGuidelineDto o1, MetricGuidelineDto o2) {
                return (int) o2.getLikeCountInList() - (int) o1.getLikeCountInList();
            }
        }
        Collections.sort(metricGuidelineDtoList, new MetricGuidelineDtoComparator());

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (MetricGuidelineDto metricGuidelineDto : metricGuidelineDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(metricGuidelineDto.getCreatedByIdx());

            metricGuidelineDto.setNewIcon(NewIconCheck.isNew(metricGuidelineDto.getCreatedDate()));
            metricGuidelineDto.setCreatedByUser(createdByUser);
            metricGuidelineDto.setCommentDtoCount(metricGuidelineCommentRepositoryImpl.countAllByMetricGuidelineCIdx(metricGuidelineDto.getIdx()));
        }

        metricDto = MetricMapper.INSTANCE.toDtoByGuideline(metricDto, metricGuidelineDtoList);

        return metricDto;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public MetricGuidelineDto findMetricGuidelineAutoComplete(MetricGuidelineDto metricGuidelineDto) {
        // title ??????
        for (String title : metricGuidelineRepositoryImpl.findDistinctTitle()) {
            metricGuidelineDto.getAutoCompleteTitle().add(title);
        }

        // hashTags ??????
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("metric_guideline")) {
            for (String hashTag : hashTags.split("#")) {
                metricGuidelineDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // projectName ??????
        for (String projectName : projectInformationRepositoryImpl.findDistinctProjectNameByTableName("metric_guideline")) {
            metricGuidelineDto.getAutoCompleteProjectName().add(projectName);
        }

        // toolName ??????
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("metric_guideline")) {
            metricGuidelineDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote ??????
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("metric_guideline")) {
            metricGuidelineDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName ??????
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("metric_guideline")) {
            metricGuidelineDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote ??????
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("metric_guideline")) {
            metricGuidelineDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return metricGuidelineDto;
    }

    /**
     * ??????
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
     * ??????
     *
     * @param idx
     * @return
     */
    public MetricGuidelineDto findMetricGuidelineByIdx(long idx) {
        MetricGuidelineDto metricGuidelineDto = new MetricGuidelineDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            metricGuidelineDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
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
     * ??????
     *
     * @param idx
     * @param metricGuidelineDto
     */
    @Transactional
    public void updateMetricGuideline(long idx, MetricGuidelineDto metricGuidelineDto) {
        MetricGuideline persistMetricGuideline = metricGuidelineRepository.getById(idx);
        MetricGuideline metricGuideline = MetricGuidelineMapper.INSTANCE.toEntity(metricGuidelineDto);
        persistMetricGuideline.update(metricGuideline);
        metricGuidelineRepository.save(persistMetricGuideline);

        HashTags persistHashTags = hashTagsRepository.getById(metricGuidelineDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("metric_guideline")
                .content(metricGuidelineDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        ProjectInformation persistProjectInformation = projectInformationRepository.getById(metricGuidelineDto.getProjectInformationIdx());
        persistProjectInformation.update(ProjectInformation.builder()
                .tableName("metric_guideline")
                .projectName(metricGuidelineDto.getProjectName())
                .build());
        projectInformationRepository.save(persistProjectInformation);

        ToolInformation persistToolInformation = toolInformationRepository.getById(metricGuidelineDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("metric_guideline")
                .toolName(metricGuidelineDto.getToolName())
                .toolNote(metricGuidelineDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getById(metricGuidelineDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("metric_guideline")
                .compilerName(metricGuidelineDto.getCompilerName())
                .compilerNote(metricGuidelineDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * ??????
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
     * ????????? ?????? ????????? ??? ???, ????????? ?????? ????????? ??????
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