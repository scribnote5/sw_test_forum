package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.domain.ProjectInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.MisraCppDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.mapper.MisraCppMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.service.MisraCppService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.MisraCppGuideline;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineSearchDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.mapper.MisraCppGuidelineMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.repository.MisraCppGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.repository.MisraCppGuidelineRepository;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.repository.MisraCppGuidelineRepositoryImpl;
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
public class MisraCppGuidelineService {
    private final MisraCppGuidelineRepository misraCppGuidelineRepository;
    private final MisraCppGuidelineRepositoryImpl misraCppGuidelineRepositoryImpl;
    private final MisraCppGuidelineCommentRepositoryImpl misraCppGuidelineCommentRepositoryImpl;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final ProjectInformationRepository projectInformationRepository;
    private final ProjectInformationRepositoryImpl projectInformationRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final MisraCppService misraCppService;
    @Value("${module.name}")
    private String moduleName;

    public MisraCppGuidelineService(MisraCppGuidelineRepository misraCppGuidelineRepository,
                                    MisraCppGuidelineRepositoryImpl misraCppGuidelineRepositoryImpl,
                                    MisraCppGuidelineCommentRepositoryImpl misraCppGuidelineCommentRepositoryImpl,
                                    HashTagsRepository hashTagsRepository,
                                    HashTagsRepositoryImpl hashTagsRepositoryImpl,
                                    ProjectInformationRepository projectInformationRepository,
                                    ProjectInformationRepositoryImpl projectInformationRepositoryImpl,
                                    ToolInformationRepository toolInformationRepository,
                                    ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                    CompilerInformationRepository compilerInformationRepository,
                                    CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                    UserService userService,
                                    MisraCppService misraCppService) {
        this.misraCppGuidelineRepository = misraCppGuidelineRepository;
        this.misraCppGuidelineRepositoryImpl = misraCppGuidelineRepositoryImpl;
        this.misraCppGuidelineCommentRepositoryImpl = misraCppGuidelineCommentRepositoryImpl;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.projectInformationRepository = projectInformationRepository;
        this.projectInformationRepositoryImpl = projectInformationRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.misraCppService = misraCppService;
    }

    /**
     * ????????? ??????
     *
     * @return
     */
    public Page<MisraCppGuidelineDto> findMisraCppGuidelineList(Pageable pageable, MisraCppGuidelineSearchDto misraCppGuidelineSearchDto) {
        Page<MisraCppGuidelineDto> misraCppGuidelineDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        misraCppGuidelineDtoList = misraCppGuidelineRepositoryImpl.findAll(pageable, misraCppGuidelineSearchDto);

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (MisraCppGuidelineDto misraCppGuidelineDto : misraCppGuidelineDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCppGuidelineDto.getCreatedByIdx());

            misraCppGuidelineDto.setNewIcon(NewIconCheck.isNew(misraCppGuidelineDto.getCreatedDate()));
            misraCppGuidelineDto.setCreatedByUser(createdByUser);
            misraCppGuidelineDto.setCommentDtoCount(misraCppGuidelineCommentRepositoryImpl.countAllByMisraCppGuidelineCIdx(misraCppGuidelineDto.getIdx()));

            // MISRA CPP ??????????????? idx??? ?????? ?????? ??????
            if (misraCppGuidelineSearchDto.getMisraCppIdx() == 0) {
                misraCppGuidelineDto.setMisraCppRule(misraCppService.findMisraCppRuleByIdx(misraCppGuidelineDto.getMisraCppIdx()));
            }
        }

