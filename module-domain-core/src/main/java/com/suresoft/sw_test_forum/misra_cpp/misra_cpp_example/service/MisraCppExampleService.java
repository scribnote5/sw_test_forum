package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepository;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepositoryImpl;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepository;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepositoryImpl;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.MisraCppDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.mapper.MisraCppMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.service.MisraCppService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.domain.MisraCppExample;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.MisraCppExampleDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.MisraCppExampleSearchDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.mapper.MisraCppExampleMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.repository.MisraCppExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.repository.MisraCppExampleRepository;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.repository.MisraCppExampleRepositoryImpl;
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
public class MisraCppExampleService {
    private final MisraCppExampleRepository misraCppExampleRepository;
    private final MisraCppExampleRepositoryImpl misraCppExampleRepositoryImpl;
    private final MisraCppExampleCommentRepositoryImpl misraCppExampleCommentRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final MisraCppService misraCppService;
    @Value("${module.name}")
    private String moduleName;

    public MisraCppExampleService(MisraCppExampleRepository misraCppExampleRepository,
                                  MisraCppExampleRepositoryImpl misraCppExampleRepositoryImpl,
                                  MisraCppExampleCommentRepositoryImpl misraCppExampleCommentRepositoryImpl,
                                  ToolInformationRepository toolInformationRepository,
                                  ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                  CompilerInformationRepository compilerInformationRepository,
                                  CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                  UserService userService,
                                  MisraCppService misraCppService) {
        this.misraCppExampleRepository = misraCppExampleRepository;
        this.misraCppExampleRepositoryImpl = misraCppExampleRepositoryImpl;
        this.misraCppExampleCommentRepositoryImpl = misraCppExampleCommentRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.misraCppService = misraCppService;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param misraCppExampleSearchDto
     * @return
     */
    public Page<MisraCppExampleDto> findMisraCppExampleList(Pageable pageable, MisraCppExampleSearchDto misraCppExampleSearchDto) {
        Page<MisraCppExampleDto> misraCppExampleDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        misraCppExampleDtoList = misraCppExampleRepositoryImpl.findAll(pageable, misraCppExampleSearchDto);

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (MisraCppExampleDto misraCppExampleDto : misraCppExampleDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCppExampleDto.getCreatedByIdx());

            misraCppExampleDto.setNewIcon(NewIconCheck.isNew(misraCppExampleDto.getCreatedDate()));
            misraCppExampleDto.setCreatedByUser(createdByUser);
            misraCppExampleDto.setCommentDtoCount(misraCppExampleCommentRepositoryImpl.countAllByMisraCppExampleIdx(misraCppExampleDto.getIdx()));

            // MISRA CPP 예제 코드 리스트를 조회하는 경우(MISRA CPP 보기 페이지에서 이동하지 않음)
            if (misraCppExampleSearchDto.getMisraCppIdx() == 0) {
                misraCppExampleDto.setMisraCppRule(misraCppService.findMisraCppRuleByIdx(misraCppExampleDto.getMisraCppIdx()));
            }
        }

        return misraCppExampleDtoList;
    }

    /**
     * MISRA CPP 읽기 페이지 일 때, 리스트 조회
     *
     * @param misraCppIdx
     * @param misraCppDto
     * @return
     */
    public MisraCppDto findMisraCppExampleList(long misraCppIdx, MisraCppDto misraCppDto) {
        List<MisraCppExampleDto> misraCppExampleDtoList = misraCppExampleRepositoryImpl.findAll(misraCppIdx);

        // NewIcon 판별, createdBy 설정
        for (MisraCppExampleDto misraCppExampleDto : misraCppExampleDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCppExampleDto.getCreatedByIdx());

            misraCppExampleDto.setNewIcon(NewIconCheck.isNew(misraCppExampleDto.getCreatedDate()));
            misraCppExampleDto.setCreatedByUser(createdByUser);
        }

        misraCppDto = MisraCppMapper.INSTANCE.toDtoByExample(misraCppDto, misraCppExampleDtoList);

