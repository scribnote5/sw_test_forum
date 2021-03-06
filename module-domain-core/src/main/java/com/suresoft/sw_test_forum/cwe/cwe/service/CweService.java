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
     * ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public List<CweDto> findAllByHighPriorityAsc() {
        List<CweDto> cweDtoList = cweRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (CweDto cweDto : cweDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweDto.getCreatedByIdx());

            cweDto.setNewIcon(NewIconCheck.isNew(cweDto.getCreatedDate()));
            cweDto.setCreatedByUser(createdByUser);
            cweDto.setCommentDtoCount(cweCommentRepositoryImpl.countAllByCweIdx(cweDto.getIdx()));
        }

        return cweDtoList;
    }

    /**
     * ??????????????? ?????? ????????? ??????
     *
     * @param pageable
     * @param cweSearchDto
     * @return
     */
    public Page<CweDto> findAll(Pageable pageable, CweSearchDto cweSearchDto) {
        Page<CweDto> cweDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        cweDtoList = cweRepositoryImpl.findAll(pageable, cweSearchDto);

        // NewIcon ??????, createdBy, comment ?????? ??????
        for (CweDto cweDto : cweDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(cweDto.getCreatedByIdx());

            cweDto.setNewIcon(NewIconCheck.isNew(cweDto.getCreatedDate()));
            cweDto.setCreatedByUser(createdByUser);
            cweDto.setCommentDtoCount(cweCommentRepositoryImpl.countAllByCweIdx(cweDto.getIdx()));
        }

        return cweDtoList;
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<CweDto> highPriorityList = cweRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "??????????????? ???????????? ????????????.");

        for (CweDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "??????????????? ???????????? ????????????.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public CweDto findCweAutoComplete(CweDto cweDto) {
        // title ??????
        for (String title : cweRepositoryImpl.findDistinctTitle()) {
            cweDto.getAutoCompleteTitle().add(title);
        }

        // hashTags ??????
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("cwe")) {
            for (String hashTag : hashTags.split("#")) {
                cweDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        return cweDto;
    }

    /**
     * ??????
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
     * ??????
     *
     * @param idx
     * @return
     */
    public CweDto findCweByIdx(long idx) {
        CweDto cweDto = new CweDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            cweDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
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
     * ??????????????????, ??????
     *
     * @return
     */
    public long countPosts() {
        return cweRepository.count();
    }

    /**
     * CWE ?????? ?????? ??????
     *
     * @param idx
     * @return
     */
    public String findCweRuleByIdx(long idx) {
        return cweRepositoryImpl.findCweByIdx(idx).getTitle();
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<CweDto> highPriorityList = cweRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        Cwe cwePriority = cweRepositoryImpl.findCwePriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "??????????????? ???????????? ????????????.");

        for (CweDto highPriority : highPriorityList) {
            if (cwePriority.getPriority() == highPriority.getPriority()) {
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
     * @param cweDto
     * @return
     */
    @Transactional
    public void updateCwe(long idx, CweDto cweDto) {
        Cwe persistCwe = cweRepository.getById(idx);
        Cwe cwe = CweMapper.INSTANCE.toEntity(cweDto);
        persistCwe.update(cwe);
        cweRepository.save(persistCwe);

        HashTags persistHashTags = hashTagsRepository.getById(cweDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("cwe")
                .content(cweDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);
    }

    /**
     * ??????
     *
     * @param idx
     */
    @Transactional
    public void deleteRelatedCweByIdx(long idx) throws Exception {
        CweDto cweDto = cweRepositoryImpl.findByIdx(idx);
        cweDto = cweExampleService.findCweExampleListWhenDelete(idx, cweDto);
        cweDto = cweGuidelineService.findCweGuidelineListWhenDelete(idx, cweDto);

        // ??????
        cweRepository.deleteById(idx);
        cweExampleRepository.deleteAllByCweIdx(idx);
        cweGuidelineRepository.deleteAllByCweIdx(idx);
        hashTagsRepository.deleteById(cweDto.getHashTagsIdx());

        // example ?????? ????????? ??????
        for (CweExampleDto exampleDto : cweDto.getCweExampleDtoList()) {
            cweExampleCommentService.deleteAllByCweExampleIdx(exampleDto.getIdx());
        }

        // guideline ?????? ????????? ??????
        for (CweGuidelineDto guidelineDto : cweDto.getCweGuidelineDtoList()) {
            cweGuidelineAttachedFileService.deleteAllAttachedFile(guidelineDto.getIdx());
            cweGuidelineLikeService.deleteAllByCweGuidelineIdx(guidelineDto.getIdx());
            cweGuidelineCommentService.deleteAllByCweGuidelineIdx(guidelineDto.getIdx());
        }
    }
}