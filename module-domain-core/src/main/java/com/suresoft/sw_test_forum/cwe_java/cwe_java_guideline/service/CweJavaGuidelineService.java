package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.domain.ProjectInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.mapper.CweJavaMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.service.CweJavaService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuideline;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineSearchDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.mapper.CweJavaGuidelineMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository.CweJavaGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository.CweJavaGuidelineRepository;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository.CweJavaGuidelineRepositoryImpl;
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
public class CweJavaGuidelineService {
    private final CweJavaGuidelineRepository cweJavaGuidelineRepository;
    private final CweJavaGuidelineRepositoryImpl cweJavaGuidelineRepositoryImpl;
    private final CweJavaGuidelineCommentRepositoryImpl cweJavaGuidelineCommentRepositoryImpl;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final ProjectInformationRepository projectInformationRepository;
    private final ProjectInformationRepositoryImpl projectInformationRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final CweJavaService cweJavaService;
    @Value("${module.name}")
    private String moduleName;

    public CweJavaGuidelineService(CweJavaGuidelineRepository cweJavaGuidelineRepository,
                                   CweJavaGuidelineRepositoryImpl cweJavaGuidelineRepositoryImpl,
                                   CweJavaGuidelineCommentRepositoryImpl cweJavaGuidelineCommentRepositoryImpl,
                                   HashTagsRepository hashTagsRepository,
                                   HashTagsRepositoryImpl hashTagsRepositoryImpl,
                                   ProjectInformationRepository projectInformationRepository,
                                   ProjectInformationRepositoryImpl projectInformationRepositoryImpl,
                                   ToolInformationRepository toolInformationRepository,
                                   ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                                   CompilerInformationRepository compilerInformationRepository,
                                   CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                                   UserService userService,
                                   CweJavaService cweJavaService) {
        this.cweJavaGuidelineRepository = cweJavaGuidelineRepository;
        this.cweJavaGuidelineRepositoryImpl = cweJavaGuidelineRepositoryImpl;
        this.cweJavaGuidelineCommentRepositoryImpl = cweJavaGuidelineCommentRepositoryImpl;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.projectInformationRepository = projectInformationRepository;
        this.projectInformationRepositoryImpl = projectInformationRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.cweJavaService = cweJavaService;
    }

    /**
     * 리스트 조회
     *
     * @return
     */
    public Page<CweJavaGuidelineDto> findCweJavaGuidelineList(Pageable pageable, CweJavaGuidelineSearchDto cweJavaGuidelineSearchDto) {
        Page<CweJavaGuidelineDto> cweJavaGuidelineDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        cweJavaGuidelineDtoList = cweJavaGuidelineRepositoryImpl.findAll(pageable, cweJavaGuidelineSearchDto);

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (CweJavaGuidelineDto cweJavaGuidelineDto : cweJavaGuidelineDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaGuidelineDto.getCreatedByIdx());

            cweJavaGuidelineDto.setNewIcon(NewIconCheck.isNew(cweJavaGuidelineDto.getCreatedDate()));
            cweJavaGuidelineDto.setCreatedByUser(createdByUser);
            cweJavaGuidelineDto.setCommentDtoCount(cweJavaGuidelineCommentRepositoryImpl.countAllByCweJavaGuidelineCIdx(cweJavaGuidelineDto.getIdx()));

            // CWE 예제 코드 리스트를 조회하는 경우(CWE 보기 페이지에서 이동하지 않음)
            if (cweJavaGuidelineSearchDto.getCweJavaIdx() == 0) {
                cweJavaGuidelineDto.setCweJavaRule(cweJavaService.findCweJavaRuleByIdx(cweJavaGuidelineDto.getCweJavaIdx()));
            }
        }

