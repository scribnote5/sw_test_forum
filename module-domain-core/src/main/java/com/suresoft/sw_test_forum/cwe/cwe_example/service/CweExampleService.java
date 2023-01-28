package com.suresoft.sw_test_forum.cwe.cwe_example.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepository;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepositoryImpl;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepository;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepositoryImpl;
import com.suresoft.sw_test_forum.cwe.cwe.dto.CweDto;
import com.suresoft.sw_test_forum.cwe.cwe.dto.mapper.CweMapper;
import com.suresoft.sw_test_forum.cwe.cwe.service.CweService;
import com.suresoft.sw_test_forum.cwe.cwe_example.domain.CweExample;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.CweExampleDto;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.CweExampleSearchDto;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.mapper.CweExampleMapper;
import com.suresoft.sw_test_forum.cwe.cwe_example.repository.CweExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.cwe.cwe_example.repository.CweExampleRepository;
import com.suresoft.sw_test_forum.cwe.cwe_example.repository.CweExampleRepositoryImpl;
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
public class CweExampleService {
    private final CweExampleRepository cweExampleRepository;
    private final CweExampleRepositoryImpl cweExampleRepositoryImpl;
    private final CweExampleCommentRepositoryImpl cweExampleCommentRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final CweService cweService;
    @Value("${module.name}")
    private String moduleName;

    public CweExampleService(CweExampleRepository cweExampleRepository,
                             CweExampleRepositoryImpl cweExampleRepositoryImpl,
                             CweExampleCommentRepositoryImpl cweExampleCommentRepositoryImpl,
                             ToolInformationRepository toolInformationRepository,
                             ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                             CompilerInformationRepository compilerInformationRepository,
                             CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                             UserService userService,
                             CweService cweService) {
        this.cweExampleRepository = cweExampleRepository;
        this.cweExampleRepositoryImpl = cweExampleRepositoryImpl;
        this.cweExampleCommentRepositoryImpl = cweExampleCommentRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.cweService = cweService;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param cweExampleSearchDto
     * @return
     */
    public Page<CweExampleDto> findCweExampleList(Pageable pageable, CweExampleSearchDto cweExampleSearchDto) {
        Page<CweExampleDto> cweExampleDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        cweExampleDtoList = cweExampleRepositoryImpl.findAll(pageable, cweExampleSearchDto);

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (CweExampleDto cweExampleDto : cweExampleDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweExampleDto.getCreatedByIdx());

            cweExampleDto.setNewIcon(NewIconCheck.isNew(cweExampleDto.getCreatedDate()));
            cweExampleDto.setCreatedByUser(createdByUser);
            cweExampleDto.setCommentDtoCount(cweExampleCommentRepositoryImpl.countAllByCweExampleIdx(cweExampleDto.getIdx()));

            // CWE 예제 코드 리스트를 조회하는 경우(CWE 보기 페이지에서 이동하지 않음)
            if (cweExampleSearchDto.getCweIdx() == 0) {
                cweExampleDto.setCweRule(cweService.findCweRuleByIdx(cweExampleDto.getCweIdx()));
            }
        }

        return cweExampleDtoList;
    }

    /**
     * CWE 읽기 페이지 일 때, 리스트 조회
     *
     * @param cweIdx
     * @param cweDto
     * @return
     */
    public CweDto findCweExampleList(long cweIdx, CweDto cweDto) {
        List<CweExampleDto> cweExampleDtoList = cweExampleRepositoryImpl.findAll(cweIdx);

        // NewIcon 판별, createdBy 설정
        for (CweExampleDto cweExampleDto : cweExampleDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweExampleDto.getCreatedByIdx());

            cweExampleDto.setNewIcon(NewIconCheck.isNew(cweExampleDto.getCreatedDate()));
            cweExampleDto.setCreatedByUser(createdByUser);
        }

        cweDto = CweMapper.INSTANCE.toDtoByExample(cweDto, cweExampleDtoList);

