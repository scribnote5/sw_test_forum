package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.domain.ProjectInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.StyleCopDto;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.mapper.StyleCopMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop.service.StyleCopService;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuideline;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineSearchDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.mapper.StyleCopGuidelineMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository.StyleCopGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository.StyleCopGuidelineRepository;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository.StyleCopGuidelineRepositoryImpl;
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
public class StyleCopGuidelineService {
    private final StyleCopGuidelineRepository styleCopGuidelineRepository;
    private final StyleCopGuidelineRepositoryImpl styleCopGuidelineRepositoryImpl;
    private final StyleCopGuidelineCommentRepositoryImpl styleCopGuidelineCommentRepositoryImpl;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final ProjectInformationRepository projectInformationRepository;
    private final ProjectInformationRepositoryImpl projectInformationRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final StyleCopService styleCopService;
    @Value("${module.name}")
    private String moduleName;

    public StyleCopGuidelineService(StyleCopGuidelineRepository styleCopGuidelineRepository,
                                    StyleCopGuidelineRepositoryImpl styleCopGuidelineRepositoryImpl,
                                    StyleCopGuidelineCommentRepositoryImpl styleCopGuidelineCommentRepositoryImpl,
                                    HashTagsRepository hashTagsRepository,
                                    HashTagsRepositoryImpl hashTagsRepositoryImpl,
                                    ProjectInformationRepository projectInformationRepository,
                                    ProjectInformationRepositoryImpl projectInformationRepositoryImpl,
                                    ToolInformationRepository toolInformationRepository,
                                    ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                    CompilerInformationRepository compilerInformationRepository,
                                    CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                    UserService userService,
                                    StyleCopService styleCopService) {
        this.styleCopGuidelineRepository = styleCopGuidelineRepository;
        this.styleCopGuidelineRepositoryImpl = styleCopGuidelineRepositoryImpl;
        this.styleCopGuidelineCommentRepositoryImpl = styleCopGuidelineCommentRepositoryImpl;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.projectInformationRepository = projectInformationRepository;
        this.projectInformationRepositoryImpl = projectInformationRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.styleCopService = styleCopService;
    }

    /**
     * ????????? ??????
     *
     * @return
     */
    public Page<StyleCopGuidelineDto> findStyleCopGuidelineList(Pageable pageable, StyleCopGuidelineSearchDto styleCopGuidelineSearchDto) {
        Page<StyleCopGuidelineDto> styleCopGuidelineDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        styleCopGuidelineDtoList = styleCopGuidelineRepositoryImpl.findAll(pageable, styleCopGuidelineSearchDto);

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (StyleCopGuidelineDto styleCopGuidelineDto : styleCopGuidelineDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(styleCopGuidelineDto.getCreatedByIdx());

            styleCopGuidelineDto.setNewIcon(NewIconCheck.isNew(styleCopGuidelineDto.getCreatedDate()));
            styleCopGuidelineDto.setCreatedByUser(createdByUser);
            styleCopGuidelineDto.setCommentDtoCount(styleCopGuidelineCommentRepositoryImpl.countAllByStyleCopGuidelineCIdx(styleCopGuidelineDto.getIdx()));

            // StyleCop ?????? ?????? ???????????? ???????????? ??????(StyleCop ?????? ??????????????? ???????????? ??????)
            if (styleCopGuidelineSearchDto.getStyleCopIdx() == 0) {
                styleCopGuidelineDto.setStyleCopRule(styleCopService.findStyleCopRuleByIdx(styleCopGuidelineDto.getStyleCopIdx()));
            }
        }

        return styleCopGuidelineDtoList;
    }

