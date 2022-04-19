package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.service;

import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.FxCopGuidelineLike;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineLikeDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.repository.FxCopGuidelineLikeRepository;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.repository.FxCopGuidelineLikeRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.stereotype.Service;

@Service
public class FxCopGuidelineLikeService {
    private final FxCopGuidelineLikeRepository fxCopGuidelineLikeRepository;
    private final FxCopGuidelineLikeRepositoryImpl fxCopGuidelineLikeRepositoryImpl;

    public FxCopGuidelineLikeService(FxCopGuidelineLikeRepository fxCopGuidelineLikeRepository,
                                        FxCopGuidelineLikeRepositoryImpl fxCopGuidelineLikeRepositoryImpl) {
        this.fxCopGuidelineLikeRepository = fxCopGuidelineLikeRepository;
        this.fxCopGuidelineLikeRepositoryImpl = fxCopGuidelineLikeRepositoryImpl;
    }

    /**
     * 좋아요 조회
     *
     * @param fxCopGuidelineDto
     * @return
     */
    public FxCopGuidelineDto findFxCopGuidelineLike(FxCopGuidelineDto fxCopGuidelineDto) {
        FxCopGuidelineLikeDto fxCopGuidelineLikeDto = fxCopGuidelineLikeRepositoryImpl.findByUsernameAndFxCopGuidelineIdx(AuthorityUtil.getCurrentUsername(), fxCopGuidelineDto.getIdx());
        boolean isEmptyFxCopGuidelineLikeDto = EmptyUtil.isEmpty(fxCopGuidelineLikeDto);

        // fxCopGuidelineLikeDto가 empty 인 경우, 객체 생성
        if (isEmptyFxCopGuidelineLikeDto) {
            fxCopGuidelineLikeDto = new FxCopGuidelineLikeDto();
        }

        fxCopGuidelineLikeDto.setFxCopGuidelineIdx(fxCopGuidelineDto.getIdx());
        fxCopGuidelineLikeDto.setLike(!isEmptyFxCopGuidelineLikeDto);
        // 좋아요 갯수 조회
        fxCopGuidelineLikeDto.setLikeCount(fxCopGuidelineLikeRepository.countByFxCopGuidelineIdx(fxCopGuidelineDto.getIdx()));
        fxCopGuidelineDto.setLikeDto(fxCopGuidelineLikeDto);

        return fxCopGuidelineDto;
    }

    /**
     * 등록
     *
     * @param fxCopGuidelineIdx
     * @return
     */
    public long insertFxCopGuidelineLike(long fxCopGuidelineIdx) {
        return fxCopGuidelineLikeRepository.save(FxCopGuidelineLike.builder()
                .activeStatus(ActiveStatus.ACTIVE)
                .fxCopGuidelineIdx(fxCopGuidelineIdx)
                .build()).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteFxCopGuidelineLikeByIdx(long idx) {
        fxCopGuidelineLikeRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param idx
     */
    public void deleteAllByFxCopGuidelineIdx(long idx) {
        fxCopGuidelineLikeRepositoryImpl.deleteAllByFxCopGuidelineIdx(idx);
    }
}