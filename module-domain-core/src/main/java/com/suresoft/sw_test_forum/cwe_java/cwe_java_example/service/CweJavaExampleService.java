package com.suresoft.sw_test_forum.cwe_java.cwe_java_example.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepository;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepositoryImpl;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepository;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepositoryImpl;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.mapper.CweJavaMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.service.CweJavaService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.domain.CweJavaExample;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.CweJavaExampleDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.CweJavaExampleSearchDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.mapper.CweJavaExampleMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.repository.CweJavaExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.repository.CweJavaExampleRepository;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.repository.CweJavaExampleRepositoryImpl;
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
public class CweJavaExampleService {
    private final CweJavaExampleRepository cweJavaExampleRepository;
    private final CweJavaExampleRepositoryImpl cweJavaExampleRepositoryImpl;
    private final CweJavaExampleCommentRepositoryImpl cweJavaExampleCommentRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final CweJavaService cweJavaService;
    @Value("${module.name}")
    private String moduleName;

    public CweJavaExampleService(CweJavaExampleRepository cweJavaExampleRepository,
                                 CweJavaExampleRepositoryImpl cweJavaExampleRepositoryImpl,
                                 CweJavaExampleCommentRepositoryImpl cweJavaExampleCommentRepositoryImpl,
                                 ToolInformationRepository toolInformationRepository,
                                 ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                 CompilerInformationRepository compilerInformationRepository,
                                 CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                 UserService userService,
                                 CweJavaService cweJavaService) {
        this.cweJavaExampleRepository = cweJavaExampleRepository;
        this.cweJavaExampleRepositoryImpl = cweJavaExampleRepositoryImpl;
        this.cweJavaExampleCommentRepositoryImpl = cweJavaExampleCommentRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.cweJavaService = cweJavaService;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param cweJavaExampleSearchDto
     * @return
     */
    public Page<CweJavaExampleDto> findCweJavaExampleList(Pageable pageable, CweJavaExampleSearchDto cweJavaExampleSearchDto) {
        Page<CweJavaExampleDto> cweJavaExampleDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        cweJavaExampleDtoList = cweJavaExampleRepositoryImpl.findAll(pageable, cweJavaExampleSearchDto);

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (CweJavaExampleDto cweJavaExampleDto : cweJavaExampleDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaExampleDto.getCreatedByIdx());

            cweJavaExampleDto.setNewIcon(NewIconCheck.isNew(cweJavaExampleDto.getCreatedDate()));
            cweJavaExampleDto.setCreatedByUser(createdByUser);
            cweJavaExampleDto.setCommentDtoCount(cweJavaExampleCommentRepositoryImpl.countAllByCweJavaExampleIdx(cweJavaExampleDto.getIdx()));

            // CWE 예제 코드 리스트를 조회하는 경우(CWE 보기 페이지에서 이동하지 않음)
            if (cweJavaExampleSearchDto.getCweJavaIdx() == 0) {
                cweJavaExampleDto.setCweJavaRule(cweJavaService.findCweJavaRuleByIdx(cweJavaExampleDto.getCweJavaIdx()));
            }
        }

        return cweJavaExampleDtoList;
    }

    /**
     * CWE 읽기 페이지 일 때, 리스트 조회
     *
     * @param cweJavaIdx
     * @param cweJavaDto
     * @return
     */
    public CweJavaDto findCweJavaExampleList(long cweJavaIdx, CweJavaDto cweJavaDto) {
        List<CweJavaExampleDto> cweJavaExampleDtoList = cweJavaExampleRepositoryImpl.findAll(cweJavaIdx);

        // NewIcon 판별, createdBy 설정
        for (CweJavaExampleDto cweJavaExampleDto : cweJavaExampleDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaExampleDto.getCreatedByIdx());

            cweJavaExampleDto.setNewIcon(NewIconCheck.isNew(cweJavaExampleDto.getCreatedDate()));
            cweJavaExampleDto.setCreatedByUser(createdByUser);
        }

        cweJavaDto = CweJavaMapper.INSTANCE.toDtoByExample(cweJavaDto, cweJavaExampleDtoList);

