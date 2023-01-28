package com.suresoft.sw_test_forum.fx_cop.fx_cop.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.domain.HashTags;
import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepository;
import com.suresoft.sw_test_forum.common.repository.HashTagsRepositoryImpl;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.FxCop;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.FxCopDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.FxCopSearchDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.mapper.FxCopMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.repository.FxCopCommentRepositoryImpl;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.repository.FxCopRepository;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.repository.FxCopRepositoryImpl;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.FxCopExampleDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.repository.FxCopExampleRepository;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.service.FxCopExampleCommentService;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.service.FxCopExampleService;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.repository.FxCopGuidelineRepository;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.service.FxCopGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.service.FxCopGuidelineCommentService;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.service.FxCopGuidelineLikeService;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.service.FxCopGuidelineService;
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
public class FxCopService {
    private final FxCopRepository fxCopRepository;
    private final FxCopRepositoryImpl fxCopRepositoryImpl;
    private final FxCopCommentRepositoryImpl fxCopCommentRepositoryImpl;
    private final FxCopExampleRepository fxCopExampleRepository;
    private final FxCopGuidelineRepository fxCopGuidelineRepository;
    private final HashTagsRepository hashTagsRepository;
    private final HashTagsRepositoryImpl hashTagsRepositoryImpl;
    private final FxCopExampleService fxCopExampleService;
    private final FxCopExampleCommentService fxCopExampleCommentService;
    private final FxCopGuidelineService fxCopGuidelineService;
    private final FxCopGuidelineAttachedFileService fxCopGuidelineAttachedFileService;
    private final FxCopGuidelineCommentService fxCopGuidelineCommentService;
    private final FxCopGuidelineLikeService fxCopGuidelineLikeService;
    private final UserService userService;
    @Value("${module.name}")
    private String moduleName;

    public FxCopService(FxCopRepository fxCopRepository,
                        FxCopRepositoryImpl fxCopRepositoryImpl,
                        FxCopCommentRepositoryImpl fxCopCommentRepositoryImpl,
                        FxCopExampleRepository fxCopExampleRepository,
                        FxCopGuidelineRepository fxCopGuidelineRepository,
                        HashTagsRepository hashTagsRepository,
                        HashTagsRepositoryImpl hashTagsRepositoryImpl,
                        @Lazy FxCopExampleService fxCopExampleService,
                        FxCopExampleCommentService fxCopExampleCommentService,
                        @Lazy FxCopGuidelineService fxCopGuidelineService,
                        FxCopGuidelineAttachedFileService fxCopGuidelineAttachedFileService,
                        FxCopGuidelineCommentService fxCopGuidelineCommentService,
                        FxCopGuidelineLikeService fxCopGuidelineLikeService,
                        UserService userService) {
        this.fxCopRepository = fxCopRepository;
        this.fxCopRepositoryImpl = fxCopRepositoryImpl;
        this.fxCopCommentRepositoryImpl = fxCopCommentRepositoryImpl;
        this.fxCopExampleRepository = fxCopExampleRepository;
        this.fxCopGuidelineRepository = fxCopGuidelineRepository;
        this.hashTagsRepository = hashTagsRepository;
        this.hashTagsRepositoryImpl = hashTagsRepositoryImpl;
        this.fxCopExampleService = fxCopExampleService;
        this.fxCopExampleCommentService = fxCopExampleCommentService;
        this.fxCopGuidelineService = fxCopGuidelineService;
        this.fxCopGuidelineAttachedFileService = fxCopGuidelineAttachedFileService;
        this.fxCopGuidelineCommentService = fxCopGuidelineCommentService;
        this.fxCopGuidelineLikeService = fxCopGuidelineLikeService;
        this.userService = userService;
    }

    /**
     * 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public List<FxCopDto> findAllByHighPriorityAsc() {
        List<FxCopDto> fxCopDtoList = fxCopRepositoryImpl.findAllByHighPriorityAsc();

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (FxCopDto fxCopDto : fxCopDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(fxCopDto.getCreatedByIdx());

            fxCopDto.setNewIcon(NewIconCheck.isNew(fxCopDto.getCreatedDate()));
            fxCopDto.setCreatedByUser(createdByUser);
            fxCopDto.setCommentDtoCount(fxCopCommentRepositoryImpl.countAllByFxCopIdx(fxCopDto.getIdx()));
        }

        return fxCopDtoList;
    }

    /**
     * 우선순위가 낮은 리스트 조회
     *
     * @param pageable
     * @param fxCopSearchDto
     * @return
     */
    public Page<FxCopDto> findAll(Pageable pageable, FxCopSearchDto fxCopSearchDto) {
        Page<FxCopDto> fxCopDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        fxCopDtoList = fxCopRepositoryImpl.findAll(pageable, fxCopSearchDto);

        // NewIcon 판별, createdBy, comment 갯수 설정
        for (FxCopDto fxCopDto : fxCopDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(fxCopDto.getCreatedByIdx());

            fxCopDto.setNewIcon(NewIconCheck.isNew(fxCopDto.getCreatedDate()));
            fxCopDto.setCreatedByUser(createdByUser);
            fxCopDto.setCommentDtoCount(fxCopCommentRepositoryImpl.countAllByFxCopIdx(fxCopDto.getIdx()));
        }

        return fxCopDtoList;
    }

