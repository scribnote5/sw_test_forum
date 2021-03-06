package com.suresoft.sw_test_forum.trouble_shooting.cover_tool.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.domain.IdeInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain.CoverTool;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.CoverToolDto;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.CoverToolSearchDto;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.mapper.CoverToolMapper;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.repository.CoverToolCommentRepositoryImpl;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.repository.CoverToolRepository;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.repository.CoverToolRepositoryImpl;
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
public class CoverToolService {
    private final CoverToolRepository coverToolRepository;
    private final CoverToolRepositoryImpl coverToolRepositoryImpl;
    private final CoverToolCommentRepositoryImpl coverToolCommentRepositoryImpl;
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

    public CoverToolService(CoverToolRepository coverToolRepository,
                            CoverToolRepositoryImpl coverToolRepositoryImpl,
                            CoverToolCommentRepositoryImpl coverToolCommentRepositoryImpl,
                            HashTagsRepository hashTagsRepository,
                            HashTagsRepositoryImpl hashTagsRepositoryImpl,
                            IdeInformationRepository ideInformationRepository,
                            IdeInformationRepositoryImpl ideInformationRepositoryImpl,
                            ToolInformationRepository toolInformationRepository,
                            ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                            CompilerInformationRepository compilerInformationRepository,
                            CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                            UserService userService) {
        this.coverToolRepository = coverToolRepository;
        this.coverToolRepositoryImpl = coverToolRepositoryImpl;
        this.coverToolCommentRepositoryImpl = coverToolCommentRepositoryImpl;
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
     * ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public List<CoverToolDto> findAllByHighPriorityAsc() {
        List<CoverToolDto> coverToolDtoList = coverToolRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (CoverToolDto coverToolDto : coverToolDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(coverToolDto.getCreatedByIdx());

            coverToolDto.setNewIcon(NewIconCheck.isNew(coverToolDto.getCreatedDate()));
            coverToolDto.setCreatedByUser(createdByUser);
            coverToolDto.setCommentDtoCount(coverToolCommentRepositoryImpl.countAllByCoverToolIdx(coverToolDto.getIdx()));
        }

        return coverToolDtoList;
    }

    /**
     * ??????????????? ?????? ????????? ??????
     *
     * @param pageable
     * @param coverToolSearchDto
     * @return
     */
    public Page<CoverToolDto> findAll(Pageable pageable, CoverToolSearchDto coverToolSearchDto) {
        Page<CoverToolDto> coverToolDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        coverToolDtoList = coverToolRepositoryImpl.findAll(pageable, coverToolSearchDto);

        // NewIcon ??????, createdBy, comment ?????? ??????
        for (CoverToolDto coverToolDto : coverToolDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(coverToolDto.getCreatedByIdx());

            coverToolDto.setNewIcon(NewIconCheck.isNew(coverToolDto.getCreatedDate()));
            coverToolDto.setCreatedByUser(createdByUser);
            coverToolDto.setCommentDtoCount(coverToolCommentRepositoryImpl.countAllByCoverToolIdx(coverToolDto.getIdx()));
        }

        return coverToolDtoList;
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<CoverToolDto> highPriorityList = coverToolRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "??????????????? ???????????? ????????????.");

        for (CoverToolDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "??????????????? ???????????? ????????????.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public CoverToolDto findCoverToolAutoComplete(CoverToolDto coverToolDto) {
        // title ??????
        for (String title : coverToolRepositoryImpl.findDistinctTitle()) {
            coverToolDto.getAutoCompleteTitle().add(title);
        }

        // hashTags ??????
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("cover_tool")) {
            for (String hashTag : hashTags.split("#")) {
                coverToolDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // ideName ??????
        for (String ideName : ideInformationRepositoryImpl.findDistinctIdeName()) {
            coverToolDto.getAutoCompleteIdeName().add(ideName);
        }

        // toolName ??????
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("cover_tool")) {
            coverToolDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote ??????
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("cover_tool")) {
            coverToolDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName ??????
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("cover_tool")) {
            coverToolDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote ??????
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("cover_tool")) {
            coverToolDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return coverToolDto;
    }

    /**
     * ??????
     *
     * @param coverToolDto
     * @return
     */
    public long insertCoverTool(CoverToolDto coverToolDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("cover_tool")
                .content(coverToolDto.getHashTags())
                .build()).getIdx();

        long ideInformationIdx = ideInformationRepository.save(IdeInformation.builder()
                .tableName("cover_tool")
                .ideName(coverToolDto.getIdeName())
                .build()).getIdx();

        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("cover_tool")
                .toolName(coverToolDto.getToolName())
                .toolNote(coverToolDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("cover_tool")
                .compilerName(coverToolDto.getCompilerName())
                .compilerNote(coverToolDto.getCompilerNote())
                .build()).getIdx();

        coverToolDto.setHashTagsIdx(hashTagsIdx);
        coverToolDto.setIdeInformationIdx(ideInformationIdx);
        coverToolDto.setToolInformationIdx(toolInformationIdx);
        coverToolDto.setCompilerInformationIdx(compilerInformationIdx);

        return coverToolRepository.save(CoverToolMapper.INSTANCE.toEntity(coverToolDto)).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     * @return
     */
    public CoverToolDto findCoverToolByIdx(long idx) {
        CoverToolDto coverToolDto = new CoverToolDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            coverToolDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
        else {
            coverToolDto = coverToolRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(coverToolDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(coverToolDto.getLastModifiedByIdx());

            coverToolDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            coverToolDto.setCreatedByUser(createdByUser);
            coverToolDto.setLastModifiedByUser(lastModifiedByUser);

            coverToolRepositoryImpl.updateViewsByIdx(idx);
            coverToolDto.setViews(coverToolDto.getViews() + 1);
        }

        return coverToolDto;
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<CoverToolDto> highPriorityList = coverToolRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        CoverTool coverToolPriority = coverToolRepositoryImpl.findCoverToolPriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "??????????????? ???????????? ????????????.");

        for (CoverToolDto highPriority : highPriorityList) {
            if (coverToolPriority.getPriority() == highPriority.getPriority()) {
                priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(false, "?????? ????????? ???????????? ?????????.");
            } else {
                priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "??????????????? ???????????? ????????????.");
            }
        }

        return priorityDtoArray;
    }

    /**
     * ??????
     *
     * @param idx
     * @param coverToolDto
     */
    @Transactional
    public void updateCoverTool(long idx, CoverToolDto coverToolDto) {
        CoverTool persistCoverTool = coverToolRepository.getById(idx);
        CoverTool coverTool = CoverToolMapper.INSTANCE.toEntity(coverToolDto);
        persistCoverTool.update(coverTool);
        coverToolRepository.save(persistCoverTool);

        HashTags persistHashTags = hashTagsRepository.getById(coverToolDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("cover_tool")
                .content(coverToolDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        IdeInformation persistIdeInformation = ideInformationRepository.getById(coverToolDto.getIdeInformationIdx());
        persistIdeInformation.update(IdeInformation.builder()
                .tableName("cover_tool")
                .ideName(coverToolDto.getIdeName())
                .build());
        ideInformationRepository.save(persistIdeInformation);

        ToolInformation persistToolInformation = toolInformationRepository.getById(coverToolDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("cover_tool")
                .toolName(coverToolDto.getToolName())
                .toolNote(coverToolDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getById(coverToolDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("cover_tool")
                .compilerName(coverToolDto.getCompilerName())
                .compilerNote(coverToolDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * ??????
     *
     * @param idx
     */
    public void deleteCoverToolByIdx(long idx) {
        CoverToolDto coverToolDto = coverToolRepositoryImpl.findByIdx(idx);

        coverToolRepository.deleteById(idx);
        hashTagsRepository.deleteById(coverToolDto.getHashTagsIdx());
        ideInformationRepository.deleteById(coverToolDto.getIdeInformationIdx());
        toolInformationRepository.deleteById(coverToolDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(coverToolDto.getCompilerInformationIdx());
    }
}