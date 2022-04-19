package com.suresoft.sw_test_forum.trouble_shooting.static_tool.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.domain.IdeInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain.StaticTool;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.StaticToolDto;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.StaticToolSearchDto;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.mapper.StaticToolMapper;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.repository.StaticToolCommentRepositoryImpl;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.repository.StaticToolRepository;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.repository.StaticToolRepositoryImpl;
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
public class StaticToolService {
    private final StaticToolRepository staticToolRepository;
    private final StaticToolRepositoryImpl staticToolRepositoryImpl;
    private final StaticToolCommentRepositoryImpl staticToolCommentRepositoryImpl;
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

    public StaticToolService(StaticToolRepository staticToolRepository,
                             StaticToolRepositoryImpl staticToolRepositoryImpl,
                             StaticToolCommentRepositoryImpl staticToolCommentRepositoryImpl,
                             HashTagsRepository hashTagsRepository,
                             HashTagsRepositoryImpl hashTagsRepositoryImpl,
                             IdeInformationRepository ideInformationRepository,
                             IdeInformationRepositoryImpl ideInformationRepositoryImpl,
                             ToolInformationRepository toolInformationRepository,
                             ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                             CompilerInformationRepository compilerInformationRepository,
                             CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                             UserService userService) {
        this.staticToolRepository = staticToolRepository;
        this.staticToolRepositoryImpl = staticToolRepositoryImpl;
        this.staticToolCommentRepositoryImpl = staticToolCommentRepositoryImpl;
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
    public List<StaticToolDto> findAllByHighPriorityAsc() {
        List<StaticToolDto> staticToolDtoList = staticToolRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (StaticToolDto staticToolDto : staticToolDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(staticToolDto.getCreatedByIdx());

            staticToolDto.setNewIcon(NewIconCheck.isNew(staticToolDto.getCreatedDate()));
            staticToolDto.setCreatedByUser(createdByUser);
            staticToolDto.setCommentDtoCount(staticToolCommentRepositoryImpl.countAllByStaticToolIdx(staticToolDto.getIdx()));
        }

        return staticToolDtoList;
    }

    /**
     * 우선순위가 낮은 리스트 조회
     *
     * @param pageable
     * @param staticToolSearchDto
     * @return
     */
    public Page<StaticToolDto> findAll(Pageable pageable, StaticToolSearchDto staticToolSearchDto) {
        Page<StaticToolDto> staticToolDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        staticToolDtoList = staticToolRepositoryImpl.findAll(pageable, staticToolSearchDto);

        // NewIcon 판별, createdBy, comment 갯수 설정
        for (StaticToolDto staticToolDto : staticToolDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(staticToolDto.getCreatedByIdx());

            staticToolDto.setNewIcon(NewIconCheck.isNew(staticToolDto.getCreatedDate()));
            staticToolDto.setCreatedByUser(createdByUser);
            staticToolDto.setCommentDtoCount(staticToolCommentRepositoryImpl.countAllByStaticToolIdx(staticToolDto.getIdx()));
        }

        return staticToolDtoList;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<StaticToolDto> highPriorityList = staticToolRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (StaticToolDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public StaticToolDto findStaticToolAutoComplete(StaticToolDto staticToolDto) {
        // title 설정
        for (String title : staticToolRepositoryImpl.findDistinctTitle()) {
            staticToolDto.getAutoCompleteTitle().add(title);
        }

        // hashTags 설정
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("static_tool")) {
            for (String hashTag : hashTags.split("#")) {
                staticToolDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // ideName 설정
        for (String ideName : ideInformationRepositoryImpl.findDistinctIdeName()) {
            staticToolDto.getAutoCompleteIdeName().add(ideName);
        }

        // toolName 설정
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("static_tool")) {
            staticToolDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote 설정
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("static_tool")) {
            staticToolDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName 설정
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("static_tool")) {
            staticToolDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote 설정
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("static_tool")) {
            staticToolDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return staticToolDto;
    }

    /**
     * 등록
     *
     * @param staticToolDto
     * @return
     */
    public long insertStaticTool(StaticToolDto staticToolDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("static_tool")
                .content(staticToolDto.getHashTags())
                .build()).getIdx();

        long ideInformationIdx = ideInformationRepository.save(IdeInformation.builder()
                .tableName("static_tool")
                .ideName(staticToolDto.getIdeName())
                .build()).getIdx();

        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("static_tool")
                .toolName(staticToolDto.getToolName())
                .toolNote(staticToolDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("static_tool")
                .compilerName(staticToolDto.getCompilerName())
                .compilerNote(staticToolDto.getCompilerNote())
                .build()).getIdx();

        staticToolDto.setHashTagsIdx(hashTagsIdx);
        staticToolDto.setIdeInformationIdx(ideInformationIdx);
        staticToolDto.setToolInformationIdx(toolInformationIdx);
        staticToolDto.setCompilerInformationIdx(compilerInformationIdx);

        return staticToolRepository.save(StaticToolMapper.INSTANCE.toEntity(staticToolDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public StaticToolDto findStaticToolByIdx(long idx) {
        StaticToolDto staticToolDto = new StaticToolDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            staticToolDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            staticToolDto = staticToolRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(staticToolDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(staticToolDto.getLastModifiedByIdx());

            staticToolDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            staticToolDto.setCreatedByUser(createdByUser);
            staticToolDto.setLastModifiedByUser(lastModifiedByUser);

            staticToolRepositoryImpl.updateViewsByIdx(idx);
            staticToolDto.setViews(staticToolDto.getViews() + 1);
        }

        return staticToolDto;
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<StaticToolDto> highPriorityList = staticToolRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        StaticTool staticToolPriority = staticToolRepositoryImpl.findStaticToolPriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (StaticToolDto highPriority : highPriorityList) {
            if (staticToolPriority.getPriority() == highPriority.getPriority()) {
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
     * @param staticToolDto
     */
    @Transactional
    public void updateStaticTool(long idx, StaticToolDto staticToolDto) {
        StaticTool persistStaticTool = staticToolRepository.getById(idx);
        StaticTool staticTool = StaticToolMapper.INSTANCE.toEntity(staticToolDto);
        persistStaticTool.update(staticTool);
        staticToolRepository.save(persistStaticTool);

        HashTags persistHashTags = hashTagsRepository.getById(staticToolDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("static_tool")
                .content(staticToolDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        IdeInformation persistIdeInformation = ideInformationRepository.getById(staticToolDto.getIdeInformationIdx());
        persistIdeInformation.update(IdeInformation.builder()
                .tableName("static_tool")
                .ideName(staticToolDto.getIdeName())
                .build());
        ideInformationRepository.save(persistIdeInformation);

        ToolInformation persistToolInformation = toolInformationRepository.getById(staticToolDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("static_tool")
                .toolName(staticToolDto.getToolName())
                .toolNote(staticToolDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getById(staticToolDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("static_tool")
                .compilerName(staticToolDto.getCompilerName())
                .compilerNote(staticToolDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteStaticToolByIdx(long idx) {
        StaticToolDto staticToolDto = staticToolRepositoryImpl.findByIdx(idx);

        staticToolRepository.deleteById(idx);
        hashTagsRepository.deleteById(staticToolDto.getHashTagsIdx());
        ideInformationRepository.deleteById(staticToolDto.getIdeInformationIdx());
        toolInformationRepository.deleteById(staticToolDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(staticToolDto.getCompilerInformationIdx());
    }
}