package com.suresoft.sw_test_forum.cwe.cwe_guideline.service;

import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuidelineLike;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineLikeDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.repository.CweGuidelineLikeRepository;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.repository.CweGuidelineLikeRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.stereotype.Service;

@Service
public class CweGuidelineLikeService {
    private final CweGuidelineLikeRepository cweGuidelineLikeRepository;
    private final CweGuidelineLikeRepositoryImpl cweGuidelineLikeRepositoryImpl;

    public CweGuidelineLikeService(CweGuidelineLikeRepository cweGuidelineLikeRepository,
                                   CweGuidelineLikeRepositoryImpl cweGuidelineLikeRepositoryImpl) {
        this.cweGuidelineLikeRepository = cweGuidelineLikeRepository;
        this.cweGuidelineLikeRepositoryImpl = cweGuidelineLikeRepositoryImpl;
    }

    /**
     * 좋아요 조회
     *
     * @param cweGuidelineDto
     * @return
     */
    public CweGuidelineDto findCweGuidelineLike(CweGuidelineDto cweGuidelineDto) {
        CweGuidelineLikeDto cweGuidelineLikeDto = cweGuidelineLikeRepositoryImpl.findByUsernameAndCweGuidelineIdx(AuthorityUtil.getCurrentUsername(), cweGuidelineDto.getIdx());
        boolean isEmptyCweGuidelineLikeDto = EmptyUtil.isEmpty(cweGuidelineLikeDto);

        // cweGuidelineLikeDto가 empty 인 경우, 객체 생성
        if (isEmptyCweGuidelineLikeDto) {
            cweGuidelineLikeDto = new CweGuidelineLikeDto();
        }

        cweGuidelineLikeDto.setCweGuidelineIdx(cweGuidelineDto.getIdx());
        cweGuidelineLikeDto.setLike(!isEmptyCweGuidelineLikeDto);
        // 좋아요 갯수 조회
        cweGuidelineLikeDto.setLikeCount(cweGuidelineLikeRepository.countByCweGuidelineIdx(cweGuidelineDto.getIdx()));
        cweGuidelineDto.setLikeDto(cweGuidelineLikeDto);

        return cweGuidelineDto;
    }

    /**
     * 등록
     *
     * @param cweGuidelineIdx
     * @return
     */
    public long insertCweGuidelineLike(long cweGuidelineIdx) {
        return cweGuidelineLikeRepository.save(CweGuidelineLike.builder()
                .activeStatus(ActiveStatus.ACTIVE)
                .cweGuidelineIdx(cweGuidelineIdx)
                .build()).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteCweGuidelineLikeByIdx(long idx) {
        cweGuidelineLikeRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param idx
     */
    public void deleteAllByCweGuidelineIdx(long idx) {
        cweGuidelineLikeRepositoryImpl.deleteAllByCweGuidelineIdx(idx);
    }
}