        return misraCppGuidelineDtoList;
    }

    /**
     * MISRA CPP ?????? ????????? ??? ???, ????????? ??????
     *
     * @param misraCppIdx
     * @param misraCppDto
     * @return
     */
    public MisraCppDto findMisraCppGuidelineList(long misraCppIdx, MisraCppDto misraCppDto) {
        List<MisraCppGuidelineDto> misraCppGuidelineDtoList = misraCppGuidelineRepositoryImpl.findAll(misraCppIdx);
        // ????????? ?????????????????? ??????
        class MisraCppGuidelineDtoComparator implements Comparator<MisraCppGuidelineDto> {
            @Override
            public int compare(MisraCppGuidelineDto o1, MisraCppGuidelineDto o2) {
                return (int) o2.getLikeCountInList() - (int) o1.getLikeCountInList();
            }
        }
        Collections.sort(misraCppGuidelineDtoList, new MisraCppGuidelineDtoComparator());

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (MisraCppGuidelineDto misraCppGuidelineDto : misraCppGuidelineDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCppGuidelineDto.getCreatedByIdx());

            misraCppGuidelineDto.setNewIcon(NewIconCheck.isNew(misraCppGuidelineDto.getCreatedDate()));
            misraCppGuidelineDto.setCreatedByUser(createdByUser);
            misraCppGuidelineDto.setCommentDtoCount(misraCppGuidelineCommentRepositoryImpl.countAllByMisraCppGuidelineCIdx(misraCppGuidelineDto.getIdx()));
        }

        misraCppDto = MisraCppMapper.INSTANCE.toDtoByGuideline(misraCppDto, misraCppGuidelineDtoList);

        return misraCppDto;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public MisraCppGuidelineDto findMisraCppGuidelineAutoComplete(MisraCppGuidelineDto misraCppGuidelineDto) {
        // title ??????
        for (String title : misraCppGuidelineRepositoryImpl.findDistinctTitle()) {
            misraCppGuidelineDto.getAutoCompleteTitle().add(title);
        }

        // hashTags ??????
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("misra_cpp_guideline")) {
            for (String hashTag : hashTags.split("#")) {
                misraCppGuidelineDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // projectName ??????
        for (String projectName : projectInformationRepositoryImpl.findDistinctProjectNameByTableName("misra_cpp_guideline")) {
            misraCppGuidelineDto.getAutoCompleteProjectName().add(projectName);
        }

        // toolName ??????
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("misra_cpp_guideline")) {
            misraCppGuidelineDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote ??????
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("misra_cpp_guideline")) {
            misraCppGuidelineDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName ??????
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("misra_cpp_guideline")) {
            misraCppGuidelineDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote ??????
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("misra_cpp_guideline")) {
            misraCppGuidelineDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return misraCppGuidelineDto;
    }

    /**
     * ??????
     *
     * @param misraCppGuidelineDto
     * @return
     */
    public long insertMisraCppGuideline(MisraCppGuidelineDto misraCppGuidelineDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("misra_cpp_guideline")
                .content(misraCppGuidelineDto.getHashTags())
                .build()).getIdx();

        long projectInformationIdx = projectInformationRepository.save(ProjectInformation.builder()
                .tableName("misra_cpp_guideline")
                .projectName(misraCppGuidelineDto.getProjectName())
                .build()).getIdx();

        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("misra_cpp_guideline")
                .toolName(misraCppGuidelineDto.getToolName())
                .toolNote(misraCppGuidelineDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("misra_cpp_guideline")
                .compilerName(misraCppGuidelineDto.getCompilerName())
                .compilerNote(misraCppGuidelineDto.getCompilerNote())
                .build()).getIdx();

        misraCppGuidelineDto.setHashTagsIdx(hashTagsIdx);
        misraCppGuidelineDto.setProjectInformationIdx(projectInformationIdx);
        misraCppGuidelineDto.setToolInformationIdx(toolInformationIdx);
        misraCppGuidelineDto.setCompilerInformationIdx(compilerInformationIdx);

        return misraCppGuidelineRepository.save(MisraCppGuidelineMapper.INSTANCE.toEntity(misraCppGuidelineDto)).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     * @return
     */
    public MisraCppGuidelineDto findMisraCppGuidelineByIdx(long idx) {
        MisraCppGuidelineDto misraCppGuidelineDto = new MisraCppGuidelineDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            misraCppGuidelineDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
        else {
            misraCppGuidelineDto = misraCppGuidelineRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCppGuidelineDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCppGuidelineDto.getLastModifiedByIdx());

            misraCppGuidelineDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            misraCppGuidelineDto.setCreatedByUser(createdByUser);
            misraCppGuidelineDto.setLastModifiedByUser(lastModifiedByUser);

            misraCppGuidelineRepositoryImpl.updateViewsByIdx(idx);
            misraCppGuidelineDto.setViews(misraCppGuidelineDto.getViews() + 1);
        }

        return misraCppGuidelineDto;
    }

    /**
     * ??????????????????, ??????
     *
     * @return
     */
    public long countPosts() {
        return misraCppGuidelineRepository.count();
    }

    /**
     * ??????
     *
     * @param idx
     * @param misraCppGuidelineDto
     */
    @Transactional
    public void updateMisraCppGuideline(long idx, MisraCppGuidelineDto misraCppGuidelineDto) {
        MisraCppGuideline persistMisraCppGuideline = misraCppGuidelineRepository.getById(idx);
        MisraCppGuideline misraCppGuideline = MisraCppGuidelineMapper.INSTANCE.toEntity(misraCppGuidelineDto);
        persistMisraCppGuideline.update(misraCppGuideline);
        misraCppGuidelineRepository.save(persistMisraCppGuideline);

        HashTags persistHashTags = hashTagsRepository.getById(misraCppGuidelineDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("misra_cpp_guideline")
                .content(misraCppGuidelineDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        ProjectInformation persistProjectInformation = projectInformationRepository.getById(misraCppGuidelineDto.getProjectInformationIdx());
        persistProjectInformation.update(ProjectInformation.builder()
                .tableName("misra_cpp_guideline")
                .projectName(misraCppGuidelineDto.getProjectName())
                .build());
        projectInformationRepository.save(persistProjectInformation);

        ToolInformation persistToolInformation = toolInformationRepository.getById(misraCppGuidelineDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("misra_cpp_guideline")
                .toolName(misraCppGuidelineDto.getToolName())
                .toolNote(misraCppGuidelineDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getById(misraCppGuidelineDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("misra_cpp_guideline")
                .compilerName(misraCppGuidelineDto.getCompilerName())
                .compilerNote(misraCppGuidelineDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * ??????
     *
     * @param idx
     */
    public void deleteMisraCppGuidelineByIdx(long idx) {
        MisraCppGuidelineDto misraCppGuidelineDto = misraCppGuidelineRepositoryImpl.findByIdx(idx);

        misraCppGuidelineRepository.deleteById(idx);
        hashTagsRepository.deleteById(misraCppGuidelineDto.getHashTagsIdx());
        projectInformationRepository.deleteById(misraCppGuidelineDto.getProjectInformationIdx());
        toolInformationRepository.deleteById(misraCppGuidelineDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(misraCppGuidelineDto.getCompilerInformationIdx());
    }

    /**
     * MISRA CPP ?????? ????????? ??? ???, ????????? ?????? ????????? ??????
     *
     * @param misraCppIdx
     * @param misraCppDto
     * @return
     */
    public MisraCppDto findMisraCppGuidelineListWhenDelete(long misraCppIdx, MisraCppDto misraCppDto) {
        List<MisraCppGuidelineDto> misraCppGuidelineDtoList = misraCppGuidelineRepositoryImpl.findAllWhenDelete(misraCppIdx);
        misraCppDto = MisraCppMapper.INSTANCE.toDtoByGuideline(misraCppDto, misraCppGuidelineDtoList);

        return misraCppDto;
    }
}