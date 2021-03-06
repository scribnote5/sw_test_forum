package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.domain.ProjectInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.FxCopDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.mapper.FxCopMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.service.FxCopService;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.FxCopGuideline;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineSearchDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.mapper.FxCopGuidelineMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.repository.FxCopGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.repository.FxCopGuidelineRepository;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.repository.FxCopGuidelineRepositoryImpl;
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
public class FxCopGuidelineService {
    private final FxCopGuidelineRepository fxCopGuidelineRepository;
    private final FxCopGuidelineRepositoryImpl fxCopGuidelineRepositoryImpl;
    private final FxCopGuidelineCommentRepositoryImpl fxCopGuidelineCommentRepositoryImpl;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final ProjectInformationRepository projectInformationRepository;
    private final ProjectInformationRepositoryImpl projectInformationRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final FxCopService fxCopService;
    @Value("${module.name}")
    private String moduleName;

    public FxCopGuidelineService(FxCopGuidelineRepository fxCopGuidelineRepository,
                                    FxCopGuidelineRepositoryImpl fxCopGuidelineRepositoryImpl,
                                    FxCopGuidelineCommentRepositoryImpl fxCopGuidelineCommentRepositoryImpl,
                                    HashTagsRepository hashTagsRepository,
                                    HashTagsRepositoryImpl hashTagsRepositoryImpl,
                                    ProjectInformationRepository projectInformationRepository,
                                    ProjectInformationRepositoryImpl projectInformationRepositoryImpl,
                                    ToolInformationRepository toolInformationRepository,
                                    ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                    CompilerInformationRepository compilerInformationRepository,
                                    CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                    UserService userService,
                                    FxCopService fxCopService) {
        this.fxCopGuidelineRepository = fxCopGuidelineRepository;
        this.fxCopGuidelineRepositoryImpl = fxCopGuidelineRepositoryImpl;
        this.fxCopGuidelineCommentRepositoryImpl = fxCopGuidelineCommentRepositoryImpl;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.projectInformationRepository = projectInformationRepository;
        this.projectInformationRepositoryImpl = projectInformationRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.fxCopService = fxCopService;
    }

    /**
     * ????????? ??????
     *
     * @return
     */
    public Page<FxCopGuidelineDto> findFxCopGuidelineList(Pageable pageable, FxCopGuidelineSearchDto fxCopGuidelineSearchDto) {
        Page<FxCopGuidelineDto> fxCopGuidelineDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        fxCopGuidelineDtoList = fxCopGuidelineRepositoryImpl.findAll(pageable, fxCopGuidelineSearchDto);

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (FxCopGuidelineDto fxCopGuidelineDto : fxCopGuidelineDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(fxCopGuidelineDto.getCreatedByIdx());

            fxCopGuidelineDto.setNewIcon(NewIconCheck.isNew(fxCopGuidelineDto.getCreatedDate()));
            fxCopGuidelineDto.setCreatedByUser(createdByUser);
            fxCopGuidelineDto.setCommentDtoCount(fxCopGuidelineCommentRepositoryImpl.countAllByFxCopGuidelineCIdx(fxCopGuidelineDto.getIdx()));

            // FxCop ?????? ?????? ???????????? ???????????? ??????(FxCop ?????? ??????????????? ???????????? ??????)
            if (fxCopGuidelineSearchDto.getFxCopIdx() == 0) {
                fxCopGuidelineDto.setFxCopRule(fxCopService.findFxCopRuleByIdx(fxCopGuidelineDto.getFxCopIdx()));
            }
        }

        return fxCopGuidelineDtoList;
    }

