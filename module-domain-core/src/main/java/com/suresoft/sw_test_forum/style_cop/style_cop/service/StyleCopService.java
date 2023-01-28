package com.suresoft.sw_test_forum.style_cop.style_cop.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepository;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepositoryImpl;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.MisraCDto;
import com.suresoft.sw_test_forum.style_cop.style_cop.domain.StyleCop;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.StyleCopDto;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.StyleCopSearchDto;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.mapper.StyleCopMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop.repository.StyleCopCommentRepositoryImpl;
import com.suresoft.sw_test_forum.style_cop.style_cop.repository.StyleCopRepository;
import com.suresoft.sw_test_forum.style_cop.style_cop.repository.StyleCopRepositoryImpl;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.StyleCopExampleDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.repository.StyleCopExampleRepository;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.service.StyleCopExampleCommentService;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.service.StyleCopExampleService;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository.StyleCopGuidelineRepository;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.service.StyleCopGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.service.StyleCopGuidelineCommentService;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.service.StyleCopGuidelineLikeService;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.service.StyleCopGuidelineService;
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
public class StyleCopService {
    private final StyleCopRepository styleCopRepository;
    private final StyleCopRepositoryImpl styleCopRepositoryImpl;
    private final StyleCopCommentRepositoryImpl styleCopCommentRepositoryImpl;
    private final StyleCopExampleRepository styleCopExampleRepository;
    private final StyleCopGuidelineRepository styleCopGuidelineRepository;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final StyleCopExampleService styleCopExampleService;
    private final StyleCopExampleCommentService styleCopExampleCommentService;
    private final StyleCopGuidelineService styleCopGuidelineService;
    private final StyleCopGuidelineAttachedFileService styleCopGuidelineAttachedFileService;
    private final StyleCopGuidelineCommentService styleCopGuidelineCommentService;
    private final StyleCopGuidelineLikeService styleCopGuidelineLikeService;
    private final UserService userService;
    @Value("${module.name}")
    private String moduleName;

    public StyleCopService(StyleCopRepository styleCopRepository,
                           StyleCopRepositoryImpl styleCopRepositoryImpl,
                           StyleCopCommentRepositoryImpl styleCopCommentRepositoryImpl,
                           StyleCopExampleRepository styleCopExampleRepository,
                           StyleCopGuidelineRepository styleCopGuidelineRepository,
                           HashTagsRepository hashTagsRepository,
                           HashTagsRepositoryImpl hashTagsRepositoryImpl,
                           @Lazy StyleCopExampleService styleCopExampleService,
                           StyleCopExampleCommentService styleCopExampleCommentService,
                           @Lazy StyleCopGuidelineService styleCopGuidelineService,
                           StyleCopGuidelineAttachedFileService styleCopGuidelineAttachedFileService,
                           StyleCopGuidelineCommentService styleCopGuidelineCommentService,
                           StyleCopGuidelineLikeService styleCopGuidelineLikeService,
                           UserService userService) {
        this.styleCopRepository = styleCopRepository;
        this.styleCopRepositoryImpl = styleCopRepositoryImpl;
        this.styleCopCommentRepositoryImpl = styleCopCommentRepositoryImpl;
        this.styleCopExampleRepository = styleCopExampleRepository;
        this.styleCopGuidelineRepository = styleCopGuidelineRepository;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.styleCopExampleService = styleCopExampleService;
        this.styleCopExampleCommentService = styleCopExampleCommentService;
        this.styleCopGuidelineService = styleCopGuidelineService;
        this.styleCopGuidelineAttachedFileService = styleCopGuidelineAttachedFileService;
        this.styleCopGuidelineCommentService = styleCopGuidelineCommentService;
        this.styleCopGuidelineLikeService = styleCopGuidelineLikeService;
        this.userService = userService;
    }