        return misraCppDto;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenWrite(long misraCppIdx) {
        List<MisraCppExample> highPriorityList = misraCppExampleRepositoryImpl.findAllByHighPriorityAsc(misraCppIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "예제 코드를 출력하지 않습니다.");

        for (MisraCppExample highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public MisraCppExampleDto findMisraCppExampleAutoComplete(MisraCppExampleDto misraCppExampleDto) {
        // title 설정
        for (String title : misraCppExampleRepositoryImpl.findDistinctTitle()) {
            misraCppExampleDto.getAutoCompleteTitle().add(title);
        }

        // toolName 설정
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("misra_cpp_example")) {
            misraCppExampleDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote 설정
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("misra_cpp_example")) {
            misraCppExampleDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName 설정
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("misra_cpp_example")) {
            misraCppExampleDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote 설정
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("misra_cpp_example")) {
            misraCppExampleDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return misraCppExampleDto;
    }

    /**
     * 등록
     *
     * @param misraCppExampleDto
     * @return
     */
    public long insertMisraCppExample(MisraCppExampleDto misraCppExampleDto) {
        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("misra_cpp_example")
                .toolName(misraCppExampleDto.getToolName())
                .toolNote(misraCppExampleDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("misra_cpp_example")
                .compilerName(misraCppExampleDto.getCompilerName())
                .compilerNote(misraCppExampleDto.getCompilerNote())
                .build()).getIdx();

        misraCppExampleDto.setToolInformationIdx(toolInformationIdx);
        misraCppExampleDto.setCompilerInformationIdx(compilerInformationIdx);

        return misraCppExampleRepository.save(MisraCppExampleMapper.INSTANCE.toEntity(misraCppExampleDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public MisraCppExampleDto findMisraCppExampleByIdx(long idx) {
        MisraCppExampleDto misraCppExampleDto = new MisraCppExampleDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            misraCppExampleDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            misraCppExampleDto = misraCppExampleRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCppExampleDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCppExampleDto.getLastModifiedByIdx());

            misraCppExampleDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            misraCppExampleDto.setCreatedByUser(createdByUser);
            misraCppExampleDto.setLastModifiedByUser(lastModifiedByUser);

            misraCppExampleRepositoryImpl.updateViewsByIdx(idx);
            misraCppExampleDto.setViews(misraCppExampleDto.getViews() + 1);
        }

        return misraCppExampleDto;
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenUpdate(long idx, long misraCppIdx) {
        List<MisraCppExample> highPriorityList = misraCppExampleRepositoryImpl.findAllByHighPriorityAsc(misraCppIdx);
        MisraCppExample misraCppExamplePriority = misraCppExampleRepositoryImpl.findAllPriorityByIdx(idx, misraCppIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "예제 코드를 출력하지 않습니다.");

        for (MisraCppExample highPriority : highPriorityList) {
            if (misraCppExamplePriority.getPriority() == highPriority.getPriority()) {
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
     * @param misraCppExampleDto
     * @return
     */
    @Transactional
    public void updateMisraCppExample(long idx, MisraCppExampleDto misraCppExampleDto) {
        MisraCppExample persistMisraCppExample = misraCppExampleRepository.getReferenceById(idx);
        MisraCppExample misraCppExample = MisraCppExampleMapper.INSTANCE.toEntity(misraCppExampleDto);
        persistMisraCppExample.update(misraCppExample);
        misraCppExampleRepository.save(persistMisraCppExample);

        ToolInformation persistToolInformation = toolInformationRepository.getReferenceById(misraCppExampleDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("misra_cpp_example")
                .toolName(misraCppExampleDto.getToolName())
                .toolNote(misraCppExampleDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getReferenceById(misraCppExampleDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("misra_cpp_example")
                .compilerName(misraCppExampleDto.getCompilerName())
                .compilerNote(misraCppExampleDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteMisraCppExampleByIdx(long idx) {
        MisraCppExampleDto misraCppExampleDto = misraCppExampleRepositoryImpl.findByIdx(idx);

        misraCppExampleRepository.deleteById(idx);
        toolInformationRepository.deleteById(misraCppExampleDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(misraCppExampleDto.getCompilerInformationIdx());
    }

    /**
     * MISRA CPP 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param misraCppIdx
     * @param misraCppDto
     * @return
     */
    public MisraCppDto findMisraCppExampleListWhenDelete(long misraCppIdx, MisraCppDto misraCppDto) {
        List<MisraCppExampleDto> misraCppExampleDtoList = misraCppExampleRepositoryImpl.findAllWhenDelete(misraCppIdx);
        misraCppDto = MisraCppMapper.INSTANCE.toDtoByExample(misraCppDto, misraCppExampleDtoList);

        return misraCppDto;
    }
}