        return cweJavaGuidelineDtoList;
    }

    /**
     * CWE 읽기 페이지 일 때, 리스트 조회
     *
     * @param cweJavaIdx
     * @param cweJavaDto
     * @return
     */
    public CweJavaDto findCweJavaGuidelineList(long cweJavaIdx, CweJavaDto cweJavaDto) {
        List<CweJavaGuidelineDto> cweJavaGuidelineDtoList = cweJavaGuidelineRepositoryImpl.findAll(cweJavaIdx);
        // 좋아요 내림차순으로 정렬
        class CweJavaGuidelineDtoComparator implements Comparator<CweJavaGuidelineDto> {
            @Override
            public int compare(CweJavaGuidelineDto o1, CweJavaGuidelineDto o2) {
                return (int) o2.getLikeCountInList() - (int) o1.getLikeCountInList();
            }
        }
        Collections.sort(cweJavaGuidelineDtoList, new CweJavaGuidelineDtoComparator());

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (CweJavaGuidelineDto cweJavaGuidelineDto : cweJavaGuidelineDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaGuidelineDto.getCreatedByIdx());

            cweJavaGuidelineDto.setNewIcon(NewIconCheck.isNew(cweJavaGuidelineDto.getCreatedDate()));
            cweJavaGuidelineDto.setCreatedByUser(createdByUser);
            cweJavaGuidelineDto.setCommentDtoCount(cweJavaGuidelineCommentRepositoryImpl.countAllByCweJavaGuidelineCIdx(cweJavaGuidelineDto.getIdx()));
        }

        cweJavaDto = CweJavaMapper.INSTANCE.toDtoByGuideline(cweJavaDto, cweJavaGuidelineDtoList);

        return cweJavaDto;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public CweJavaGuidelineDto findCweJavaGuidelineAutoComplete(CweJavaGuidelineDto cweJavaGuidelineDto) {
        // title 설정
        for (String title : cweJavaGuidelineRepositoryImpl.findDistinctTitle()) {
            cweJavaGuidelineDto.getAutoCompleteTitle().add(title);
        }

        // hashTags 설정
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("cwe_java_guideline")) {
            for (String hashTag : hashTags.split("#")) {
                cweJavaGuidelineDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // projectName 설정
        for (String projectName : projectInformationRepositoryImpl.findDistinctProjectNameByTableName("cwe_java_guideline")) {
            cweJavaGuidelineDto.getAutoCompleteProjectName().add(projectName);
        }

        // toolName 설정
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("cwe_java_guideline")) {
            cweJavaGuidelineDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote 설정
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("cwe_java_guideline")) {
            cweJavaGuidelineDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName 설정
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("cwe_java_guideline")) {
            cweJavaGuidelineDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote 설정
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("cwe_java_guideline")) {
            cweJavaGuidelineDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return cweJavaGuidelineDto;
    }

    /**
     * 등록
     *
     * @param cweJavaGuidelineDto
     * @return
     */
    public long insertCweJavaGuideline(CweJavaGuidelineDto cweJavaGuidelineDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("cwe_java_guideline")
                .content(cweJavaGuidelineDto.getHashTags())
                .build()).getIdx();

        long projectInformationIdx = projectInformationRepository.save(ProjectInformation.builder()
                .tableName("cwe_java_guideline")
                .projectName(cweJavaGuidelineDto.getProjectName())
                .build()).getIdx();

        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("cwe_java_guideline")
                .toolName(cweJavaGuidelineDto.getToolName())
                .toolNote(cweJavaGuidelineDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("cwe_java_guideline")
                .compilerName(cweJavaGuidelineDto.getCompilerName())
                .compilerNote(cweJavaGuidelineDto.getCompilerNote())
                .build()).getIdx();

        cweJavaGuidelineDto.setHashTagsIdx(hashTagsIdx);
        cweJavaGuidelineDto.setProjectInformationIdx(projectInformationIdx);
        cweJavaGuidelineDto.setToolInformationIdx(toolInformationIdx);
        cweJavaGuidelineDto.setCompilerInformationIdx(compilerInformationIdx);

        return cweJavaGuidelineRepository.save(CweJavaGuidelineMapper.INSTANCE.toEntity(cweJavaGuidelineDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public CweJavaGuidelineDto findCweJavaGuidelineByIdx(long idx) {
        CweJavaGuidelineDto cweJavaGuidelineDto = new CweJavaGuidelineDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            cweJavaGuidelineDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            cweJavaGuidelineDto = cweJavaGuidelineRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaGuidelineDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaGuidelineDto.getLastModifiedByIdx());

            cweJavaGuidelineDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            cweJavaGuidelineDto.setCreatedByUser(createdByUser);
            cweJavaGuidelineDto.setLastModifiedByUser(lastModifiedByUser);

            cweJavaGuidelineRepositoryImpl.updateViewsByIdx(idx);
            cweJavaGuidelineDto.setViews(cweJavaGuidelineDto.getViews() + 1);
        }

        return cweJavaGuidelineDto;
    }

    /**
     * 대시보드에서, 조회
     *
     * @return
     */
    public long countPosts() {
        return cweJavaGuidelineRepository.count();
    }

    /**
     * 수정
     *
     * @param idx
     * @param cweJavaGuidelineDto
     */
    @Transactional
    public void updateCweJavaGuideline(long idx, CweJavaGuidelineDto cweJavaGuidelineDto) {
        CweJavaGuideline persistCweJavaGuideline = cweJavaGuidelineRepository.getReferenceById(idx);
        CweJavaGuideline cweJavaGuideline = CweJavaGuidelineMapper.INSTANCE.toEntity(cweJavaGuidelineDto);
        persistCweJavaGuideline.update(cweJavaGuideline);
        cweJavaGuidelineRepository.save(persistCweJavaGuideline);

        HashTags persistHashTags = hashTagsRepository.getReferenceById(cweJavaGuidelineDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("cwe_java_guideline")
                .content(cweJavaGuidelineDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        ProjectInformation persistProjectInformation = projectInformationRepository.getReferenceById(cweJavaGuidelineDto.getProjectInformationIdx());
        persistProjectInformation.update(ProjectInformation.builder()
                .tableName("cwe_java_guideline")
                .projectName(cweJavaGuidelineDto.getProjectName())
                .build());
        projectInformationRepository.save(persistProjectInformation);

        ToolInformation persistToolInformation = toolInformationRepository.getReferenceById(cweJavaGuidelineDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("cwe_java_guideline")
                .toolName(cweJavaGuidelineDto.getToolName())
                .toolNote(cweJavaGuidelineDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getReferenceById(cweJavaGuidelineDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("cwe_java_guideline")
                .compilerName(cweJavaGuidelineDto.getCompilerName())
                .compilerNote(cweJavaGuidelineDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteCweJavaGuidelineByIdx(long idx) {
        CweJavaGuidelineDto cweJavaGuidelineDto = cweJavaGuidelineRepositoryImpl.findByIdx(idx);

        cweJavaGuidelineRepository.deleteById(idx);
        hashTagsRepository.deleteById(cweJavaGuidelineDto.getHashTagsIdx());
        projectInformationRepository.deleteById(cweJavaGuidelineDto.getProjectInformationIdx());
        toolInformationRepository.deleteById(cweJavaGuidelineDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(cweJavaGuidelineDto.getCompilerInformationIdx());
    }

    /**
     * CWE 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param cweJavaIdx
     * @param cweJavaDto
     * @return
     */
    public CweJavaDto findCweJavaGuidelineListWhenDelete(long cweJavaIdx, CweJavaDto cweJavaDto) {
        List<CweJavaGuidelineDto> cweJavaGuidelineDtoList = cweJavaGuidelineRepositoryImpl.findAllWhenDelete(cweJavaIdx);
        cweJavaDto = CweJavaMapper.INSTANCE.toDtoByGuideline(cweJavaDto, cweJavaGuidelineDtoList);

        return cweJavaDto;
    }
}