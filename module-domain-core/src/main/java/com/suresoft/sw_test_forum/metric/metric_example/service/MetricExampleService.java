package com.suresoft.sw_test_forum.metric.metric_example.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepository;
import com.suresoft.sw_test_forum.common.repository.CompilerInformationRepositoryImpl;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepository;
import com.suresoft.sw_test_forum.common.repository.ToolInformationRepositoryImpl;
import com.suresoft.sw_test_forum.metric.metric.dto.MetricDto;
import com.suresoft.sw_test_forum.metric.metric.dto.mapper.MetricMapper;
import com.suresoft.sw_test_forum.metric.metric.service.MetricService;
import com.suresoft.sw_test_forum.metric.metric_example.domain.MetricExample;
import com.suresoft.sw_test_forum.metric.metric_example.dto.MetricExampleDto;
import com.suresoft.sw_test_forum.metric.metric_example.dto.MetricExampleSearchDto;
import com.suresoft.sw_test_forum.metric.metric_example.dto.mapper.MetricExampleMapper;
import com.suresoft.sw_test_forum.metric.metric_example.repository.MetricExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.metric.metric_example.repository.MetricExampleRepository;
import com.suresoft.sw_test_forum.metric.metric_example.repository.MetricExampleRepositoryImpl;
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
public class MetricExampleService {
    private final MetricExampleRepository metricExampleRepository;
    private final MetricExampleRepositoryImpl metricExampleRepositoryImpl;
    private final MetricExampleCommentRepositoryImpl metricExampleCommentRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final MetricService metricService;
    @Value("${module.name}")
    private String moduleName;

