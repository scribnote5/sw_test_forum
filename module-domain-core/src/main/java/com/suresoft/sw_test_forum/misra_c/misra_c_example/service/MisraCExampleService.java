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
     * ????????? ??????
     *
     * @param pageable
     * @param misraCExampleSearchDto
     * @return
     */
    public Page<MisraCExampleDto> findMisraCExampleList(Pageable pageable, MisraCExampleSearchDto misraCExampleSearchDto) {
        Page<MisraCExampleDto> misraCExampleDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        misraCExampleDtoList = misraCExampleRepositoryImpl.findAll(pageable, misraCExampleSearchDto);

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (MisraCExampleDto misraCExampleDto : misraCExampleDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCExampleDto.getCreatedByIdx());

            misraCExampleDto.setNewIcon(NewIconCheck.isNew(misraCExampleDto.getCreatedDate()));
            misraCExampleDto.setCreatedByUser(createdByUser);
            misraCExampleDto.setCommentDtoCount(misraCExampleCommentRepositoryImpl.countAllByMisraCExampleIdx(misraCExampleDto.getIdx()));

            // MISRA C ?????? ?????? ???????????? ???????????? ??????(MISRA C ?????? ??????????????? ???????????? ??????)
            if (misraCExampleSearchDto.getMisraCIdx() == 0) {
                misraCExampleDto.setMisraCRule(misraCService.findMisraCRuleByIdx(misraCExampleDto.getMisraCIdx()));
            }
        }

        return misraCExampleDtoList;
    }

    /**
     * MISRA C ?????? ????????? ??? ???, ????????? ??????
     *
     * @param misraCIdx
     * @param misraCDto
     * @return
     */
    public MisraCDto findMisraCExampleList(long misraCIdx, MisraCDto misraCDto) {
        List<MisraCExampleDto> misraCExampleDtoList = misraCExampleRepositoryImpl.findAll(misraCIdx);

        // NewIcon ??????, createdBy ??????
        for (MisraCExampleDto misraCExampleDto : misraCExampleDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCExampleDto.getCreatedByIdx());

            misraCExampleDto.setNewIcon(NewIconCheck.isNew(misraCExampleDto.getCreatedDate()));
            misraCExampleDto.setCreatedByUser(createdByUser);
        }

        misraCDto = MisraCMapper.INSTANCE.toDtoByExample(misraCDto, misraCExampleDtoList);

        return misraCDto;
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenWrite(long misraCIdx) {
        List<MisraCExample> highPriorityList = misraCExampleRepositoryImpl.findAllByHighPriorityAsc(misraCIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "?????? ????????? ???????????? ????????????.");

        for (MisraCExample highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "??????????????? ???????????? ????????????.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public MisraCExampleDto findMisraCExampleAutoComplete(MisraCExampleDto misraCExampleDto) {
        // title ??????
        for (String title : misraCExampleRepositoryImpl.findDistinctTitle()) {
            misraCExampleDto.getAutoCompleteTitle().add(title);
        }

        // toolName ??????
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("misra_c_example")) {
            misraCExampleDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote ??????
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("misra_c_example")) {
            misraCExampleDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName ??????
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("misra_c_example")) {
            misraCExampleDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote ??????
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("misra_c_example")) {
            misraCExampleDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return misraCExampleDto;
    }

    /**
     * ??????
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
     * ??????
     *
     * @param idx
     * @return
     */
    public MisraCExampleDto findMisraCExampleByIdx(long idx) {
        MisraCExampleDto misraCExampleDto = new MisraCExampleDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            misraCExampleDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
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
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenUpdate(long idx, long misraCIdx) {
        List<MisraCExample> highPriorityList = misraCExampleRepositoryImpl.findAllByHighPriorityAsc(misraCIdx);
        MisraCExample misraCExamplePriority = misraCExampleRepositoryImpl.findAllPriorityByIdx(idx, misraCIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "?????? ????????? ???????????? ????????????.");

        for (MisraCExample highPriority : highPriorityList) {
            if (misraCExamplePriority.getPriority() == highPriority.getPriority()) {
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
     * @param misraCExampleDto
     * @return
     */
    @Transactional
    public void updateMisraCExample(long idx, MisraCExampleDto misraCExampleDto) {
        MisraCExample persistMisraCExample = misraCExampleRepository.getById(idx);
        MisraCExample misraCExample = MisraCExampleMapper.INSTANCE.toEntity(misraCExampleDto);
        persistMisraCExample.update(misraCExample);
        misraCExampleRepository.save(persistMisraCExample);

        ToolInformation persistToolInformation = toolInformationRepository.getById(misraCExampleDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("misra_c_example")
                .toolName(misraCExampleDto.getToolName())
                .toolNote(misraCExampleDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getById(misraCExampleDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("misra_c_example")
                .compilerName(misraCExampleDto.getCompilerName())
                .compilerNote(misraCExampleDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * ??????
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
     * MISRA C ?????? ????????? ??? ???, ????????? ?????? ????????? ??????
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