    /**
     * 대시보드 일 때, 조회수 많은 10개 리스트 조회
     *
     * @return
     */
    public List<StyleCopDto> findTop10ByViews() {
        List<StyleCopDto> styleCopDtoList = styleCopRepositoryImpl.findTop10ByViews();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (StyleCopDto styleCopDto : styleCopDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(styleCopDto.getCreatedByIdx());

            styleCopDto.setNewIcon(NewIconCheck.isNew(styleCopDto.getCreatedDate()));
            styleCopDto.setCreatedByUser(createdByUser);
            styleCopDto.setCommentDtoCount(styleCopCommentRepositoryImpl.countAllByStyleCopIdx(styleCopDto.getIdx()));
        }

        return styleCopDtoList;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<StyleCopDto> findAllByHighPriorityAsc() {
        List<StyleCopDto> styleCopDtoList = styleCopRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (StyleCopDto styleCopDto : styleCopDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(styleCopDto.getCreatedByIdx());

            styleCopDto.setNewIcon(NewIconCheck.isNew(styleCopDto.getCreatedDate()));
            styleCopDto.setCreatedByUser(createdByUser);
            styleCopDto.setCommentDtoCount(styleCopCommentRepositoryImpl.countAllByStyleCopIdx(styleCopDto.getIdx()));
        }

        return styleCopDtoList;
    }

    /**
     * 우선순위가 낮은 리스트 조회
     *
     * @param pageable
     * @param styleCopSearchDto
     * @return
     */
    public Page<StyleCopDto> findAll(Pageable pageable, StyleCopSearchDto styleCopSearchDto) {
        Page<StyleCopDto> styleCopDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        styleCopDtoList = styleCopRepositoryImpl.findAll(pageable, styleCopSearchDto);

        // NewIcon 판별, createdBy, comment 갯수 설정
        for (StyleCopDto styleCopDto : styleCopDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(styleCopDto.getCreatedByIdx());

            styleCopDto.setNewIcon(NewIconCheck.isNew(styleCopDto.getCreatedDate()));
            styleCopDto.setCreatedByUser(createdByUser);
            styleCopDto.setCommentDtoCount(styleCopCommentRepositoryImpl.countAllByStyleCopIdx(styleCopDto.getIdx()));
        }

        return styleCopDtoList;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<StyleCopDto> highPriorityList = styleCopRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (StyleCopDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public StyleCopDto findStyleCopAutoComplete(StyleCopDto styleCopDto) {
        // title 설정
        for (String title : styleCopRepositoryImpl.findDistinctTitle()) {
            styleCopDto.getAutoCompleteTitle().add(title);
        }

        // hashTags 설정
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("style_cop")) {
            for (String hashTag : hashTags.split("#")) {
                styleCopDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        return styleCopDto;
    }

    /**
     * 등록
     *
     * @param styleCopDto
     * @return
     */
    public long insertStyleCop(StyleCopDto styleCopDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("style_cop")
                .content(styleCopDto.getHashTags())
                .build()).getIdx();

        styleCopDto.setHashTagsIdx(hashTagsIdx);

        return styleCopRepository.save(StyleCopMapper.INSTANCE.toEntity(styleCopDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public StyleCopDto findStyleCopByIdx(long idx) {
        StyleCopDto styleCopDto = new StyleCopDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            styleCopDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            styleCopDto = styleCopRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(styleCopDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(styleCopDto.getLastModifiedByIdx());

            styleCopDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            styleCopDto.setCreatedByUser(createdByUser);
            styleCopDto.setLastModifiedByUser(lastModifiedByUser);

            styleCopRepositoryImpl.updateViewsByIdx(idx);
            styleCopDto.setViews(styleCopDto.getViews() + 1);
        }

        return styleCopDto;
    }

    /**
     * 대시보드에서, 조회
     *
     * @return
     */
    public long countPosts() {
        return styleCopRepository.count();
    }

    /**
     * StyleCop 규칙 규칙명 조회
     *
     * @param idx
     * @return
     */
    public String findStyleCopRuleByIdx(long idx) {
        return styleCopRepositoryImpl.findStyleCopByIdx(idx).getTitle();
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<StyleCopDto> highPriorityList = styleCopRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        StyleCop styleCopPriority = styleCopRepositoryImpl.findStyleCopPriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (StyleCopDto highPriority : highPriorityList) {
            if (styleCopPriority.getPriority() == highPriority.getPriority()) {
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
     * @param styleCopDto
     * @return
     */
    @Transactional
    public void updateStyleCop(long idx, StyleCopDto styleCopDto) {
        StyleCop persistStyleCop = styleCopRepository.getReferenceById(idx);
        StyleCop styleCop = StyleCopMapper.INSTANCE.toEntity(styleCopDto);
        persistStyleCop.update(styleCop);
        styleCopRepository.save(persistStyleCop);

        HashTags persistHashTags = hashTagsRepository.getReferenceById(styleCopDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("style_cop")
                .content(styleCopDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    @Transactional
    public void deleteRelatedStyleCopByIdx(long idx) throws Exception {
        StyleCopDto styleCopDto = styleCopRepositoryImpl.findByIdx(idx);
        styleCopDto = styleCopExampleService.findStyleCopExampleListWhenDelete(idx, styleCopDto);
        styleCopDto = styleCopGuidelineService.findStyleCopGuidelineListWhenDelete(idx, styleCopDto);

        // 삭제
        styleCopRepository.deleteById(idx);
        styleCopExampleRepository.deleteAllByStyleCopIdx(idx);
        styleCopGuidelineRepository.deleteAllByStyleCopIdx(idx);
        hashTagsRepository.deleteById(styleCopDto.getHashTagsIdx());

        // example 연관 데이터 삭제
        for (StyleCopExampleDto exampleDto : styleCopDto.getStyleCopExampleDtoList()) {
            styleCopExampleCommentService.deleteAllByStyleCopExampleIdx(exampleDto.getIdx());
        }

        // guideline 연관 데이터 삭제
        for (StyleCopGuidelineDto guidelineDto : styleCopDto.getStyleCopGuidelineDtoList()) {
            styleCopGuidelineAttachedFileService.deleteAllAttachedFile(guidelineDto.getIdx());
            styleCopGuidelineLikeService.deleteAllByStyleCopGuidelineIdx(guidelineDto.getIdx());
            styleCopGuidelineCommentService.deleteAllByStyleCopGuidelineIdx(guidelineDto.getIdx());
        }
    }
}