    /**
     * FxCop ?????? ????????? ??? ???, ????????? ??????
     *
     * @param fxCopIdx
     * @param fxCopDto
     * @return
     */
    public FxCopDto findFxCopGuidelineList(long fxCopIdx, FxCopDto fxCopDto) {
        List<FxCopGuidelineDto> fxCopGuidelineDtoList = fxCopGuidelineRepositoryImpl.findAll(fxCopIdx);
        // ????????? ?????????????????? ??????
        class FxCopGuidelineDtoComparator implements Comparator<FxCopGuidelineDto> {
            @Override
            public int compare(FxCopGuidelineDto o1, FxCopGuidelineDto o2) {
                return (int) o2.getLikeCountInList() - (int) o1.getLikeCountInList();
            }
        }
        Collections.sort(fxCopGuidelineDtoList, new FxCopGuidelineDtoComparator());

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (FxCopGuidelineDto fxCopGuidelineDto : fxCopGuidelineDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(fxCopGuidelineDto.getCreatedByIdx());

            fxCopGuidelineDto.setNewIcon(NewIconCheck.isNew(fxCopGuidelineDto.getCreatedDate()));
            fxCopGuidelineDto.setCreatedByUser(createdByUser);
            fxCopGuidelineDto.setCommentDtoCount(fxCopGuidelineCommentRepositoryImpl.countAllByFxCopGuidelineCIdx(fxCopGuidelineDto.getIdx()));
        }

        fxCopDto = FxCopMapper.INSTANCE.toDtoByGuideline(fxCopDto, fxCopGuidelineDtoList);

        return fxCopDto;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public FxCopGuidelineDto findFxCopGuidelineAutoComplete(FxCopGuidelineDto fxCopGuidelineDto) {
        // title ??????
        for (String title : fxCopGuidelineRepositoryImpl.findDistinctTitle()) {
            fxCopGuidelineDto.getAutoCompleteTitle().add(title);
        }

        // hashTags ??????
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("fx_cop_guideline")) {
            for (String hashTag : hashTags.split("#")) {
                fxCopGuidelineDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // projectName ??????
        for (String projectName : projectInformationRepositoryImpl.findDistinctProjectNameByTableName("fx_cop_guideline")) {
            fxCopGuidelineDto.getAutoCompleteProjectName().add(projectName);
        }

        // toolName ??????
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("fx_cop_guideline")) {
            fxCopGuidelineDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote ??????
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("fx_cop_guideline")) {
            fxCopGuidelineDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName ??????
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("fx_cop_guideline")) {
            fxCopGuidelineDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote ??????
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("fx_cop_guideline")) {
            fxCopGuidelineDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return fxCopGuidelineDto;
    }

    /**
     * ??????
     *
     * @param fxCopGuidelineDto
     * @return
     */
    public long insertFxCopGuideline(FxCopGuidelineDto fxCopGuidelineDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("fx_cop_guideline")
                .content(fxCopGuidelineDto.getHashTags())
                .build()).getIdx();

        long projectInformationIdx = projectInformationRepository.save(ProjectInformation.builder()
                .tableName("fx_cop_guideline")
                .projectName(fxCopGuidelineDto.getProjectName())
                .build()).getIdx();

        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("fx_cop_guideline")
                .toolName(fxCopGuidelineDto.getToolName())
                .toolNote(fxCopGuidelineDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("fx_cop_guideline")
                .compilerName(fxCopGuidelineDto.getCompilerName())
                .compilerNote(fxCopGuidelineDto.getCompilerNote())
                .build()).getIdx();

        fxCopGuidelineDto.setHashTagsIdx(hashTagsIdx);
        fxCopGuidelineDto.setProjectInformationIdx(projectInformationIdx);
        fxCopGuidelineDto.setToolInformationIdx(toolInformationIdx);
        fxCopGuidelineDto.setCompilerInformationIdx(compilerInformationIdx);

        return fxCopGuidelineRepository.save(FxCopGuidelineMapper.INSTANCE.toEntity(fxCopGuidelineDto)).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     * @return
     */
    public FxCopGuidelineDto findFxCopGuidelineByIdx(long idx) {
        FxCopGuidelineDto fxCopGuidelineDto = new FxCopGuidelineDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            fxCopGuidelineDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
        else {
            fxCopGuidelineDto = fxCopGuidelineRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(fxCopGuidelineDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(fxCopGuidelineDto.getLastModifiedByIdx());

            fxCopGuidelineDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            fxCopGuidelineDto.setCreatedByUser(createdByUser);
            fxCopGuidelineDto.setLastModifiedByUser(lastModifiedByUser);

            fxCopGuidelineRepositoryImpl.updateViewsByIdx(idx);
            fxCopGuidelineDto.setViews(fxCopGuidelineDto.getViews() + 1);
        }

        return fxCopGuidelineDto;
    }

    /**
     * ??????????????????, ??????
     *
     * @return
     */
    public long countPosts() {
        return fxCopGuidelineRepository.count();
    }

    /**
     * ??????
     *
     * @param idx
     * @param fxCopGuidelineDto
     */
    @Transactional
    public void updateFxCopGuideline(long idx, FxCopGuidelineDto fxCopGuidelineDto) {
        FxCopGuideline persistFxCopGuideline = fxCopGuidelineRepository.getById(idx);
        FxCopGuideline fxCopGuideline = FxCopGuidelineMapper.INSTANCE.toEntity(fxCopGuidelineDto);
        persistFxCopGuideline.update(fxCopGuideline);
        fxCopGuidelineRepository.save(persistFxCopGuideline);

        HashTags persistHashTags = hashTagsRepository.getById(fxCopGuidelineDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("fx_cop_guideline")
                .content(fxCopGuidelineDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        ProjectInformation persistProjectInformation = projectInformationRepository.getById(fxCopGuidelineDto.getProjectInformationIdx());
        persistProjectInformation.update(ProjectInformation.builder()
                .tableName("fx_cop_guideline")
                .projectName(fxCopGuidelineDto.getProjectName())
                .build());
        projectInformationRepository.save(persistProjectInformation);

        ToolInformation persistToolInformation = toolInformationRepository.getById(fxCopGuidelineDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("fx_cop_guideline")
                .toolName(fxCopGuidelineDto.getToolName())
                .toolNote(fxCopGuidelineDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getById(fxCopGuidelineDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("fx_cop_guideline")
                .compilerName(fxCopGuidelineDto.getCompilerName())
                .compilerNote(fxCopGuidelineDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * ??????
     *
     * @param idx
     */
    public void deleteFxCopGuidelineByIdx(long idx) {
        FxCopGuidelineDto fxCopGuidelineDto = fxCopGuidelineRepositoryImpl.findByIdx(idx);

        fxCopGuidelineRepository.deleteById(idx);
        hashTagsRepository.deleteById(fxCopGuidelineDto.getHashTagsIdx());
        projectInformationRepository.deleteById(fxCopGuidelineDto.getProjectInformationIdx());
        toolInformationRepository.deleteById(fxCopGuidelineDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(fxCopGuidelineDto.getCompilerInformationIdx());
    }

    /**
     * FxCop ?????? ????????? ??? ???, ????????? ?????? ????????? ??????
     *
     * @param fxCopIdx
     * @param fxCopDto
     * @return
     */
    public FxCopDto findFxCopGuidelineListWhenDelete(long fxCopIdx, FxCopDto fxCopDto) {
        List<FxCopGuidelineDto> fxCopGuidelineDtoList = fxCopGuidelineRepositoryImpl.findAllWhenDelete(fxCopIdx);
        fxCopDto = FxCopMapper.INSTANCE.toDtoByGuideline(fxCopDto, fxCopGuidelineDtoList);

        return fxCopDto;
    }
}