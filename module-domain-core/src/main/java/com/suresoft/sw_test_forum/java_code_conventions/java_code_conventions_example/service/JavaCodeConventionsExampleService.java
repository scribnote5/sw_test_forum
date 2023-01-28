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
     * 리스트 조회
     *
     * @param pageable
     * @param javaCodeConventionsExampleSearchDto
     * @return
     */
    public Page<JavaCodeConventionsExampleDto> findJavaCodeConventionsExampleList(Pageable pageable, JavaCodeConventionsExampleSearchDto javaCodeConventionsExampleSearchDto) {
        Page<JavaCodeConventionsExampleDto> javaCodeConventionsExampleDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        javaCodeConventionsExampleDtoList = javaCodeConventionsExampleRepositoryImpl.findAll(pageable, javaCodeConventionsExampleSearchDto);

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (JavaCodeConventionsExampleDto javaCodeConventionsExampleDto : javaCodeConventionsExampleDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(javaCodeConventionsExampleDto.getCreatedByIdx());

            javaCodeConventionsExampleDto.setNewIcon(NewIconCheck.isNew(javaCodeConventionsExampleDto.getCreatedDate()));
            javaCodeConventionsExampleDto.setCreatedByUser(createdByUser);
            javaCodeConventionsExampleDto.setCommentDtoCount(javaCodeConventionsExampleCommentRepositoryImpl.countAllByJavaCodeConventionsExampleIdx(javaCodeConventionsExampleDto.getIdx()));

            // JAVA CODE CONVENTIONS 예제 코드 리스트를 조회하는 경우(JAVA CODE CONVENTIONS 보기 페이지에서 이동하지 않음)
            if (javaCodeConventionsExampleSearchDto.getJavaCodeConventionsIdx() == 0) {
                javaCodeConventionsExampleDto.setJavaCodeConventionsRule(javaCodeConventionsService.findJavaCodeConventionsRuleByIdx(javaCodeConventionsExampleDto.getJavaCodeConventionsIdx()));
            }
        }

        return javaCodeConventionsExampleDtoList;
    }

    /**
     * JAVA CODE CONVENTIONS 읽기 페이지 일 때, 리스트 조회
     *
     * @param javaCodeConventionsIdx
     * @param javaCodeConventionsDto
     * @return
     */
    public JavaCodeConventionsDto findJavaCodeConventionsExampleList(long javaCodeConventionsIdx, JavaCodeConventionsDto javaCodeConventionsDto) {
        List<JavaCodeConventionsExampleDto> javaCodeConventionsExampleDtoList = javaCodeConventionsExampleRepositoryImpl.findAll(javaCodeConventionsIdx);

        // NewIcon 판별, createdBy 설정
        for (JavaCodeConventionsExampleDto javaCodeConventionsExampleDto : javaCodeConventionsExampleDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(javaCodeConventionsExampleDto.getCreatedByIdx());

            javaCodeConventionsExampleDto.setNewIcon(NewIconCheck.isNew(javaCodeConventionsExampleDto.getCreatedDate()));
            javaCodeConventionsExampleDto.setCreatedByUser(createdByUser);
        }

        javaCodeConventionsDto = JavaCodeConventionsMapper.INSTANCE.toDtoByExample(javaCodeConventionsDto, javaCodeConventionsExampleDtoList);

        return javaCodeConventionsDto;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenWrite(long javaCodeConventionsIdx) {
        List<JavaCodeConventionsExample> highPriorityList = javaCodeConventionsExampleRepositoryImpl.findAllByHighPriorityAsc(javaCodeConventionsIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "예제 코드를 출력하지 않습니다.");

        for (JavaCodeConventionsExample highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public JavaCodeConventionsExampleDto findJavaCodeConventionsExampleAutoComplete(JavaCodeConventionsExampleDto javaCodeConventionsExampleDto) {
        // title 설정
        for (String title : javaCodeConventionsExampleRepositoryImpl.findDistinctTitle()) {
            javaCodeConventionsExampleDto.getAutoCompleteTitle().add(title);
        }

        // toolName 설정
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("java_code_conventions_example")) {
            javaCodeConventionsExampleDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote 설정
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("java_code_conventions_example")) {
            javaCodeConventionsExampleDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName 설정
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("java_code_conventions_example")) {
            javaCodeConventionsExampleDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote 설정
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("java_code_conventions_example")) {
            javaCodeConventionsExampleDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return javaCodeConventionsExampleDto;
    }

    /**
     * 등록
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
     * 조회
     *
     * @param idx
     * @return
     */
    public JavaCodeConventionsExampleDto findJavaCodeConventionsExampleByIdx(long idx) {
        JavaCodeConventionsExampleDto javaCodeConventionsExampleDto = new JavaCodeConventionsExampleDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            javaCodeConventionsExampleDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
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
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenUpdate(long idx, long javaCodeConventionsIdx) {
        List<JavaCodeConventionsExample> highPriorityList = javaCodeConventionsExampleRepositoryImpl.findAllByHighPriorityAsc(javaCodeConventionsIdx);
        JavaCodeConventionsExample javaCodeConventionsExamplePriority = javaCodeConventionsExampleRepositoryImpl.findAllPriorityByIdx(idx, javaCodeConventionsIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "예제 코드를 출력하지 않습니다.");

        for (JavaCodeConventionsExample highPriority : highPriorityList) {
            if (javaCodeConventionsExamplePriority.getPriority() == highPriority.getPriority()) {
                priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(false, "지금 설정된 우선순위 입니다.");
            } else {
                priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
            }
        }

        return priorityDtoArray;
    }

    /**
     * 수정
     *
     * @param idx
     * @param javaCodeConventionsExampleDto
     * @return
     */
    @Transactional
    public void updateJavaCodeConventionsExample(long idx, JavaCodeConventionsExampleDto javaCodeConventionsExampleDto) {
        JavaCodeConventionsExample persistJavaCodeConventionsExample = javaCodeConventionsExampleRepository.getReferenceById(idx);
        JavaCodeConventionsExample javaCodeConventionsExample = JavaCodeConventionsExampleMapper.INSTANCE.toEntity(javaCodeConventionsExampleDto);
        persistJavaCodeConventionsExample.update(javaCodeConventionsExample);
        javaCodeConventionsExampleRepository.save(persistJavaCodeConventionsExample);

        ToolInformation persistToolInformation = toolInformationRepository.getReferenceById(javaCodeConventionsExampleDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("java_code_conventions_example")
                .toolName(javaCodeConventionsExampleDto.getToolName())
                .toolNote(javaCodeConventionsExampleDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getReferenceById(javaCodeConventionsExampleDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("java_code_conventions_example")
                .compilerName(javaCodeConventionsExampleDto.getCompilerName())
                .compilerNote(javaCodeConventionsExampleDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * 삭제
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
     * JAVA CODE CONVENTIONS 읽기 페이지 일 때, 삭제를 위해 리스트 조회
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