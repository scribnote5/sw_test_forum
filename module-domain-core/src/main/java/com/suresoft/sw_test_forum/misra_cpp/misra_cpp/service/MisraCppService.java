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
     * ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public List<MisraCppDto> findAllByHighPriorityAsc() {
        List<MisraCppDto> misraCppDtoList = misraCppRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon ??????, createdBy ??????, comment ?????? ??????
        for (MisraCppDto misraCppDto : misraCppDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCppDto.getCreatedByIdx());

            misraCppDto.setNewIcon(NewIconCheck.isNew(misraCppDto.getCreatedDate()));
            misraCppDto.setCreatedByUser(createdByUser);
            misraCppDto.setCommentDtoCount(misraCppCommentRepositoryImpl.countAllByMisraCppIdx(misraCppDto.getIdx()));
        }

        return misraCppDtoList;
    }

    /**
     * ??????????????? ?????? ????????? ??????
     *
     * @param pageable
     * @param misraCppSearchDto
     * @return
     */
    public Page<MisraCppDto> findAll(Pageable pageable, MisraCppSearchDto misraCppSearchDto) {
        Page<MisraCppDto> misraCppDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        misraCppDtoList = misraCppRepositoryImpl.findAll(pageable, misraCppSearchDto);

        // NewIcon ??????, createdBy, comment ?????? ??????
        for (MisraCppDto misraCppDto : misraCppDtoList) {
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(misraCppDto.getCreatedByIdx());

            misraCppDto.setNewIcon(NewIconCheck.isNew(misraCppDto.getCreatedDate()));
            misraCppDto.setCreatedByUser(createdByUser);
            misraCppDto.setCommentDtoCount(misraCppCommentRepositoryImpl.countAllByMisraCppIdx(misraCppDto.getIdx()));
        }

        return misraCppDtoList;
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<MisraCppDto> highPriorityList = misraCppRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "??????????????? ???????????? ????????????.");

        for (MisraCppDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "??????????????? ???????????? ????????????.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete ??????
     *
     * @return
     */
    public MisraCppDto findMisraCppAutoComplete(MisraCppDto misraCppDto) {
        // title ??????
        for (String title : misraCppRepositoryImpl.findDistinctTitle()) {
            misraCppDto.getAutoCompleteTitle().add(title);
        }

        // hashTags ??????
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("misra_cpp")) {
            for (String hashTag : hashTags.split("#")) {
                misraCppDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        return misraCppDto;
    }

    /**
     * ??????
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
     * ??????
     *
     * @param idx
     * @return
     */
    public MisraCppDto findMisraCppByIdx(long idx) {
        MisraCppDto misraCppDto = new MisraCppDto();

        // ?????? ??????
        // Register: ???????????? ????????? ????????? ?????????
        if (idx == 0) {
            misraCppDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
        // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
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
     * ??????????????????, ??????
     *
     * @return
     */
    public long countPosts() {
        return misraCppRepository.count();
    }

    /**
     * MISRA CPP ?????? ?????? ??????
     *
     * @param idx
     * @return
     */
    public String findMisraCppRuleByIdx(long idx) {
        return misraCppRepositoryImpl.findMisraCppByIdx(idx).getTitle();
    }

    /**
     * ????????? ???, ??????????????? ?????? ????????? ??????
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<MisraCppDto> highPriorityList = misraCppRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        MisraCpp misraCppPriority = misraCppRepositoryImpl.findMisraCppPriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "??????????????? ???????????? ????????????.");

        for (MisraCppDto highPriority : highPriorityList) {
            if (misraCppPriority.getPriority() == highPriority.getPriority()) {
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
     * @param misraCppDto
     * @return
     */
    @Transactional
    public void updateMisraCpp(long idx, MisraCppDto misraCppDto) {
        MisraCpp persistMisraCpp = misraCppRepository.getById(idx);
        MisraCpp misraCpp = MisraCppMapper.INSTANCE.toEntity(misraCppDto);
        persistMisraCpp.update(misraCpp);
        misraCppRepository.save(persistMisraCpp);

        HashTags persistHashTags = hashTagsRepository.getById(misraCppDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("misra_cpp")
                .content(misraCppDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);
    }

    /**
     * ??????
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
     * ??????
     *
     * @param idx
     */
    @Transactional
    public void deleteRelatedMisraCppByIdx(long idx) throws Exception {
        MisraCppDto misraCppDto = misraCppRepositoryImpl.findByIdx(idx);
        misraCppDto = misraCppExampleService.findMisraCppExampleListWhenDelete(idx, misraCppDto);
        misraCppDto = misraCppGuidelineService.findMisraCppGuidelineListWhenDelete(idx, misraCppDto);

        // ??????
        misraCppRepository.deleteById(idx);
        misraCppExampleRepository.deleteAllByMisraCppIdx(idx);
        misraCppGuidelineRepository.deleteAllByMisraCppIdx(idx);
        hashTagsRepository.deleteById(misraCppDto.getHashTagsIdx());

        // example ?????? ????????? ??????
        for (MisraCppExampleDto exampleDto : misraCppDto.getMisraCppExampleDtoList()) {
            misraCppExampleCommentService.deleteAllByMisraCppExampleIdx(exampleDto.getIdx());
        }

        // guideline ?????? ????????? ??????
        for (MisraCppGuidelineDto guidelineDto : misraCppDto.getMisraCppGuidelineDtoList()) {
            misraCppGuidelineAttachedFileService.deleteAllAttachedFile(guidelineDto.getIdx());
            misraCppGuidelineLikeService.deleteAllByMisraCppGuidelineIdx(guidelineDto.getIdx());
            misraCppGuidelineCommentService.deleteAllByMisraCppGuidelineIdx(guidelineDto.getIdx());
        }
    }
}