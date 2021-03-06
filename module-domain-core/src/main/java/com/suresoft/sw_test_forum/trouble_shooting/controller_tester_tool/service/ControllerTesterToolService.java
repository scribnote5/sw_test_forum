package com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.domain.IdeInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain.ControllerTesterTool;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.ControllerTesterToolDto;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.ControllerTesterToolSearchDto;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.mapper.ControllerTesterToolMapper;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.repository.ControllerTesterToolCommentRepositoryImpl;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.repository.ControllerTesterToolRepository;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.repository.ControllerTesterToolRepositoryImpl;
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
public class ControllerTesterToolService {
    private final ControllerTesterToolRepository controllerTesterToolRepository;
    private final ControllerTesterToolRepositoryImpl controllerTesterToolRepositoryImpl;
    private final ControllerTesterToolCommentRepositoryImpl controllerTesterToolCommentRepositoryImpl;
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

    public ControllerTesterToolService(ControllerTesterToolRepository controllerTesterToolRepository,
                                       ControllerTesterToolRepositoryImpl controllerTesterToolRepositoryImpl,
                                       ControllerTesterToolCommentRepositoryImpl controllerTesterToolCommentRepositoryImpl,
                                       HashTagsRepository hashTagsRepository,
                                       HashTagsRepositoryImpl hashTagsRepositoryImpl,
                                       IdeInformationRepository ideInformationRepository,
                                       IdeInformationRepositoryImpl ideInformationRepositoryImpl,
                                       ToolInformationRepository toolInformationRepository,
                                       ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                       CompilerInformationRepository compilerInformationRepository,
                                       CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                       UserService userService) {
        this.controllerTesterToolRepository = controllerTesterToolRepository;
        this.controllerTesterToolRepositoryImpl = controllerTesterToolRepositoryImpl;
        this.controllerTesterToolCommentRepositoryImpl = controllerTesterToolCommentRepositoryImpl;
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
    public List<ControllerTesterToolDto> findAllByHighPriorityAsc() {
        List<ControllerTesterToolDto> controllerTesterToolDtoList = controllerTesterToolRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (ControllerTesterToolDto controllerTesterToolDto : controllerTesterToolDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(controllerTesterToolDto.getCreatedByIdx());

            controllerTesterToolDto.setNewIcon(NewIconCheck.isNew(controllerTesterToolDto.getCreatedDate()));
            controllerTesterToolDto.setCreatedByUser(createdByUser);
            controllerTesterToolDto.setCommentDtoCount(controllerTesterToolCommentRepositoryImpl.countAllByControllerTesterToolIdx(controllerTesterToolDto.getIdx()));
        }

        return controllerTesterToolDtoList;
    }

    /**
     * ??????????????? ?????? ????????? ??????
     *
     * @param pageable
     * @param controllerTesterToolSearchDto
     * @return
     */
    public Page<ControllerTesterToolDto> findAll(Pageable pageable, ControllerTesterToolSearchDto controllerTesterToolSearchDto) {
        Page<ControllerTesterToolDto> controllerTesterToolDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        controllerTesterToolDtoList = controllerTesterToolRepositoryImpl.findAll(pageable, controllerTesterToolSearchDto);

        // NewIcon ??????, createdBy, comment ?????? ??????
        for (ControllerTesterToolDto controllerTesterToolDto : controllerTesterToolDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(controllerTesterToolDto.getCreatedByIdx());

            controllerTesterToolDto.setNewIcon(NewIconCheck.isNew(controllerTesterToolDto.getCreatedDate()));
            controllerTesterToolDto.setCreatedByUser(createdByUser);
            controllerTesterToolDto.setCommentDtoCount(controllerTesterToolCommentRepositoryImpl.countAllByControllerTesterToolIdx(controllerTesterToolDto.getIdx()));
        }

        return controllerTesterToolDtoList;
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<ControllerTesterToolDto> highPriorityList = controllerTesterToolRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "??????????????? ???????????? ????????????.");

        for (ControllerTesterToolDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "??????????????? ???????????? ????????????.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public ControllerTesterToolDto findControllerTesterToolAutoComplete(ControllerTesterToolDto controllerTesterToolDto) {
        // title ??????
        for (String title : controllerTesterToolRepositoryImpl.findDistinctTitle()) {
            controllerTesterToolDto.getAutoCompleteTitle().add(title);
        }

        // hashTags ??????
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("controller_tester_tool")) {
            for (String hashTag : hashTags.split("#")) {
                controllerTesterToolDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // ideName ??????
        for (String ideName : ideInformationRepositoryImpl.findDistinctIdeName()) {
            controllerTesterToolDto.getAutoCompleteIdeName().add(ideName);
        }

        // toolName ??????
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("controllerTester_tool")) {
            controllerTesterToolDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote ??????
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("controllerTester_tool")) {
            controllerTesterToolDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName ??????
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("controllerTester_tool")) {
            controllerTesterToolDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote ??????
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("controllerTester_tool")) {
            controllerTesterToolDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return controllerTesterToolDto;
    }

    /**
     * ??????
     *
     * @param controllerTesterToolDto
     * @return
     */
    public long insertControllerTesterTool(ControllerTesterToolDto controllerTesterToolDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("controllerTester_tool")
                .content(controllerTesterToolDto.getHashTags())
                .build()).getIdx();

        long ideInformationIdx = ideInformationRepository.save(IdeInformation.builder()
                .tableName("controllerTester_tool")
                .ideName(controllerTesterToolDto.getIdeName())
                .build()).getIdx();

        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("controllerTester_tool")
                .toolName(controllerTesterToolDto.getToolName())
                .toolNote(controllerTesterToolDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("controllerTester_tool")
                .compilerName(controllerTesterToolDto.getCompilerName())
                .compilerNote(controllerTesterToolDto.getCompilerNote())
                .build()).getIdx();

        controllerTesterToolDto.setHashTagsIdx(hashTagsIdx);
        controllerTesterToolDto.setIdeInformationIdx(ideInformationIdx);
        controllerTesterToolDto.setToolInformationIdx(toolInformationIdx);
        controllerTesterToolDto.setCompilerInformationIdx(compilerInformationIdx);

        return controllerTesterToolRepository.save(ControllerTesterToolMapper.INSTANCE.toEntity(controllerTesterToolDto)).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     * @return
     */
    public ControllerTesterToolDto findControllerTesterToolByIdx(long idx) {
        ControllerTesterToolDto controllerTesterToolDto = new ControllerTesterToolDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            controllerTesterToolDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
        else {
            controllerTesterToolDto = controllerTesterToolRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(controllerTesterToolDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(controllerTesterToolDto.getLastModifiedByIdx());

            controllerTesterToolDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            controllerTesterToolDto.setCreatedByUser(createdByUser);
            controllerTesterToolDto.setLastModifiedByUser(lastModifiedByUser);

            controllerTesterToolRepositoryImpl.updateViewsByIdx(idx);
            controllerTesterToolDto.setViews(controllerTesterToolDto.getViews() + 1);
        }

        return controllerTesterToolDto;
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<ControllerTesterToolDto> highPriorityList = controllerTesterToolRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        ControllerTesterTool controllerTesterToolPriority = controllerTesterToolRepositoryImpl.findControllerTesterToolPriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "??????????????? ???????????? ????????????.");

        for (ControllerTesterToolDto highPriority : highPriorityList) {
            if (controllerTesterToolPriority.getPriority() == highPriority.getPriority()) {
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
     * @param controllerTesterToolDto
     */
    @Transactional
    public void updateControllerTesterTool(long idx, ControllerTesterToolDto controllerTesterToolDto) {
        ControllerTesterTool persistControllerTesterTool = controllerTesterToolRepository.getById(idx);
        ControllerTesterTool controllerTesterTool = ControllerTesterToolMapper.INSTANCE.toEntity(controllerTesterToolDto);
        persistControllerTesterTool.update(controllerTesterTool);
        controllerTesterToolRepository.save(persistControllerTesterTool);

        HashTags persistHashTags = hashTagsRepository.getById(controllerTesterToolDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("controller_tester_tool")
                .content(controllerTesterToolDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        IdeInformation persistIdeInformation = ideInformationRepository.getById(controllerTesterToolDto.getIdeInformationIdx());
        persistIdeInformation.update(IdeInformation.builder()
                .tableName("controllerTester_tool")
                .ideName(controllerTesterToolDto.getIdeName())
                .build());
        ideInformationRepository.save(persistIdeInformation);

        ToolInformation persistToolInformation = toolInformationRepository.getById(controllerTesterToolDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("controllerTester_tool")
                .toolName(controllerTesterToolDto.getToolName())
                .toolNote(controllerTesterToolDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getById(controllerTesterToolDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("controllerTester_tool")
                .compilerName(controllerTesterToolDto.getCompilerName())
                .compilerNote(controllerTesterToolDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * ??????
     *
     * @param idx
     */
    public void deleteControllerTesterToolByIdx(long idx) {
        ControllerTesterToolDto controllerTesterToolDto = controllerTesterToolRepositoryImpl.findByIdx(idx);

        controllerTesterToolRepository.deleteById(idx);
        hashTagsRepository.deleteById(controllerTesterToolDto.getHashTagsIdx());
        ideInformationRepository.deleteById(controllerTesterToolDto.getIdeInformationIdx());
        toolInformationRepository.deleteById(controllerTesterToolDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(controllerTesterToolDto.getCompilerInformationIdx());
    }
}