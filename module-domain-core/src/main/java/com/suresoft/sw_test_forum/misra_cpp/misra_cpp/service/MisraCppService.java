package com.suresoft.sw_test_forum.misra_cpp.misra_cpp.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepository;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepositoryImpl;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.MisraCpp;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.MisraCppDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.MisraCppSearchDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.mapper.MisraCppMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.repository.MisraCppCommentRepositoryImpl;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.repository.MisraCppRepository;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.repository.MisraCppRepositoryImpl;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.MisraCppExampleDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.repository.MisraCppExampleRepository;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.service.MisraCppExampleCommentService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.service.MisraCppExampleService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.repository.MisraCppGuidelineRepository;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.service.MisraCppGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.service.MisraCppGuidelineCommentService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.service.MisraCppGuidelineLikeService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.service.MisraCppGuidelineService;
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
public class MisraCppService {
    private final MisraCppRepository misraCppRepository;
    private final MisraCppRepositoryImpl misraCppRepositoryImpl;
    private final MisraCppCommentRepositoryImpl misraCppCommentRepositoryImpl;
    private final MisraCppExampleRepository misraCppExampleRepository;
    private final MisraCppGuidelineRepository misraCppGuidelineRepository;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final MisraCppExampleService misraCppExampleService;
    private final MisraCppExampleCommentService misraCppExampleCommentService;
    private final MisraCppGuidelineService misraCppGuidelineService;
    private final MisraCppGuidelineAttachedFileService misraCppGuidelineAttachedFileService;
    private final MisraCppGuidelineCommentService misraCppGuidelineCommentService;
    private final MisraCppGuidelineLikeService misraCppGuidelineLikeService;
    private final UserService userService;
    @Value("${module.name}")
    private String moduleName;

    public MisraCppService(MisraCppRepository misraCppRepository,
                           MisraCppRepositoryImpl misraCppRepositoryImpl,
                           MisraCppCommentRepositoryImpl misraCppCommentRepositoryImpl,
                           MisraCppExampleRepository misraCppExampleRepository,
                           MisraCppGuidelineRepository misraCppGuidelineRepository,
                           HashTagsRepository hashTagsRepository,
                           HashTagsRepositoryImpl hashTagsRepositoryImpl,
                           @Lazy MisraCppExampleService misraCppExampleService,
                           MisraCppExampleCommentService misraCppExampleCommentService,
                           @Lazy MisraCppGuidelineService misraCppGuidelineService,
                           MisraCppGuidelineAttachedFileService misraCppGuidelineAttachedFileService,
                           MisraCppGuidelineCommentService misraCppGuidelineCommentService,
                           MisraCppGuidelineLikeService misraCppGuidelineLikeService,
                           UserService userService) {
        this.misraCppRepository = misraCppRepository;
        this.misraCppRepositoryImpl = misraCppRepositoryImpl;
        this.misraCppCommentRepositoryImpl = misraCppCommentRepositoryImpl;
        this.misraCppExampleRepository = misraCppExampleRepository;
        this.misraCppGuidelineRepository = misraCppGuidelineRepository;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.misraCppExampleService = misraCppExampleService;
        this.misraCppExampleCommentService = misraCppExampleCommentService;
        this.misraCppGuidelineService = misraCppGuidelineService;
        this.misraCppGuidelineAttachedFileService = misraCppGuidelineAttachedFileService;
        this.misraCppGuidelineCommentService = misraCppGuidelineCommentService;
        this.misraCppGuidelineLikeService = misraCppGuidelineLikeService;
        this.userService = userService;
    }

    /**
     * 대시보드 일 때, 조회수 많은 10개 리스트 조회
     *
     * @return
     */
    public List<MisraCppDto> findTop10ByViews() {
        List<MisraCppDto> misraCppDtoList = misraCppRepositoryImpl.findTop10ByViews();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (MisraCppDto misraCppDto : misraCppDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCppDto.getCreatedByIdx());

            misraCppDto.setNewIcon(NewIconCheck.isNew(misraCppDto.getCreatedDate()));
            misraCppDto.setCreatedByUser(createdByUser);
            misraCppDto.setCommentDtoCount(misraCppCommentRepositoryImpl.countAllByMisraCppIdx(misraCppDto.getIdx()));
        }

        return misraCppDtoList;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<MisraCppDto> findAllByHighPriorityAsc() {
        List<MisraCppDto> misraCppDtoList = misraCppRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (MisraCppDto misraCppDto : misraCppDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCppDto.getCreatedByIdx());

            misraCppDto.setNewIcon(NewIconCheck.isNew(misraCppDto.getCreatedDate()));
            misraCppDto.setCreatedByUser(createdByUser);
            misraCppDto.setCommentDtoCount(misraCppCommentRepositoryImpl.countAllByMisraCppIdx(misraCppDto.getIdx()));
        }

