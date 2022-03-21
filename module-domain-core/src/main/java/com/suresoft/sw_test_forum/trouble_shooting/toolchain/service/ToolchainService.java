package com.suresoft.sw_test_forum.trouble_shooting.toolchain.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.domain.IdeInformation;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain.Toolchain;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.ToolchainDto;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.ToolchainSearchDto;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.mapper.ToolchainMapper;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.repository.ToolchainCommentRepositoryImpl;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.repository.ToolchainRepository;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.repository.ToolchainRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ToolchainService {
    private final ToolchainRepository toolchainRepository;
    private final ToolchainRepositoryImpl toolchainRepositoryImpl;
    private final ToolchainCommentRepositoryImpl toolchainCommentRepositoryImpl;
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

    public ToolchainService(ToolchainRepository toolchainRepository,
                            ToolchainRepositoryImpl toolchainRepositoryImpl,
                            ToolchainCommentRepositoryImpl toolchainCommentRepositoryImpl,
                            HashTagsRepository hashTagsRepository,
                            HashTagsRepositoryImpl hashTagsRepositoryImpl,
                            IdeInformationRepository ideInformationRepository,
                            IdeInformationRepositoryImpl ideInformationRepositoryImpl,
                            ToolInformationRepository toolInformationRepository,
                            ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                            CompilerInformationRepository compilerInformationRepository,
                            CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                            UserService userService) {
        this.toolchainRepository = toolchainRepository;
        this.toolchainRepositoryImpl = toolchainRepositoryImpl;
        this.toolchainCommentRepositoryImpl = toolchainCommentRepositoryImpl;
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
    public List<ToolchainDto> findAllByHighPriorityAsc() {
        List<ToolchainDto> toolchainDtoList = toolchainRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (ToolchainDto toolchainDto : toolchainDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(toolchainDto.getCreatedByIdx());

            toolchainDto.setNewIcon(NewIconCheck.isNew(toolchainDto.getCreatedDate()));
            toolchainDto.setCreatedByUser(createdByUser);
            toolchainDto.setCommentDtoCount(toolchainCommentRepositoryImpl.countAllByToolchainIdx(toolchainDto.getIdx()));
        }

        return toolchainDtoList;
    }

    /**
     * 우선순위가 낮은 리스트 조회
     *
     * @param pageable
     * @param toolchainSearchDto
     * @return
     */
    public Page<ToolchainDto> findAll(Pageable pageable, ToolchainSearchDto toolchainSearchDto) {
        Page<ToolchainDto> toolchainDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        toolchainDtoList = toolchainRepositoryImpl.findAll(pageable, toolchainSearchDto);

        // NewIcon 판별, createdBy, comment 갯수 설정
        for (ToolchainDto toolchainDto : toolchainDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(toolchainDto.getCreatedByIdx());

            toolchainDto.setNewIcon(NewIconCheck.isNew(toolchainDto.getCreatedDate()));
            toolchainDto.setCreatedByUser(createdByUser);
            toolchainDto.setCommentDtoCount(toolchainCommentRepositoryImpl.countAllByToolchainIdx(toolchainDto.getIdx()));
        }

        return toolchainDtoList;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<ToolchainDto> highPriorityList = toolchainRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (ToolchainDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public ToolchainDto findToolchainAutoComplete(ToolchainDto toolchainDto) {
        // title 설정
        for (String title : toolchainRepositoryImpl.findDistinctTitle()) {
            toolchainDto.getAutoCompleteTitle().add(title);
        }

        // hashTags 설정
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTags()) {
            for (String hashTag : hashTags.split("#")) {
                toolchainDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // ideName 설정
        for (String ideName : ideInformationRepositoryImpl.findDistinctIdeName()) {
            toolchainDto.getAutoCompleteIdeName().add(ideName);
        }

        // toolName 설정
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolName()) {
            toolchainDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote 설정
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNote()) {
            toolchainDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName 설정
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerName()) {
            toolchainDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote 설정
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNote()) {
            toolchainDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return toolchainDto;
    }

    /**
     * 등록
     *
     * @param toolchainDto
     * @return
     */
    public long insertToolchain(ToolchainDto toolchainDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("toolchain")
                .content(toolchainDto.getHashTags())
                .build()).getIdx();

        long ideInformationIdx = ideInformationRepository.save(IdeInformation.builder()
                .tableName("toolchain")
                .ideName(toolchainDto.getIdeName())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("toolchain")
                .compilerName(toolchainDto.getCompilerName())
                .compilerNote(toolchainDto.getCompilerNote())
                .build()).getIdx();

        toolchainDto.setHashTagsIdx(hashTagsIdx);
        toolchainDto.setIdeInformationIdx(ideInformationIdx);
        toolchainDto.setCompilerInformationIdx(compilerInformationIdx);

        return toolchainRepository.save(ToolchainMapper.INSTANCE.toEntity(toolchainDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public ToolchainDto findToolchainByIdx(long idx) {
        ToolchainDto toolchainDto = new ToolchainDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            toolchainDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            toolchainDto = toolchainRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(toolchainDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(toolchainDto.getLastModifiedByIdx());

            toolchainDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            toolchainDto.setCreatedByUser(createdByUser);
            toolchainDto.setLastModifiedByUser(lastModifiedByUser);

            toolchainRepositoryImpl.updateViewsByIdx(idx);
            toolchainDto.setViews(toolchainDto.getViews() + 1);
        }

        return toolchainDto;
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<ToolchainDto> highPriorityList = toolchainRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        Toolchain toolchainPriority = toolchainRepositoryImpl.findToolchainPriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (ToolchainDto highPriority : highPriorityList) {
            if (toolchainPriority.getPriority() == highPriority.getPriority()) {
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
     * @param toolchainDto
     */
    @Transactional
    public void updateToolchain(long idx, ToolchainDto toolchainDto) {
        Toolchain persistToolchain = toolchainRepository.getById(idx);
        Toolchain toolchain = ToolchainMapper.INSTANCE.toEntity(toolchainDto);
        persistToolchain.update(toolchain);
        toolchainRepository.save(persistToolchain);

        HashTags persistHashTags = hashTagsRepository.getById(toolchainDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .content(toolchainDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        IdeInformation persistIdeInformation = ideInformationRepository.getById(toolchainDto.getIdeInformationIdx());
        persistIdeInformation.update(IdeInformation.builder()
                .ideName(toolchainDto.getIdeName())
                .build());
        ideInformationRepository.save(persistIdeInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getById(toolchainDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .compilerName(toolchainDto.getCompilerName())
                .compilerNote(toolchainDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteToolchainByIdx(long idx) {
        ToolchainDto toolchainDto = toolchainRepositoryImpl.findByIdx(idx);

        toolchainRepository.deleteById(idx);
        hashTagsRepository.deleteById(toolchainDto.getHashTagsIdx());
        ideInformationRepository.deleteById(toolchainDto.getIdeInformationIdx());
        compilerInformationRepository.deleteById(toolchainDto.getCompilerInformationIdx());
    }
}