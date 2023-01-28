package com.suresoft.sw_test_forum.cwe.cwe_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.CompilerInformation;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.domain.ProjectInformation;
import com.suresoft.sw_test_forum.common.domain.ToolInformation;
import com.suresoft.sw_test_forum.common.repository.*;
import com.suresoft.sw_test_forum.cwe.cwe.dto.CweDto;
import com.suresoft.sw_test_forum.cwe.cwe.dto.mapper.CweMapper;
import com.suresoft.sw_test_forum.cwe.cwe.service.CweService;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuideline;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineSearchDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.mapper.CweGuidelineMapper;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.repository.CweGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.repository.CweGuidelineRepository;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.repository.CweGuidelineRepositoryImpl;
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
public class CweGuidelineService {
    private final CweGuidelineRepository cweGuidelineRepository;
    private final CweGuidelineRepositoryImpl cweGuidelineRepositoryImpl;
    private final CweGuidelineCommentRepositoryImpl cweGuidelineCommentRepositoryImpl;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final ProjectInformationRepository projectInformationRepository;
    private final ProjectInformationRepositoryImpl projectInformationRepositoryImpl;
    private final ToolInformationRepository toolInformationRepository;
    private final ToolInformationRepositoryImpl toolInformationRepositoryImpl;
    private final CompilerInformationRepository compilerInformationRepository;
    private final CompilerInformationRepositoryImpl compilerInformationRepositoryImpl;
    private final UserService userService;
    private final CweService cweService;
    @Value("${module.name}")
    private String moduleName;

    public CweGuidelineService(CweGuidelineRepository cweGuidelineRepository,
                               CweGuidelineRepositoryImpl cweGuidelineRepositoryImpl,
                               CweGuidelineCommentRepositoryImpl cweGuidelineCommentRepositoryImpl,
                               HashTagsRepository hashTagsRepository,
                               HashTagsRepositoryImpl hashTagsRepositoryImpl,
                               ProjectInformationRepository projectInformationRepository,
                               ProjectInformationRepositoryImpl projectInformationRepositoryImpl,
                               ToolInformationRepository toolInformationRepository,
                               ToolInformationRepositoryImpl toolInformationRepositoryImpl,
                               CompilerInformationRepository compilerInformationRepository,
                               CompilerInformationRepositoryImpl compilerInformationRepositoryImpl,
                               UserService userService,
                               CweService cweService) {
        this.cweGuidelineRepository = cweGuidelineRepository;
        this.cweGuidelineRepositoryImpl = cweGuidelineRepositoryImpl;
        this.cweGuidelineCommentRepositoryImpl = cweGuidelineCommentRepositoryImpl;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.projectInformationRepository = projectInformationRepository;
        this.projectInformationRepositoryImpl = projectInformationRepositoryImpl;
        this.toolInformationRepository = toolInformationRepository;
        this.toolInformationRepositoryImpl = toolInformationRepositoryImpl;
        this.compilerInformationRepository = compilerInformationRepository;
        this.compilerInformationRepositoryImpl = compilerInformationRepositoryImpl;
        this.userService = userService;
        this.cweService = cweService;
    }

    /**
     * 대시보드 읽기 페이지 일 때, 리스트 조회
     *
     * @return
     */
    public List<CweGuidelineDto> findTop10ByViewsAndGuidelinelike() {
        List<CweGuidelineDto> cweGuidelineDtoList = cweGuidelineRepositoryImpl.findTop10ByViews();
        // 좋아요 내림차순으로 정렬
        class CweGuidelineDtoComparator implements Comparator<CweGuidelineDto> {
            @Override
            public int compare(CweGuidelineDto o1, CweGuidelineDto o2) {
                return (int) o2.getLikeCountInList() - (int) o1.getLikeCountInList();
            }
        }
        Collections.sort(cweGuidelineDtoList, new CweGuidelineDtoComparator());

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (CweGuidelineDto cweGuidelineDto : cweGuidelineDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweGuidelineDto.getCreatedByIdx());

            cweGuidelineDto.setNewIcon(NewIconCheck.isNew(cweGuidelineDto.getCreatedDate()));
            cweGuidelineDto.setCreatedByUser(createdByUser);
            cweGuidelineDto.setCommentDtoCount(cweGuidelineCommentRepositoryImpl.countAllByCweGuidelineCIdx(cweGuidelineDto.getIdx()));
        }

