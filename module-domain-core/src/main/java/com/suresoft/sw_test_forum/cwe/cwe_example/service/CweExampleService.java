package com.suresoft.sw_test_forum.cwe.cwe_example.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepository;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepositoryImpl;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepository;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepositoryImpl;
import com.suresoft.sw_test_forum.cwe.cwe.dto.CweDto;
import com.suresoft.sw_test_forum.cwe.cwe.dto.mapper.CweMapper;
import com.suresoft.sw_test_forum.cwe.cwe.service.CweService;
import com.suresoft.sw_test_forum.cwe.cwe_example.domain.CweExample;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.CweExampleDto;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.CweExampleSearchDto;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.mapper.CweExampleMapper;
import com.suresoft.sw_test_forum.cwe.cwe_example.repository.CweExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.cwe.cwe_example.repository.CweExampleRepository;
import com.suresoft.sw_test_forum.cwe.cwe_example.repository.CweExampleRepositoryImpl;
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
public class CweExampleService {
    private final CweExampleRepository cweExampleRepository;
    private final CweExampleRepositoryImpl cweExampleRepositoryImpl;
    private final CweExampleCommentRepositoryImpl cweExampleCommentRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final CweService cweService;
    @Value("${module.name}")
    private String moduleName;

    public CweExampleService(CweExampleRepository cweExampleRepository,
                             CweExampleRepositoryImpl cweExampleRepositoryImpl,
                             CweExampleCommentRepositoryImpl cweExampleCommentRepositoryImpl,
                             ToolInformationRepository toolInformationRepository,
                             ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                             CompilerInformationRepository compilerInformationRepository,
                             CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                             UserService userService,
                             CweService cweService) {
        this.cweExampleRepository = cweExampleRepository;
        this.cweExampleRepositoryImpl = cweExampleRepositoryImpl;
        this.cweExampleCommentRepositoryImpl = cweExampleCommentRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.cweService = cweService;
    }

    /**
     * ????????? ??????
     *
     * @param pageable
     * @param cweExampleSearchDto
     * @return
     */
    public Page<CweExampleDto> findCweExampleList(Pageable pageable, CweExampleSearchDto cweExampleSearchDto) {
        Page<CweExampleDto> cweExampleDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        cweExampleDtoList = cweExampleRepositoryImpl.findAll(pageable, cweExampleSearchDto);

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (CweExampleDto cweExampleDto : cweExampleDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweExampleDto.getCreatedByIdx());

            cweExampleDto.setNewIcon(NewIconCheck.isNew(cweExampleDto.getCreatedDate()));
            cweExampleDto.setCreatedByUser(createdByUser);
            cweExampleDto.setCommentDtoCount(cweExampleCommentRepositoryImpl.countAllByCweExampleIdx(cweExampleDto.getIdx()));

            // CWE ?????? ?????? ???????????? ???????????? ??????(CWE ?????? ??????????????? ???????????? ??????)
            if (cweExampleSearchDto.getCweIdx() == 0) {
                cweExampleDto.setCweRule(cweService.findCweRuleByIdx(cweExampleDto.getCweIdx()));
            }
        }