    public MetricExampleService(MetricExampleRepository metricExampleRepository,
                                MetricExampleRepositoryImpl metricExampleRepositoryImpl,
                                MetricExampleCommentRepositoryImpl metricExampleCommentRepositoryImpl,
                                ToolInformationRepository toolInformationRepository,
                                ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                CompilerInformationRepository compilerInformationRepository,
                                CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                UserService userService,
                                MetricService metricService) {
        this.metricExampleRepository = metricExampleRepository;
        this.metricExampleRepositoryImpl = metricExampleRepositoryImpl;
        this.metricExampleCommentRepositoryImpl = metricExampleCommentRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.metricService = metricService;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param metricExampleSearchDto
     * @return
     */
    public Page<MetricExampleDto> findMetricExampleList(Pageable pageable, MetricExampleSearchDto metricExampleSearchDto) {
        Page<MetricExampleDto> metricExampleDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        metricExampleDtoList = metricExampleRepositoryImpl.findAll(pageable, metricExampleSearchDto);

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (MetricExampleDto metricExampleDto : metricExampleDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(metricExampleDto.getCreatedByIdx());

            metricExampleDto.setNewIcon(NewIconCheck.isNew(metricExampleDto.getCreatedDate()));
            metricExampleDto.setCreatedByUser(createdByUser);
            metricExampleDto.setCommentDtoCount(metricExampleCommentRepositoryImpl.countAllByMetricExampleIdx(metricExampleDto.getIdx()));

            // 메트릭 예제 코드 리스트를 조회하는 경우(메트릭 보기 페이지에서 이동하지 않음)
            if (metricExampleSearchDto.getMetricIdx() == 0) {
                metricExampleDto.setMetricRule(metricService.findMetricRuleByIdx(metricExampleDto.getMetricIdx()));
            }
        }

        return metricExampleDtoList;
    }

    /**
     * 메트릭 읽기 페이지 일 때, 리스트 조회
     *
     * @param metricIdx
     * @param metricDto
     * @return
     */
    public MetricDto findMetricExampleList(long metricIdx, MetricDto metricDto) {
        List<MetricExampleDto> metricExampleDtoList = metricExampleRepositoryImpl.findAll(metricIdx);

        // NewIcon 판별, createdBy 설정
        for (MetricExampleDto metricExampleDto : metricExampleDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(metricExampleDto.getCreatedByIdx());

            metricExampleDto.setNewIcon(NewIconCheck.isNew(metricExampleDto.getCreatedDate()));
            metricExampleDto.setCreatedByUser(createdByUser);
        }

        metricDto = MetricMapper.INSTANCE.toDtoByExample(metricDto, metricExampleDtoList);

        return metricDto;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenWrite(long metricIdx) {
        List<MetricExample> highPriorityList = metricExampleRepositoryImpl.findAllByHighPriorityAsc(metricIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "예제 코드를 출력하지 않습니다.");

        for (MetricExample highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public MetricExampleDto findMetricExampleAutoComplete(MetricExampleDto metricExampleDto) {
        // title 설정
        for (String title : metricExampleRepositoryImpl.findDistinctTitle()) {
            metricExampleDto.getAutoCompleteTitle().add(title);
        }

        // toolName 설정
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("metric_example")) {
            metricExampleDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote 설정
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("metric_example")) {
            metricExampleDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName 설정
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("metric_example")) {
            metricExampleDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote 설정
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("metric_example")) {
            metricExampleDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return metricExampleDto;
    }

    /**
     * 등록
     *
     * @param metricExampleDto
     * @return
     */
    public long insertMetricExample(MetricExampleDto metricExampleDto) {
        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("metric_example")
                .toolName(metricExampleDto.getToolName())
                .toolNote(metricExampleDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("metric_example")
                .compilerName(metricExampleDto.getCompilerName())
                .compilerNote(metricExampleDto.getCompilerNote())
                .build()).getIdx();

        metricExampleDto.setToolInformationIdx(toolInformationIdx);
        metricExampleDto.setCompilerInformationIdx(compilerInformationIdx);

        return metricExampleRepository.save(MetricExampleMapper.INSTANCE.toEntity(metricExampleDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public MetricExampleDto findMetricExampleByIdx(long idx) {
        MetricExampleDto metricExampleDto = new MetricExampleDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            metricExampleDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            metricExampleDto = metricExampleRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(metricExampleDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(metricExampleDto.getLastModifiedByIdx());

            metricExampleDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            metricExampleDto.setCreatedByUser(createdByUser);
            metricExampleDto.setLastModifiedByUser(lastModifiedByUser);

            metricExampleRepositoryImpl.updateViewsByIdx(idx);
            metricExampleDto.setViews(metricExampleDto.getViews() + 1);
        }

        return metricExampleDto;
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findPriorityListByHighPriorityAscWhenUpdate(long idx, long metricIdx) {
        List<MetricExample> highPriorityList = metricExampleRepositoryImpl.findAllByHighPriorityAsc(metricIdx);
        MetricExample metricExamplePriority = metricExampleRepositoryImpl.findAllPriorityByIdx(idx, metricIdx);
        PriorityDto[] priorityDtoArray = new PriorityDto[4];
        priorityDtoArray[3] = new PriorityDto(false, "예제 코드를 출력하지 않습니다.");

        for (MetricExample highPriority : highPriorityList) {
            if (metricExamplePriority.getPriority() == highPriority.getPriority()) {
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
     * @param metricExampleDto
     * @return
     */
    @Transactional
    public void updateMetricExample(long idx, MetricExampleDto metricExampleDto) {
        MetricExample persistMetricExample = metricExampleRepository.getReferenceById(idx);
        MetricExample metricExample = MetricExampleMapper.INSTANCE.toEntity(metricExampleDto);
        persistMetricExample.update(metricExample);
        metricExampleRepository.save(persistMetricExample);

        ToolInformation persistToolInformation = toolInformationRepository.getReferenceById(metricExampleDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("metric_example")
                .toolName(metricExampleDto.getToolName())
                .toolNote(metricExampleDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getReferenceById(metricExampleDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("metric_example")
                .compilerName(metricExampleDto.getCompilerName())
                .compilerNote(metricExampleDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteMetricExampleByIdx(long idx) {
        MetricExampleDto metricExampleDto = metricExampleRepositoryImpl.findByIdx(idx);

        metricExampleRepository.deleteById(idx);
        toolInformationRepository.deleteById(metricExampleDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(metricExampleDto.getCompilerInformationIdx());
    }

    /**
     * 메트릭 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param metricIdx
     * @param metricDto
     * @return
     */
    public MetricDto findMetricExampleListWhenDelete(long metricIdx, MetricDto metricDto) {
        List<MetricExampleDto> metricExampleDtoList = metricExampleRepositoryImpl.findAllWhenDelete(metricIdx);
        metricDto = MetricMapper.INSTANCE.toDtoByExample(metricDto, metricExampleDtoList);

        return metricDto;
    }
}