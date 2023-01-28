package com.suresoft.sw_test_forum.tool.tool_configuration.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.*;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.tool.tool_configuration.domain.ToolConfiguration;
import com.suresoft.sw_test_forum.tool.tool_configuration.dto.ToolConfigurationDto;
import com.suresoft.sw_test_forum.tool.tool_configuration.dto.ToolConfigurationSearchDto;
import com.suresoft.sw_test_forum.tool.tool_configuration.dto.mapper.ConfigurationMapper;
import com.suresoft.sw_test_forum.tool.tool_configuration.repository.ToolConfigurationCommentRepositoryImpl;
import com.suresoft.sw_test_forum.tool.tool_configuration.repository.ToolConfigurationRepository;
import com.suresoft.sw_test_forum.tool.tool_configuration.repository.ToolConfigurationRepositoryImpl;
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
public class ToolConfigurationService {
    private final ToolConfigurationRepository toolConfigurationRepository;
    private final ToolConfigurationRepositoryImpl toolConfigurationRepositoryImpl;
    private final ToolConfigurationCommentRepositoryImpl toolConfigurationCommentRepositoryImpl;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final DevelopmentEnvironmentInformationRepository developmentEnvironmentInformationRepository;
    private final DevelopmentEnvironmentInformationRepositoryImpl developmentEnvironmentInformationRepositoryImpl;
    private final IdeInformationRepository ideInformationRepository;
    private final IdeInformationRepositoryImpl ideInformationRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final TargetEnvironmentInformationRepository targetEnvironmentInformationRepository;
    private final TargetEnvironmentInformationRepositoryImpl targetEnvironmentInformationRepositoryImpl;
    private final TargetInformationRepository targetInformationRepository;
    private final TargetInformationRepositoryImpl targetInformationRepositoryImpl;

    private final UserService userService;
    @Value("${module.name}")
    private String moduleName;

