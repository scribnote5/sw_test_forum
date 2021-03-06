package com.suresoft.sw_test_forum.cwe.cwe_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.domain.ProjectInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.cwe.cwe.dto.CweDto;
import com.suresoft.sw_test_forum.cwe.cwe.dto.mapper.CweMapper;
import com.suresoft.sw_test_forum.cwe.cwe.service.CweService;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuideline;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineSearchDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.mapper.CweGuidelineMapper;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.repository.CweGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.repository.CweGuidelineRepository;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.repository.CweGuidelineRepositoryImpl;
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
public class CweGuidelineService {
    private final CweGuidelineRepository cweGuidelineRepository;
    private final CweGuidelineRepositoryImpl cweGuidelineRepositoryImpl;
    private final CweGuidelineCommentRepositoryImpl cweGuidelineCommentRepositoryImpl;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final ProjectInformationRepository projectInformationRepository;
    private final ProjectInformationRepositoryImpl projectInformationRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final CweService cweService;
    @Value("${module.name}")
    private String moduleName;

    public CweGuidelineService(CweGuidelineRepository cweGuidelineRepository,
                               CweGuidelineRepositoryImpl cweGuidelineRepositoryImpl,
                               CweGuidelineCommentRepositoryImpl cweGuidelineCommentRepositoryImpl,
                               HashTagsRepository hashTagsRepository,
                               HashTagsRepositoryImpl hashTagsRepositoryImpl,
                               ProjectInformationRepository projectInformationRepository,
                               ProjectInformationRepositoryImpl projectInformationRepositoryImpl,
                               ToolInformationRepository toolInformationRepository,
                               ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                               CompilerInformationRepository compilerInformationRepository,
                               CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                               UserService userService,
                               CweService cweService) {
        this.cweGuidelineRepository = cweGuidelineRepository;
        this.cweGuidelineRepositoryImpl = cweGuidelineRepositoryImpl;
        this.cweGuidelineCommentRepositoryImpl = cweGuidelineCommentRepositoryImpl;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.projectInformationRepository = projectInformationRepository;
        this.projectInformationRepositoryImpl = projectInformationRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.cweService = cweService;
    }

    /**
     * ????????? ??????
     *
     * @return
     */
    public Page<CweGuidelineDto> findCweGuidelineList(Pageable pageable, CweGuidelineSearchDto cweGuidelineSearchDto) {
        Page<CweGuidelineDto> cweGuidelineDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        cweGuidelineDtoList = cweGuidelineRepositoryImpl.findAll(pageable, cweGuidelineSearchDto);

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (CweGuidelineDto cweGuidelineDto : cweGuidelineDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweGuidelineDto.getCreatedByIdx());

            cweGuidelineDto.setNewIcon(NewIconCheck.isNew(cweGuidelineDto.getCreatedDate()));
            cweGuidelineDto.setCreatedByUser(createdByUser);
            cweGuidelineDto.setCommentDtoCount(cweGuidelineCommentRepositoryImpl.countAllByCweGuidelineCIdx(cweGuidelineDto.getIdx()));

            // CWE ?????? ?????? ???????????? ???????????? ??????(CWE ?????? ??????????????? ???????????? ??????)
            if (cweGuidelineSearchDto.getCweIdx() == 0) {
                cweGuidelineDto.setCweRule(cweService.findCweRuleByIdx(cweGuidelineDto.getCweIdx()));
            }
        }