        return cweDto;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenWrite(long cweIdx) {
        List<CweExample> highPriorityList = cweExampleRepositoryImpl.findAllByHighPriorityAsc(cweIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "예제 코드를 출력하지 않습니다.");

        for (CweExample highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public CweExampleDto findCweExampleAutoComplete(CweExampleDto cweExampleDto) {
        // title 설정
        for (String title : cweExampleRepositoryImpl.findDistinctTitle()) {
            cweExampleDto.getAutoCompleteTitle().add(title);
        }

        // toolName 설정
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("cwe_example")) {
            cweExampleDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote 설정
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("cwe_example")) {
            cweExampleDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName 설정
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("cwe_example")) {
            cweExampleDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote 설정
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("cwe_example")) {
            cweExampleDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return cweExampleDto;
    }

    /**
     * 등록
     *
     * @param cweExampleDto
     * @return
     */
    public long insertCweExample(CweExampleDto cweExampleDto) {
        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("cwe_example")
                .toolName(cweExampleDto.getToolName())
                .toolNote(cweExampleDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("cwe_example")
                .compilerName(cweExampleDto.getCompilerName())
                .compilerNote(cweExampleDto.getCompilerNote())
                .build()).getIdx();

        cweExampleDto.setToolInformationIdx(toolInformationIdx);
        cweExampleDto.setCompilerInformationIdx(compilerInformationIdx);

        return cweExampleRepository.save(CweExampleMapper.INSTANCE.toEntity(cweExampleDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public CweExampleDto findCweExampleByIdx(long idx) {
        CweExampleDto cweExampleDto = new CweExampleDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            cweExampleDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            cweExampleDto = cweExampleRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweExampleDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweExampleDto.getLastModifiedByIdx());

            cweExampleDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            cweExampleDto.setCreatedByUser(createdByUser);
            cweExampleDto.setLastModifiedByUser(lastModifiedByUser);

            cweExampleRepositoryImpl.updateViewsByIdx(idx);
            cweExampleDto.setViews(cweExampleDto.getViews() + 1);
        }

        return cweExampleDto;
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenUpdate(long idx, long cweIdx) {
        List<CweExample> highPriorityList = cweExampleRepositoryImpl.findAllByHighPriorityAsc(cweIdx);
        CweExample cweExamplePriority = cweExampleRepositoryImpl.findAllPriorityByIdx(idx, cweIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "예제 코드를 출력하지 않습니다.");

        for (CweExample highPriority : highPriorityList) {
            if (cweExamplePriority.getPriority() == highPriority.getPriority()) {
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
     * @param cweExampleDto
     * @return
     */
    @Transactional
    public void updateCweExample(long idx, CweExampleDto cweExampleDto) {
        CweExample persistCweExample = cweExampleRepository.getReferenceById(idx);
        CweExample cweExample = CweExampleMapper.INSTANCE.toEntity(cweExampleDto);
        persistCweExample.update(cweExample);
        cweExampleRepository.save(persistCweExample);

        ToolInformation persistToolInformation = toolInformationRepository.getReferenceById(cweExampleDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("cwe_example")
                .toolName(cweExampleDto.getToolName())
                .toolNote(cweExampleDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getReferenceById(cweExampleDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("cwe_example")
                .compilerName(cweExampleDto.getCompilerName())
                .compilerNote(cweExampleDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteCweExampleByIdx(long idx) {
        CweExampleDto cweExampleDto = cweExampleRepositoryImpl.findByIdx(idx);

        cweExampleRepository.deleteById(idx);
        toolInformationRepository.deleteById(cweExampleDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(cweExampleDto.getCompilerInformationIdx());
    }

    /**
     * CWE 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param cweIdx
     * @param cweDto
     * @return
     */
    public CweDto findCweExampleListWhenDelete(long cweIdx, CweDto cweDto) {
        List<CweExampleDto> cweExampleDtoList = cweExampleRepositoryImpl.findAllWhenDelete(cweIdx);
        cweDto = CweMapper.INSTANCE.toDtoByExample(cweDto, cweExampleDtoList);

        return cweDto;
    }
}