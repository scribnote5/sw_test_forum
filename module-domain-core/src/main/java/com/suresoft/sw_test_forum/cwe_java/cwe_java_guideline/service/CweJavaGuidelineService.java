package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.domain.ProjectInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.mapper.CweJavaMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.service.CweJavaService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuideline;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineSearchDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.mapper.CweJavaGuidelineMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository.CweJavaGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository.CweJavaGuidelineRepository;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository.CweJavaGuidelineRepositoryImpl;
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
public class CweJavaGuidelineService {
    private final CweJavaGuidelineRepository cweJavaGuidelineRepository;
    private final CweJavaGuidelineRepositoryImpl cweJavaGuidelineRepositoryImpl;
    private final CweJavaGuidelineCommentRepositoryImpl cweJavaGuidelineCommentRepositoryImpl;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final ProjectInformationRepository projectInformationRepository;
    private final ProjectInformationRepositoryImpl projectInformationRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final CweJavaService cweJavaService;
    @Value("${module.name}")
    private String moduleName;

    public CweJavaGuidelineService(CweJavaGuidelineRepository cweJavaGuidelineRepository,
                                   CweJavaGuidelineRepositoryImpl cweJavaGuidelineRepositoryImpl,
                                   CweJavaGuidelineCommentRepositoryImpl cweJavaGuidelineCommentRepositoryImpl,
                                   HashTagsRepository hashTagsRepository,
                                   HashTagsRepositoryImpl hashTagsRepositoryImpl,
                                   ProjectInformationRepository projectInformationRepository,
                                   ProjectInformationRepositoryImpl projectInformationRepositoryImpl,
                                   ToolInformationRepository toolInformationRepository,
                                   ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                   CompilerInformationRepository compilerInformationRepository,
                                   CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                   UserService userService,
                                   CweJavaService cweJavaService) {
        this.cweJavaGuidelineRepository = cweJavaGuidelineRepository;
        this.cweJavaGuidelineRepositoryImpl = cweJavaGuidelineRepositoryImpl;
        this.cweJavaGuidelineCommentRepositoryImpl = cweJavaGuidelineCommentRepositoryImpl;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.projectInformationRepository = projectInformationRepository;
        this.projectInformationRepositoryImpl = projectInformationRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.cweJavaService = cweJavaService;
    }

    /**
     * ????????? ??????
     *
     * @return
     */
    public Page<CweJavaGuidelineDto> findCweJavaGuidelineList(Pageable pageable, CweJavaGuidelineSearchDto cweJavaGuidelineSearchDto) {
        Page<CweJavaGuidelineDto> cweJavaGuidelineDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        cweJavaGuidelineDtoList = cweJavaGuidelineRepositoryImpl.findAll(pageable, cweJavaGuidelineSearchDto);

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (CweJavaGuidelineDto cweJavaGuidelineDto : cweJavaGuidelineDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaGuidelineDto.getCreatedByIdx());

            cweJavaGuidelineDto.setNewIcon(NewIconCheck.isNew(cweJavaGuidelineDto.getCreatedDate()));
            cweJavaGuidelineDto.setCreatedByUser(createdByUser);
            cweJavaGuidelineDto.setCommentDtoCount(cweJavaGuidelineCommentRepositoryImpl.countAllByCweJavaGuidelineCIdx(cweJavaGuidelineDto.getIdx()));

            // CWE ?????? ?????? ???????????? ???????????? ??????(CWE ?????? ??????????????? ???????????? ??????)
            if (cweJavaGuidelineSearchDto.getCweJavaIdx() == 0) {
                cweJavaGuidelineDto.setCweJavaRule(cweJavaService.findCweJavaRuleByIdx(cweJavaGuidelineDto.getCweJavaIdx()));
            }
        }

