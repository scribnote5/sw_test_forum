package com.suresoft.sw_test_forum.fx_cop.fx_cop_example.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepository;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepositoryImpl;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepository;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepositoryImpl;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.FxCopDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.mapper.FxCopMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.service.FxCopService;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.domain.FxCopExample;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.FxCopExampleDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.FxCopExampleSearchDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.mapper.FxCopExampleMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.repository.FxCopExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.repository.FxCopExampleRepository;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.repository.FxCopExampleRepositoryImpl;
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
public class FxCopExampleService {
    private final FxCopExampleRepository fxCopExampleRepository;
    private final FxCopExampleRepositoryImpl fxCopExampleRepositoryImpl;
    private final FxCopExampleCommentRepositoryImpl fxCopExampleCommentRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final FxCopService fxCopService;
    @Value("${module.name}")
    private String moduleName;

    public FxCopExampleService(FxCopExampleRepository fxCopExampleRepository,
                               FxCopExampleRepositoryImpl fxCopExampleRepositoryImpl,
                               FxCopExampleCommentRepositoryImpl fxCopExampleCommentRepositoryImpl,
                               ToolInformationRepository toolInformationRepository,
                               ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                               CompilerInformationRepository compilerInformationRepository,
                               CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                               UserService userService,
                               FxCopService fxCopService) {
        this.fxCopExampleRepository = fxCopExampleRepository;
        this.fxCopExampleRepositoryImpl = fxCopExampleRepositoryImpl;
        this.fxCopExampleCommentRepositoryImpl = fxCopExampleCommentRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.fxCopService = fxCopService;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param fxCopExampleSearchDto
     * @return
     */
    public Page<FxCopExampleDto> findFxCopExampleList(Pageable pageable, FxCopExampleSearchDto fxCopExampleSearchDto) {
        Page<FxCopExampleDto> fxCopExampleDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        fxCopExampleDtoList = fxCopExampleRepositoryImpl.findAll(pageable, fxCopExampleSearchDto);

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (FxCopExampleDto fxCopExampleDto : fxCopExampleDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(fxCopExampleDto.getCreatedByIdx());

            fxCopExampleDto.setNewIcon(NewIconCheck.isNew(fxCopExampleDto.getCreatedDate()));
            fxCopExampleDto.setCreatedByUser(createdByUser);
            fxCopExampleDto.setCommentDtoCount(fxCopExampleCommentRepositoryImpl.countAllByFxCopExampleIdx(fxCopExampleDto.getIdx()));

            // FxCop 예제 코드 리스트를 조회하는 경우(FxCop 보기 페이지에서 이동하지 않음)
            if (fxCopExampleSearchDto.getFxCopIdx() == 0) {
                fxCopExampleDto.setFxCopRule(fxCopService.findFxCopRuleByIdx(fxCopExampleDto.getFxCopIdx()));
            }
        }

        return fxCopExampleDtoList;
    }

    /**
     * FxCop 읽기 페이지 일 때, 리스트 조회
     *
     * @param fxCopIdx
     * @param fxCopDto
     * @return
     */
    public FxCopDto findFxCopExampleList(long fxCopIdx, FxCopDto fxCopDto) {
        List<FxCopExampleDto> fxCopExampleDtoList = fxCopExampleRepositoryImpl.findAll(fxCopIdx);

        // NewIcon 판별, createdBy 설정
        for (FxCopExampleDto fxCopExampleDto : fxCopExampleDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(fxCopExampleDto.getCreatedByIdx());

            fxCopExampleDto.setNewIcon(NewIconCheck.isNew(fxCopExampleDto.getCreatedDate()));
            fxCopExampleDto.setCreatedByUser(createdByUser);
        }

        fxCopDto = FxCopMapper.INSTANCE.toDtoByExample(fxCopDto, fxCopExampleDtoList);

        return fxCopDto;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenWrite(long fxCopIdx) {
        List<FxCopExample> highPriorityList = fxCopExampleRepositoryImpl.findAllByHighPriorityAsc(fxCopIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "예제 코드를 출력하지 않습니다.");

        for (FxCopExample highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public FxCopExampleDto findFxCopExampleAutoComplete(FxCopExampleDto fxCopExampleDto) {
        // title 설정
        for (String title : fxCopExampleRepositoryImpl.findDistinctTitle()) {
            fxCopExampleDto.getAutoCompleteTitle().add(title);
        }

        // toolName 설정
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("fx_cop_example")) {
            fxCopExampleDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote 설정
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("fx_cop_example")) {
            fxCopExampleDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName 설정
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("fx_cop_example")) {
            fxCopExampleDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote 설정
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("fx_cop_example")) {
            fxCopExampleDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return fxCopExampleDto;
    }

    /**
     * 등록
     *
     * @param fxCopExampleDto
     * @return
     */
    public long insertFxCopExample(FxCopExampleDto fxCopExampleDto) {
        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("fx_cop_example")
                .toolName(fxCopExampleDto.getToolName())
                .toolNote(fxCopExampleDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("fx_cop_example")
                .compilerName(fxCopExampleDto.getCompilerName())
                .compilerNote(fxCopExampleDto.getCompilerNote())
                .build()).getIdx();

        fxCopExampleDto.setToolInformationIdx(toolInformationIdx);
        fxCopExampleDto.setCompilerInformationIdx(compilerInformationIdx);

        return fxCopExampleRepository.save(FxCopExampleMapper.INSTANCE.toEntity(fxCopExampleDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public FxCopExampleDto findFxCopExampleByIdx(long idx) {
        FxCopExampleDto fxCopExampleDto = new FxCopExampleDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            fxCopExampleDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            fxCopExampleDto = fxCopExampleRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(fxCopExampleDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(fxCopExampleDto.getLastModifiedByIdx());

            fxCopExampleDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            fxCopExampleDto.setCreatedByUser(createdByUser);
            fxCopExampleDto.setLastModifiedByUser(lastModifiedByUser);

            fxCopExampleRepositoryImpl.updateViewsByIdx(idx);
            fxCopExampleDto.setViews(fxCopExampleDto.getViews() + 1);
        }

        return fxCopExampleDto;
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenUpdate(long idx, long fxCopIdx) {
        List<FxCopExample> highPriorityList = fxCopExampleRepositoryImpl.findAllByHighPriorityAsc(fxCopIdx);
        FxCopExample fxCopExamplePriority = fxCopExampleRepositoryImpl.findAllPriorityByIdx(idx, fxCopIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "예제 코드를 출력하지 않습니다.");

        for (FxCopExample highPriority : highPriorityList) {
            if (fxCopExamplePriority.getPriority() == highPriority.getPriority()) {
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
     * @param fxCopExampleDto
     * @return
     */
    @Transactional
    public void updateFxCopExample(long idx, FxCopExampleDto fxCopExampleDto) {
        FxCopExample persistFxCopExample = fxCopExampleRepository.getReferenceById(idx);
        FxCopExample fxCopExample = FxCopExampleMapper.INSTANCE.toEntity(fxCopExampleDto);
        persistFxCopExample.update(fxCopExample);
        fxCopExampleRepository.save(persistFxCopExample);

        ToolInformation persistToolInformation = toolInformationRepository.getReferenceById(fxCopExampleDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("fx_cop_example")
                .toolName(fxCopExampleDto.getToolName())
                .toolNote(fxCopExampleDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getReferenceById(fxCopExampleDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("fx_cop_example")
                .compilerName(fxCopExampleDto.getCompilerName())
                .compilerNote(fxCopExampleDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteFxCopExampleByIdx(long idx) {
        FxCopExampleDto fxCopExampleDto = fxCopExampleRepositoryImpl.findByIdx(idx);

        fxCopExampleRepository.deleteById(idx);
        toolInformationRepository.deleteById(fxCopExampleDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(fxCopExampleDto.getCompilerInformationIdx());
    }

    /**
     * FxCop 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param fxCopIdx
     * @param fxCopDto
     * @return
     */
    public FxCopDto findFxCopExampleListWhenDelete(long fxCopIdx, FxCopDto fxCopDto) {
        List<FxCopExampleDto> fxCopExampleDtoList = fxCopExampleRepositoryImpl.findAllWhenDelete(fxCopIdx);
        fxCopDto = FxCopMapper.INSTANCE.toDtoByExample(fxCopDto, fxCopExampleDtoList);

        return fxCopDto;
    }
}