        return cweJavaDto;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenWrite(long cweJavaIdx) {
        List<CweJavaExample> highPriorityList = cweJavaExampleRepositoryImpl.findAllByHighPriorityAsc(cweJavaIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "예제 코드를 출력하지 않습니다.");

        for (CweJavaExample highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public CweJavaExampleDto findCweJavaExampleAutoComplete(CweJavaExampleDto cweJavaExampleDto) {
        // title 설정
        for (String title : cweJavaExampleRepositoryImpl.findDistinctTitle()) {
            cweJavaExampleDto.getAutoCompleteTitle().add(title);
        }

        // toolName 설정
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("cwe_java_example")) {
            cweJavaExampleDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote 설정
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("cwe_java_example")) {
            cweJavaExampleDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName 설정
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("cwe_java_example")) {
            cweJavaExampleDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote 설정
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("cwe_java_example")) {
            cweJavaExampleDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return cweJavaExampleDto;
    }

    /**
     * 등록
     *
     * @param cweJavaExampleDto
     * @return
     */
    public long insertCweJavaExample(CweJavaExampleDto cweJavaExampleDto) {
        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("cwe_java_example")
                .toolName(cweJavaExampleDto.getToolName())
                .toolNote(cweJavaExampleDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("cwe_java_example")
                .compilerName(cweJavaExampleDto.getCompilerName())
                .compilerNote(cweJavaExampleDto.getCompilerNote())
                .build()).getIdx();

        cweJavaExampleDto.setToolInformationIdx(toolInformationIdx);
        cweJavaExampleDto.setCompilerInformationIdx(compilerInformationIdx);

        return cweJavaExampleRepository.save(CweJavaExampleMapper.INSTANCE.toEntity(cweJavaExampleDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public CweJavaExampleDto findCweJavaExampleByIdx(long idx) {
        CweJavaExampleDto cweJavaExampleDto = new CweJavaExampleDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            cweJavaExampleDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            cweJavaExampleDto = cweJavaExampleRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaExampleDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaExampleDto.getLastModifiedByIdx());

            cweJavaExampleDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            cweJavaExampleDto.setCreatedByUser(createdByUser);
            cweJavaExampleDto.setLastModifiedByUser(lastModifiedByUser);

            cweJavaExampleRepositoryImpl.updateViewsByIdx(idx);
            cweJavaExampleDto.setViews(cweJavaExampleDto.getViews() + 1);
        }

        return cweJavaExampleDto;
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenUpdate(long idx, long cweJavaIdx) {
        List<CweJavaExample> highPriorityList = cweJavaExampleRepositoryImpl.findAllByHighPriorityAsc(cweJavaIdx);
        CweJavaExample cweJavaExamplePriority = cweJavaExampleRepositoryImpl.findAllPriorityByIdx(idx, cweJavaIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "예제 코드를 출력하지 않습니다.");

        for (CweJavaExample highPriority : highPriorityList) {
            if (cweJavaExamplePriority.getPriority() == highPriority.getPriority()) {
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
     * @param cweJavaExampleDto
     * @return
     */
    @Transactional
    public void updateCweJavaExample(long idx, CweJavaExampleDto cweJavaExampleDto) {
        CweJavaExample persistCweJavaExample = cweJavaExampleRepository.getReferenceById(idx);
        CweJavaExample cweJavaExample = CweJavaExampleMapper.INSTANCE.toEntity(cweJavaExampleDto);
        persistCweJavaExample.update(cweJavaExample);
        cweJavaExampleRepository.save(persistCweJavaExample);

        ToolInformation persistToolInformation = toolInformationRepository.getReferenceById(cweJavaExampleDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("cwe_java_example")
                .toolName(cweJavaExampleDto.getToolName())
                .toolNote(cweJavaExampleDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getReferenceById(cweJavaExampleDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("cwe_java_example")
                .compilerName(cweJavaExampleDto.getCompilerName())
                .compilerNote(cweJavaExampleDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteCweJavaExampleByIdx(long idx) {
        CweJavaExampleDto cweJavaExampleDto = cweJavaExampleRepositoryImpl.findByIdx(idx);

        cweJavaExampleRepository.deleteById(idx);
        toolInformationRepository.deleteById(cweJavaExampleDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(cweJavaExampleDto.getCompilerInformationIdx());
    }

    /**
     * CWE 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param cweJavaIdx
     * @param cweJavaDto
     * @return
     */
    public CweJavaDto findCweJavaExampleListWhenDelete(long cweJavaIdx, CweJavaDto cweJavaDto) {
        List<CweJavaExampleDto> cweJavaExampleDtoList = cweJavaExampleRepositoryImpl.findAllWhenDelete(cweJavaIdx);
        cweJavaDto = CweJavaMapper.INSTANCE.toDtoByExample(cweJavaDto, cweJavaExampleDtoList);

        return cweJavaDto;
    }
}