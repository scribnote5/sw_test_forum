package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepository;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepositoryImpl;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.JavaCodeConventions;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.JavaCodeConventionsDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.JavaCodeConventionsSearchDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.mapper.JavaCodeConventionsMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.repository.JavaCodeConventionsCommentRepositoryImpl;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.repository.JavaCodeConventionsRepository;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.repository.JavaCodeConventionsRepositoryImpl;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.JavaCodeConventionsExampleDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.repository.JavaCodeConventionsExampleRepository;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.service.JavaCodeConventionsExampleCommentService;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.service.JavaCodeConventionsExampleService;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.repository.JavaCodeConventionsGuidelineRepository;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.service.JavaCodeConventionsGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.service.JavaCodeConventionsGuidelineCommentService;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.service.JavaCodeConventionsGuidelineLikeService;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.service.JavaCodeConventionsGuidelineService;
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
public class JavaCodeConventionsService {
    private final JavaCodeConventionsRepository javaCodeConventionsRepository;
    private final JavaCodeConventionsRepositoryImpl javaCodeConventionsRepositoryImpl;
    private final JavaCodeConventionsCommentRepositoryImpl javaCodeConventionsCommentRepositoryImpl;
    private final JavaCodeConventionsExampleRepository javaCodeConventionsExampleRepository;
    private final JavaCodeConventionsGuidelineRepository javaCodeConventionsGuidelineRepository;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final JavaCodeConventionsExampleService javaCodeConventionsExampleService;
    private final JavaCodeConventionsExampleCommentService javaCodeConventionsExampleCommentService;
    private final JavaCodeConventionsGuidelineService javaCodeConventionsGuidelineService;
    private final JavaCodeConventionsGuidelineAttachedFileService javaCodeConventionsGuidelineAttachedFileService;
    private final JavaCodeConventionsGuidelineCommentService javaCodeConventionsGuidelineCommentService;
    private final JavaCodeConventionsGuidelineLikeService javaCodeConventionsGuidelineLikeService;
    private final UserService userService;
    @Value("${module.name}")
    private String moduleName;

    public JavaCodeConventionsService(JavaCodeConventionsRepository javaCodeConventionsRepository,
                                      JavaCodeConventionsRepositoryImpl javaCodeConventionsRepositoryImpl,
                                      JavaCodeConventionsCommentRepositoryImpl javaCodeConventionsCommentRepositoryImpl,
                                      JavaCodeConventionsExampleRepository javaCodeConventionsExampleRepository,
                                      JavaCodeConventionsGuidelineRepository javaCodeConventionsGuidelineRepository,
                                      HashTagsRepository hashTagsRepository,
                                      HashTagsRepositoryImpl hashTagsRepositoryImpl,
                                      @Lazy JavaCodeConventionsExampleService javaCodeConventionsExampleService,
                                      JavaCodeConventionsExampleCommentService javaCodeConventionsExampleCommentService,
                                      @Lazy JavaCodeConventionsGuidelineService javaCodeConventionsGuidelineService,
                                      JavaCodeConventionsGuidelineAttachedFileService javaCodeConventionsGuidelineAttachedFileService,
                                      JavaCodeConventionsGuidelineCommentService javaCodeConventionsGuidelineCommentService,
                                      JavaCodeConventionsGuidelineLikeService javaCodeConventionsGuidelineLikeService,
                                      UserService userService) {
        this.javaCodeConventionsRepository = javaCodeConventionsRepository;
        this.javaCodeConventionsRepositoryImpl = javaCodeConventionsRepositoryImpl;
        this.javaCodeConventionsCommentRepositoryImpl = javaCodeConventionsCommentRepositoryImpl;
        this.javaCodeConventionsExampleRepository = javaCodeConventionsExampleRepository;
        this.javaCodeConventionsGuidelineRepository = javaCodeConventionsGuidelineRepository;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.javaCodeConventionsExampleService = javaCodeConventionsExampleService;
        this.javaCodeConventionsExampleCommentService = javaCodeConventionsExampleCommentService;
        this.javaCodeConventionsGuidelineService = javaCodeConventionsGuidelineService;
        this.javaCodeConventionsGuidelineAttachedFileService = javaCodeConventionsGuidelineAttachedFileService;
        this.javaCodeConventionsGuidelineCommentService = javaCodeConventionsGuidelineCommentService;
        this.javaCodeConventionsGuidelineLikeService = javaCodeConventionsGuidelineLikeService;
        this.userService = userService;
    }

