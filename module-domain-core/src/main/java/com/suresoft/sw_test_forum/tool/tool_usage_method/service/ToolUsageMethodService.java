package com.suresoft.sw_test_forum.tool.tool_usage_method.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.tool.tool_usage_method.domain.ToolUsageMethod;
import com.suresoft.sw_test_forum.tool.tool_usage_method.dto.ToolUsageMethodDto;
import com.suresoft.sw_test_forum.tool.tool_usage_method.dto.ToolUsageMethodSearchDto;
import com.suresoft.sw_test_forum.tool.tool_usage_method.dto.mapper.ToolUsageMethodMapper;
import com.suresoft.sw_test_forum.tool.tool_usage_method.repository.ToolUsageMethodCommentRepositoryImpl;
import com.suresoft.sw_test_forum.tool.tool_usage_method.repository.ToolUsageMethodRepository;
import com.suresoft.sw_test_forum.tool.tool_usage_method.repository.ToolUsageMethodRepositoryImpl;
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
public class ToolUsageMethodService {
    private final ToolUsageMethodRepository toolUsageMethodRepository;
    private final ToolUsageMethodRepositoryImpl toolUsageMethodRepositoryImpl;
    private final ToolUsageMethodCommentRepositoryImpl toolUsageMethodCommentRepositoryImpl;
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

    public ToolUsageMethodService(ToolUsageMethodRepository toolUsageMethodRepository,
                                  ToolUsageMethodRepositoryImpl toolUsageMethodRepositoryImpl,
                                  ToolUsageMethodCommentRepositoryImpl toolUsageMethodCommentRepositoryImpl,
                                  HashTagsRepository hashTagsRepository,
                                  HashTagsRepositoryImpl hashTagsRepositoryImpl,
                                  IdeInformationRepository ideInformationRepository,
                                  IdeInformationRepositoryImpl ideInformationRepositoryImpl,
                                  ToolInformationRepository toolInformationRepository,
                                  ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                  CompilerInformationRepository compilerInformationRepository,
                                  CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                  UserService userService) {
        this.toolUsageMethodRepository = toolUsageMethodRepository;
        this.toolUsageMethodRepositoryImpl = toolUsageMethodRepositoryImpl;
        this.toolUsageMethodCommentRepositoryImpl = toolUsageMethodCommentRepositoryImpl;
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
    public List<ToolUsageMethodDto> findAllByHighPriorityAsc() {
        List<ToolUsageMethodDto> toolUsageMethodDtoList = toolUsageMethodRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (ToolUsageMethodDto toolUsageMethodDto : toolUsageMethodDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(toolUsageMethodDto.getCreatedByIdx());

            toolUsageMethodDto.setNewIcon(NewIconCheck.isNew(toolUsageMethodDto.getCreatedDate()));
            toolUsageMethodDto.setCreatedByUser(createdByUser);
            toolUsageMethodDto.setCommentDtoCount(toolUsageMethodCommentRepositoryImpl.countAllByToolUsageMethodIdx(toolUsageMethodDto.getIdx()));
        }

        return toolUsageMethodDtoList;
    }

    /**
     * 우선순위가 낮은 리스트 조회
     *
     * @param pageable
     * @param toolUsageMethodSearchDto
     * @return
     */
    public Page<ToolUsageMethodDto> findAll(Pageable pageable, ToolUsageMethodSearchDto toolUsageMethodSearchDto) {
        Page<ToolUsageMethodDto> toolUsageMethodDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        toolUsageMethodDtoList = toolUsageMethodRepositoryImpl.findAll(pageable, toolUsageMethodSearchDto);

        // NewIcon 판별, createdBy, comment 갯수 설정
        for (ToolUsageMethodDto toolUsageMethodDto : toolUsageMethodDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(toolUsageMethodDto.getCreatedByIdx());

            toolUsageMethodDto.setNewIcon(NewIconCheck.isNew(toolUsageMethodDto.getCreatedDate()));
            toolUsageMethodDto.setCreatedByUser(createdByUser);
            toolUsageMethodDto.setCommentDtoCount(toolUsageMethodCommentRepositoryImpl.countAllByToolUsageMethodIdx(toolUsageMethodDto.getIdx()));
        }

        return toolUsageMethodDtoList;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<ToolUsageMethodDto> highPriorityList = toolUsageMethodRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (ToolUsageMethodDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public ToolUsageMethodDto findToolUsageMethodAutoComplete(ToolUsageMethodDto toolUsageMethodDto) {
        // title 설정
        for (String title : toolUsageMethodRepositoryImpl.findDistinctTitle()) {
            toolUsageMethodDto.getAutoCompleteTitle().add(title);
        }

        // hashTags 설정
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("tool_usage_method")) {
            for (String hashTag : hashTags.split("#")) {
                toolUsageMethodDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        return toolUsageMethodDto;
    }

    /**
     * 등록
     *
     * @param toolUsageMethodDto
     * @return
     */
    public long insertToolUsageMethod(ToolUsageMethodDto toolUsageMethodDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("tool_usage_method")
                .content(toolUsageMethodDto.getHashTags())
                .build()).getIdx();

        toolUsageMethodDto.setHashTagsIdx(hashTagsIdx);

        return toolUsageMethodRepository.save(ToolUsageMethodMapper.INSTANCE.toEntity(toolUsageMethodDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public ToolUsageMethodDto findToolUsageMethodByIdx(long idx) {
        ToolUsageMethodDto toolUsageMethodDto = new ToolUsageMethodDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            toolUsageMethodDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            toolUsageMethodDto = toolUsageMethodRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(toolUsageMethodDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(toolUsageMethodDto.getLastModifiedByIdx());

            toolUsageMethodDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            toolUsageMethodDto.setCreatedByUser(createdByUser);
            toolUsageMethodDto.setLastModifiedByUser(lastModifiedByUser);

            toolUsageMethodRepositoryImpl.updateViewsByIdx(idx);
            toolUsageMethodDto.setViews(toolUsageMethodDto.getViews() + 1);
        }

        return toolUsageMethodDto;
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<ToolUsageMethodDto> highPriorityList = toolUsageMethodRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        ToolUsageMethod toolUsageMethodPriority = toolUsageMethodRepositoryImpl.findToolUsageMethodPriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (ToolUsageMethodDto highPriority : highPriorityList) {
            if (toolUsageMethodPriority.getPriority() == highPriority.getPriority()) {
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
     * @param toolUsageMethodDto
     */
    @Transactional
    public void updateToolUsageMethod(long idx, ToolUsageMethodDto toolUsageMethodDto) {
        ToolUsageMethod persistToolUsageMethod = toolUsageMethodRepository.getReferenceById(idx);
        ToolUsageMethod toolUsageMethod = ToolUsageMethodMapper.INSTANCE.toEntity(toolUsageMethodDto);
        persistToolUsageMethod.update(toolUsageMethod);
        toolUsageMethodRepository.save(persistToolUsageMethod);

        HashTags persistHashTags = hashTagsRepository.getReferenceById(toolUsageMethodDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("tool_usage_method")
                .content(toolUsageMethodDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteToolUsageMethodByIdx(long idx) {
        ToolUsageMethodDto toolUsageMethodDto = toolUsageMethodRepositoryImpl.findByIdx(idx);

        toolUsageMethodRepository.deleteById(idx);
        hashTagsRepository.deleteById(toolUsageMethodDto.getHashTagsIdx());
    }
}