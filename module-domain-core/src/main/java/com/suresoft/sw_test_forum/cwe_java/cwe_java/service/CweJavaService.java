package com.suresoft.sw_test_forum.cwe_java.cwe_java.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepository;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepositoryImpl;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.CweJava;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaSearchDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.mapper.CweJavaMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.repository.CweJavaCommentRepositoryImpl;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.repository.CweJavaRepository;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.repository.CweJavaRepositoryImpl;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.CweJavaExampleDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.repository.CweJavaExampleRepository;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.service.CweJavaExampleCommentService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.service.CweJavaExampleService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository.CweJavaGuidelineRepository;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service.CweJavaGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service.CweJavaGuidelineCommentService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service.CweJavaGuidelineLikeService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service.CweJavaGuidelineService;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CweJavaService {
    private final CweJavaRepository cweJavaRepository;
    private final CweJavaRepositoryImpl cweJavaRepositoryImpl;
    private final CweJavaCommentRepositoryImpl cweJavaCommentRepositoryImpl;
    private final CweJavaExampleRepository cweJavaExampleRepository;
    private final CweJavaGuidelineRepository cweJavaGuidelineRepository;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final CweJavaExampleService cweJavaExampleService;
    private final CweJavaExampleCommentService cweJavaExampleCommentService;
    private final CweJavaGuidelineService cweJavaGuidelineService;
    private final CweJavaGuidelineAttachedFileService cweJavaGuidelineAttachedFileService;
    private final CweJavaGuidelineCommentService cweJavaGuidelineCommentService;
    private final CweJavaGuidelineLikeService cweJavaGuidelineLikeService;
    private final UserService userService;
    @Value("${module.name}")
    private String moduleName;

    public CweJavaService(CweJavaRepository cweJavaRepository,
                          CweJavaRepositoryImpl cweJavaRepositoryImpl,
                          CweJavaCommentRepositoryImpl cweJavaCommentRepositoryImpl,
                          CweJavaExampleRepository cweJavaExampleRepository,
                          CweJavaGuidelineRepository cweJavaGuidelineRepository,
                          HashTagsRepository hashTagsRepository,
                          HashTagsRepositoryImpl hashTagsRepositoryImpl,
                          @Lazy CweJavaExampleService cweJavaExampleService,
                          CweJavaExampleCommentService cweJavaExampleCommentService,
                          @Lazy CweJavaGuidelineService cweJavaGuidelineService,
                          CweJavaGuidelineAttachedFileService cweJavaGuidelineAttachedFileService,
                          CweJavaGuidelineCommentService cweJavaGuidelineCommentService,
                          CweJavaGuidelineLikeService cweJavaGuidelineLikeService,
                          UserService userService) {
        this.cweJavaRepository = cweJavaRepository;
        this.cweJavaRepositoryImpl = cweJavaRepositoryImpl;
        this.cweJavaCommentRepositoryImpl = cweJavaCommentRepositoryImpl;
        this.cweJavaExampleRepository = cweJavaExampleRepository;
        this.cweJavaGuidelineRepository = cweJavaGuidelineRepository;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.cweJavaExampleService = cweJavaExampleService;
        this.cweJavaExampleCommentService = cweJavaExampleCommentService;
        this.cweJavaGuidelineService = cweJavaGuidelineService;
        this.cweJavaGuidelineAttachedFileService = cweJavaGuidelineAttachedFileService;
        this.cweJavaGuidelineCommentService = cweJavaGuidelineCommentService;
        this.cweJavaGuidelineLikeService = cweJavaGuidelineLikeService;
        this.userService = userService;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<CweJavaDto> findAllByHighPriorityAsc() {
        List<CweJavaDto> cweJavaDtoList = cweJavaRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (CweJavaDto cweJavaDto : cweJavaDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaDto.getCreatedByIdx());

            cweJavaDto.setNewIcon(NewIconCheck.isNew(cweJavaDto.getCreatedDate()));
            cweJavaDto.setCreatedByUser(createdByUser);
            cweJavaDto.setCommentDtoCount(cweJavaCommentRepositoryImpl.countAllByCweJavaIdx(cweJavaDto.getIdx()));
        }

        return cweJavaDtoList;
    }

    /**
     * 우선순위가 낮은 리스트 조회
     *
     * @param pageable
     * @param cweJavaSearchDto
     * @return
     */
    public Page<CweJavaDto> findAll(Pageable pageable, CweJavaSearchDto cweJavaSearchDto) {
        Page<CweJavaDto> cweJavaDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        cweJavaDtoList = cweJavaRepositoryImpl.findAll(pageable, cweJavaSearchDto);

        // NewIcon 판별, createdBy, comment 갯수 설정
        for (CweJavaDto cweJavaDto : cweJavaDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaDto.getCreatedByIdx());

            cweJavaDto.setNewIcon(NewIconCheck.isNew(cweJavaDto.getCreatedDate()));
            cweJavaDto.setCreatedByUser(createdByUser);
            cweJavaDto.setCommentDtoCount(cweJavaCommentRepositoryImpl.countAllByCweJavaIdx(cweJavaDto.getIdx()));
        }

        return cweJavaDtoList;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<CweJavaDto> highPriorityList = cweJavaRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (CweJavaDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public CweJavaDto findCweJavaAutoComplete(CweJavaDto cweJavaDto) {
        // title 설정
        for (String title : cweJavaRepositoryImpl.findDistinctTitle()) {
            cweJavaDto.getAutoCompleteTitle().add(title);
        }

        // hashTags 설정
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("cwe_java")) {
            for (String hashTag : hashTags.split("#")) {
                cweJavaDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        return cweJavaDto;
    }

    /**
     * 등록
     *
     * @param cweJavaDto
     * @return
     */
    public long insertCweJava(CweJavaDto cweJavaDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("cwe_java")
                .content(cweJavaDto.getHashTags())
                .build()).getIdx();

        cweJavaDto.setHashTagsIdx(hashTagsIdx);

        return cweJavaRepository.save(CweJavaMapper.INSTANCE.toEntity(cweJavaDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public CweJavaDto findCweJavaByIdx(long idx) {
        CweJavaDto cweJavaDto = new CweJavaDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            cweJavaDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            cweJavaDto = cweJavaRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaDto.getLastModifiedByIdx());

            cweJavaDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            cweJavaDto.setCreatedByUser(createdByUser);
            cweJavaDto.setLastModifiedByUser(lastModifiedByUser);

            cweJavaRepositoryImpl.updateViewsByIdx(idx);
            cweJavaDto.setViews(cweJavaDto.getViews() + 1);
        }

        return cweJavaDto;
    }

    /**
     * 대시보드에서, 조회
     *
     * @return
     */
    public long countPosts() {
        return cweJavaRepository.count();
    }

    /**
     * CWE 규칙 규칙명 조회
     *
     * @param idx
     * @return
     */
    public String findCweJavaRuleByIdx(long idx) {
        return cweJavaRepositoryImpl.findCweJavaByIdx(idx).getTitle();
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<CweJavaDto> highPriorityList = cweJavaRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        CweJava cweJavaPriority = cweJavaRepositoryImpl.findCweJavaPriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (CweJavaDto highPriority : highPriorityList) {
            if (cweJavaPriority.getPriority() == highPriority.getPriority()) {
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
     * @param cweJavaDto
     * @return
     */
    @Transactional
    public void updateCweJava(long idx, CweJavaDto cweJavaDto) {
        CweJava persistCweJava = cweJavaRepository.getReferenceById(idx);
        CweJava cweJava = CweJavaMapper.INSTANCE.toEntity(cweJavaDto);
        persistCweJava.update(cweJava);
        cweJavaRepository.save(persistCweJava);

        HashTags persistHashTags = hashTagsRepository.getReferenceById(cweJavaDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("cwe_java")
                .content(cweJavaDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    @Transactional
    public void deleteRelatedCweJavaByIdx(long idx) throws Exception {
        CweJavaDto cweJavaDto = cweJavaRepositoryImpl.findByIdx(idx);
        cweJavaDto = cweJavaExampleService.findCweJavaExampleListWhenDelete(idx, cweJavaDto);
        cweJavaDto = cweJavaGuidelineService.findCweJavaGuidelineListWhenDelete(idx, cweJavaDto);

        // 삭제
        cweJavaRepository.deleteById(idx);
        cweJavaExampleRepository.deleteAllByCweJavaIdx(idx);
        cweJavaGuidelineRepository.deleteAllByCweJavaIdx(idx);
        hashTagsRepository.deleteById(cweJavaDto.getHashTagsIdx());

        // example 연관 데이터 삭제
        for (CweJavaExampleDto exampleDto : cweJavaDto.getCweJavaExampleDtoList()) {
            cweJavaExampleCommentService.deleteAllByCweJavaExampleIdx(exampleDto.getIdx());
        }

        // guideline 연관 데이터 삭제
        for (CweJavaGuidelineDto guidelineDto : cweJavaDto.getCweJavaGuidelineDtoList()) {
            cweJavaGuidelineAttachedFileService.deleteAllAttachedFile(guidelineDto.getIdx());
            cweJavaGuidelineLikeService.deleteAllByCweJavaGuidelineIdx(guidelineDto.getIdx());
            cweJavaGuidelineCommentService.deleteAllByCweJavaGuidelineIdx(guidelineDto.getIdx());
        }
    }
}