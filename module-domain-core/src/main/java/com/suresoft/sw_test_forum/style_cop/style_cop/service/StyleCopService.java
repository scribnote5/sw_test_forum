package com.suresoft.sw_test_forum.style_cop.style_cop.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepository;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepositoryImpl;
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
     * ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public List<StyleCopDto> findAllByHighPriorityAsc() {
        List<StyleCopDto> styleCopDtoList = styleCopRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (StyleCopDto styleCopDto : styleCopDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(styleCopDto.getCreatedByIdx());

            styleCopDto.setNewIcon(NewIconCheck.isNew(styleCopDto.getCreatedDate()));
            styleCopDto.setCreatedByUser(createdByUser);
            styleCopDto.setCommentDtoCount(styleCopCommentRepositoryImpl.countAllByStyleCopIdx(styleCopDto.getIdx()));
        }

        return styleCopDtoList;
    }

    /**
     * ??????????????? ?????? ????????? ??????
     *
     * @param pageable
     * @param styleCopSearchDto
     * @return
     */
    public Page<StyleCopDto> findAll(Pageable pageable, StyleCopSearchDto styleCopSearchDto) {
        Page<StyleCopDto> styleCopDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        styleCopDtoList = styleCopRepositoryImpl.findAll(pageable, styleCopSearchDto);

        // NewIcon ??????, createdBy, comment ?????? ??????
        for (StyleCopDto styleCopDto : styleCopDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(styleCopDto.getCreatedByIdx());

            styleCopDto.setNewIcon(NewIconCheck.isNew(styleCopDto.getCreatedDate()));
            styleCopDto.setCreatedByUser(createdByUser);
            styleCopDto.setCommentDtoCount(styleCopCommentRepositoryImpl.countAllByStyleCopIdx(styleCopDto.getIdx()));
        }

        return styleCopDtoList;
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<StyleCopDto> highPriorityList = styleCopRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "??????????????? ???????????? ????????????.");

        for (StyleCopDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "??????????????? ???????????? ????????????.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public StyleCopDto findStyleCopAutoComplete(StyleCopDto styleCopDto) {
        // title ??????
        for (String title : styleCopRepositoryImpl.findDistinctTitle()) {
            styleCopDto.getAutoCompleteTitle().add(title);
        }

        // hashTags ??????
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("style_cop")) {
            for (String hashTag : hashTags.split("#")) {
                styleCopDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        return styleCopDto;
    }

    /**
     * ??????
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
     * ??????
     *
     * @param idx
     * @return
     */
    public StyleCopDto findStyleCopByIdx(long idx) {
        StyleCopDto styleCopDto = new StyleCopDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            styleCopDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
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
     * ??????????????????, ??????
     *
     * @return
     */
    public long countPosts() {
        return styleCopRepository.count();
    }

    /**
     * StyleCop ?????? ?????? ??????
     *
     * @param idx
     * @return
     */
    public String findStyleCopRuleByIdx(long idx) {
        return styleCopRepositoryImpl.findStyleCopByIdx(idx).getTitle();
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<StyleCopDto> highPriorityList = styleCopRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        StyleCop styleCopPriority = styleCopRepositoryImpl.findStyleCopPriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "??????????????? ???????????? ????????????.");

        for (StyleCopDto highPriority : highPriorityList) {
            if (styleCopPriority.getPriority() == highPriority.getPriority()) {
                priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(false, "?????? ????????? ???????????? ?????????.");
            } else {
                priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "??????????????? ???????????? ????????????.");
            }
        }

        return priorityDtoArray;
    }

    /**
     * ??????
     *
     * @param idx
     * @param styleCopDto
     * @return
     */
    @Transactional
    public void updateStyleCop(long idx, StyleCopDto styleCopDto) {
        StyleCop persistStyleCop = styleCopRepository.getById(idx);
        StyleCop styleCop = StyleCopMapper.INSTANCE.toEntity(styleCopDto);
        persistStyleCop.update(styleCop);
        styleCopRepository.save(persistStyleCop);

        HashTags persistHashTags = hashTagsRepository.getById(styleCopDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("style_cop")
                .content(styleCopDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);
    }

    /**
     * ??????
     *
     * @param idx
     */
    @Transactional
    public void deleteRelatedStyleCopByIdx(long idx) throws Exception {
        StyleCopDto styleCopDto = styleCopRepositoryImpl.findByIdx(idx);
        styleCopDto = styleCopExampleService.findStyleCopExampleListWhenDelete(idx, styleCopDto);
        styleCopDto = styleCopGuidelineService.findStyleCopGuidelineListWhenDelete(idx, styleCopDto);

        // ??????
        styleCopRepository.deleteById(idx);
        styleCopExampleRepository.deleteAllByStyleCopIdx(idx);
        styleCopGuidelineRepository.deleteAllByStyleCopIdx(idx);
        hashTagsRepository.deleteById(styleCopDto.getHashTagsIdx());

        // example ?????? ????????? ??????
        for (StyleCopExampleDto exampleDto : styleCopDto.getStyleCopExampleDtoList()) {
            styleCopExampleCommentService.deleteAllByStyleCopExampleIdx(exampleDto.getIdx());
        }

        // guideline ?????? ????????? ??????
        for (StyleCopGuidelineDto guidelineDto : styleCopDto.getStyleCopGuidelineDtoList()) {
            styleCopGuidelineAttachedFileService.deleteAllAttachedFile(guidelineDto.getIdx());
            styleCopGuidelineLikeService.deleteAllByStyleCopGuidelineIdx(guidelineDto.getIdx());
            styleCopGuidelineCommentService.deleteAllByStyleCopGuidelineIdx(guidelineDto.getIdx());
        }
    }
}