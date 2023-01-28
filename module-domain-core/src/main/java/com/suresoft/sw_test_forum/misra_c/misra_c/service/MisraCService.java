package com.suresoft.sw_test_forum.misra_c.misra_c.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepository;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepositoryImpl;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraC;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.MisraCDto;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.MisraCSearchDto;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.mapper.MisraCMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c.repository.MisraCCommentRepositoryImpl;
import com.suresoft.sw_test_forum.misra_c.misra_c.repository.MisraCRepository;
import com.suresoft.sw_test_forum.misra_c.misra_c.repository.MisraCRepositoryImpl;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.MisraCExampleDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.repository.MisraCExampleRepository;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.service.MisraCExampleCommentService;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.service.MisraCExampleService;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.repository.MisraCGuidelineRepository;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.service.MisraCGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.service.MisraCGuidelineCommentService;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.service.MisraCGuidelineLikeService;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.service.MisraCGuidelineService;
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
public class MisraCService {
    private final MisraCRepository misraCRepository;
    private final MisraCRepositoryImpl misraCRepositoryImpl;
    private final MisraCCommentRepositoryImpl misraCCommentRepositoryImpl;
    private final MisraCExampleRepository misraCExampleRepository;
    private final MisraCGuidelineRepository misraCGuidelineRepository;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final MisraCExampleService misraCExampleService;
    private final MisraCExampleCommentService misraCExampleCommentService;
    private final MisraCGuidelineService misraCGuidelineService;
    private final MisraCGuidelineAttachedFileService misraCGuidelineAttachedFileService;
    private final MisraCGuidelineCommentService misraCGuidelineCommentService;
    private final MisraCGuidelineLikeService misraCGuidelineLikeService;
    private final UserService userService;
    @Value("${module.name}")
    private String moduleName;

    public MisraCService(MisraCRepository misraCRepository,
                         MisraCRepositoryImpl misraCRepositoryImpl,
                         MisraCCommentRepositoryImpl misraCCommentRepositoryImpl,
                         MisraCExampleRepository misraCExampleRepository,
                         MisraCGuidelineRepository misraCGuidelineRepository,
                         HashTagsRepository hashTagsRepository,
                         HashTagsRepositoryImpl hashTagsRepositoryImpl,
                         @Lazy MisraCExampleService misraCExampleService,
                         MisraCExampleCommentService misraCExampleCommentService,
                         @Lazy MisraCGuidelineService misraCGuidelineService,
                         MisraCGuidelineAttachedFileService misraCGuidelineAttachedFileService,
                         MisraCGuidelineCommentService misraCGuidelineCommentService,
                         MisraCGuidelineLikeService misraCGuidelineLikeService,
                         UserService userService) {
        this.misraCRepository = misraCRepository;
        this.misraCRepositoryImpl = misraCRepositoryImpl;
        this.misraCCommentRepositoryImpl = misraCCommentRepositoryImpl;
        this.misraCExampleRepository = misraCExampleRepository;
        this.misraCGuidelineRepository = misraCGuidelineRepository;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.misraCExampleService = misraCExampleService;
        this.misraCExampleCommentService = misraCExampleCommentService;
        this.misraCGuidelineService = misraCGuidelineService;
        this.misraCGuidelineAttachedFileService = misraCGuidelineAttachedFileService;
        this.misraCGuidelineCommentService = misraCGuidelineCommentService;
        this.misraCGuidelineLikeService = misraCGuidelineLikeService;
        this.userService = userService;
    }

    /**
     * 대시보드 일 때, 조회수 많은 10개 리스트 조회
     *
     * @return
     */
    public List<MisraCDto> findTop10ByViews() {
        List<MisraCDto> misraCDtoList = misraCRepositoryImpl.findTop10ByViews();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (MisraCDto misraCDto : misraCDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCDto.getCreatedByIdx());

            misraCDto.setNewIcon(NewIconCheck.isNew(misraCDto.getCreatedDate()));
            misraCDto.setCreatedByUser(createdByUser);
            misraCDto.setCommentDtoCount(misraCCommentRepositoryImpl.countAllByMisraCIdx(misraCDto.getIdx()));
        }

        return misraCDtoList;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<MisraCDto> findAllByHighPriorityAsc() {
        List<MisraCDto> misraCDtoList = misraCRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (MisraCDto misraCDto : misraCDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCDto.getCreatedByIdx());

            misraCDto.setNewIcon(NewIconCheck.isNew(misraCDto.getCreatedDate()));
            misraCDto.setCreatedByUser(createdByUser);
            misraCDto.setCommentDtoCount(misraCCommentRepositoryImpl.countAllByMisraCIdx(misraCDto.getIdx()));
        }

