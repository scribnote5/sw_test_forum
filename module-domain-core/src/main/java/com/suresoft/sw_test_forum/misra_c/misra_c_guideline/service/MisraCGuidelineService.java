package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.domain.ProjectInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.MisraCDto;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.mapper.MisraCMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c.service.MisraCService;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.MisraCGuideline;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineSearchDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.mapper.MisraCGuidelineMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.repository.MisraCGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.repository.MisraCGuidelineRepository;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.repository.MisraCGuidelineRepositoryImpl;
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
public class MisraCGuidelineService {
    private final MisraCGuidelineRepository misraCGuidelineRepository;
    private final MisraCGuidelineRepositoryImpl misraCGuidelineRepositoryImpl;
    private final MisraCGuidelineCommentRepositoryImpl misraCGuidelineCommentRepositoryImpl;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final ProjectInformationRepository projectInformationRepository;
    private final ProjectInformationRepositoryImpl projectInformationRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final MisraCService misraCService;
    @Value("${module.name}")
    private String moduleName;

    public MisraCGuidelineService(MisraCGuidelineRepository misraCGuidelineRepository,
                                  MisraCGuidelineRepositoryImpl misraCGuidelineRepositoryImpl,
                                  MisraCGuidelineCommentRepositoryImpl misraCGuidelineCommentRepositoryImpl,
                                  HashTagsRepository hashTagsRepository,
                                  HashTagsRepositoryImpl hashTagsRepositoryImpl,
                                  ProjectInformationRepository projectInformationRepository,
                                  ProjectInformationRepositoryImpl projectInformationRepositoryImpl,
                                  ToolInformationRepository toolInformationRepository,
                                  ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                  CompilerInformationRepository compilerInformationRepository,
                                  CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                  UserService userService,
                                  MisraCService misraCService) {
        this.misraCGuidelineRepository = misraCGuidelineRepository;
        this.misraCGuidelineRepositoryImpl = misraCGuidelineRepositoryImpl;
        this.misraCGuidelineCommentRepositoryImpl = misraCGuidelineCommentRepositoryImpl;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.projectInformationRepository = projectInformationRepository;
        this.projectInformationRepositoryImpl = projectInformationRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.misraCService = misraCService;
    }

    /**
     * ????????? ??????
     *
     * @return
     */
    public Page<MisraCGuidelineDto> findMisraCGuidelineList(Pageable pageable, MisraCGuidelineSearchDto misraCGuidelineSearchDto) {
        Page<MisraCGuidelineDto> misraCGuidelineDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        misraCGuidelineDtoList = misraCGuidelineRepositoryImpl.findAll(pageable, misraCGuidelineSearchDto);

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (MisraCGuidelineDto misraCGuidelineDto : misraCGuidelineDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCGuidelineDto.getCreatedByIdx());

            misraCGuidelineDto.setNewIcon(NewIconCheck.isNew(misraCGuidelineDto.getCreatedDate()));
            misraCGuidelineDto.setCreatedByUser(createdByUser);
            misraCGuidelineDto.setCommentDtoCount(misraCGuidelineCommentRepositoryImpl.countAllByMisraCGuidelineCIdx(misraCGuidelineDto.getIdx()));

            // MISRA C ??????????????? idx??? ?????? ?????? ??????
            if (misraCGuidelineSearchDto.getMisraCIdx() == 0) {
                misraCGuidelineDto.setMisraCRule(misraCService.findMisraCRuleByIdx(misraCGuidelineDto.getMisraCIdx()));
            }
        }

