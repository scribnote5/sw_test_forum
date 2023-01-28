package com.suresoft.sw_test_forum.cwe.cwe.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepository;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepositoryImpl;
import com.suresoft.sw_test_forum.cwe.cwe.domain.Cwe;
import com.suresoft.sw_test_forum.cwe.cwe.dto.CweDto;
import com.suresoft.sw_test_forum.cwe.cwe.dto.CweSearchDto;
import com.suresoft.sw_test_forum.cwe.cwe.dto.mapper.CweMapper;
import com.suresoft.sw_test_forum.cwe.cwe.repository.CweCommentRepositoryImpl;
import com.suresoft.sw_test_forum.cwe.cwe.repository.CweRepository;
import com.suresoft.sw_test_forum.cwe.cwe.repository.CweRepositoryImpl;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.CweExampleDto;
import com.suresoft.sw_test_forum.cwe.cwe_example.repository.CweExampleRepository;
import com.suresoft.sw_test_forum.cwe.cwe_example.service.CweExampleCommentService;
import com.suresoft.sw_test_forum.cwe.cwe_example.service.CweExampleService;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.repository.CweGuidelineRepository;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.service.CweGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.service.CweGuidelineCommentService;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.service.CweGuidelineLikeService;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.service.CweGuidelineService;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.MisraCDto;
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
public class CweService {
    private final CweRepository cweRepository;
    private final CweRepositoryImpl cweRepositoryImpl;
    private final CweCommentRepositoryImpl cweCommentRepositoryImpl;
    private final CweExampleRepository cweExampleRepository;
    private final CweGuidelineRepository cweGuidelineRepository;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final CweExampleService cweExampleService;
    private final CweExampleCommentService cweExampleCommentService;
    private final CweGuidelineService cweGuidelineService;
    private final CweGuidelineAttachedFileService cweGuidelineAttachedFileService;
    private final CweGuidelineCommentService cweGuidelineCommentService;
    private final CweGuidelineLikeService cweGuidelineLikeService;
    private final UserService userService;
    @Value("${module.name}")
    private String moduleName;

    public CweService(CweRepository cweRepository,
                      CweRepositoryImpl cweRepositoryImpl,
                      CweCommentRepositoryImpl cweCommentRepositoryImpl,
                      CweExampleRepository cweExampleRepository,
                      CweGuidelineRepository cweGuidelineRepository,
                      HashTagsRepository hashTagsRepository,
                      HashTagsRepositoryImpl hashTagsRepositoryImpl,
                      @Lazy CweExampleService cweExampleService,
                      CweExampleCommentService cweExampleCommentService,
                      @Lazy CweGuidelineService cweGuidelineService,
                      CweGuidelineAttachedFileService cweGuidelineAttachedFileService,
                      CweGuidelineCommentService cweGuidelineCommentService,
                      CweGuidelineLikeService cweGuidelineLikeService,
                      UserService userService) {
        this.cweRepository = cweRepository;
        this.cweRepositoryImpl = cweRepositoryImpl;
        this.cweCommentRepositoryImpl = cweCommentRepositoryImpl;
        this.cweExampleRepository = cweExampleRepository;
        this.cweGuidelineRepository = cweGuidelineRepository;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.cweExampleService = cweExampleService;
        this.cweExampleCommentService = cweExampleCommentService;
        this.cweGuidelineService = cweGuidelineService;
        this.cweGuidelineAttachedFileService = cweGuidelineAttachedFileService;
        this.cweGuidelineCommentService = cweGuidelineCommentService;
        this.cweGuidelineLikeService = cweGuidelineLikeService;
        this.userService = userService;
    }

    /**
     * 대시보드 일 때, 조회수 많은 CWE 10개 리스트 조회
     *
     * @return
     */
    public List<CweDto> findTop10ByViews() {
        List<CweDto> cweDtoList = cweRepositoryImpl.findTop10ByViews();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (CweDto cweDto : cweDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweDto.getCreatedByIdx());

            cweDto.setNewIcon(NewIconCheck.isNew(cweDto.getCreatedDate()));
            cweDto.setCreatedByUser(createdByUser);
            cweDto.setCommentDtoCount(cweCommentRepositoryImpl.countAllByCweIdx(cweDto.getIdx()));
        }

        return cweDtoList;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<CweDto> findAllByHighPriorityAsc() {
        List<CweDto> cweDtoList = cweRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (CweDto cweDto : cweDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweDto.getCreatedByIdx());

            cweDto.setNewIcon(NewIconCheck.isNew(cweDto.getCreatedDate()));
            cweDto.setCreatedByUser(createdByUser);
            cweDto.setCommentDtoCount(cweCommentRepositoryImpl.countAllByCweIdx(cweDto.getIdx()));
        }