        return cweExampleDtoList;
    }

    /**
     * CWE ?????? ????????? ??? ???, ????????? ??????
     *
     * @param cweIdx
     * @param cweDto
     * @return
     */
    public CweDto findCweExampleList(long cweIdx, CweDto cweDto) {
        List<CweExampleDto> cweExampleDtoList = cweExampleRepositoryImpl.findAll(cweIdx);

        // NewIcon ??????, createdBy ??????
        for (CweExampleDto cweExampleDto : cweExampleDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweExampleDto.getCreatedByIdx());

            cweExampleDto.setNewIcon(NewIconCheck.isNew(cweExampleDto.getCreatedDate()));
            cweExampleDto.setCreatedByUser(createdByUser);
        }

        cweDto = CweMapper.INSTANCE.toDtoByExample(cweDto, cweExampleDtoList);

        return cweDto;
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenWrite(long cweIdx) {
        List<CweExample> highPriorityList = cweExampleRepositoryImpl.findAllByHighPriorityAsc(cweIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "?????? ????????? ???????????? ????????????.");

        for (CweExample highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "??????????????? ???????????? ????????????.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public CweExampleDto findCweExampleAutoComplete(CweExampleDto cweExampleDto) {
        // title ??????
        for (String title : cweExampleRepositoryImpl.findDistinctTitle()) {
            cweExampleDto.getAutoCompleteTitle().add(title);
        }

        // toolName ??????
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("cwe_example")) {
            cweExampleDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote ??????
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("cwe_example")) {
            cweExampleDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName ??????
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("cwe_example")) {
            cweExampleDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote ??????
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("cwe_example")) {
            cweExampleDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return cweExampleDto;
    }

    /**
     * ??????
     *
     * @param cweExampleDto
     * @return
     */
    public long insertCweExample(CweExampleDto cweExampleDto) {
        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("cwe_example")
                .toolName(cweExampleDto.getToolName())
                .toolNote(cweExampleDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("cwe_example")
                .compilerName(cweExampleDto.getCompilerName())
                .compilerNote(cweExampleDto.getCompilerNote())
                .build()).getIdx();

        cweExampleDto.setToolInformationIdx(toolInformationIdx);
        cweExampleDto.setCompilerInformationIdx(compilerInformationIdx);

        return cweExampleRepository.save(CweExampleMapper.INSTANCE.toEntity(cweExampleDto)).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     * @return
     */
    public CweExampleDto findCweExampleByIdx(long idx) {
        CweExampleDto cweExampleDto = new CweExampleDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            cweExampleDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
        else {
            cweExampleDto = cweExampleRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweExampleDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweExampleDto.getLastModifiedByIdx());

            cweExampleDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            cweExampleDto.setCreatedByUser(createdByUser);
            cweExampleDto.setLastModifiedByUser(lastModifiedByUser);

            cweExampleRepositoryImpl.updateViewsByIdx(idx);
            cweExampleDto.setViews(cweExampleDto.getViews() + 1);
        }

        return cweExampleDto;
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenUpdate(long idx, long cweIdx) {
        List<CweExample> highPriorityList = cweExampleRepositoryImpl.findAllByHighPriorityAsc(cweIdx);
        CweExample cweExamplePriority = cweExampleRepositoryImpl.findAllPriorityByIdx(idx, cweIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "?????? ????????? ???????????? ????????????.");

        for (CweExample highPriority : highPriorityList) {
            if (cweExamplePriority.getPriority() == highPriority.getPriority()) {
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
     * @param cweExampleDto
     * @return
     */
    @Transactional
    public void updateCweExample(long idx, CweExampleDto cweExampleDto) {
        CweExample persistCweExample = cweExampleRepository.getById(idx);
        CweExample cweExample = CweExampleMapper.INSTANCE.toEntity(cweExampleDto);
        persistCweExample.update(cweExample);
        cweExampleRepository.save(persistCweExample);

        ToolInformation persistToolInformation = toolInformationRepository.getById(cweExampleDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("cwe_example")
                .toolName(cweExampleDto.getToolName())
                .toolNote(cweExampleDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getById(cweExampleDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("cwe_example")
                .compilerName(cweExampleDto.getCompilerName())
                .compilerNote(cweExampleDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * ??????
     *
     * @param idx
     */
    public void deleteCweExampleByIdx(long idx) {
        CweExampleDto cweExampleDto = cweExampleRepositoryImpl.findByIdx(idx);

        cweExampleRepository.deleteById(idx);
        toolInformationRepository.deleteById(cweExampleDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(cweExampleDto.getCompilerInformationIdx());
    }

    /**
     * CWE ?????? ????????? ??? ???, ????????? ?????? ????????? ??????
     *
     * @param cweIdx
     * @param cweDto
     * @return
     */
    public CweDto findCweExampleListWhenDelete(long cweIdx, CweDto cweDto) {
        List<CweExampleDto> cweExampleDtoList = cweExampleRepositoryImpl.findAllWhenDelete(cweIdx);
        cweDto = CweMapper.INSTANCE.toDtoByExample(cweDto, cweExampleDtoList);

        return cweDto;
    }
}