    /**
     * ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public List<JavaCodeConventionsDto> findAllByHighPriorityAsc() {
        List<JavaCodeConventionsDto> javaCodeConventionsDtoList = javaCodeConventionsRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (JavaCodeConventionsDto javaCodeConventionsDto : javaCodeConventionsDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(javaCodeConventionsDto.getCreatedByIdx());

            javaCodeConventionsDto.setNewIcon(NewIconCheck.isNew(javaCodeConventionsDto.getCreatedDate()));
            javaCodeConventionsDto.setCreatedByUser(createdByUser);
            javaCodeConventionsDto.setCommentDtoCount(javaCodeConventionsCommentRepositoryImpl.countAllByJavaCodeConventionsIdx(javaCodeConventionsDto.getIdx()));
        }

        return javaCodeConventionsDtoList;
    }

    /**
     * ??????????????? ?????? ????????? ??????
     *
     * @param pageable
     * @param javaCodeConventionsSearchDto
     * @return
     */
    public Page<JavaCodeConventionsDto> findAll(Pageable pageable, JavaCodeConventionsSearchDto javaCodeConventionsSearchDto) {
        Page<JavaCodeConventionsDto> javaCodeConventionsDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        javaCodeConventionsDtoList = javaCodeConventionsRepositoryImpl.findAll(pageable, javaCodeConventionsSearchDto);

        // NewIcon ??????, createdBy, comment ?????? ??????
        for (JavaCodeConventionsDto javaCodeConventionsDto : javaCodeConventionsDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(javaCodeConventionsDto.getCreatedByIdx());

            javaCodeConventionsDto.setNewIcon(NewIconCheck.isNew(javaCodeConventionsDto.getCreatedDate()));
            javaCodeConventionsDto.setCreatedByUser(createdByUser);
            javaCodeConventionsDto.setCommentDtoCount(javaCodeConventionsCommentRepositoryImpl.countAllByJavaCodeConventionsIdx(javaCodeConventionsDto.getIdx()));
        }

        return javaCodeConventionsDtoList;
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<JavaCodeConventionsDto> highPriorityList = javaCodeConventionsRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "??????????????? ???????????? ????????????.");