        return cweJavaGuidelineDtoList;
    }

    /**
     * CWE ?????? ????????? ??? ???, ????????? ??????
     *
     * @param cweJavaIdx
     * @param cweJavaDto
     * @return
     */
    public CweJavaDto findCweJavaGuidelineList(long cweJavaIdx, CweJavaDto cweJavaDto) {
        List<CweJavaGuidelineDto> cweJavaGuidelineDtoList = cweJavaGuidelineRepositoryImpl.findAll(cweJavaIdx);
        // ????????? ?????????????????? ??????
        class CweJavaGuidelineDtoComparator implements Comparator<CweJavaGuidelineDto> {
            @Override
            public int compare(CweJavaGuidelineDto o1, CweJavaGuidelineDto o2) {
                return (int) o2.getLikeCountInList() - (int) o1.getLikeCountInList();
            }
        }
        Collections.sort(cweJavaGuidelineDtoList, new CweJavaGuidelineDtoComparator());

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (CweJavaGuidelineDto cweJavaGuidelineDto : cweJavaGuidelineDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaGuidelineDto.getCreatedByIdx());

            cweJavaGuidelineDto.setNewIcon(NewIconCheck.isNew(cweJavaGuidelineDto.getCreatedDate()));
            cweJavaGuidelineDto.setCreatedByUser(createdByUser);
            cweJavaGuidelineDto.setCommentDtoCount(cweJavaGuidelineCommentRepositoryImpl.countAllByCweJavaGuidelineCIdx(cweJavaGuidelineDto.getIdx()));
        }

        cweJavaDto = CweJavaMapper.INSTANCE.toDtoByGuideline(cweJavaDto, cweJavaGuidelineDtoList);

        return cweJavaDto;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public CweJavaGuidelineDto findCweJavaGuidelineAutoComplete(CweJavaGuidelineDto cweJavaGuidelineDto) {
        // title ??????
        for (String title : cweJavaGuidelineRepositoryImpl.findDistinctTitle()) {
            cweJavaGuidelineDto.getAutoCompleteTitle().add(title);
        }

        // hashTags ??????
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("cwe_java_guideline")) {
            for (String hashTag : hashTags.split("#")) {
                cweJavaGuidelineDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // projectName ??????
        for (String projectName : projectInformationRepositoryImpl.findDistinctProjectNameByTableName("cwe_java_guideline")) {
            cweJavaGuidelineDto.getAutoCompleteProjectName().add(projectName);
        }

        // toolName ??????
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("cwe_java_guideline")) {
            cweJavaGuidelineDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote ??????
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("cwe_java_guideline")) {
            cweJavaGuidelineDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName ??????
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("cwe_java_guideline")) {
            cweJavaGuidelineDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote ??????
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("cwe_java_guideline")) {
            cweJavaGuidelineDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return cweJavaGuidelineDto;
    }

    /**
     * ??????
     *
     * @param cweJavaGuidelineDto
     * @return
     */
    public long insertCweJavaGuideline(CweJavaGuidelineDto cweJavaGuidelineDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("cwe_java_guideline")
                .content(cweJavaGuidelineDto.getHashTags())
                .build()).getIdx();

        long projectInformationIdx = projectInformationRepository.save(ProjectInformation.builder()
                .tableName("cwe_java_guideline")
                .projectName(cweJavaGuidelineDto.getProjectName())
                .build()).getIdx();

        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("cwe_java_guideline")
                .toolName(cweJavaGuidelineDto.getToolName())
                .toolNote(cweJavaGuidelineDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("cwe_java_guideline")
                .compilerName(cweJavaGuidelineDto.getCompilerName())
                .compilerNote(cweJavaGuidelineDto.getCompilerNote())
                .build()).getIdx();

        cweJavaGuidelineDto.setHashTagsIdx(hashTagsIdx);
        cweJavaGuidelineDto.setProjectInformationIdx(projectInformationIdx);
        cweJavaGuidelineDto.setToolInformationIdx(toolInformationIdx);
        cweJavaGuidelineDto.setCompilerInformationIdx(compilerInformationIdx);

        return cweJavaGuidelineRepository.save(CweJavaGuidelineMapper.INSTANCE.toEntity(cweJavaGuidelineDto)).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     * @return
     */
    public CweJavaGuidelineDto findCweJavaGuidelineByIdx(long idx) {
        CweJavaGuidelineDto cweJavaGuidelineDto = new CweJavaGuidelineDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            cweJavaGuidelineDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
        else {
            cweJavaGuidelineDto = cweJavaGuidelineRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaGuidelineDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaGuidelineDto.getLastModifiedByIdx());

            cweJavaGuidelineDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            cweJavaGuidelineDto.setCreatedByUser(createdByUser);
            cweJavaGuidelineDto.setLastModifiedByUser(lastModifiedByUser);

            cweJavaGuidelineRepositoryImpl.updateViewsByIdx(idx);
            cweJavaGuidelineDto.setViews(cweJavaGuidelineDto.getViews() + 1);
        }

        return cweJavaGuidelineDto;
    }

    /**
     * ??????????????????, ??????
     *
     * @return
     */
    public long countPosts() {
        return cweJavaGuidelineRepository.count();
    }

    /**
     * ??????
     *
     * @param idx
     * @param cweJavaGuidelineDto
     */
    @Transactional
    public void updateCweJavaGuideline(long idx, CweJavaGuidelineDto cweJavaGuidelineDto) {
        CweJavaGuideline persistCweJavaGuideline = cweJavaGuidelineRepository.getById(idx);
        CweJavaGuideline cweJavaGuideline = CweJavaGuidelineMapper.INSTANCE.toEntity(cweJavaGuidelineDto);
        persistCweJavaGuideline.update(cweJavaGuideline);
        cweJavaGuidelineRepository.save(persistCweJavaGuideline);

        HashTags persistHashTags = hashTagsRepository.getById(cweJavaGuidelineDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("cwe_java_guideline")
                .content(cweJavaGuidelineDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        ProjectInformation persistProjectInformation = projectInformationRepository.getById(cweJavaGuidelineDto.getProjectInformationIdx());
        persistProjectInformation.update(ProjectInformation.builder()
                .tableName("cwe_java_guideline")
                .projectName(cweJavaGuidelineDto.getProjectName())
                .build());
        projectInformationRepository.save(persistProjectInformation);

        ToolInformation persistToolInformation = toolInformationRepository.getById(cweJavaGuidelineDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("cwe_java_guideline")
                .toolName(cweJavaGuidelineDto.getToolName())
                .toolNote(cweJavaGuidelineDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getById(cweJavaGuidelineDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("cwe_java_guideline")
                .compilerName(cweJavaGuidelineDto.getCompilerName())
                .compilerNote(cweJavaGuidelineDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * ??????
     *
     * @param idx
     */
    public void deleteCweJavaGuidelineByIdx(long idx) {
        CweJavaGuidelineDto cweJavaGuidelineDto = cweJavaGuidelineRepositoryImpl.findByIdx(idx);

        cweJavaGuidelineRepository.deleteById(idx);
        hashTagsRepository.deleteById(cweJavaGuidelineDto.getHashTagsIdx());
        projectInformationRepository.deleteById(cweJavaGuidelineDto.getProjectInformationIdx());
        toolInformationRepository.deleteById(cweJavaGuidelineDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(cweJavaGuidelineDto.getCompilerInformationIdx());
    }

    /**
     * CWE ?????? ????????? ??? ???, ????????? ?????? ????????? ??????
     *
     * @param cweJavaIdx
     * @param cweJavaDto
     * @return
     */
    public CweJavaDto findCweJavaGuidelineListWhenDelete(long cweJavaIdx, CweJavaDto cweJavaDto) {
        List<CweJavaGuidelineDto> cweJavaGuidelineDtoList = cweJavaGuidelineRepositoryImpl.findAllWhenDelete(cweJavaIdx);
        cweJavaDto = CweJavaMapper.INSTANCE.toDtoByGuideline(cweJavaDto, cweJavaGuidelineDtoList);

        return cweJavaDto;
    }
}