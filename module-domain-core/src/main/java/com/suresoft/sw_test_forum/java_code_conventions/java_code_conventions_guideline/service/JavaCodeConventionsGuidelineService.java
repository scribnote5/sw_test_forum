package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.domain.ProjectInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.JavaCodeConventionsGuideline;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineSearchDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.mapper.JavaCodeConventionsGuidelineMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.repository.JavaCodeConventionsGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.repository.JavaCodeConventionsGuidelineRepository;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.repository.JavaCodeConventionsGuidelineRepositoryImpl;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.JavaCodeConventionsDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.mapper.JavaCodeConventionsMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.service.JavaCodeConventionsService;
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
public class JavaCodeConventionsGuidelineService {
    private final JavaCodeConventionsGuidelineRepository javaCodeConventionsGuidelineRepository;
    private final JavaCodeConventionsGuidelineRepositoryImpl javaCodeConventionsGuidelineRepositoryImpl;
    private final JavaCodeConventionsGuidelineCommentRepositoryImpl javaCodeConventionsGuidelineCommentRepositoryImpl;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final ProjectInformationRepository projectInformationRepository;
    private final ProjectInformationRepositoryImpl projectInformationRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final JavaCodeConventionsService javaCodeConventionsService;
    @Value("${module.name}")
    private String moduleName;

    public JavaCodeConventionsGuidelineService(JavaCodeConventionsGuidelineRepository javaCodeConventionsGuidelineRepository,
                                               JavaCodeConventionsGuidelineRepositoryImpl javaCodeConventionsGuidelineRepositoryImpl,
                                               JavaCodeConventionsGuidelineCommentRepositoryImpl javaCodeConventionsGuidelineCommentRepositoryImpl,
                                               HashTagsRepository hashTagsRepository,
                                               HashTagsRepositoryImpl hashTagsRepositoryImpl,
                                               ProjectInformationRepository projectInformationRepository,
                                               ProjectInformationRepositoryImpl projectInformationRepositoryImpl,
                                               ToolInformationRepository toolInformationRepository,
                                               ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                               CompilerInformationRepository compilerInformationRepository,
                                               CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                               UserService userService,
                                               JavaCodeConventionsService javaCodeConventionsService) {
        this.javaCodeConventionsGuidelineRepository = javaCodeConventionsGuidelineRepository;
        this.javaCodeConventionsGuidelineRepositoryImpl = javaCodeConventionsGuidelineRepositoryImpl;
        this.javaCodeConventionsGuidelineCommentRepositoryImpl = javaCodeConventionsGuidelineCommentRepositoryImpl;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.projectInformationRepository = projectInformationRepository;
        this.projectInformationRepositoryImpl = projectInformationRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.javaCodeConventionsService = javaCodeConventionsService;
    }

    /**
     * ????????? ??????
     *
     * @return
     */
    public Page<JavaCodeConventionsGuidelineDto> findJavaCodeConventionsGuidelineList(Pageable pageable, JavaCodeConventionsGuidelineSearchDto javaCodeConventionsGuidelineSearchDto) {
        Page<JavaCodeConventionsGuidelineDto> javaCodeConventionsGuidelineDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        javaCodeConventionsGuidelineDtoList = javaCodeConventionsGuidelineRepositoryImpl.findAll(pageable, javaCodeConventionsGuidelineSearchDto);

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (JavaCodeConventionsGuidelineDto javaCodeConventionsGuidelineDto : javaCodeConventionsGuidelineDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(javaCodeConventionsGuidelineDto.getCreatedByIdx());

            javaCodeConventionsGuidelineDto.setNewIcon(NewIconCheck.isNew(javaCodeConventionsGuidelineDto.getCreatedDate()));
            javaCodeConventionsGuidelineDto.setCreatedByUser(createdByUser);
            javaCodeConventionsGuidelineDto.setCommentDtoCount(javaCodeConventionsGuidelineCommentRepositoryImpl.countAllByJavaCodeConventionsGuidelineCIdx(javaCodeConventionsGuidelineDto.getIdx()));

            // JAVA CODE CONVENTIONS ?????? ?????? ???????????? ???????????? ??????(JAVA CODE CONVENTIONS ?????? ??????????????? ???????????? ??????)
            if (javaCodeConventionsGuidelineSearchDto.getJavaCodeConventionsIdx() == 0) {
                javaCodeConventionsGuidelineDto.setJavaCodeConventionsRule(javaCodeConventionsService.findJavaCodeConventionsRuleByIdx(javaCodeConventionsGuidelineDto.getJavaCodeConventionsIdx()));
            }
        }

        return javaCodeConventionsGuidelineDtoList;
    }