        return misraCGuidelineDtoList;
    }

    /**
     * MISRA C ?????? ????????? ??? ???, ????????? ??????
     *
     * @param misraCIdx
     * @param misraCDto
     * @return
     */
    public MisraCDto findMisraCGuidelineList(long misraCIdx, MisraCDto misraCDto) {
        List<MisraCGuidelineDto> misraCGuidelineDtoList = misraCGuidelineRepositoryImpl.findAll(misraCIdx);
        // ????????? ?????????????????? ??????
        class MisraCGuidelineDtoComparator implements Comparator<MisraCGuidelineDto> {
            @Override
            public int compare(MisraCGuidelineDto o1, MisraCGuidelineDto o2) {
                return (int) o2.getLikeCountInList() - (int) o1.getLikeCountInList();
            }
        }
        Collections.sort(misraCGuidelineDtoList, new MisraCGuidelineDtoComparator());

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (MisraCGuidelineDto misraCGuidelineDto : misraCGuidelineDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCGuidelineDto.getCreatedByIdx());

            misraCGuidelineDto.setNewIcon(NewIconCheck.isNew(misraCGuidelineDto.getCreatedDate()));
            misraCGuidelineDto.setCreatedByUser(createdByUser);
            misraCGuidelineDto.setCommentDtoCount(misraCGuidelineCommentRepositoryImpl.countAllByMisraCGuidelineCIdx(misraCGuidelineDto.getIdx()));
        }

        misraCDto = MisraCMapper.INSTANCE.toDtoByGuideline(misraCDto, misraCGuidelineDtoList);

        return misraCDto;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public MisraCGuidelineDto findMisraCGuidelineAutoComplete(MisraCGuidelineDto misraCGuidelineDto) {
        // title ??????
        for (String title : misraCGuidelineRepositoryImpl.findDistinctTitle()) {
            misraCGuidelineDto.getAutoCompleteTitle().add(title);
        }

        // hashTags ??????
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("misra_c_guideline")) {
            for (String hashTag : hashTags.split("#")) {
                misraCGuidelineDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // projectName ??????
        for (String projectName : projectInformationRepositoryImpl.findDistinctProjectNameByTableName("misra_c_guideline")) {
            misraCGuidelineDto.getAutoCompleteProjectName().add(projectName);
        }

        // toolName ??????
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("misra_c_guideline")) {
            misraCGuidelineDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote ??????
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("misra_c_guideline")) {
            misraCGuidelineDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName ??????
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("misra_c_guideline")) {
            misraCGuidelineDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote ??????
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("misra_c_guideline")) {
            misraCGuidelineDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return misraCGuidelineDto;
    }

    /**
     * ??????
     *
     * @param misraCGuidelineDto
     * @return
     */
    public long insertMisraCGuideline(MisraCGuidelineDto misraCGuidelineDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("misra_c_guideline")
                .content(misraCGuidelineDto.getHashTags())
                .build()).getIdx();

        long projectInformationIdx = projectInformationRepository.save(ProjectInformation.builder()
                .tableName("misra_c_guideline")
                .projectName(misraCGuidelineDto.getProjectName())
                .build()).getIdx();

        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("misra_c_guideline")
                .toolName(misraCGuidelineDto.getToolName())
                .toolNote(misraCGuidelineDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("misra_c_guideline")
                .compilerName(misraCGuidelineDto.getCompilerName())
                .compilerNote(misraCGuidelineDto.getCompilerNote())
                .build()).getIdx();

        misraCGuidelineDto.setHashTagsIdx(hashTagsIdx);
        misraCGuidelineDto.setProjectInformationIdx(projectInformationIdx);
        misraCGuidelineDto.setToolInformationIdx(toolInformationIdx);
        misraCGuidelineDto.setCompilerInformationIdx(compilerInformationIdx);

        return misraCGuidelineRepository.save(MisraCGuidelineMapper.INSTANCE.toEntity(misraCGuidelineDto)).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     * @return
     */
    public MisraCGuidelineDto findMisraCGuidelineByIdx(long idx) {
        MisraCGuidelineDto misraCGuidelineDto = new MisraCGuidelineDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            misraCGuidelineDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
        else {
            misraCGuidelineDto = misraCGuidelineRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCGuidelineDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCGuidelineDto.getLastModifiedByIdx());

            misraCGuidelineDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            misraCGuidelineDto.setCreatedByUser(createdByUser);
            misraCGuidelineDto.setLastModifiedByUser(lastModifiedByUser);

            misraCGuidelineRepositoryImpl.updateViewsByIdx(idx);
            misraCGuidelineDto.setViews(misraCGuidelineDto.getViews() + 1);
        }

        return misraCGuidelineDto;
    }

    /**
     * ??????????????????, ??????
     *
     * @return
     */
    public long countPosts() {
        return misraCGuidelineRepository.count();
    }

    /**
     * ??????
     *
     * @param idx
     * @param misraCGuidelineDto
     */
    @Transactional
    public void updateMisraCGuideline(long idx, MisraCGuidelineDto misraCGuidelineDto) {
        MisraCGuideline persistMisraCGuideline = misraCGuidelineRepository.getById(idx);
        MisraCGuideline misraCGuideline = MisraCGuidelineMapper.INSTANCE.toEntity(misraCGuidelineDto);
        persistMisraCGuideline.update(misraCGuideline);
        misraCGuidelineRepository.save(persistMisraCGuideline);

        HashTags persistHashTags = hashTagsRepository.getById(misraCGuidelineDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("misra_c_guideline")
                .content(misraCGuidelineDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        ProjectInformation persistProjectInformation = projectInformationRepository.getById(misraCGuidelineDto.getProjectInformationIdx());
        persistProjectInformation.update(ProjectInformation.builder()
                .tableName("misra_c_guideline")
                .projectName(misraCGuidelineDto.getProjectName())
                .build());
        projectInformationRepository.save(persistProjectInformation);

        ToolInformation persistToolInformation = toolInformationRepository.getById(misraCGuidelineDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("misra_c_guideline")
                .toolName(misraCGuidelineDto.getToolName())
                .toolNote(misraCGuidelineDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getById(misraCGuidelineDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("misra_c_guideline")
                .compilerName(misraCGuidelineDto.getCompilerName())
                .compilerNote(misraCGuidelineDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * ??????
     *
     * @param idx
     */
    public void deleteMisraCGuidelineByIdx(long idx) {
        MisraCGuidelineDto misraCGuidelineDto = misraCGuidelineRepositoryImpl.findByIdx(idx);

        misraCGuidelineRepository.deleteById(idx);
        hashTagsRepository.deleteById(misraCGuidelineDto.getHashTagsIdx());
        projectInformationRepository.deleteById(misraCGuidelineDto.getProjectInformationIdx());
        toolInformationRepository.deleteById(misraCGuidelineDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(misraCGuidelineDto.getCompilerInformationIdx());
    }

    /**
     * MISRA C ?????? ????????? ??? ???, ????????? ?????? ????????? ??????
     *
     * @param misraCIdx
     * @param misraCDto
     * @return
     */
    public MisraCDto findMisraCGuidelineListWhenDelete(long misraCIdx, MisraCDto misraCDto) {
        List<MisraCGuidelineDto> misraCGuidelineDtoList = misraCGuidelineRepositoryImpl.findAllWhenDelete(misraCIdx);
        misraCDto = MisraCMapper.INSTANCE.toDtoByGuideline(misraCDto, misraCGuidelineDtoList);

        return misraCDto;
    }
}