        return cweGuidelineDtoList;
    }

    /**
     * 리스트 조회
     *
     * @return
     */
    public Page<CweGuidelineDto> findCweGuidelineList(Pageable pageable, CweGuidelineSearchDto cweGuidelineSearchDto) {
        Page<CweGuidelineDto> cweGuidelineDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        cweGuidelineDtoList = cweGuidelineRepositoryImpl.findAll(pageable, cweGuidelineSearchDto);

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (CweGuidelineDto cweGuidelineDto : cweGuidelineDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweGuidelineDto.getCreatedByIdx());

            cweGuidelineDto.setNewIcon(NewIconCheck.isNew(cweGuidelineDto.getCreatedDate()));
            cweGuidelineDto.setCreatedByUser(createdByUser);
            cweGuidelineDto.setCommentDtoCount(cweGuidelineCommentRepositoryImpl.countAllByCweGuidelineCIdx(cweGuidelineDto.getIdx()));

            // CWE 예제 코드 리스트를 조회하는 경우(CWE 보기 페이지에서 이동하지 않음)
            if (cweGuidelineSearchDto.getCweIdx() == 0) {
                cweGuidelineDto.setCweRule(cweService.findCweRuleByIdx(cweGuidelineDto.getCweIdx()));
            }
        }

        return cweGuidelineDtoList;
    }

    /**
     * CWE 읽기 페이지 일 때, 리스트 조회
     *
     * @param cweIdx
     * @param cweDto
     * @return
     */
    public CweDto findCweGuidelineList(long cweIdx, CweDto cweDto) {
        List<CweGuidelineDto> cweGuidelineDtoList = cweGuidelineRepositoryImpl.findAll(cweIdx);
        // 좋아요 내림차순으로 정렬
        class CweGuidelineDtoComparator implements Comparator<CweGuidelineDto> {
            @Override
            public int compare(CweGuidelineDto o1, CweGuidelineDto o2) {
                return (int) o2.getLikeCountInList() - (int) o1.getLikeCountInList();
            }
        }
        Collections.sort(cweGuidelineDtoList, new CweGuidelineDtoComparator());

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (CweGuidelineDto cweGuidelineDto : cweGuidelineDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweGuidelineDto.getCreatedByIdx());

            cweGuidelineDto.setNewIcon(NewIconCheck.isNew(cweGuidelineDto.getCreatedDate()));
            cweGuidelineDto.setCreatedByUser(createdByUser);
            cweGuidelineDto.setCommentDtoCount(cweGuidelineCommentRepositoryImpl.countAllByCweGuidelineCIdx(cweGuidelineDto.getIdx()));
        }

        cweDto = CweMapper.INSTANCE.toDtoByGuideline(cweDto, cweGuidelineDtoList);

        return cweDto;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public CweGuidelineDto findCweGuidelineAutoComplete(CweGuidelineDto cweGuidelineDto) {
        // title 설정
        for (String title : cweGuidelineRepositoryImpl.findDistinctTitle()) {
            cweGuidelineDto.getAutoCompleteTitle().add(title);
        }

        // hashTags 설정
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("cwe_guideline")) {
            for (String hashTag : hashTags.split("#")) {
                cweGuidelineDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        // projectName 설정
        for (String projectName : projectInformationRepositoryImpl.findDistinctProjectNameByTableName("cwe_guideline")) {
            cweGuidelineDto.getAutoCompleteProjectName().add(projectName);
        }

        // toolName 설정
        for (String toolName : toolInformationRepositoryImpl.findDistinctToolNameByTableName("cwe_guideline")) {
            cweGuidelineDto.getAutoCompleteToolName().add(toolName);
        }

        // toolNote 설정
        for (String toolNote : toolInformationRepositoryImpl.findDistinctToolNoteByTableName("cwe_guideline")) {
            cweGuidelineDto.getAutoCompleteToolNote().add(toolNote);
        }

        // compilerName 설정
        for (String compilerName : compilerInformationRepositoryImpl.findDistinctCompilerNameByTableName("cwe_guideline")) {
            cweGuidelineDto.getAutoCompleteCompilerName().add(compilerName);
        }

        // compilerNote 설정
        for (String compilerNote : compilerInformationRepositoryImpl.findDistinctCompilerNoteByTableName("cwe_guideline")) {
            cweGuidelineDto.getAutoCompleteCompilerNote().add(compilerNote);
        }

        return cweGuidelineDto;
    }

    /**
     * 등록
     *
     * @param cweGuidelineDto
     * @return
     */
    public long insertCweGuideline(CweGuidelineDto cweGuidelineDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("cwe_guideline")
                .content(cweGuidelineDto.getHashTags())
                .build()).getIdx();

        long projectInformationIdx = projectInformationRepository.save(ProjectInformation.builder()
                .tableName("cwe_guideline")
                .projectName(cweGuidelineDto.getProjectName())
                .build()).getIdx();

        long toolInformationIdx = toolInformationRepository.save(ToolInformation.builder()
                .tableName("cwe_guideline")
                .toolName(cweGuidelineDto.getToolName())
                .toolNote(cweGuidelineDto.getToolNote())
                .build()).getIdx();

        long compilerInformationIdx = compilerInformationRepository.save(CompilerInformation.builder()
                .tableName("cwe_guideline")
                .compilerName(cweGuidelineDto.getCompilerName())
                .compilerNote(cweGuidelineDto.getCompilerNote())
                .build()).getIdx();

        cweGuidelineDto.setHashTagsIdx(hashTagsIdx);
        cweGuidelineDto.setProjectInformationIdx(projectInformationIdx);
        cweGuidelineDto.setToolInformationIdx(toolInformationIdx);
        cweGuidelineDto.setCompilerInformationIdx(compilerInformationIdx);

        return cweGuidelineRepository.save(CweGuidelineMapper.INSTANCE.toEntity(cweGuidelineDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public CweGuidelineDto findCweGuidelineByIdx(long idx) {
        CweGuidelineDto cweGuidelineDto = new CweGuidelineDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            cweGuidelineDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            cweGuidelineDto = cweGuidelineRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweGuidelineDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweGuidelineDto.getLastModifiedByIdx());

            cweGuidelineDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            cweGuidelineDto.setCreatedByUser(createdByUser);
            cweGuidelineDto.setLastModifiedByUser(lastModifiedByUser);

            cweGuidelineRepositoryImpl.updateViewsByIdx(idx);
            cweGuidelineDto.setViews(cweGuidelineDto.getViews() + 1);
        }

        return cweGuidelineDto;
    }

    /**
     * 대시보드에서, 조회
     *
     * @return
     */
    public long countPosts() {
        return cweGuidelineRepository.count();
    }

    /**
     * 수정
     *
     * @param idx
     * @param cweGuidelineDto
     */
    @Transactional
    public void updateCweGuideline(long idx, CweGuidelineDto cweGuidelineDto) {
        CweGuideline persistCweGuideline = cweGuidelineRepository.getReferenceById(idx);
        CweGuideline cweGuideline = CweGuidelineMapper.INSTANCE.toEntity(cweGuidelineDto);
        persistCweGuideline.update(cweGuideline);
        cweGuidelineRepository.save(persistCweGuideline);

        HashTags persistHashTags = hashTagsRepository.getReferenceById(cweGuidelineDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("cwe_guideline")
                .content(cweGuidelineDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);

        ProjectInformation persistProjectInformation = projectInformationRepository.getReferenceById(cweGuidelineDto.getProjectInformationIdx());
        persistProjectInformation.update(ProjectInformation.builder()
                .tableName("cwe_guideline")
                .projectName(cweGuidelineDto.getProjectName())
                .build());
        projectInformationRepository.save(persistProjectInformation);

        ToolInformation persistToolInformation = toolInformationRepository.getReferenceById(cweGuidelineDto.getToolInformationIdx());
        persistToolInformation.update(ToolInformation.builder()
                .tableName("cwe_guideline")
                .toolName(cweGuidelineDto.getToolName())
                .toolNote(cweGuidelineDto.getToolNote())
                .build());
        toolInformationRepository.save(persistToolInformation);

        CompilerInformation persistCompilerInformation = compilerInformationRepository.getReferenceById(cweGuidelineDto.getCompilerInformationIdx());
        persistCompilerInformation.update(CompilerInformation.builder()
                .tableName("cwe_guideline")
                .compilerName(cweGuidelineDto.getCompilerName())
                .compilerNote(cweGuidelineDto.getCompilerNote())
                .build());
        compilerInformationRepository.save(persistCompilerInformation);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteCweGuidelineByIdx(long idx) {
        CweGuidelineDto cweGuidelineDto = cweGuidelineRepositoryImpl.findByIdx(idx);

        cweGuidelineRepository.deleteById(idx);
        hashTagsRepository.deleteById(cweGuidelineDto.getHashTagsIdx());
        projectInformationRepository.deleteById(cweGuidelineDto.getProjectInformationIdx());
        toolInformationRepository.deleteById(cweGuidelineDto.getToolInformationIdx());
        compilerInformationRepository.deleteById(cweGuidelineDto.getCompilerInformationIdx());
    }

    /**
     * CWE 읽기 페이지 일 때, 삭제를 위해 리스트 조회
     *
     * @param cweIdx
     * @param cweDto
     * @return
     */
    public CweDto findCweGuidelineListWhenDelete(long cweIdx, CweDto cweDto) {
        List<CweGuidelineDto> cweGuidelineDtoList = cweGuidelineRepositoryImpl.findAllWhenDelete(cweIdx);
        cweDto = CweMapper.INSTANCE.toDtoByGuideline(cweDto, cweGuidelineDtoList);

        return cweDto;
    }
}