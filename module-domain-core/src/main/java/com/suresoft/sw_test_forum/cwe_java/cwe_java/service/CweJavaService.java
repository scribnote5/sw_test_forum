package com.suresoft.sw_test_forum.cwe_java.cwe_java.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepository;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepositoryImpl;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.repository.CweJavaExampleRepository;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.service.CweJavaExampleCommentService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.service.CweJavaExampleService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository.CweJavaGuidelineRepository;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service.CweJavaGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service.CweJavaGuidelineCommentService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service.CweJavaGuidelineLikeService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service.CweJavaGuidelineService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.CweJava;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaSearchDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.mapper.CweJavaMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.repository.CweJavaCommentRepositoryImpl;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.repository.CweJavaRepository;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.repository.CweJavaRepositoryImpl;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.CweJavaExampleDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineDto;
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
     * ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public List<CweJavaDto> findAllByHighPriorityAsc() {
        List<CweJavaDto> cweJavaDtoList = cweJavaRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (CweJavaDto cweJavaDto : cweJavaDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaDto.getCreatedByIdx());

            cweJavaDto.setNewIcon(NewIconCheck.isNew(cweJavaDto.getCreatedDate()));
            cweJavaDto.setCreatedByUser(createdByUser);
            cweJavaDto.setCommentDtoCount(cweJavaCommentRepositoryImpl.countAllByCweJavaIdx(cweJavaDto.getIdx()));
        }

        return cweJavaDtoList;
    }

    /**
     * ??????????????? ?????? ????????? ??????
     *
     * @param pageable
     * @param cweJavaSearchDto
     * @return
     */
    public Page<CweJavaDto> findAll(Pageable pageable, CweJavaSearchDto cweJavaSearchDto) {
        Page<CweJavaDto> cweJavaDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        cweJavaDtoList = cweJavaRepositoryImpl.findAll(pageable, cweJavaSearchDto);

        // NewIcon ??????, createdBy, comment ?????? ??????
        for (CweJavaDto cweJavaDto : cweJavaDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweJavaDto.getCreatedByIdx());

            cweJavaDto.setNewIcon(NewIconCheck.isNew(cweJavaDto.getCreatedDate()));
            cweJavaDto.setCreatedByUser(createdByUser);
            cweJavaDto.setCommentDtoCount(cweJavaCommentRepositoryImpl.countAllByCweJavaIdx(cweJavaDto.getIdx()));
        }

        return cweJavaDtoList;
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<CweJavaDto> highPriorityList = cweJavaRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "??????????????? ???????????? ????????????.");

        for (CweJavaDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "??????????????? ???????????? ????????????.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public CweJavaDto findCweJavaAutoComplete(CweJavaDto cweJavaDto) {
        // title ??????
        for (String title : cweJavaRepositoryImpl.findDistinctTitle()) {
            cweJavaDto.getAutoCompleteTitle().add(title);
        }

        // hashTags ??????
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("cwe_java")) {
            for (String hashTag : hashTags.split("#")) {
                cweJavaDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        return cweJavaDto;
    }

    /**
     * ??????
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
     * ??????
     *
     * @param idx
     * @return
     */
    public CweJavaDto findCweJavaByIdx(long idx) {
        CweJavaDto cweJavaDto = new CweJavaDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            cweJavaDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
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
     * ??????????????????, ??????
     *
     * @return
     */
    public long countPosts() {
        return cweJavaRepository.count();
    }

    /**
     * CWE ?????? ?????? ??????
     *
     * @param idx
     * @return
     */
    public String findCweJavaRuleByIdx(long idx) {
        return cweJavaRepositoryImpl.findCweJavaByIdx(idx).getTitle();
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<CweJavaDto> highPriorityList = cweJavaRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        CweJava cweJavaPriority = cweJavaRepositoryImpl.findCweJavaPriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "??????????????? ???????????? ????????????.");

        for (CweJavaDto highPriority : highPriorityList) {
            if (cweJavaPriority.getPriority() == highPriority.getPriority()) {
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
     * @param cweJavaDto
     * @return
     */
    @Transactional
    public void updateCweJava(long idx, CweJavaDto cweJavaDto) {
        CweJava persistCweJava = cweJavaRepository.getById(idx);
        CweJava cweJava = CweJavaMapper.INSTANCE.toEntity(cweJavaDto);
        persistCweJava.update(cweJava);
        cweJavaRepository.save(persistCweJava);

        HashTags persistHashTags = hashTagsRepository.getById(cweJavaDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("cwe_java")
                .content(cweJavaDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);
    }

    /**
     * ??????
     *
     * @param idx
     */
    @Transactional
    public void deleteRelatedCweJavaByIdx(long idx) throws Exception {
        CweJavaDto cweJavaDto = cweJavaRepositoryImpl.findByIdx(idx);
        cweJavaDto = cweJavaExampleService.findCweJavaExampleListWhenDelete(idx, cweJavaDto);
        cweJavaDto = cweJavaGuidelineService.findCweJavaGuidelineListWhenDelete(idx, cweJavaDto);

        // ??????
        cweJavaRepository.deleteById(idx);
        cweJavaExampleRepository.deleteAllByCweJavaIdx(idx);
        cweJavaGuidelineRepository.deleteAllByCweJavaIdx(idx);
        hashTagsRepository.deleteById(cweJavaDto.getHashTagsIdx());

        // example ?????? ????????? ??????
        for (CweJavaExampleDto exampleDto : cweJavaDto.getCweJavaExampleDtoList()) {
            cweJavaExampleCommentService.deleteAllByCweJavaExampleIdx(exampleDto.getIdx());
        }

        // guideline ?????? ????????? ??????
        for (CweJavaGuidelineDto guidelineDto : cweJavaDto.getCweJavaGuidelineDtoList()) {
            cweJavaGuidelineAttachedFileService.deleteAllAttachedFile(guidelineDto.getIdx());
            cweJavaGuidelineLikeService.deleteAllByCweJavaGuidelineIdx(guidelineDto.getIdx());
            cweJavaGuidelineCommentService.deleteAllByCweJavaGuidelineIdx(guidelineDto.getIdx());
        }
    }
}