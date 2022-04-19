package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.service;

import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuidelineLike;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineLikeDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository.StyleCopGuidelineLikeRepository;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository.StyleCopGuidelineLikeRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.stereotype.Service;

@Service
public class StyleCopGuidelineLikeService {
    private final StyleCopGuidelineLikeRepository styleCopGuidelineLikeRepository;
    private final StyleCopGuidelineLikeRepositoryImpl styleCopGuidelineLikeRepositoryImpl;

    public StyleCopGuidelineLikeService(StyleCopGuidelineLikeRepository styleCopGuidelineLikeRepository,
                                        StyleCopGuidelineLikeRepositoryImpl styleCopGuidelineLikeRepositoryImpl) {
        this.styleCopGuidelineLikeRepository = styleCopGuidelineLikeRepository;
        this.styleCopGuidelineLikeRepositoryImpl = styleCopGuidelineLikeRepositoryImpl;
    }

    /**
     * 좋아요 조회
     *
     * @param styleCopGuidelineDto
     * @return
     */
    public StyleCopGuidelineDto findStyleCopGuidelineLike(StyleCopGuidelineDto styleCopGuidelineDto) {
        StyleCopGuidelineLikeDto styleCopGuidelineLikeDto = styleCopGuidelineLikeRepositoryImpl.findByUsernameAndStyleCopGuidelineIdx(AuthorityUtil.getCurrentUsername(), styleCopGuidelineDto.getIdx());
        boolean isEmptyStyleCopGuidelineLikeDto = EmptyUtil.isEmpty(styleCopGuidelineLikeDto);

        // styleCopGuidelineLikeDto가 empty 인 경우, 객체 생성
        if (isEmptyStyleCopGuidelineLikeDto) {
            styleCopGuidelineLikeDto = new StyleCopGuidelineLikeDto();
        }

        styleCopGuidelineLikeDto.setStyleCopGuidelineIdx(styleCopGuidelineDto.getIdx());
        styleCopGuidelineLikeDto.setLike(!isEmptyStyleCopGuidelineLikeDto);
        // 좋아요 갯수 조회
        styleCopGuidelineLikeDto.setLikeCount(styleCopGuidelineLikeRepository.countByStyleCopGuidelineIdx(styleCopGuidelineDto.getIdx()));
        styleCopGuidelineDto.setLikeDto(styleCopGuidelineLikeDto);

        return styleCopGuidelineDto;
    }

    /**
     * 등록
     *
     * @param styleCopGuidelineIdx
     * @return
     */
    public long insertStyleCopGuidelineLike(long styleCopGuidelineIdx) {
        return styleCopGuidelineLikeRepository.save(StyleCopGuidelineLike.builder()
                .activeStatus(ActiveStatus.ACTIVE)
                .styleCopGuidelineIdx(styleCopGuidelineIdx)
                .build()).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteStyleCopGuidelineLikeByIdx(long idx) {
        styleCopGuidelineLikeRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param idx
     */
    public void deleteAllByStyleCopGuidelineIdx(long idx) {
        styleCopGuidelineLikeRepositoryImpl.deleteAllByStyleCopGuidelineIdx(idx);
    }
}