package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service;

import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuidelineLike;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineLikeDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository.CweJavaGuidelineLikeRepository;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository.CweJavaGuidelineLikeRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.stereotype.Service;

@Service
public class CweJavaGuidelineLikeService {
    private final CweJavaGuidelineLikeRepository cweJavaGuidelineLikeRepository;
    private final CweJavaGuidelineLikeRepositoryImpl cweJavaGuidelineLikeRepositoryImpl;

    public CweJavaGuidelineLikeService(CweJavaGuidelineLikeRepository cweJavaGuidelineLikeRepository,
                                       CweJavaGuidelineLikeRepositoryImpl cweJavaGuidelineLikeRepositoryImpl) {
        this.cweJavaGuidelineLikeRepository = cweJavaGuidelineLikeRepository;
        this.cweJavaGuidelineLikeRepositoryImpl = cweJavaGuidelineLikeRepositoryImpl;
    }

    /**
     * 좋아요 조회
     *
     * @param cweJavaGuidelineDto
     * @return
     */
    public CweJavaGuidelineDto findCweJavaGuidelineLike(CweJavaGuidelineDto cweJavaGuidelineDto) {
        CweJavaGuidelineLikeDto cweJavaGuidelineLikeDto = cweJavaGuidelineLikeRepositoryImpl.findByUsernameAndCweJavaGuidelineIdx(AuthorityUtil.getCurrentUsername(), cweJavaGuidelineDto.getIdx());
        boolean isEmptyCweJavaGuidelineLikeDto = EmptyUtil.isEmpty(cweJavaGuidelineLikeDto);

        // cweJavaGuidelineLikeDto가 empty 인 경우, 객체 생성
        if (isEmptyCweJavaGuidelineLikeDto) {
            cweJavaGuidelineLikeDto = new CweJavaGuidelineLikeDto();
        }

        cweJavaGuidelineLikeDto.setCweJavaGuidelineIdx(cweJavaGuidelineDto.getIdx());
        cweJavaGuidelineLikeDto.setLike(!isEmptyCweJavaGuidelineLikeDto);
        // 좋아요 갯수 조회
        cweJavaGuidelineLikeDto.setLikeCount(cweJavaGuidelineLikeRepository.countByCweJavaGuidelineIdx(cweJavaGuidelineDto.getIdx()));
        cweJavaGuidelineDto.setLikeDto(cweJavaGuidelineLikeDto);

        return cweJavaGuidelineDto;
    }

    /**
     * 등록
     *
     * @param cweJavaGuidelineIdx
     * @return
     */
    public long insertCweJavaGuidelineLike(long cweJavaGuidelineIdx) {
        return cweJavaGuidelineLikeRepository.save(CweJavaGuidelineLike.builder()
                .activeStatus(ActiveStatus.ACTIVE)
                .cweJavaGuidelineIdx(cweJavaGuidelineIdx)
                .build()).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteCweJavaGuidelineLikeByIdx(long idx) {
        cweJavaGuidelineLikeRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param idx
     */
    public void deleteAllByCweJavaGuidelineIdx(long idx) {
        cweJavaGuidelineLikeRepositoryImpl.deleteAllByCweJavaGuidelineIdx(idx);
    }
}