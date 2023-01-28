package com.suresoft.sw_test_forum.tool.tool_trouble_shooting.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.domain.IdeInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain.ToolTroubleShooting;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.ToolTroubleShootingDto;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.ToolTroubleShootingSearchDto;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.mapper.ToolTroubleShootingMapper;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.repository.ToolTroubleShootingCommentRepositoryImpl;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.repository.ToolTroubleShootingRepository;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.repository.ToolTroubleShootingRepositoryImpl;
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
public class ToolTroubleShootingService {
    private final ToolTroubleShootingRepository toolTroubleShootingRepository;
    private final ToolTroubleShootingRepositoryImpl toolTroubleShootingRepositoryImpl;
    private final ToolTroubleShootingCommentRepositoryImpl toolTroubleShootingCommentRepositoryImpl;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final IdeInformationRepository ideInformationRepository;
    private final IdeInformationRepositoryImpl ideInformationRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    @Value("${module.name}")
    private String moduleName;

    public ToolTroubleShootingService(ToolTroubleShootingRepository toolTroubleShootingRepository,
                                      ToolTroubleShootingRepositoryImpl toolTroubleShootingRepositoryImpl,
                                      ToolTroubleShootingCommentRepositoryImpl toolTroubleShootingCommentRepositoryImpl,
                                      HashTagsRepository hashTagsRepository,
                                      HashTagsRepositoryImpl hashTagsRepositoryImpl,
                                      IdeInformationRepository ideInformationRepository,
                                      IdeInformationRepositoryImpl ideInformationRepositoryImpl,
                                      ToolInformationRepository toolInformationRepository,
                                      ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                      CompilerInformationRepository compilerInformationRepository,
                                      CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                      UserService userService) {
        this.toolTroubleShootingRepository = toolTroubleShootingRepository;
        this.toolTroubleShootingRepositoryImpl = toolTroubleShootingRepositoryImpl;
        this.toolTroubleShootingCommentRepositoryImpl = toolTroubleShootingCommentRepositoryImpl;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.ideInformationRepository = ideInformationRepository;
        this.ideInformationRepositoryImpl = ideInformationRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<ToolTroubleShootingDto> findAllByHighPriorityAsc() {
        List<ToolTroubleShootingDto> toolTroubleShootingDtoList = toolTroubleShootingRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (ToolTroubleShootingDto toolTroubleShootingDto : toolTroubleShootingDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(toolTroubleShootingDto.getCreatedByIdx());

            toolTroubleShootingDto.setNewIcon(NewIconCheck.isNew(toolTroubleShootingDto.getCreatedDate()));
            toolTroubleShootingDto.setCreatedByUser(createdByUser);
            toolTroubleShootingDto.setCommentDtoCount(toolTroubleShootingCommentRepositoryImpl.countAllByToolTroubleShootingIdx(toolTroubleShootingDto.getIdx()));
        }

        return toolTroubleShootingDtoList;
    }

    /**
     * 우선순위가 낮은 리스트 조회
     *
     * @param pageable
     * @param toolTroubleShootingSearchDto
     * @return
     */
    public Page<ToolTroubleShootingDto> findAll(Pageable pageable, ToolTroubleShootingSearchDto toolTroubleShootingSearchDto) {
        Page<ToolTroubleShootingDto> toolTroubleShootingDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        toolTroubleShootingDtoList = toolTroubleShootingRepositoryImpl.findAll(pageable, toolTroubleShootingSearchDto);

        // NewIcon 판별, createdBy, comment 갯수 설정
        for (ToolTroubleShootingDto toolTroubleShootingDto : toolTroubleShootingDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(toolTroubleShootingDto.getCreatedByIdx());

            toolTroubleShootingDto.setNewIcon(NewIconCheck.isNew(toolTroubleShootingDto.getCreatedDate()));
            toolTroubleShootingDto.setCreatedByUser(createdByUser);
            toolTroubleShootingDto.setCommentDtoCount(toolTroubleShootingCommentRepositoryImpl.countAllByToolTroubleShootingIdx(toolTroubleShootingDto.getIdx()));
        }

        return toolTroubleShootingDtoList;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<ToolTroubleShootingDto> highPriorityList = toolTroubleShootingRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (ToolTroubleShootingDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public ToolTroubleShootingDto findToolTroubleShootingAutoComplete(ToolTroubleShootingDto toolTroubleShootingDto) {
        // title 설정
        for (String title : toolTroubleShootingRepositoryImpl.findDistinctTitle()) {
            toolTroubleShootingDto.getAutoCompleteTitle().add(title);
        }

        // hashTags 설정
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("tool_trouble_shooting")) {
            for (String hashTag : hashTags.split("#")) {
                toolTroubleShootingDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // ideName 설정
        for (String ideName : ideInformationRepositoryImpl.findDistinctIdeName()) {
            toolTroubleShootingDto.getAutoCompleteIdeName().add(ideName);
        }

        // toolName 설정
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("tool_trouble_shooting")) {
            toolTroubleShootingDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote 설정
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("tool_trouble_shooting")) {
            toolTroubleShootingDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName 설정
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("tool_trouble_shooting")) {
            toolTroubleShootingDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote 설정
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("tool_trouble_shooting")) {
            toolTroubleShootingDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return toolTroubleShootingDto;
    }

    /**
     * 등록
     *
     * @param toolTroubleShootingDto
     * @return
     */
    public long insertToolTroubleShooting(ToolTroubleShootingDto toolTroubleShootingDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("tool_trouble_shooting")
                .content(toolTroubleShootingDto.getHashTags())
                .build()).getIdx();

        long ideInformationIdx = ideInformationRepository.save(IdeInformation.builder()
                .tableName("tool_trouble_shooting")
                .ideName(toolTroubleShootingDto.getIdeName())
                .build()).getIdx();

        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("tool_trouble_shooting")
                .toolName(toolTroubleShootingDto.getToolName())
                .toolNote(toolTroubleShootingDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("tool_trouble_shooting")
                .compilerName(toolTroubleShootingDto.getCompilerName())
                .compilerNote(toolTroubleShootingDto.getCompilerNote())
                .build()).getIdx();

        toolTroubleShootingDto.setHashTagsIdx(hashTagsIdx);
        toolTroubleShootingDto.setIdeInformationIdx(ideInformationIdx);
        toolTroubleShootingDto.setToolInformationIdx(toolInformationIdx);
        toolTroubleShootingDto.setCompilerInformationIdx(compilerInformationIdx);

        return toolTroubleShootingRepository.save(ToolTroubleShootingMapper.INSTANCE.toEntity(toolTroubleShootingDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public ToolTroubleShootingDto findToolTroubleShootingByIdx(long idx) {
        ToolTroubleShootingDto toolTroubleShootingDto = new ToolTroubleShootingDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            toolTroubleShootingDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            toolTroubleShootingDto = toolTroubleShootingRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(toolTroubleShootingDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(toolTroubleShootingDto.getLastModifiedByIdx());

            toolTroubleShootingDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            toolTroubleShootingDto.setCreatedByUser(createdByUser);
            toolTroubleShootingDto.setLastModifiedByUser(lastModifiedByUser);

            toolTroubleShootingRepositoryImpl.updateViewsByIdx(idx);
            toolTroubleShootingDto.setViews(toolTroubleShootingDto.getViews() + 1);
        }

        return toolTroubleShootingDto;
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<ToolTroubleShootingDto> highPriorityList = toolTroubleShootingRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        ToolTroubleShooting toolTroubleShootingPriority = toolTroubleShootingRepositoryImpl.findToolTroubleShootingPriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (ToolTroubleShootingDto highPriority : highPriorityList) {
            if (toolTroubleShootingPriority.getPriority() == highPriority.getPriority()) {
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
     * @param toolTroubleShootingDto
     */
    @Transactional
    public void updateToolTroubleShooting(long idx, ToolTroubleShootingDto toolTroubleShootingDto) {
        ToolTroubleShooting persistToolTroubleShooting = toolTroubleShootingRepository.getReferenceById(idx);
        ToolTroubleShooting toolTroubleShooting = ToolTroubleShootingMapper.INSTANCE.toEntity(toolTroubleShootingDto);
        persistToolTroubleShooting.update(toolTroubleShooting);
        toolTroubleShootingRepository.save(persistToolTroubleShooting);

        HashTags persistHashTags = hashTagsRepository.getReferenceById(toolTroubleShootingDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("tool_trouble_shooting")
                .content(toolTroubleShootingDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        IdeInformation persistIdeInformation = ideInformationRepository.getReferenceById(toolTroubleShootingDto.getIdeInformationIdx());
        persistIdeInformation.update(IdeInformation.builder()
                .tableName("tool_trouble_shooting")
                .ideName(toolTroubleShootingDto.getIdeName())
                .build());
        ideInformationRepository.save(persistIdeInformation);

        ToolInformation persistToolInformation = toolInformationRepository.getReferenceById(toolTroubleShootingDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("tool_trouble_shooting")
                .toolName(toolTroubleShootingDto.getToolName())
                .toolNote(toolTroubleShootingDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getReferenceById(toolTroubleShootingDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("tool_trouble_shooting")
                .compilerName(toolTroubleShootingDto.getCompilerName())
                .compilerNote(toolTroubleShootingDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteToolTroubleShootingByIdx(long idx) {
        ToolTroubleShootingDto toolTroubleShootingDto = toolTroubleShootingRepositoryImpl.findByIdx(idx);

        toolTroubleShootingRepository.deleteById(idx);
        hashTagsRepository.deleteById(toolTroubleShootingDto.getHashTagsIdx());
        ideInformationRepository.deleteById(toolTroubleShootingDto.getIdeInformationIdx());
        toolInformationRepository.deleteById(toolTroubleShootingDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(toolTroubleShootingDto.getCompilerInformationIdx());
    }
}