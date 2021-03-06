package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepository;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepositoryImpl;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepository;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepositoryImpl;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.JavaCodeConventionsDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.mapper.JavaCodeConventionsMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.service.JavaCodeConventionsService;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.domain.JavaCodeConventionsExample;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.JavaCodeConventionsExampleDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.JavaCodeConventionsExampleSearchDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.mapper.JavaCodeConventionsExampleMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.repository.JavaCodeConventionsExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.repository.JavaCodeConventionsExampleRepository;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.repository.JavaCodeConventionsExampleRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class JavaCodeConventionsExampleService {
    private final JavaCodeConventionsExampleRepository javaCodeConventionsExampleRepository;
    private final JavaCodeConventionsExampleRepositoryImpl javaCodeConventionsExampleRepositoryImpl;
    private final JavaCodeConventionsExampleCommentRepositoryImpl javaCodeConventionsExampleCommentRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final JavaCodeConventionsService javaCodeConventionsService;
    @Value("${module.name}")
    private String moduleName;

    public JavaCodeConventionsExampleService(JavaCodeConventionsExampleRepository javaCodeConventionsExampleRepository,
                                             JavaCodeConventionsExampleRepositoryImpl javaCodeConventionsExampleRepositoryImpl,
                                             JavaCodeConventionsExampleCommentRepositoryImpl javaCodeConventionsExampleCommentRepositoryImpl,
                                             ToolInformationRepository toolInformationRepository,
                                             ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                             CompilerInformationRepository compilerInformationRepository,
                                             CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                             UserService userService,
                                             JavaCodeConventionsService javaCodeConventionsService) {
        this.javaCodeConventionsExampleRepository = javaCodeConventionsExampleRepository;
        this.javaCodeConventionsExampleRepositoryImpl = javaCodeConventionsExampleRepositoryImpl;
        this.javaCodeConventionsExampleCommentRepositoryImpl = javaCodeConventionsExampleCommentRepositoryImpl;
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
     * @param pageable
     * @param javaCodeConventionsExampleSearchDto
     * @return
     */
    public Page<JavaCodeConventionsExampleDto> findJavaCodeConventionsExampleList(Pageable pageable, JavaCodeConventionsExampleSearchDto javaCodeConventionsExampleSearchDto) {
        Page<JavaCodeConventionsExampleDto> javaCodeConventionsExampleDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        javaCodeConventionsExampleDtoList = javaCodeConventionsExampleRepositoryImpl.findAll(pageable, javaCodeConventionsExampleSearchDto);

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (JavaCodeConventionsExampleDto javaCodeConventionsExampleDto : javaCodeConventionsExampleDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(javaCodeConventionsExampleDto.getCreatedByIdx());

            javaCodeConventionsExampleDto.setNewIcon(NewIconCheck.isNew(javaCodeConventionsExampleDto.getCreatedDate()));
            javaCodeConventionsExampleDto.setCreatedByUser(createdByUser);
            javaCodeConventionsExampleDto.setCommentDtoCount(javaCodeConventionsExampleCommentRepositoryImpl.countAllByJavaCodeConventionsExampleIdx(javaCodeConventionsExampleDto.getIdx()));

            // JAVA CODE CONVENTIONS ?????? ?????? ???????????? ???????????? ??????(JAVA CODE CONVENTIONS ?????? ??????????????? ???????????? ??????)
            if (javaCodeConventionsExampleSearchDto.getJavaCodeConventionsIdx() == 0) {
                javaCodeConventionsExampleDto.setJavaCodeConventionsRule(javaCodeConventionsService.findJavaCodeConventionsRuleByIdx(javaCodeConventionsExampleDto.getJavaCodeConventionsIdx()));
            }
        }

        return javaCodeConventionsExampleDtoList;
    }

    /**
     * JAVA CODE CONVENTIONS ?????? ????????? ??? ???, ????????? ??????
     *
     * @param javaCodeConventionsIdx
     * @param javaCodeConventionsDto
     * @return
     */
    public JavaCodeConventionsDto findJavaCodeConventionsExampleList(long javaCodeConventionsIdx, JavaCodeConventionsDto javaCodeConventionsDto) {
        List<JavaCodeConventionsExampleDto> javaCodeConventionsExampleDtoList = javaCodeConventionsExampleRepositoryImpl.findAll(javaCodeConventionsIdx);

        // NewIcon ??????, createdBy ??????
        for (JavaCodeConventionsExampleDto javaCodeConventionsExampleDto : javaCodeConventionsExampleDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(javaCodeConventionsExampleDto.getCreatedByIdx());

            javaCodeConventionsExampleDto.setNewIcon(NewIconCheck.isNew(javaCodeConventionsExampleDto.getCreatedDate()));
            javaCodeConventionsExampleDto.setCreatedByUser(createdByUser);
        }

        javaCodeConventionsDto = JavaCodeConventionsMapper.INSTANCE.toDtoByExample(javaCodeConventionsDto, javaCodeConventionsExampleDtoList);

        return javaCodeConventionsDto;
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenWrite(long javaCodeConventionsIdx) {
        List<JavaCodeConventionsExample> highPriorityList = javaCodeConventionsExampleRepositoryImpl.findAllByHighPriorityAsc(javaCodeConventionsIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "?????? ????????? ???????????? ????????????.");

        for (JavaCodeConventionsExample highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "??????????????? ???????????? ????????????.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public JavaCodeConventionsExampleDto findJavaCodeConventionsExampleAutoComplete(JavaCodeConventionsExampleDto javaCodeConventionsExampleDto) {
        // title ??????
        for (String title : javaCodeConventionsExampleRepositoryImpl.findDistinctTitle()) {
            javaCodeConventionsExampleDto.getAutoCompleteTitle().add(title);
        }

        // toolName ??????
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("java_code_conventions_example")) {
            javaCodeConventionsExampleDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote ??????
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("java_code_conventions_example")) {
            javaCodeConventionsExampleDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName ??????
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("java_code_conventions_example")) {
            javaCodeConventionsExampleDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote ??????
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("java_code_conventions_example")) {
            javaCodeConventionsExampleDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return javaCodeConventionsExampleDto;
    }

    /**
     * ??????
     *
     * @param javaCodeConventionsExampleDto
     * @return
     */
    public long insertJavaCodeConventionsExample(JavaCodeConventionsExampleDto javaCodeConventionsExampleDto) {
        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("java_code_conventions_example")
                .toolName(javaCodeConventionsExampleDto.getToolName())
                .toolNote(javaCodeConventionsExampleDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("java_code_conventions_example")
                .compilerName(javaCodeConventionsExampleDto.getCompilerName())
                .compilerNote(javaCodeConventionsExampleDto.getCompilerNote())
                .build()).getIdx();

        javaCodeConventionsExampleDto.setToolInformationIdx(toolInformationIdx);
        javaCodeConventionsExampleDto.setCompilerInformationIdx(compilerInformationIdx);

        return javaCodeConventionsExampleRepository.save(JavaCodeConventionsExampleMapper.INSTANCE.toEntity(javaCodeConventionsExampleDto)).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     * @return
     */
    public JavaCodeConventionsExampleDto findJavaCodeConventionsExampleByIdx(long idx) {
        JavaCodeConventionsExampleDto javaCodeConventionsExampleDto = new JavaCodeConventionsExampleDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            javaCodeConventionsExampleDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
        else {
            javaCodeConventionsExampleDto = javaCodeConventionsExampleRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(javaCodeConventionsExampleDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(javaCodeConventionsExampleDto.getLastModifiedByIdx());

            javaCodeConventionsExampleDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            javaCodeConventionsExampleDto.setCreatedByUser(createdByUser);
            javaCodeConventionsExampleDto.setLastModifiedByUser(lastModifiedByUser);

            javaCodeConventionsExampleRepositoryImpl.updateViewsByIdx(idx);
            javaCodeConventionsExampleDto.setViews(javaCodeConventionsExampleDto.getViews() + 1);
        }

        return javaCodeConventionsExampleDto;
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenUpdate(long idx, long javaCodeConventionsIdx) {
        List<JavaCodeConventionsExample> highPriorityList = javaCodeConventionsExampleRepositoryImpl.findAllByHighPriorityAsc(javaCodeConventionsIdx);
        JavaCodeConventionsExample javaCodeConventionsExamplePriority = javaCodeConventionsExampleRepositoryImpl.findAllPriorityByIdx(idx, javaCodeConventionsIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "?????? ????????? ???????????? ????????????.");

        for (JavaCodeConventionsExample highPriority : highPriorityList) {
            if (javaCodeConventionsExamplePriority.getPriority() == highPriority.getPriority()) {
                priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(false, "?????? ????????? ???????????? ?????????.");
            } else {
                priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "??????????????? ???????????? ????????????.");
            }
        }

        return priorityDtoArray;
    }

    /**
     * ??????
     *
     * @param idx
     * @param javaCodeConventionsExampleDto
     * @return
     */
    @Transactional
    public void updateJavaCodeConventionsExample(long idx, JavaCodeConventionsExampleDto javaCodeConventionsExampleDto) {
        JavaCodeConventionsExample persistJavaCodeConventionsExample = javaCodeConventionsExampleRepository.getById(idx);
        JavaCodeConventionsExample javaCodeConventionsExample = JavaCodeConventionsExampleMapper.INSTANCE.toEntity(javaCodeConventionsExampleDto);
        persistJavaCodeConventionsExample.update(javaCodeConventionsExample);
        javaCodeConventionsExampleRepository.save(persistJavaCodeConventionsExample);

        ToolInformation persistToolInformation = toolInformationRepository.getById(javaCodeConventionsExampleDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("java_code_conventions_example")
                .toolName(javaCodeConventionsExampleDto.getToolName())
                .toolNote(javaCodeConventionsExampleDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getById(javaCodeConventionsExampleDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("java_code_conventions_example")
                .compilerName(javaCodeConventionsExampleDto.getCompilerName())
                .compilerNote(javaCodeConventionsExampleDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * ??????
     *
     * @param idx
     */
    public void deleteJavaCodeConventionsExampleByIdx(long idx) {
        JavaCodeConventionsExampleDto javaCodeConventionsExampleDto = javaCodeConventionsExampleRepositoryImpl.findByIdx(idx);

        javaCodeConventionsExampleRepository.deleteById(idx);
        toolInformationRepository.deleteById(javaCodeConventionsExampleDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(javaCodeConventionsExampleDto.getCompilerInformationIdx());
    }

    /**
     * JAVA CODE CONVENTIONS ?????? ????????? ??? ???, ????????? ?????? ????????? ??????
     *
     * @param javaCodeConventionsIdx
     * @param javaCodeConventionsDto
     * @return
     */
    public JavaCodeConventionsDto findJavaCodeConventionsExampleListWhenDelete(long javaCodeConventionsIdx, JavaCodeConventionsDto javaCodeConventionsDto) {
        List<JavaCodeConventionsExampleDto> javaCodeConventionsExampleDtoList = javaCodeConventionsExampleRepositoryImpl.findAllWhenDelete(javaCodeConventionsIdx);
        javaCodeConventionsDto = JavaCodeConventionsMapper.INSTANCE.toDtoByExample(javaCodeConventionsDto, javaCodeConventionsExampleDtoList);

        return javaCodeConventionsDto;
    }
}