        return cweDtoList;
    }

    /**
     * 우선순위가 낮은 리스트 조회
     *
     * @param pageable
     * @param cweSearchDto
     * @return
     */
    public Page<CweDto> findAll(Pageable pageable, CweSearchDto cweSearchDto) {
        Page<CweDto> cweDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        cweDtoList = cweRepositoryImpl.findAll(pageable, cweSearchDto);

        // NewIcon 판별, createdBy, comment 갯수 설정
        for (CweDto cweDto : cweDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweDto.getCreatedByIdx());

            cweDto.setNewIcon(NewIconCheck.isNew(cweDto.getCreatedDate()));
            cweDto.setCreatedByUser(createdByUser);
            cweDto.setCommentDtoCount(cweCommentRepositoryImpl.countAllByCweIdx(cweDto.getIdx()));
        }

        return cweDtoList;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<CweDto> highPriorityList = cweRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (CweDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public CweDto findCweAutoComplete(CweDto cweDto) {
        // title 설정
        for (String title : cweRepositoryImpl.findDistinctTitle()) {
            cweDto.getAutoCompleteTitle().add(title);
        }

        // hashTags 설정
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("cwe")) {
            for (String hashTag : hashTags.split("#")) {
                cweDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        return cweDto;
    }

    /**
     * 등록
     *
     * @param cweDto
     * @return
     */
    public long insertCwe(CweDto cweDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("cwe")
                .content(cweDto.getHashTags())
                .build()).getIdx();

        cweDto.setHashTagsIdx(hashTagsIdx);

        return cweRepository.save(CweMapper.INSTANCE.toEntity(cweDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public CweDto findCweByIdx(long idx) {
        CweDto cweDto = new CweDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            cweDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            cweDto = cweRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweDto.getLastModifiedByIdx());

            cweDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            cweDto.setCreatedByUser(createdByUser);
            cweDto.setLastModifiedByUser(lastModifiedByUser);

            cweRepositoryImpl.updateViewsByIdx(idx);
            cweDto.setViews(cweDto.getViews() + 1);
        }

        return cweDto;
    }

    /**
     * 대시보드에서, 조회
     *
     * @return
     */
    public long countPosts() {
        return cweRepository.count();
    }

    /**
     * CWE 규칙 규칙명 조회
     *
     * @param idx
     * @return
     */
    public String findCweRuleByIdx(long idx) {
        return cweRepositoryImpl.findCweByIdx(idx).getTitle();
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<CweDto> highPriorityList = cweRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        Cwe cwePriority = cweRepositoryImpl.findCwePriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (CweDto highPriority : highPriorityList) {
            if (cwePriority.getPriority() == highPriority.getPriority()) {
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
     * @param cweDto
     * @return
     */
    @Transactional
    public void updateCwe(long idx, CweDto cweDto) {
        Cwe persistCwe = cweRepository.getReferenceById(idx);
        Cwe cwe = CweMapper.INSTANCE.toEntity(cweDto);
        persistCwe.update(cwe);
        cweRepository.save(persistCwe);

        HashTags persistHashTags = hashTagsRepository.getReferenceById(cweDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("cwe")
                .content(cweDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    @Transactional
    public void deleteRelatedCweByIdx(long idx) throws Exception {
        CweDto cweDto = cweRepositoryImpl.findByIdx(idx);
        cweDto = cweExampleService.findCweExampleListWhenDelete(idx, cweDto);
        cweDto = cweGuidelineService.findCweGuidelineListWhenDelete(idx, cweDto);

        // 삭제
        cweRepository.deleteById(idx);
        cweExampleRepository.deleteAllByCweIdx(idx);
        cweGuidelineRepository.deleteAllByCweIdx(idx);
        hashTagsRepository.deleteById(cweDto.getHashTagsIdx());

        // example 연관 데이터 삭제
        for (CweExampleDto exampleDto : cweDto.getCweExampleDtoList()) {
            cweExampleCommentService.deleteAllByCweExampleIdx(exampleDto.getIdx());
        }

        // guideline 연관 데이터 삭제
        for (CweGuidelineDto guidelineDto : cweDto.getCweGuidelineDtoList()) {
            cweGuidelineAttachedFileService.deleteAllAttachedFile(guidelineDto.getIdx());
            cweGuidelineLikeService.deleteAllByCweGuidelineIdx(guidelineDto.getIdx());
            cweGuidelineCommentService.deleteAllByCweGuidelineIdx(guidelineDto.getIdx());
        }
    }
}