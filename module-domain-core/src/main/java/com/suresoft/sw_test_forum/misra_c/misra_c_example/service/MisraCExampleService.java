package com.suresoft.sw_test_forum.misra_c.misra_c_example.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepository;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepositoryImpl;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepository;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepositoryImpl;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.MisraCDto;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.mapper.MisraCMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c.service.MisraCService;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.domain.MisraCExample;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.MisraCExampleDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.MisraCExampleSearchDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.mapper.MisraCExampleMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.repository.MisraCExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.repository.MisraCExampleRepository;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.repository.MisraCExampleRepositoryImpl;
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
public class MisraCExampleService {
    private final MisraCExampleRepository misraCExampleRepository;
    private final MisraCExampleRepositoryImpl misraCExampleRepositoryImpl;
    private final MisraCExampleCommentRepositoryImpl misraCExampleCommentRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final MisraCService misraCService;
    @Value("${module.name}")
    private String moduleName;

    public MisraCExampleService(MisraCExampleRepository misraCExampleRepository,
                                MisraCExampleRepositoryImpl misraCExampleRepositoryImpl,
                                MisraCExampleCommentRepositoryImpl misraCExampleCommentRepositoryImpl,
                                ToolInformationRepository toolInformationRepository,
                                ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                CompilerInformationRepository compilerInformationRepository,
                                CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                UserService userService,
                                MisraCService misraCService) {
        this.misraCExampleRepository = misraCExampleRepository;
        this.misraCExampleRepositoryImpl = misraCExampleRepositoryImpl;
        this.misraCExampleCommentRepositoryImpl = misraCExampleCommentRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.misraCService = misraCService;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param misraCExampleSearchDto
     * @return
     */
    public Page<MisraCExampleDto> findMisraCExampleList(Pageable pageable, MisraCExampleSearchDto misraCExampleSearchDto) {
        Page<MisraCExampleDto> misraCExampleDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        misraCExampleDtoList = misraCExampleRepositoryImpl.findAll(pageable, misraCExampleSearchDto);

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (MisraCExampleDto misraCExampleDto : misraCExampleDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCExampleDto.getCreatedByIdx());

            misraCExampleDto.setNewIcon(NewIconCheck.isNew(misraCExampleDto.getCreatedDate()));
            misraCExampleDto.setCreatedByUser(createdByUser);
            misraCExampleDto.setCommentDtoCount(misraCExampleCommentRepositoryImpl.countAllByMisraCExampleIdx(misraCExampleDto.getIdx()));

            // MISRA C 예제 코드 리스트를 조회하는 경우(MISRA C 보기 페이지에서 이동하지 않음)
            if (misraCExampleSearchDto.getMisraCIdx() == 0) {
                misraCExampleDto.setMisraCRule(misraCService.findMisraCRuleByIdx(misraCExampleDto.getMisraCIdx()));
            }
        }

        return misraCExampleDtoList;
    }

    /**
     * MISRA C 읽기 페이지 일 때, 리스트 조회
     *
     * @param misraCIdx
     * @param misraCDto
     * @return
     */
    public MisraCDto findMisraCExampleList(long misraCIdx, MisraCDto misraCDto) {
        List<MisraCExampleDto> misraCExampleDtoList = misraCExampleRepositoryImpl.findAll(misraCIdx);

        // NewIcon 판별, createdBy 설정
        for (MisraCExampleDto misraCExampleDto : misraCExampleDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCExampleDto.getCreatedByIdx());

            misraCExampleDto.setNewIcon(NewIconCheck.isNew(misraCExampleDto.getCreatedDate()));
            misraCExampleDto.setCreatedByUser(createdByUser);
        }

        misraCDto = MisraCMapper.INSTANCE.toDtoByExample(misraCDto, misraCExampleDtoList);

