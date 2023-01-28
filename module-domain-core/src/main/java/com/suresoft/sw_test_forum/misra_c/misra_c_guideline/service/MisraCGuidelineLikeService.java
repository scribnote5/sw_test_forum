package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.service;

import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.MisraCGuidelineLike;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineLikeDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.repository.MisraCGuidelineLikeRepository;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.repository.MisraCGuidelineLikeRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.stereotype.Service;

@Service
public class MisraCGuidelineLikeService {
    private final MisraCGuidelineLikeRepository misraCGuidelineLikeRepository;
    private final MisraCGuidelineLikeRepositoryImpl misraCGuidelineLikeRepositoryImpl;

    public MisraCGuidelineLikeService(MisraCGuidelineLikeRepository misraCGuidelineLikeRepository,
                                      MisraCGuidelineLikeRepositoryImpl misraCGuidelineLikeRepositoryImpl) {
        this.misraCGuidelineLikeRepository = misraCGuidelineLikeRepository;
        this.misraCGuidelineLikeRepositoryImpl = misraCGuidelineLikeRepositoryImpl;
    }

    /**
     * 좋아요 조회
     *
     * @param misraCGuidelineDto
     * @return
     */
    public MisraCGuidelineDto findMisraCGuidelineLike(MisraCGuidelineDto misraCGuidelineDto) {
        MisraCGuidelineLikeDto misraCGuidelineLikeDto = misraCGuidelineLikeRepositoryImpl.findByUsernameAndMisraCGuidelineIdx(AuthorityUtil.getCurrentUsername(), misraCGuidelineDto.getIdx());
        boolean isEmptyMisraCGuidelineLikeDto = EmptyUtil.isEmpty(misraCGuidelineLikeDto);

        // misraCGuidelineLikeDto가 empty 인 경우, 객체 생성
        if (isEmptyMisraCGuidelineLikeDto) {
            misraCGuidelineLikeDto = new MisraCGuidelineLikeDto();
        }

        misraCGuidelineLikeDto.setMisraCGuidelineIdx(misraCGuidelineDto.getIdx());
        misraCGuidelineLikeDto.setLike(!isEmptyMisraCGuidelineLikeDto);
        // 좋아요 갯수 조회
        misraCGuidelineLikeDto.setLikeCount(misraCGuidelineLikeRepository.countByMisraCGuidelineIdx(misraCGuidelineDto.getIdx()));
        misraCGuidelineDto.setLikeDto(misraCGuidelineLikeDto);

        return misraCGuidelineDto;
    }

    /**
     * 등록
     *
     * @param misraCGuidelineIdx
     * @return
     */
    public long insertMisraCGuidelineLike(long misraCGuidelineIdx) {
        return misraCGuidelineLikeRepository.save(MisraCGuidelineLike.builder()
                .activeStatus(ActiveStatus.ACTIVE)
                .misraCGuidelineIdx(misraCGuidelineIdx)
                .build()).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteMisraCGuidelineLikeByIdx(long idx) {
        misraCGuidelineLikeRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param idx
     */
    public void deleteAllByMisraCGuidelineIdx(long idx) {
        misraCGuidelineLikeRepositoryImpl.deleteAllByMisraCGuidelineIdx(idx);
    }
}