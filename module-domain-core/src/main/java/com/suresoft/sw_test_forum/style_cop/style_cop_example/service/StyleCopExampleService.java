package com.suresoft.sw_test_forum.style_cop.style_cop_example.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepository;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepositoryImpl;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepository;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepositoryImpl;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.StyleCopDto;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.mapper.StyleCopMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop.service.StyleCopService;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.domain.StyleCopExample;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.StyleCopExampleDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.StyleCopExampleSearchDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.mapper.StyleCopExampleMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.repository.StyleCopExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.repository.StyleCopExampleRepository;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.repository.StyleCopExampleRepositoryImpl;
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
public class StyleCopExampleService {
    private final StyleCopExampleRepository styleCopExampleRepository;
    private final StyleCopExampleRepositoryImpl styleCopExampleRepositoryImpl;
    private final StyleCopExampleCommentRepositoryImpl styleCopExampleCommentRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final StyleCopService styleCopService;
    @Value("${module.name}")
    private String moduleName;

    public StyleCopExampleService(StyleCopExampleRepository styleCopExampleRepository,
                                  StyleCopExampleRepositoryImpl styleCopExampleRepositoryImpl,
                                  StyleCopExampleCommentRepositoryImpl styleCopExampleCommentRepositoryImpl,
                                  ToolInformationRepository toolInformationRepository,
                                  ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                  CompilerInformationRepository compilerInformationRepository,
                                  CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                  UserService userService,
                                  StyleCopService styleCopService) {
        this.styleCopExampleRepository = styleCopExampleRepository;
        this.styleCopExampleRepositoryImpl = styleCopExampleRepositoryImpl;
        this.styleCopExampleCommentRepositoryImpl = styleCopExampleCommentRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.styleCopService = styleCopService;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param styleCopExampleSearchDto
     * @return
     */
    public Page<StyleCopExampleDto> findStyleCopExampleList(Pageable pageable, StyleCopExampleSearchDto styleCopExampleSearchDto) {
        Page<StyleCopExampleDto> styleCopExampleDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        styleCopExampleDtoList = styleCopExampleRepositoryImpl.findAll(pageable, styleCopExampleSearchDto);

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (StyleCopExampleDto styleCopExampleDto : styleCopExampleDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(styleCopExampleDto.getCreatedByIdx());

            styleCopExampleDto.setNewIcon(NewIconCheck.isNew(styleCopExampleDto.getCreatedDate()));
            styleCopExampleDto.setCreatedByUser(createdByUser);
            styleCopExampleDto.setCommentDtoCount(styleCopExampleCommentRepositoryImpl.countAllByStyleCopExampleIdx(styleCopExampleDto.getIdx()));

            // StyleCop 예제 코드 리스트를 조회하는 경우(StyleCop 보기 페이지에서 이동하지 않음)
            if (styleCopExampleSearchDto.getStyleCopIdx() == 0) {
                styleCopExampleDto.setStyleCopRule(styleCopService.findStyleCopRuleByIdx(styleCopExampleDto.getStyleCopIdx()));
            }
        }

        return styleCopExampleDtoList;
    }

    /**
     * StyleCop 읽기 페이지 일 때, 리스트 조회
     *
     * @param styleCopIdx
     * @param styleCopDto
     * @return
     */
    public StyleCopDto findStyleCopExampleList(long styleCopIdx, StyleCopDto styleCopDto) {
        List<StyleCopExampleDto> styleCopExampleDtoList = styleCopExampleRepositoryImpl.findAll(styleCopIdx);

        // NewIcon 판별, createdBy 설정
        for (StyleCopExampleDto styleCopExampleDto : styleCopExampleDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(styleCopExampleDto.getCreatedByIdx());

            styleCopExampleDto.setNewIcon(NewIconCheck.isNew(styleCopExampleDto.getCreatedDate()));
            styleCopExampleDto.setCreatedByUser(createdByUser);
        }

        styleCopDto = StyleCopMapper.INSTANCE.toDtoByExample(styleCopDto, styleCopExampleDtoList);

        return styleCopDto;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenWrite(long styleCopIdx) {
        List<StyleCopExample> highPriorityList = styleCopExampleRepositoryImpl.findAllByHighPriorityAsc(styleCopIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "예제 코드를 출력하지 않습니다.");

        for (StyleCopExample highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public StyleCopExampleDto findStyleCopExampleAutoComplete(StyleCopExampleDto styleCopExampleDto) {
        // title 설정
        for (String title : styleCopExampleRepositoryImpl.findDistinctTitle()) {
            styleCopExampleDto.getAutoCompleteTitle().add(title);
        }

        // toolName 설정
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("style_cop_example")) {
            styleCopExampleDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote 설정
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("style_cop_example")) {
            styleCopExampleDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName 설정
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("style_cop_example")) {
            styleCopExampleDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote 설정
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("style_cop_example")) {
            styleCopExampleDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return styleCopExampleDto;
    }

    /**
     * 등록
     *
     * @param styleCopExampleDto
     * @return
     */
    public long insertStyleCopExample(StyleCopExampleDto styleCopExampleDto) {
        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("style_cop_example")
                .toolName(styleCopExampleDto.getToolName())
                .toolNote(styleCopExampleDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("style_cop_example")
                .compilerName(styleCopExampleDto.getCompilerName())
                .compilerNote(styleCopExampleDto.getCompilerNote())
                .build()).getIdx();

        styleCopExampleDto.setToolInformationIdx(toolInformationIdx);
        styleCopExampleDto.setCompilerInformationIdx(compilerInformationIdx);

        return styleCopExampleRepository.save(StyleCopExampleMapper.INSTANCE.toEntity(styleCopExampleDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public StyleCopExampleDto findStyleCopExampleByIdx(long idx) {
        StyleCopExampleDto styleCopExampleDto = new StyleCopExampleDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            styleCopExampleDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            styleCopExampleDto = styleCopExampleRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(styleCopExampleDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(styleCopExampleDto.getLastModifiedByIdx());

            styleCopExampleDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            styleCopExampleDto.setCreatedByUser(createdByUser);
            styleCopExampleDto.setLastModifiedByUser(lastModifiedByUser);

            styleCopExampleRepositoryImpl.updateViewsByIdx(idx);
            styleCopExampleDto.setViews(styleCopExampleDto.getViews() + 1);
        }

        return styleCopExampleDto;
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenUpdate(long idx, long styleCopIdx) {
        List<StyleCopExample> highPriorityList = styleCopExampleRepositoryImpl.findAllByHighPriorityAsc(styleCopIdx);
        StyleCopExample styleCopExamplePriority = styleCopExampleRepositoryImpl.findAllPriorityByIdx(idx, styleCopIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "예제 코드를 출력하지 않습니다.");

        for (StyleCopExample highPriority : highPriorityList) {
            if (styleCopExamplePriority.getPriority() == highPriority.getPriority()) {
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
     * @param styleCopExampleDto
     * @return
     */
    @Transactional
    public void updateStyleCopExample(long idx, StyleCopExampleDto styleCopExampleDto) {
        StyleCopExample persistStyleCopExample = styleCopExampleRepository.getReferenceById(idx);
        StyleCopExample styleCopExample = StyleCopExampleMapper.INSTANCE.toEntity(styleCopExampleDto);
        persistStyleCopExample.update(styleCopExample);
        styleCopExampleRepository.save(persistStyleCopExample);

        ToolInformation persistToolInformation = toolInformationRepository.getReferenceById(styleCopExampleDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("style_cop_example")
                .toolName(styleCopExampleDto.getToolName())
                .toolNote(styleCopExampleDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getReferenceById(styleCopExampleDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("style_cop_example")
                .compilerName(styleCopExampleDto.getCompilerName())
                .compilerNote(styleCopExampleDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteStyleCopExampleByIdx(long idx) {
        StyleCopExampleDto styleCopExampleDto = styleCopExampleRepositoryImpl.findByIdx(idx);

        styleCopExampleRepository.deleteById(idx);
        toolInformationRepository.deleteById(styleCopExampleDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(styleCopExampleDto.getCompilerInformationIdx());
    }

    /**
     * StyleCop 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param styleCopIdx
     * @param styleCopDto
     * @return
     */
    public StyleCopDto findStyleCopExampleListWhenDelete(long styleCopIdx, StyleCopDto styleCopDto) {
        List<StyleCopExampleDto> styleCopExampleDtoList = styleCopExampleRepositoryImpl.findAllWhenDelete(styleCopIdx);
        styleCopDto = StyleCopMapper.INSTANCE.toDtoByExample(styleCopDto, styleCopExampleDtoList);

        return styleCopDto;
    }
}