        return misraCppDtoList;
    }

    /**
     * 우선순위가 낮은 리스트 조회
     *
     * @param pageable
     * @param misraCppSearchDto
     * @return
     */
    public Page<MisraCppDto> findAll(Pageable pageable, MisraCppSearchDto misraCppSearchDto) {
        Page<MisraCppDto> misraCppDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        misraCppDtoList = misraCppRepositoryImpl.findAll(pageable, misraCppSearchDto);

        // NewIcon 판별, createdBy, comment 갯수 설정
        for (MisraCppDto misraCppDto : misraCppDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCppDto.getCreatedByIdx());

            misraCppDto.setNewIcon(NewIconCheck.isNew(misraCppDto.getCreatedDate()));
            misraCppDto.setCreatedByUser(createdByUser);
            misraCppDto.setCommentDtoCount(misraCppCommentRepositoryImpl.countAllByMisraCppIdx(misraCppDto.getIdx()));
        }

        return misraCppDtoList;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<MisraCppDto> highPriorityList = misraCppRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (MisraCppDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public MisraCppDto findMisraCppAutoComplete(MisraCppDto misraCppDto) {
        // title 설정
        for (String title : misraCppRepositoryImpl.findDistinctTitle()) {
            misraCppDto.getAutoCompleteTitle().add(title);
        }

        // hashTags 설정
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("misra_cpp")) {
            for (String hashTag : hashTags.split("#")) {
                misraCppDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        return misraCppDto;
    }

    /**
     * 등록
     *
     * @param misraCppDto
     * @return
     */
    public long insertMisraCpp(MisraCppDto misraCppDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("misra_cpp")
                .content(misraCppDto.getHashTags())
                .build()).getIdx();

        misraCppDto.setHashTagsIdx(hashTagsIdx);

        return misraCppRepository.save(MisraCppMapper.INSTANCE.toEntity(misraCppDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public MisraCppDto findMisraCppByIdx(long idx) {
        MisraCppDto misraCppDto = new MisraCppDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            misraCppDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            misraCppDto = misraCppRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCppDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCppDto.getLastModifiedByIdx());

            misraCppDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            misraCppDto.setCreatedByUser(createdByUser);
            misraCppDto.setLastModifiedByUser(lastModifiedByUser);

            misraCppRepositoryImpl.updateViewsByIdx(idx);
            misraCppDto.setViews(misraCppDto.getViews() + 1);
        }

        return misraCppDto;
    }

    /**
     * 대시보드에서, 조회
     *
     * @return
     */
    public long countPosts() {
        return misraCppRepository.count();
    }

    /**
     * MISRA CPP 규칙 규칙명 조회
     *
     * @param idx
     * @return
     */
    public String findMisraCppRuleByIdx(long idx) {
        return misraCppRepositoryImpl.findMisraCppByIdx(idx).getTitle();
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<MisraCppDto> highPriorityList = misraCppRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        MisraCpp misraCppPriority = misraCppRepositoryImpl.findMisraCppPriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (MisraCppDto highPriority : highPriorityList) {
            if (misraCppPriority.getPriority() == highPriority.getPriority()) {
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
     * @param misraCppDto
     * @return
     */
    @Transactional
    public void updateMisraCpp(long idx, MisraCppDto misraCppDto) {
        MisraCpp persistMisraCpp = misraCppRepository.getReferenceById(idx);
        MisraCpp misraCpp = MisraCppMapper.INSTANCE.toEntity(misraCppDto);
        persistMisraCpp.update(misraCpp);
        misraCppRepository.save(persistMisraCpp);

        HashTags persistHashTags = hashTagsRepository.getReferenceById(misraCppDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("misra_cpp")
                .content(misraCppDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    @Transactional
    public void deleteMisraCppByIdx(long idx) {
        MisraCppDto misraCppDto = misraCppRepositoryImpl.findByIdx(idx);

        misraCppRepository.deleteById(idx);
        misraCppExampleRepository.deleteAllByMisraCppIdx(idx);
        misraCppGuidelineRepository.deleteAllByMisraCppIdx(idx);
        hashTagsRepository.deleteById(misraCppDto.getHashTagsIdx());
    }

    /**
     * 삭제
     *
     * @param idx
     */
    @Transactional
    public void deleteRelatedMisraCppByIdx(long idx) throws Exception {
        MisraCppDto misraCppDto = misraCppRepositoryImpl.findByIdx(idx);
        misraCppDto = misraCppExampleService.findMisraCppExampleListWhenDelete(idx, misraCppDto);
        misraCppDto = misraCppGuidelineService.findMisraCppGuidelineListWhenDelete(idx, misraCppDto);

        // 삭제
        misraCppRepository.deleteById(idx);
        misraCppExampleRepository.deleteAllByMisraCppIdx(idx);
        misraCppGuidelineRepository.deleteAllByMisraCppIdx(idx);
        hashTagsRepository.deleteById(misraCppDto.getHashTagsIdx());

        // example 연관 데이터 삭제
        for (MisraCppExampleDto exampleDto : misraCppDto.getMisraCppExampleDtoList()) {
            misraCppExampleCommentService.deleteAllByMisraCppExampleIdx(exampleDto.getIdx());
        }

        // guideline 연관 데이터 삭제
        for (MisraCppGuidelineDto guidelineDto : misraCppDto.getMisraCppGuidelineDtoList()) {
            misraCppGuidelineAttachedFileService.deleteAllAttachedFile(guidelineDto.getIdx());
            misraCppGuidelineLikeService.deleteAllByMisraCppGuidelineIdx(guidelineDto.getIdx());
            misraCppGuidelineCommentService.deleteAllByMisraCppGuidelineIdx(guidelineDto.getIdx());
        }
    }
}