        for (JavaCodeConventionsDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "??????????????? ???????????? ????????????.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public JavaCodeConventionsDto findJavaCodeConventionsAutoComplete(JavaCodeConventionsDto javaCodeConventionsDto) {
        // title ??????
        for (String title : javaCodeConventionsRepositoryImpl.findDistinctTitle()) {
            javaCodeConventionsDto.getAutoCompleteTitle().add(title);
        }

        // hashTags ??????
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("java_code_conventions")) {
            for (String hashTag : hashTags.split("#")) {
                javaCodeConventionsDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        return javaCodeConventionsDto;
    }

    /**
     * ??????
     *
     * @param javaCodeConventionsDto
     * @return
     */
    public long insertJavaCodeConventions(JavaCodeConventionsDto javaCodeConventionsDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("java_code_conventions")
                .content(javaCodeConventionsDto.getHashTags())
                .build()).getIdx();

        javaCodeConventionsDto.setHashTagsIdx(hashTagsIdx);

        return javaCodeConventionsRepository.save(JavaCodeConventionsMapper.INSTANCE.toEntity(javaCodeConventionsDto)).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     * @return
     */
    public JavaCodeConventionsDto findJavaCodeConventionsByIdx(long idx) {
        JavaCodeConventionsDto javaCodeConventionsDto = new JavaCodeConventionsDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            javaCodeConventionsDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
        else {
            javaCodeConventionsDto = javaCodeConventionsRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(javaCodeConventionsDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(javaCodeConventionsDto.getLastModifiedByIdx());

            javaCodeConventionsDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            javaCodeConventionsDto.setCreatedByUser(createdByUser);
            javaCodeConventionsDto.setLastModifiedByUser(lastModifiedByUser);

            javaCodeConventionsRepositoryImpl.updateViewsByIdx(idx);
            javaCodeConventionsDto.setViews(javaCodeConventionsDto.getViews() + 1);
        }

        return javaCodeConventionsDto;
    }

    /**
     * ??????????????????, ??????
     *
     * @return
     */
    public long countPosts() {
        return javaCodeConventionsRepository.count();
    }

    /**
     * JAVA CODE CONVENTIONS ?????? ?????? ??????
     *
     * @param idx
     * @return
     */
    public String findJavaCodeConventionsRuleByIdx(long idx) {
        return javaCodeConventionsRepositoryImpl.findjavaCodeConventionsByIdx(idx).getTitle();
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<JavaCodeConventionsDto> highPriorityList = javaCodeConventionsRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        JavaCodeConventions javaCodeConventionsPriority = javaCodeConventionsRepositoryImpl.findjavaCodeConventionsPriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "??????????????? ???????????? ????????????.");

        for (JavaCodeConventionsDto highPriority : highPriorityList) {
            if (javaCodeConventionsPriority.getPriority() == highPriority.getPriority()) {
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
     * @param javaCodeConventionsDto
     * @return
     */
    @Transactional
    public void updateJavaCodeConventions(long idx, JavaCodeConventionsDto javaCodeConventionsDto) {
        JavaCodeConventions persistJavaCodeConventions = javaCodeConventionsRepository.getById(idx);
        JavaCodeConventions javaCodeConventions = JavaCodeConventionsMapper.INSTANCE.toEntity(javaCodeConventionsDto);
        persistJavaCodeConventions.update(javaCodeConventions);
        javaCodeConventionsRepository.save(persistJavaCodeConventions);

        HashTags persistHashTags = hashTagsRepository.getById(javaCodeConventionsDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("java_code_conventions")
                .content(javaCodeConventionsDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);
    }

    /**
     * ??????
     *
     * @param idx
     */
    @Transactional
    public void deleteRelatedJavaCodeConventionsByIdx(long idx) throws Exception {
        JavaCodeConventionsDto javaCodeConventionsDto = javaCodeConventionsRepositoryImpl.findByIdx(idx);
        javaCodeConventionsDto = javaCodeConventionsExampleService.findJavaCodeConventionsExampleListWhenDelete(idx, javaCodeConventionsDto);
        javaCodeConventionsDto = javaCodeConventionsGuidelineService.findJavaCodeConventionsGuidelineListWhenDelete(idx, javaCodeConventionsDto);

        // ??????
        javaCodeConventionsRepository.deleteById(idx);
        javaCodeConventionsExampleRepository.deleteAllByJavaCodeConventionsIdx(idx);
        javaCodeConventionsGuidelineRepository.deleteAllByJavaCodeConventionsIdx(idx);
        hashTagsRepository.deleteById(javaCodeConventionsDto.getHashTagsIdx());

        // example ?????? ????????? ??????
        for (JavaCodeConventionsExampleDto exampleDto : javaCodeConventionsDto.getJavaCodeConventionsExampleDtoList()) {
            javaCodeConventionsExampleCommentService.deleteAllByJavaCodeConventionsExampleIdx(exampleDto.getIdx());
        }

        // guideline ?????? ????????? ??????
        for (JavaCodeConventionsGuidelineDto guidelineDto : javaCodeConventionsDto.getJavaCodeConventionsGuidelineDtoList()) {
            javaCodeConventionsGuidelineAttachedFileService.deleteAllAttachedFile(guidelineDto.getIdx());
            javaCodeConventionsGuidelineLikeService.deleteAllByJavaCodeConventionsGuidelineIdx(guidelineDto.getIdx());
            javaCodeConventionsGuidelineCommentService.deleteAllByJavaCodeConventionsGuidelineIdx(guidelineDto.getIdx());
        }
    }
}