    public ToolConfigurationService(ToolConfigurationRepository toolConfigurationRepository,
                                    ToolConfigurationRepositoryImpl toolConfigurationRepositoryImpl,
                                    ToolConfigurationCommentRepositoryImpl toolConfigurationCommentRepositoryImpl,
                                    HashTagsRepository hashTagsRepository,
                                    HashTagsRepositoryImpl hashTagsRepositoryImpl,
                                    DevelopmentEnvironmentInformationRepository developmentEnvironmentInformationRepository,
                                    DevelopmentEnvironmentInformationRepositoryImpl developmentEnvironmentInformationRepositoryImpl,
                                    IdeInformationRepository ideInformationRepository,
                                    IdeInformationRepositoryImpl ideInformationRepositoryImpl,
                                    ToolInformationRepository toolInformationRepository,
                                    ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                    CompilerInformationRepository compilerInformationRepository,
                                    CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                    TargetEnvironmentInformationRepository targetEnvironmentInformationRepository,
                                    TargetEnvironmentInformationRepositoryImpl targetEnvironmentInformationRepositoryImpl,
                                    TargetInformationRepository targetInformationRepository,
                                    TargetInformationRepositoryImpl targetInformationRepositoryImpl,
                                    UserService userService) {
        this.toolConfigurationRepository = toolConfigurationRepository;
        this.toolConfigurationRepositoryImpl = toolConfigurationRepositoryImpl;
        this.toolConfigurationCommentRepositoryImpl = toolConfigurationCommentRepositoryImpl;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.developmentEnvironmentInformationRepository = developmentEnvironmentInformationRepository;
        this.developmentEnvironmentInformationRepositoryImpl = developmentEnvironmentInformationRepositoryImpl;
        this.ideInformationRepository = ideInformationRepository;
        this.ideInformationRepositoryImpl = ideInformationRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.targetEnvironmentInformationRepository = targetEnvironmentInformationRepository;
        this.targetEnvironmentInformationRepositoryImpl = targetEnvironmentInformationRepositoryImpl;
        this.targetInformationRepository = targetInformationRepository;
        this.targetInformationRepositoryImpl = targetInformationRepositoryImpl;
        this.userService = userService;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<ToolConfigurationDto> findAllByHighPriorityAsc() {
        List<ToolConfigurationDto> toolConfigurationDtoList = toolConfigurationRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (ToolConfigurationDto toolConfigurationDto : toolConfigurationDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(toolConfigurationDto.getCreatedByIdx());

            toolConfigurationDto.setNewIcon(NewIconCheck.isNew(toolConfigurationDto.getCreatedDate()));
            toolConfigurationDto.setCreatedByUser(createdByUser);
            toolConfigurationDto.setCommentDtoCount(toolConfigurationCommentRepositoryImpl.countAllByConfigurationIdx(toolConfigurationDto.getIdx()));
        }

        return toolConfigurationDtoList;
    }

    /**
     * 우선순위가 낮은 리스트 조회
     *
     * @param pageable
     * @param toolConfigurationSearchDto
     * @return
     */
    public Page<ToolConfigurationDto> findAll(Pageable pageable, ToolConfigurationSearchDto toolConfigurationSearchDto) {
        Page<ToolConfigurationDto> configurationDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        configurationDtoList = toolConfigurationRepositoryImpl.findAll(pageable, toolConfigurationSearchDto);

        // NewIcon 판별, createdBy, comment 갯수 설정
        for (ToolConfigurationDto toolConfigurationDto : configurationDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(toolConfigurationDto.getCreatedByIdx());

            toolConfigurationDto.setNewIcon(NewIconCheck.isNew(toolConfigurationDto.getCreatedDate()));
            toolConfigurationDto.setCreatedByUser(createdByUser);
            toolConfigurationDto.setCommentDtoCount(toolConfigurationCommentRepositoryImpl.countAllByConfigurationIdx(toolConfigurationDto.getIdx()));
        }

        return configurationDtoList;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<ToolConfigurationDto> highPriorityList = toolConfigurationRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (ToolConfigurationDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public ToolConfigurationDto findConfigurationAutoComplete(ToolConfigurationDto toolConfigurationDto) {
        // title 설정
        for (String title : toolConfigurationRepositoryImpl.findDistinctTitle()) {
            toolConfigurationDto.getAutoCompleteTitle().add(title);
        }

        // hashTags 설정
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("toolConfiguration")) {
            for (String hashTag : hashTags.split("#")) {
                toolConfigurationDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // developmentEnvironmentName 설정
        for (String developmentEnvironmentName : developmentEnvironmentInformationRepositoryImpl.findDistinctDevelopmentEnvironmentNameByTableName("toolConfiguration")) {
            toolConfigurationDto.getAutoCompleteDevelopmentEnvironmentName().add(developmentEnvironmentName);
        }

        // ideName 설정
        for (String ideName : ideInformationRepositoryImpl.findDistinctIdeName()) {
            toolConfigurationDto.getAutoCompleteIdeName().add(ideName);
        }

        // toolName 설정
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("toolConfiguration")) {
            toolConfigurationDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote 설정
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("toolConfiguration")) {
            toolConfigurationDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName 설정
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("toolConfiguration")) {
            toolConfigurationDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote 설정
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("toolConfiguration")) {
            toolConfigurationDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        // targetEnvironmentName 설정
        for (String targetEnvironmentName : targetEnvironmentInformationRepositoryImpl.findDistinctTargetEnvironmentNameByTableName("toolConfiguration")) {
            toolConfigurationDto.getAutoCompleteTargetEnvironmentName().add(targetEnvironmentName);
        }

        // boardName 설정
        for (String boardName : targetInformationRepositoryImpl.findDistinctBoardNameByTableName("toolConfiguration")) {
            toolConfigurationDto.getAutoCompleteBoardName().add(boardName);
        }

        // architecture 설정
        for (String architecture : targetInformationRepositoryImpl.findDistinctArchitectureByTableName("toolConfiguration")) {
            toolConfigurationDto.getAutoCompleteArchitecture().add(architecture);
        }

        // interfaceMethod 설정
        for (String interfaceMethod : targetInformationRepositoryImpl.findDistinctInterfaceMethodByTableName("toolConfiguration")) {
            toolConfigurationDto.getAutoCompleteInterfaceMethod().add(interfaceMethod);
        }

        // debuggerName 설정
        for (String debuggerName : targetInformationRepositoryImpl.findDistinctDebuggerByTableName("toolConfiguration")) {
            toolConfigurationDto.getAutoCompleteDebuggerName().add(debuggerName);
        }

        // executableSize 설정
        for (String executableSize : targetInformationRepositoryImpl.findDistinctExecutableSizeByTableName("toolConfiguration")) {
            toolConfigurationDto.getAutoCompleteExecutableSize().add(executableSize);
        }

        // RamUsage 설정
        for (String ramUsage : targetInformationRepositoryImpl.findDistinctRamUsageByTableName("toolConfiguration")) {
            toolConfigurationDto.getAutoCompleteRamUsage().add(ramUsage);
        }

        // RamFreeSize 설정
        for (String ramFreeSize : targetInformationRepositoryImpl.findDistinctRamFreeSizeByTableName("toolConfiguration")) {
            toolConfigurationDto.getAutoCompleteRamFreeSize().add(ramFreeSize);
        }

        // FlashFreeSize 설정
        for (String flashFreeSize : targetInformationRepositoryImpl.findDistinctFlashFreeSizeByTableName("toolConfiguration")) {
            toolConfigurationDto.getAutoCompleteFlashFreeSize().add(flashFreeSize);
        }

        return toolConfigurationDto;
    }

    /**
     * 등록
     *
     * @param toolConfigurationDto
     * @return
     */
    public long insertConfiguration(ToolConfigurationDto toolConfigurationDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("toolConfiguration")
                .content(toolConfigurationDto.getHashTags())
                .build()).getIdx();

        long ideInformationIdx = ideInformationRepository.save(IdeInformation.builder()
                .tableName("toolConfiguration")
                .ideName(toolConfigurationDto.getIdeName())
                .build()).getIdx();

        long developmentEnvironmentInformationIdx = developmentEnvironmentInformationRepository.save(DevelopmentEnvironmentInformation.builder()
                .tableName("toolConfiguration")
                .developmentEnvironmentName(toolConfigurationDto.getDevelopmentEnvironmentName())
                .build()).getIdx();

        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("toolConfiguration")
                .toolName(toolConfigurationDto.getToolName())
                .toolNote(toolConfigurationDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("toolConfiguration")
                .compilerName(toolConfigurationDto.getCompilerName())
                .compilerNote(toolConfigurationDto.getCompilerNote())
                .build()).getIdx();

        long targetEnvironmentInformationIdx = targetEnvironmentInformationRepository.save(TargetEnvironmentInformation.builder()
                .tableName("toolConfiguration")
                .targetEnvironmentName(toolConfigurationDto.getTargetEnvironmentName())
                .build()).getIdx();

        long targetInformationIdx = targetInformationRepository.save(TargetInformation.builder()
                .tableName("toolConfiguration")
                .boardName(toolConfigurationDto.getBoardName())
                .architecture(toolConfigurationDto.getArchitecture())
                .interfaceMethod(toolConfigurationDto.getInterfaceMethod())
                .debuggerName(toolConfigurationDto.getDebuggerName())
                .executableSize(toolConfigurationDto.getExecutableSize())
                .bit(toolConfigurationDto.getBit())
                .ramUsage(toolConfigurationDto.getRamUsage())
                .ramFreeSize(toolConfigurationDto.getRamFreeSize())
                .flashFreeSize(toolConfigurationDto.getFlashFreeSize())
                .build()).getIdx();

        toolConfigurationDto.setHashTagsIdx(hashTagsIdx);
        toolConfigurationDto.setDevelopmentEnvironmentInformationIdx(developmentEnvironmentInformationIdx);
        toolConfigurationDto.setIdeInformationIdx(ideInformationIdx);
        toolConfigurationDto.setToolInformationIdx(toolInformationIdx);
        toolConfigurationDto.setCompilerInformationIdx(compilerInformationIdx);
        toolConfigurationDto.setTargetEnvironmentInformationIdx(targetEnvironmentInformationIdx);
        toolConfigurationDto.setTargetInformationIdx(targetInformationIdx);

        return toolConfigurationRepository.save(ConfigurationMapper.INSTANCE.toEntity(toolConfigurationDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public ToolConfigurationDto findConfigurationByIdx(long idx) {
        ToolConfigurationDto toolConfigurationDto = new ToolConfigurationDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            toolConfigurationDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            toolConfigurationDto = toolConfigurationRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(toolConfigurationDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(toolConfigurationDto.getLastModifiedByIdx());

            toolConfigurationDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            toolConfigurationDto.setCreatedByUser(createdByUser);
            toolConfigurationDto.setLastModifiedByUser(lastModifiedByUser);

            toolConfigurationRepositoryImpl.updateViewsByIdx(idx);
            toolConfigurationDto.setViews(toolConfigurationDto.getViews() + 1);
        }

        return toolConfigurationDto;
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<ToolConfigurationDto> highPriorityList = toolConfigurationRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        ToolConfiguration toolConfigurationPriority = toolConfigurationRepositoryImpl.findConfigurationPriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (ToolConfigurationDto highPriority : highPriorityList) {
            if (toolConfigurationPriority.getPriority() == highPriority.getPriority()) {
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
     * @param toolConfigurationDto
     */
    @Transactional
    public void updateConfiguration(long idx, ToolConfigurationDto toolConfigurationDto) {
        ToolConfiguration persistToolConfiguration = toolConfigurationRepository.getReferenceById(idx);
        ToolConfiguration toolConfiguration = ConfigurationMapper.INSTANCE.toEntity(toolConfigurationDto);
        persistToolConfiguration.update(toolConfiguration);
        toolConfigurationRepository.save(persistToolConfiguration);

        HashTags persistHashTags = hashTagsRepository.getReferenceById(toolConfigurationDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("toolConfiguration")
                .content(toolConfigurationDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        DevelopmentEnvironmentInformation persistDevelopmentEnvironmentInformation = developmentEnvironmentInformationRepository.getReferenceById(toolConfigurationDto.getDevelopmentEnvironmentInformationIdx());
        persistDevelopmentEnvironmentInformation.update(DevelopmentEnvironmentInformation.builder()
                .tableName("toolConfiguration")
                .developmentEnvironmentName(toolConfigurationDto.getDevelopmentEnvironmentName())
                .build());
        developmentEnvironmentInformationRepository.save(persistDevelopmentEnvironmentInformation);

        IdeInformation persistIdeInformation = ideInformationRepository.getReferenceById(toolConfigurationDto.getIdeInformationIdx());
        persistIdeInformation.update(IdeInformation.builder()
                .tableName("toolConfiguration")
                .ideName(toolConfigurationDto.getIdeName())
                .build());
        ideInformationRepository.save(persistIdeInformation);

        ToolInformation persistToolInformation = toolInformationRepository.getReferenceById(toolConfigurationDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("toolConfiguration")
                .toolName(toolConfigurationDto.getToolName())
                .toolNote(toolConfigurationDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getReferenceById(toolConfigurationDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("toolConfiguration")
                .compilerName(toolConfigurationDto.getCompilerName())
                .compilerNote(toolConfigurationDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);

        TargetEnvironmentInformation persistTargetEnvironmentInformation = targetEnvironmentInformationRepository.getReferenceById(toolConfigurationDto.getTargetEnvironmentInformationIdx());
        persistTargetEnvironmentInformation.update(TargetEnvironmentInformation.builder()
                .tableName("toolConfiguration")
                .targetEnvironmentName(toolConfigurationDto.getTargetEnvironmentName())
                .build());
        targetEnvironmentInformationRepository.save(persistTargetEnvironmentInformation);

        TargetInformation persistTargetInformation = targetInformationRepository.getReferenceById(toolConfigurationDto.getTargetInformationIdx());
        persistTargetInformation.update(TargetInformation.builder()
                .tableName("toolConfiguration")
                .boardName(toolConfigurationDto.getBoardName())
                .architecture(toolConfigurationDto.getArchitecture())
                .interfaceMethod(toolConfigurationDto.getInterfaceMethod())
                .debuggerName(toolConfigurationDto.getDebuggerName())
                .executableSize(toolConfigurationDto.getExecutableSize())
                .bit(toolConfigurationDto.getBit())
                .ramUsage(toolConfigurationDto.getRamUsage())
                .ramFreeSize(toolConfigurationDto.getRamFreeSize())
                .flashFreeSize(toolConfigurationDto.getFlashFreeSize())
                .build());
        targetEnvironmentInformationRepository.save(persistTargetEnvironmentInformation);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteConfigurationByIdx(long idx) {
        ToolConfigurationDto toolConfigurationDto = toolConfigurationRepositoryImpl.findByIdx(idx);

        toolConfigurationRepository.deleteById(idx);
        hashTagsRepository.deleteById(toolConfigurationDto.getHashTagsIdx());
        ideInformationRepository.deleteById(toolConfigurationDto.getIdeInformationIdx());
        toolInformationRepository.deleteById(toolConfigurationDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(toolConfigurationDto.getCompilerInformationIdx());
    }
}