        return misraCDto;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenWrite(long misraCIdx) {
        List<MisraCExample> highPriorityList = misraCExampleRepositoryImpl.findAllByHighPriorityAsc(misraCIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "예제 코드를 출력하지 않습니다.");

        for (MisraCExample highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public MisraCExampleDto findMisraCExampleAutoComplete(MisraCExampleDto misraCExampleDto) {
        // title 설정
        for (String title : misraCExampleRepositoryImpl.findDistinctTitle()) {
            misraCExampleDto.getAutoCompleteTitle().add(title);
        }

        // toolName 설정
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("misra_c_example")) {
            misraCExampleDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote 설정
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("misra_c_example")) {
            misraCExampleDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName 설정
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("misra_c_example")) {
            misraCExampleDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote 설정
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("misra_c_example")) {
            misraCExampleDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return misraCExampleDto;
    }

    /**
     * 등록
     *
     * @param misraCExampleDto
     * @return
     */
    public long insertMisraCExample(MisraCExampleDto misraCExampleDto) {
        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("misra_c_example")
                .toolName(misraCExampleDto.getToolName())
                .toolNote(misraCExampleDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("misra_c_example")
                .compilerName(misraCExampleDto.getCompilerName())
                .compilerNote(misraCExampleDto.getCompilerNote())
                .build()).getIdx();

        misraCExampleDto.setToolInformationIdx(toolInformationIdx);
        misraCExampleDto.setCompilerInformationIdx(compilerInformationIdx);

        return misraCExampleRepository.save(MisraCExampleMapper.INSTANCE.toEntity(misraCExampleDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public MisraCExampleDto findMisraCExampleByIdx(long idx) {
        MisraCExampleDto misraCExampleDto = new MisraCExampleDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            misraCExampleDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            misraCExampleDto = misraCExampleRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCExampleDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCExampleDto.getLastModifiedByIdx());

            misraCExampleDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            misraCExampleDto.setCreatedByUser(createdByUser);
            misraCExampleDto.setLastModifiedByUser(lastModifiedByUser);

            misraCExampleRepositoryImpl.updateViewsByIdx(idx);
            misraCExampleDto.setViews(misraCExampleDto.getViews() + 1);
        }

        return misraCExampleDto;
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenUpdate(long idx, long misraCIdx) {
        List<MisraCExample> highPriorityList = misraCExampleRepositoryImpl.findAllByHighPriorityAsc(misraCIdx);
        MisraCExample misraCExamplePriority = misraCExampleRepositoryImpl.findAllPriorityByIdx(idx, misraCIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "예제 코드를 출력하지 않습니다.");

        for (MisraCExample highPriority : highPriorityList) {
            if (misraCExamplePriority.getPriority() == highPriority.getPriority()) {
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
     * @param misraCExampleDto
     * @return
     */
    @Transactional
    public void updateMisraCExample(long idx, MisraCExampleDto misraCExampleDto) {
        MisraCExample persistMisraCExample = misraCExampleRepository.getReferenceById(idx);
        MisraCExample misraCExample = MisraCExampleMapper.INSTANCE.toEntity(misraCExampleDto);
        persistMisraCExample.update(misraCExample);
        misraCExampleRepository.save(persistMisraCExample);

        ToolInformation persistToolInformation = toolInformationRepository.getReferenceById(misraCExampleDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("misra_c_example")
                .toolName(misraCExampleDto.getToolName())
                .toolNote(misraCExampleDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getReferenceById(misraCExampleDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("misra_c_example")
                .compilerName(misraCExampleDto.getCompilerName())
                .compilerNote(misraCExampleDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteMisraCExampleByIdx(long idx) {
        MisraCExampleDto misraCExampleDto = misraCExampleRepositoryImpl.findByIdx(idx);

        misraCExampleRepository.deleteById(idx);
        toolInformationRepository.deleteById(misraCExampleDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(misraCExampleDto.getCompilerInformationIdx());
    }

    /**
     * MISRA C 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param misraCIdx
     * @param misraCDto
     * @return
     */
    public MisraCDto findMisraCExampleListWhenDelete(long misraCIdx, MisraCDto misraCDto) {
        List<MisraCExampleDto> misraCExampleDtoList = misraCExampleRepositoryImpl.findAllWhenDelete(misraCIdx);
        misraCDto = MisraCMapper.INSTANCE.toDtoByExample(misraCDto, misraCExampleDtoList);

        return misraCDto;
    }
}