        return cweGuidelineDtoList;
    }

    /**
     * CWE ?????? ????????? ??? ???, ????????? ??????
     *
     * @param cweIdx
     * @param cweDto
     * @return
     */
    public CweDto findCweGuidelineList(long cweIdx, CweDto cweDto) {
        List<CweGuidelineDto> cweGuidelineDtoList = cweGuidelineRepositoryImpl.findAll(cweIdx);
        // ????????? ?????????????????? ??????
        class CweGuidelineDtoComparator implements Comparator<CweGuidelineDto> {
            @Override
            public int compare(CweGuidelineDto o1, CweGuidelineDto o2) {
                return (int) o2.getLikeCountInList() - (int) o1.getLikeCountInList();
            }
        }
        Collections.sort(cweGuidelineDtoList, new CweGuidelineDtoComparator());

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (CweGuidelineDto cweGuidelineDto : cweGuidelineDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweGuidelineDto.getCreatedByIdx());

            cweGuidelineDto.setNewIcon(NewIconCheck.isNew(cweGuidelineDto.getCreatedDate()));
            cweGuidelineDto.setCreatedByUser(createdByUser);
            cweGuidelineDto.setCommentDtoCount(cweGuidelineCommentRepositoryImpl.countAllByCweGuidelineCIdx(cweGuidelineDto.getIdx()));
        }

        cweDto = CweMapper.INSTANCE.toDtoByGuideline(cweDto, cweGuidelineDtoList);

        return cweDto;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public CweGuidelineDto findCweGuidelineAutoComplete(CweGuidelineDto cweGuidelineDto) {
        // title ??????
        for (String title : cweGuidelineRepositoryImpl.findDistinctTitle()) {
            cweGuidelineDto.getAutoCompleteTitle().add(title);
        }

        // hashTags ??????
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("cwe_guideline")) {
            for (String hashTag : hashTags.split("#")) {
                cweGuidelineDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // projectName ??????
        for (String projectName : projectInformationRepositoryImpl.findDistinctProjectNameByTableName("cwe_guideline")) {
            cweGuidelineDto.getAutoCompleteProjectName().add(projectName);
        }

        // toolName ??????
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("cwe_guideline")) {
            cweGuidelineDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote ??????
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("cwe_guideline")) {
            cweGuidelineDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName ??????
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("cwe_guideline")) {
            cweGuidelineDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote ??????
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("cwe_guideline")) {
            cweGuidelineDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return cweGuidelineDto;
    }

    /**
     * ??????
     *
     * @param cweGuidelineDto
     * @return
     */
    public long insertCweGuideline(CweGuidelineDto cweGuidelineDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("cwe_guideline")
                .content(cweGuidelineDto.getHashTags())
                .build()).getIdx();

        long projectInformationIdx = projectInformationRepository.save(ProjectInformation.builder()
                .tableName("cwe_guideline")
                .projectName(cweGuidelineDto.getProjectName())
                .build()).getIdx();

        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("cwe_guideline")
                .toolName(cweGuidelineDto.getToolName())
                .toolNote(cweGuidelineDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("cwe_guideline")
                .compilerName(cweGuidelineDto.getCompilerName())
                .compilerNote(cweGuidelineDto.getCompilerNote())
                .build()).getIdx();

        cweGuidelineDto.setHashTagsIdx(hashTagsIdx);
        cweGuidelineDto.setProjectInformationIdx(projectInformationIdx);
        cweGuidelineDto.setToolInformationIdx(toolInformationIdx);
        cweGuidelineDto.setCompilerInformationIdx(compilerInformationIdx);

        return cweGuidelineRepository.save(CweGuidelineMapper.INSTANCE.toEntity(cweGuidelineDto)).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     * @return
     */
    public CweGuidelineDto findCweGuidelineByIdx(long idx) {
        CweGuidelineDto cweGuidelineDto = new CweGuidelineDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            cweGuidelineDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
        else {
            cweGuidelineDto = cweGuidelineRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweGuidelineDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweGuidelineDto.getLastModifiedByIdx());

            cweGuidelineDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            cweGuidelineDto.setCreatedByUser(createdByUser);
            cweGuidelineDto.setLastModifiedByUser(lastModifiedByUser);

            cweGuidelineRepositoryImpl.updateViewsByIdx(idx);
            cweGuidelineDto.setViews(cweGuidelineDto.getViews() + 1);
        }

        return cweGuidelineDto;
    }

    /**
     * ??????????????????, ??????
     *
     * @return
     */
    public long countPosts() {
        return cweGuidelineRepository.count();
    }

    /**
     * ??????
     *
     * @param idx
     * @param cweGuidelineDto
     */
    @Transactional
    public void updateCweGuideline(long idx, CweGuidelineDto cweGuidelineDto) {
        CweGuideline persistCweGuideline = cweGuidelineRepository.getById(idx);
        CweGuideline cweGuideline = CweGuidelineMapper.INSTANCE.toEntity(cweGuidelineDto);
        persistCweGuideline.update(cweGuideline);
        cweGuidelineRepository.save(persistCweGuideline);

        HashTags persistHashTags = hashTagsRepository.getById(cweGuidelineDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("cwe_guideline")
                .content(cweGuidelineDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        ProjectInformation persistProjectInformation = projectInformationRepository.getById(cweGuidelineDto.getProjectInformationIdx());
        persistProjectInformation.update(ProjectInformation.builder()
                .tableName("cwe_guideline")
                .projectName(cweGuidelineDto.getProjectName())
                .build());
        projectInformationRepository.save(persistProjectInformation);

        ToolInformation persistToolInformation = toolInformationRepository.getById(cweGuidelineDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("cwe_guideline")
                .toolName(cweGuidelineDto.getToolName())
                .toolNote(cweGuidelineDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getById(cweGuidelineDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("cwe_guideline")
                .compilerName(cweGuidelineDto.getCompilerName())
                .compilerNote(cweGuidelineDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * ??????
     *
     * @param idx
     */
    public void deleteCweGuidelineByIdx(long idx) {
        CweGuidelineDto cweGuidelineDto = cweGuidelineRepositoryImpl.findByIdx(idx);

        cweGuidelineRepository.deleteById(idx);
        hashTagsRepository.deleteById(cweGuidelineDto.getHashTagsIdx());
        projectInformationRepository.deleteById(cweGuidelineDto.getProjectInformationIdx());
        toolInformationRepository.deleteById(cweGuidelineDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(cweGuidelineDto.getCompilerInformationIdx());
    }

    /**
     * CWE ?????? ????????? ??? ???, ????????? ?????? ????????? ??????
     *
     * @param cweIdx
     * @param cweDto
     * @return
     */
    public CweDto findCweGuidelineListWhenDelete(long cweIdx, CweDto cweDto) {
        List<CweGuidelineDto> cweGuidelineDtoList = cweGuidelineRepositoryImpl.findAllWhenDelete(cweIdx);
        cweDto = CweMapper.INSTANCE.toDtoByGuideline(cweDto, cweGuidelineDtoList);

        return cweDto;
    }
}