    /**
     * 작성할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenWrite() {
        List<FxCopDto> highPriorityList = fxCopRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (FxCopDto highPriority : highPriorityList) {
            priorityDtoArray[(int) highPriority.getPriority() - 1] = new PriorityDto(true, "우선순위가 설정되어 있습니다.");
        }

        return priorityDtoArray;
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public FxCopDto findFxCopAutoComplete(FxCopDto fxCopDto) {
        // title 설정
        for (String title : fxCopRepositoryImpl.findDistinctTitle()) {
            fxCopDto.getAutoCompleteTitle().add(title);
        }

        // hashTags 설정
        for (String hashTags : hashTagsRepositoryImpl.findDistinctHashTagsByTableName("fx_cop")) {
            for (String hashTag : hashTags.split("#")) {
                fxCopDto.getAutoCompleteHashTags().add("#" + hashTag);
            }
        }

        return fxCopDto;
    }

    /**
     * 등록
     *
     * @param fxCopDto
     * @return
     */
    public long insertFxCop(FxCopDto fxCopDto) {
        long hashTagsIdx = hashTagsRepository.save(HashTags.builder()
                .tableName("fx_cop")
                .content(fxCopDto.getHashTags())
                .build()).getIdx();

        fxCopDto.setHashTagsIdx(hashTagsIdx);

        return fxCopRepository.save(FxCopMapper.INSTANCE.toEntity(fxCopDto)).getIdx();
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public FxCopDto findFxCopByIdx(long idx) {
        FxCopDto fxCopDto = new FxCopDto();

        // 권한 설정
        // Register: 로그인한 사용자 권한을 검사함
        if (idx == 0) {
            fxCopDto.setAccess(AuthorityUtil.isAccessInRegister());
        }
        // isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            fxCopDto = fxCopRepositoryImpl.findByIdx(idx);

            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(fxCopDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(fxCopDto.getLastModifiedByIdx());

            fxCopDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            fxCopDto.setCreatedByUser(createdByUser);
            fxCopDto.setLastModifiedByUser(lastModifiedByUser);

            fxCopRepositoryImpl.updateViewsByIdx(idx);
            fxCopDto.setViews(fxCopDto.getViews() + 1);
        }

        return fxCopDto;
    }

    /**
     * 대시보드에서, 조회
     *
     * @return
     */
    public long countPosts() {
        return fxCopRepository.count();
    }

    /**
     * FxCop 규칙 규칙명 조회
     *
     * @param idx
     * @return
     */
    public String findFxCopRuleByIdx(long idx) {
        return fxCopRepositoryImpl.findFxCopByIdx(idx).getTitle();
    }

    /**
     * 수정할 때, 우선순위가 높은 리스트 조회
     *
     * @return
     */
    public PriorityDto[] findAllByHighPriorityAscWhenUpdate(long idx) {
        List<FxCopDto> highPriorityList = fxCopRepositoryImpl.findAllByHighPriorityAscCheckPriority();
        FxCop fxCopPriority = fxCopRepositoryImpl.findFxCopPriorityByIdx(idx);
        PriorityDto[] priorityDtoArray = new PriorityDto[6];
        priorityDtoArray[5] = new PriorityDto(false, "우선순위를 설정하지 않습니다.");

        for (FxCopDto highPriority : highPriorityList) {
            if (fxCopPriority.getPriority() == highPriority.getPriority()) {
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
     * @param fxCopDto
     * @return
     */
    @Transactional
    public void updateFxCop(long idx, FxCopDto fxCopDto) {
        FxCop persistFxCop = fxCopRepository.getReferenceById(idx);
        FxCop fxCop = FxCopMapper.INSTANCE.toEntity(fxCopDto);
        persistFxCop.update(fxCop);
        fxCopRepository.save(persistFxCop);

        HashTags persistHashTags = hashTagsRepository.getReferenceById(fxCopDto.getHashTagsIdx());
        persistHashTags.update(HashTags.builder()
                .tableName("fx_cop")
                .content(fxCopDto.getHashTags())
                .build());
        hashTagsRepository.save(persistHashTags);
    }

    /**
     * 삭제
     *
     * @param idx
     */
    @Transactional
    public void deleteRelatedFxCopByIdx(long idx) throws Exception {
        FxCopDto fxCopDto = fxCopRepositoryImpl.findByIdx(idx);
        fxCopDto = fxCopExampleService.findFxCopExampleListWhenDelete(idx, fxCopDto);
        fxCopDto = fxCopGuidelineService.findFxCopGuidelineListWhenDelete(idx, fxCopDto);

        // 삭제
        fxCopRepository.deleteById(idx);
        fxCopExampleRepository.deleteAllByFxCopIdx(idx);
        fxCopGuidelineRepository.deleteAllByFxCopIdx(idx);
        hashTagsRepository.deleteById(fxCopDto.getHashTagsIdx());

        // example 연관 데이터 삭제
        for (FxCopExampleDto exampleDto : fxCopDto.getFxCopExampleDtoList()) {
            fxCopExampleCommentService.deleteAllByFxCopExampleIdx(exampleDto.getIdx());
        }

        // guideline 연관 데이터 삭제
        for (FxCopGuidelineDto guidelineDto : fxCopDto.getFxCopGuidelineDtoList()) {
            fxCopGuidelineAttachedFileService.deleteAllAttachedFile(guidelineDto.getIdx());
            fxCopGuidelineLikeService.deleteAllByFxCopGuidelineIdx(guidelineDto.getIdx());
            fxCopGuidelineCommentService.deleteAllByFxCopGuidelineIdx(guidelineDto.getIdx());
        }
    }
}