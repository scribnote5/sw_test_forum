package com.suresoft.sw_test_forum.left_reference.dynamic_test.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.domain.ProjectInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTest;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.DynamicTestDto;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.DynamicTestSearchDto;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.mapper.DynamicTestMapper;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.repository.DynamicTestCommentRepositoryImpl;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.repository.DynamicTestRepository;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.repository.DynamicTestRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class DynamicTestService {
    private final DynamicTestRepository dynamicTestRepository;
    private final DynamicTestRepositoryImpl dynamicTestRepositoryImpl;
    private final DynamicTestCommentRepositoryImpl dynamicTestCommentRepositoryImpl;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final ProjectInformationRepository projectInformationRepository;
    private final ProjectInformationRepositoryImpl projectInformationRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;

    @Value("${module.name}")
    private String moduleName;

    public DynamicTestService(DynamicTestRepository dynamicTestRepository,
                              DynamicTestRepositoryImpl dynamicTestRepositoryImpl,
                              DynamicTestCommentRepositoryImpl dynamicTestCommentRepositoryImpl,
                              HashTagsRepository hashTagsRepository,
                              HashTagsRepositoryImpl hashTagsRepositoryImpl,
                              ProjectInformationRepository projectInformationRepository,
                              ProjectInformationRepositoryImpl projectInformationRepositoryImpl,
                              ToolInformationRepository toolInformationRepository,
                              ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                              CompilerInformationRepository compilerInformationRepository,
                              CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                              UserService userService) {
        this.dynamicTestRepository = dynamicTestRepository;
        this.dynamicTestRepositoryImpl = dynamicTestRepositoryImpl;
        this.dynamicTestCommentRepositoryImpl = dynamicTestCommentRepositoryImpl;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.projectInformationRepository = projectInformationRepository;
        this.projectInformationRepositoryImpl = projectInformationRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
    }

    /**
     * 대시보드 일 때, 조회수 많은 10개 리스트 조회
     *
     * @return
     */
    public List<DynamicTestDto> findTop10ByViewsAndGuidelinelike() {
        List<DynamicTestDto> dynamicTestDtoList = dynamicTestRepositoryImpl.findTop10ByViews();
        // 좋아요 내림차순으로 정렬
        class DynamicTestDtoComparator implements Comparator<DynamicTestDto> {
            @Override
            public int compare(DynamicTestDto o1, DynamicTestDto o2) {
                return (int) o2.getLikeCountInList() - (int) o1.getLikeCountInList();
            }
        }
        Collections.sort(dynamicTestDtoList, new DynamicTestDtoComparator());

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (DynamicTestDto dynamicTestDto : dynamicTestDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(dynamicTestDto.getCreatedByIdx());

            dynamicTestDto.setNewIcon(NewIconCheck.isNew(dynamicTestDto.getCreatedDate()));
            dynamicTestDto.setCreatedByUser(createdByUser);
            dynamicTestDto.setCommentDtoCount(dynamicTestCommentRepositoryImpl.countAllByDynamicTestCIdx(dynamicTestDto.getIdx()));
        }

        return dynamicTestDtoList;
    }

    /**
     * 리스트 조회
     *
     * @return
     */
    public Page<DynamicTestDto> findDynamicTestList(Pageable pageable, DynamicTestSearchDto dynamicTestSearchDto) {
        Page<DynamicTestDto> dynamicTestDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        dynamicTestDtoList = dynamicTestRepositoryImpl.findAll(pageable, dynamicTestSearchDto);

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (DynamicTestDto dynamicTestDto : dynamicTestDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(dynamicTestDto.getCreatedByIdx());

            dynamicTestDto.setNewIcon(NewIconCheck.isNew(dynamicTestDto.getCreatedDate()));
            dynamicTestDto.setCreatedByUser(createdByUser);
            dynamicTestDto.setCommentDtoCount(dynamicTestCommentRepositoryImpl.countAllByDynamicTestCIdx(dynamicTestDto.getIdx()));
        }

        return dynamicTestDtoList;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public DynamicTestDto findDynamicTestAutoComplete(DynamicTestDto dynamicTestDto) {
        // title 설정
        for (String title : dynamicTestRepositoryImpl.findDistinctTitle()) {
            dynamicTestDto.getAutoCompleteTitle().add(title);
        }

        // hashTags 설정
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("dynamic_test")) {
            for (String hashTag : hashTags.split("#")) {
                dynamicTestDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // projectName 설정
        for (String projectName : projectInformationRepositoryImpl.findDistinctProjectNameByTableName("dynamic_test")) {
            dynamicTestDto.getAutoCompleteProjectName().add(projectName);
        }

        // toolName 설정
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("dynamic_test")) {
            dynamicTestDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote 설정
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("dynamic_test")) {
            dynamicTestDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName 설정
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("dynamic_test")) {
            dynamicTestDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote 설정
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("dynamic_test")) {
            dynamicTestDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return dynamicTestDto;
    }

    /**
     * 등록
     *
     * @param dynamicTestDto
     * @return
     */
    public long insertDynamicTest(DynamicTestDto dynamicTestDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("dynamic_test")
                .content(dynamicTestDto.getHashTags())
                .build()).getIdx();

        long projectInformationIdx = projectInformationRepository.save(ProjectInformation.builder()
                .tableName("dynamic_test")
                .projectName(dynamicTestDto.getProjectName())
                .build()).getIdx();

        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("dynamic_test")
                .toolName(dynamicTestDto.getToolName())
                .toolNote(dynamicTestDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("dynamic_test")
                .compilerName(dynamicTestDto.getCompilerName())
                .compilerNote(dynamicTestDto.getCompilerNote())
                .build()).getIdx();

        dynamicTestDto.setHashTagsIdx(hashTagsIdx);
        dynamicTestDto.setProjectInformationIdx(projectInformationIdx);
        dynamicTestDto.setToolInformationIdx(toolInformationIdx);
        dynamicTestDto.setCompilerInformationIdx(compilerInformationIdx);

        return dynamicTestRepository.save(DynamicTestMapper.INSTANCE.toEntity(dynamicTestDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public DynamicTestDto findDynamicTestByIdx(long idx) {
        DynamicTestDto dynamicTestDto = new DynamicTestDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            dynamicTestDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            dynamicTestDto = dynamicTestRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(dynamicTestDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(dynamicTestDto.getLastModifiedByIdx());

            dynamicTestDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            dynamicTestDto.setCreatedByUser(createdByUser);
            dynamicTestDto.setLastModifiedByUser(lastModifiedByUser);

            dynamicTestRepositoryImpl.updateViewsByIdx(idx);
            dynamicTestDto.setViews(dynamicTestDto.getViews() + 1);
        }

        return dynamicTestDto;
    }

    /**
     * 수정
     *
     * @param idx
     * @param dynamicTestDto
     */
    @Transactional
    public void updateDynamicTest(long idx, DynamicTestDto dynamicTestDto) {
        DynamicTest persistDynamicTest = dynamicTestRepository.getReferenceById(idx);
        DynamicTest dynamicTest = DynamicTestMapper.INSTANCE.toEntity(dynamicTestDto);
        persistDynamicTest.update(dynamicTest);
        dynamicTestRepository.save(persistDynamicTest);

        HashTags persistHashTags = hashTagsRepository.getReferenceById(dynamicTestDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("dynamic_test")
                .content(dynamicTestDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        ProjectInformation persistProjectInformation = projectInformationRepository.getReferenceById(dynamicTestDto.getProjectInformationIdx());
        persistProjectInformation.update(ProjectInformation.builder()
                .tableName("dynamic_test")
                .projectName(dynamicTestDto.getProjectName())
                .build());
        projectInformationRepository.save(persistProjectInformation);

        ToolInformation persistToolInformation = toolInformationRepository.getReferenceById(dynamicTestDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("dynamic_test")
                .toolName(dynamicTestDto.getToolName())
                .toolNote(dynamicTestDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getReferenceById(dynamicTestDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("dynamic_test")
                .compilerName(dynamicTestDto.getCompilerName())
                .compilerNote(dynamicTestDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteDynamicTestByIdx(long idx) {
        DynamicTestDto dynamicTestDto = dynamicTestRepositoryImpl.findByIdx(idx);

        dynamicTestRepository.deleteById(idx);
        hashTagsRepository.deleteById(dynamicTestDto.getHashTagsIdx());
        projectInformationRepository.deleteById(dynamicTestDto.getProjectInformationIdx());
        toolInformationRepository.deleteById(dynamicTestDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(dynamicTestDto.getCompilerInformationIdx());
    }
}