    /**
     * JAVA CODE CONVENTIONS ?????? ????????? ??? ???, ????????? ??????
     *
     * @param javaCodeConventionsIdx
     * @param javaCodeConventionsDto
     * @return
     */
    public JavaCodeConventionsDto findJavaCodeConventionsGuidelineList(long javaCodeConventionsIdx, JavaCodeConventionsDto javaCodeConventionsDto) {
        List<JavaCodeConventionsGuidelineDto> javaCodeConventionsGuidelineDtoList = javaCodeConventionsGuidelineRepositoryImpl.findAll(javaCodeConventionsIdx);
        // ????????? ?????????????????? ??????
        class JavaCodeConventionsGuidelineDtoComparator implements Comparator<JavaCodeConventionsGuidelineDto> {
            @Override
            public int compare(JavaCodeConventionsGuidelineDto o1, JavaCodeConventionsGuidelineDto o2) {
                return (int) o2.getLikeCountInList() - (int) o1.getLikeCountInList();
            }
        }
        Collections.sort(javaCodeConventionsGuidelineDtoList, new JavaCodeConventionsGuidelineDtoComparator());

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (JavaCodeConventionsGuidelineDto javaCodeConventionsGuidelineDto : javaCodeConventionsGuidelineDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(javaCodeConventionsGuidelineDto.getCreatedByIdx());

            javaCodeConventionsGuidelineDto.setNewIcon(NewIconCheck.isNew(javaCodeConventionsGuidelineDto.getCreatedDate()));
            javaCodeConventionsGuidelineDto.setCreatedByUser(createdByUser);
            javaCodeConventionsGuidelineDto.setCommentDtoCount(javaCodeConventionsGuidelineCommentRepositoryImpl.countAllByJavaCodeConventionsGuidelineCIdx(javaCodeConventionsGuidelineDto.getIdx()));
        }

        javaCodeConventionsDto = JavaCodeConventionsMapper.INSTANCE.toDtoByGuideline(javaCodeConventionsDto, javaCodeConventionsGuidelineDtoList);

        return javaCodeConventionsDto;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public JavaCodeConventionsGuidelineDto findJavaCodeConventionsGuidelineAutoComplete(JavaCodeConventionsGuidelineDto javaCodeConventionsGuidelineDto) {
        // title ??????
        for (String title : javaCodeConventionsGuidelineRepositoryImpl.findDistinctTitle()) {
            javaCodeConventionsGuidelineDto.getAutoCompleteTitle().add(title);
        }

        // hashTags ??????
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("java_code_conventions_guideline")) {
            for (String hashTag : hashTags.split("#")) {
                javaCodeConventionsGuidelineDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // projectName ??????
        for (String projectName : projectInformationRepositoryImpl.findDistinctProjectNameByTableName("java_code_conventions_guideline")) {
            javaCodeConventionsGuidelineDto.getAutoCompleteProjectName().add(projectName);
        }

        // toolName ??????
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("java_code_conventions_guideline")) {
            javaCodeConventionsGuidelineDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote ??????
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("java_code_conventions_guideline")) {
            javaCodeConventionsGuidelineDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName ??????
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("java_code_conventions_guideline")) {
            javaCodeConventionsGuidelineDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote ??????
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("java_code_conventions_guideline")) {
            javaCodeConventionsGuidelineDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return javaCodeConventionsGuidelineDto;
    }

    /**
     * ??????
     *
     * @param javaCodeConventionsGuidelineDto
     * @return
     */
    public long insertJavaCodeConventionsGuideline(JavaCodeConventionsGuidelineDto javaCodeConventionsGuidelineDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("java_code_conventions_guideline")
                .content(javaCodeConventionsGuidelineDto.getHashTags())
                .build()).getIdx();

        long projectInformationIdx = projectInformationRepository.save(ProjectInformation.builder()
                .tableName("java_code_conventions_guideline")
                .projectName(javaCodeConventionsGuidelineDto.getProjectName())
                .build()).getIdx();

        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("java_code_conventions_guideline")
                .toolName(javaCodeConventionsGuidelineDto.getToolName())
                .toolNote(javaCodeConventionsGuidelineDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("java_code_conventions_guideline")
                .compilerName(javaCodeConventionsGuidelineDto.getCompilerName())
                .compilerNote(javaCodeConventionsGuidelineDto.getCompilerNote())
                .build()).getIdx();

        javaCodeConventionsGuidelineDto.setHashTagsIdx(hashTagsIdx);
        javaCodeConventionsGuidelineDto.setProjectInformationIdx(projectInformationIdx);
        javaCodeConventionsGuidelineDto.setToolInformationIdx(toolInformationIdx);
        javaCodeConventionsGuidelineDto.setCompilerInformationIdx(compilerInformationIdx);

        return javaCodeConventionsGuidelineRepository.save(JavaCodeConventionsGuidelineMapper.INSTANCE.toEntity(javaCodeConventionsGuidelineDto)).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     * @return
     */
    public JavaCodeConventionsGuidelineDto findJavaCodeConventionsGuidelineByIdx(long idx) {
        JavaCodeConventionsGuidelineDto javaCodeConventionsGuidelineDto = new JavaCodeConventionsGuidelineDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            javaCodeConventionsGuidelineDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
        else {
            javaCodeConventionsGuidelineDto = javaCodeConventionsGuidelineRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(javaCodeConventionsGuidelineDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(javaCodeConventionsGuidelineDto.getLastModifiedByIdx());

            javaCodeConventionsGuidelineDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            javaCodeConventionsGuidelineDto.setCreatedByUser(createdByUser);
            javaCodeConventionsGuidelineDto.setLastModifiedByUser(lastModifiedByUser);

            javaCodeConventionsGuidelineRepositoryImpl.updateViewsByIdx(idx);
            javaCodeConventionsGuidelineDto.setViews(javaCodeConventionsGuidelineDto.getViews() + 1);
        }

        return javaCodeConventionsGuidelineDto;
    }

    /**
     * ??????????????????, ??????
     *
     * @return
     */
    public long countPosts() {
        return javaCodeConventionsGuidelineRepository.count();
    }

    /**
     * ??????
     *
     * @param idx
     * @param javaCodeConventionsGuidelineDto
     */
    @Transactional
    public void updateJavaCodeConventionsGuideline(long idx, JavaCodeConventionsGuidelineDto javaCodeConventionsGuidelineDto) {
        JavaCodeConventionsGuideline persistJavaCodeConventionsGuideline = javaCodeConventionsGuidelineRepository.getById(idx);
        JavaCodeConventionsGuideline javaCodeConventionsGuideline = JavaCodeConventionsGuidelineMapper.INSTANCE.toEntity(javaCodeConventionsGuidelineDto);
        persistJavaCodeConventionsGuideline.update(javaCodeConventionsGuideline);
        javaCodeConventionsGuidelineRepository.save(persistJavaCodeConventionsGuideline);

        HashTags persistHashTags = hashTagsRepository.getById(javaCodeConventionsGuidelineDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("java_code_conventions_guideline")
                .content(javaCodeConventionsGuidelineDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        ProjectInformation persistProjectInformation = projectInformationRepository.getById(javaCodeConventionsGuidelineDto.getProjectInformationIdx());
        persistProjectInformation.update(ProjectInformation.builder()
                .tableName("java_code_conventions_guideline")
                .projectName(javaCodeConventionsGuidelineDto.getProjectName())
                .build());
        projectInformationRepository.save(persistProjectInformation);

        ToolInformation persistToolInformation = toolInformationRepository.getById(javaCodeConventionsGuidelineDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("java_code_conventions_guideline")
                .toolName(javaCodeConventionsGuidelineDto.getToolName())
                .toolNote(javaCodeConventionsGuidelineDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getById(javaCodeConventionsGuidelineDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("java_code_conventions_guideline")
                .compilerName(javaCodeConventionsGuidelineDto.getCompilerName())
                .compilerNote(javaCodeConventionsGuidelineDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * ??????
     *
     * @param idx
     */
    public void deleteJavaCodeConventionsGuidelineByIdx(long idx) {
        JavaCodeConventionsGuidelineDto javaCodeConventionsGuidelineDto = javaCodeConventionsGuidelineRepositoryImpl.findByIdx(idx);

        javaCodeConventionsGuidelineRepository.deleteById(idx);
        hashTagsRepository.deleteById(javaCodeConventionsGuidelineDto.getHashTagsIdx());
        projectInformationRepository.deleteById(javaCodeConventionsGuidelineDto.getProjectInformationIdx());
        toolInformationRepository.deleteById(javaCodeConventionsGuidelineDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(javaCodeConventionsGuidelineDto.getCompilerInformationIdx());
    }

    /**
     * JAVA CODE CONVENTIONS ?????? ????????? ??? ???, ????????? ?????? ????????? ??????
     *
     * @param javaCodeConventionsIdx
     * @param javaCodeConventionsDto
     * @return
     */
    public JavaCodeConventionsDto findJavaCodeConventionsGuidelineListWhenDelete(long javaCodeConventionsIdx, JavaCodeConventionsDto javaCodeConventionsDto) {
        List<JavaCodeConventionsGuidelineDto> javaCodeConventionsGuidelineDtoList = javaCodeConventionsGuidelineRepositoryImpl.findAllWhenDelete(javaCodeConventionsIdx);
        javaCodeConventionsDto = JavaCodeConventionsMapper.INSTANCE.toDtoByGuideline(javaCodeConventionsDto, javaCodeConventionsGuidelineDtoList);

        return javaCodeConventionsDto;
    }
}