    /**
     * StyleCop ?????? ????????? ??? ???, ????????? ??????
     *
     * @param styleCopIdx
     * @param styleCopDto
     * @return
     */
    public StyleCopDto findStyleCopGuidelineList(long styleCopIdx, StyleCopDto styleCopDto) {
        List<StyleCopGuidelineDto> styleCopGuidelineDtoList = styleCopGuidelineRepositoryImpl.findAll(styleCopIdx);
        // ????????? ?????????????????? ??????
        class StyleCopGuidelineDtoComparator implements Comparator<StyleCopGuidelineDto> {
            @Override
            public int compare(StyleCopGuidelineDto o1, StyleCopGuidelineDto o2) {
                return (int) o2.getLikeCountInList() - (int) o1.getLikeCountInList();
            }
        }
        Collections.sort(styleCopGuidelineDtoList, new StyleCopGuidelineDtoComparator());

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (StyleCopGuidelineDto styleCopGuidelineDto : styleCopGuidelineDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(styleCopGuidelineDto.getCreatedByIdx());

            styleCopGuidelineDto.setNewIcon(NewIconCheck.isNew(styleCopGuidelineDto.getCreatedDate()));
            styleCopGuidelineDto.setCreatedByUser(createdByUser);
            styleCopGuidelineDto.setCommentDtoCount(styleCopGuidelineCommentRepositoryImpl.countAllByStyleCopGuidelineCIdx(styleCopGuidelineDto.getIdx()));
        }

        styleCopDto = StyleCopMapper.INSTANCE.toDtoByGuideline(styleCopDto, styleCopGuidelineDtoList);

        return styleCopDto;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public StyleCopGuidelineDto findStyleCopGuidelineAutoComplete(StyleCopGuidelineDto styleCopGuidelineDto) {
        // title ??????
        for (String title : styleCopGuidelineRepositoryImpl.findDistinctTitle()) {
            styleCopGuidelineDto.getAutoCompleteTitle().add(title);
        }

        // hashTags ??????
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("style_cop_guideline")) {
            for (String hashTag : hashTags.split("#")) {
                styleCopGuidelineDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // projectName ??????
        for (String projectName : projectInformationRepositoryImpl.findDistinctProjectNameByTableName("style_cop_guideline")) {
            styleCopGuidelineDto.getAutoCompleteProjectName().add(projectName);
        }

        // toolName ??????
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("style_cop_guideline")) {
            styleCopGuidelineDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote ??????
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("style_cop_guideline")) {
            styleCopGuidelineDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName ??????
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("style_cop_guideline")) {
            styleCopGuidelineDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote ??????
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("style_cop_guideline")) {
            styleCopGuidelineDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return styleCopGuidelineDto;
    }

    /**
     * ??????
     *
     * @param styleCopGuidelineDto
     * @return
     */
    public long insertStyleCopGuideline(StyleCopGuidelineDto styleCopGuidelineDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("style_cop_guideline")
                .content(styleCopGuidelineDto.getHashTags())
                .build()).getIdx();

        long projectInformationIdx = projectInformationRepository.save(ProjectInformation.builder()
                .tableName("style_cop_guideline")
                .projectName(styleCopGuidelineDto.getProjectName())
                .build()).getIdx();

        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("style_cop_guideline")
                .toolName(styleCopGuidelineDto.getToolName())
                .toolNote(styleCopGuidelineDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("style_cop_guideline")
                .compilerName(styleCopGuidelineDto.getCompilerName())
                .compilerNote(styleCopGuidelineDto.getCompilerNote())
                .build()).getIdx();

        styleCopGuidelineDto.setHashTagsIdx(hashTagsIdx);
        styleCopGuidelineDto.setProjectInformationIdx(projectInformationIdx);
        styleCopGuidelineDto.setToolInformationIdx(toolInformationIdx);
        styleCopGuidelineDto.setCompilerInformationIdx(compilerInformationIdx);

        return styleCopGuidelineRepository.save(StyleCopGuidelineMapper.INSTANCE.toEntity(styleCopGuidelineDto)).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     * @return
     */
    public StyleCopGuidelineDto findStyleCopGuidelineByIdx(long idx) {
        StyleCopGuidelineDto styleCopGuidelineDto = new StyleCopGuidelineDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            styleCopGuidelineDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
        else {
            styleCopGuidelineDto = styleCopGuidelineRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(styleCopGuidelineDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(styleCopGuidelineDto.getLastModifiedByIdx());

            styleCopGuidelineDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            styleCopGuidelineDto.setCreatedByUser(createdByUser);
            styleCopGuidelineDto.setLastModifiedByUser(lastModifiedByUser);

            styleCopGuidelineRepositoryImpl.updateViewsByIdx(idx);
            styleCopGuidelineDto.setViews(styleCopGuidelineDto.getViews() + 1);
        }

        return styleCopGuidelineDto;
    }

    /**
     * ??????????????????, ??????
     *
     * @return
     */
    public long countPosts() {
        return styleCopGuidelineRepository.count();
    }

    /**
     * ??????
     *
     * @param idx
     * @param styleCopGuidelineDto
     */
    @Transactional
    public void updateStyleCopGuideline(long idx, StyleCopGuidelineDto styleCopGuidelineDto) {
        StyleCopGuideline persistStyleCopGuideline = styleCopGuidelineRepository.getById(idx);
        StyleCopGuideline styleCopGuideline = StyleCopGuidelineMapper.INSTANCE.toEntity(styleCopGuidelineDto);
        persistStyleCopGuideline.update(styleCopGuideline);
        styleCopGuidelineRepository.save(persistStyleCopGuideline);

        HashTags persistHashTags = hashTagsRepository.getById(styleCopGuidelineDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("style_cop_guideline")
                .content(styleCopGuidelineDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        ProjectInformation persistProjectInformation = projectInformationRepository.getById(styleCopGuidelineDto.getProjectInformationIdx());
        persistProjectInformation.update(ProjectInformation.builder()
                .tableName("style_cop_guideline")
                .projectName(styleCopGuidelineDto.getProjectName())
                .build());
        projectInformationRepository.save(persistProjectInformation);

        ToolInformation persistToolInformation = toolInformationRepository.getById(styleCopGuidelineDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("style_cop_guideline")
                .toolName(styleCopGuidelineDto.getToolName())
                .toolNote(styleCopGuidelineDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getById(styleCopGuidelineDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("style_cop_guideline")
                .compilerName(styleCopGuidelineDto.getCompilerName())
                .compilerNote(styleCopGuidelineDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * ??????
     *
     * @param idx
     */
    public void deleteStyleCopGuidelineByIdx(long idx) {
        StyleCopGuidelineDto styleCopGuidelineDto = styleCopGuidelineRepositoryImpl.findByIdx(idx);

        styleCopGuidelineRepository.deleteById(idx);
        hashTagsRepository.deleteById(styleCopGuidelineDto.getHashTagsIdx());
        projectInformationRepository.deleteById(styleCopGuidelineDto.getProjectInformationIdx());
        toolInformationRepository.deleteById(styleCopGuidelineDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(styleCopGuidelineDto.getCompilerInformationIdx());
    }

    /**
     * StyleCop ?????? ????????? ??? ???, ????????? ?????? ????????? ??????
     *
     * @param styleCopIdx
     * @param styleCopDto
     * @return
     */
    public StyleCopDto findStyleCopGuidelineListWhenDelete(long styleCopIdx, StyleCopDto styleCopDto) {
        List<StyleCopGuidelineDto> styleCopGuidelineDtoList = styleCopGuidelineRepositoryImpl.findAllWhenDelete(styleCopIdx);
        styleCopDto = StyleCopMapper.INSTANCE.toDtoByGuideline(styleCopDto, styleCopGuidelineDtoList);

        return styleCopDto;
    }
}