        return misraCDtoList;
    }

    /**
     * 우선순위가 낮은 리스트 조회
     *
     * @param pageable
     * @param misraCSearchDto
     * @return
     */
    public Page<MisraCDto> findAll(Pageable pageable, MisraCSearchDto misraCSearchDto) {
        Page<MisraCDto> misraCDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        misraCDtoList = misraCRepositoryImpl.findAll(pageable, misraCSearchDto);

        // NewIcon 판별, createdBy, comment 갯수 설정
        for (MisraCDto misraCDto : misraCDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCDto.getCreatedByIdx());

            misraCDto.setNewIcon(NewIconCheck.isNew(misraCDto.getCreatedDate()));
            misraCDto.setCreatedByUser(createdByUser);
            misraCDto.setCommentDtoCount(misraCCommentRepositoryImpl.countAllByMisraCIdx(misraCDto.getIdx()));
        }

        return misraCDtoList;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<MisraCDto> highPriorityList = misraCRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (MisraCDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public MisraCDto findMisraCAutoComplete(MisraCDto misraCDto) {
        // title 설정
        for (String title : misraCRepositoryImpl.findDistinctTitle()) {
            misraCDto.getAutoCompleteTitle().add(title);
        }

        // hashTags 설정
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("misra_c")) {
            for (String hashTag : hashTags.split("#")) {
                misraCDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        return misraCDto;
    }

    /**
     * 등록
     *
     * @param misraCDto
     * @return
     */
    public long insertMisraC(MisraCDto misraCDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("misra_c")
                .content(misraCDto.getHashTags())
                .build()).getIdx();

        misraCDto.setHashTagsIdx(hashTagsIdx);

        return misraCRepository.save(MisraCMapper.INSTANCE.toEntity(misraCDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public MisraCDto findMisraCByIdx(long idx) {
        MisraCDto misraCDto = new MisraCDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            misraCDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            misraCDto = misraCRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCDto.getLastModifiedByIdx());

            misraCDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            misraCDto.setCreatedByUser(createdByUser);
            misraCDto.setLastModifiedByUser(lastModifiedByUser);

            misraCRepositoryImpl.updateViewsByIdx(idx);
            misraCDto.setViews(misraCDto.getViews() + 1);
        }

        return misraCDto;
    }

    /**
     * 대시보드에서, 조회
     *
     * @return
     */
    public long countPosts() {
        return misraCRepository.count();
    }

    /**
     * MISRA C 규칙 규칙명 조회
     *
     * @param idx
     * @return
     */
    public String findMisraCRuleByIdx(long idx) {
        return misraCRepositoryImpl.findMisraCByIdx(idx).getTitle();
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<MisraCDto> highPriorityList = misraCRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        MisraC misraCPriority = misraCRepositoryImpl.findMisraCPriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (MisraCDto highPriority : highPriorityList) {
            if (misraCPriority.getPriority() == highPriority.getPriority()) {
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
     * @param misraCDto
     * @return
     */
    @Transactional
    public void updateMisraC(long idx, MisraCDto misraCDto) {
        MisraC persistMisraC = misraCRepository.getReferenceById(idx);
        MisraC misraC = MisraCMapper.INSTANCE.toEntity(misraCDto);
        persistMisraC.update(misraC);
        misraCRepository.save(persistMisraC);

        HashTags persistHashTags = hashTagsRepository.getReferenceById(misraCDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("misra_c")
                .content(misraCDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    @Transactional
    public void deleteRelatedMisraCByIdx(long idx) throws Exception {
        MisraCDto misraCDto = misraCRepositoryImpl.findByIdx(idx);
        misraCDto = misraCExampleService.findMisraCExampleListWhenDelete(idx, misraCDto);
        misraCDto = misraCGuidelineService.findMisraCGuidelineListWhenDelete(idx, misraCDto);

        // 삭제
        misraCRepository.deleteById(idx);
        misraCExampleRepository.deleteAllByMisraCIdx(idx);
        misraCGuidelineRepository.deleteAllByMisraCIdx(idx);
        hashTagsRepository.deleteById(misraCDto.getHashTagsIdx());

        // example 연관 데이터 삭제
        for (MisraCExampleDto exampleDto : misraCDto.getMisraCExampleDtoList()) {
            misraCExampleCommentService.deleteAllByMisraCExampleIdx(exampleDto.getIdx());
        }

        // guideline 연관 데이터 삭제
        for (MisraCGuidelineDto guidelineDto : misraCDto.getMisraCGuidelineDtoList()) {
            misraCGuidelineAttachedFileService.deleteAllAttachedFile(guidelineDto.getIdx());
            misraCGuidelineLikeService.deleteAllByMisraCGuidelineIdx(guidelineDto.getIdx());
            misraCGuidelineCommentService.deleteAllByMisraCGuidelineIdx(guidelineDto.getIdx());
        }
    }
}