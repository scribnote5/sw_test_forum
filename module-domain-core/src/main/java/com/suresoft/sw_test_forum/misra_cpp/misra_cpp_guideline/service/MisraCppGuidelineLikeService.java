package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.service;

import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.MisraCppGuidelineLike;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineLikeDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.repository.MisraCppGuidelineLikeRepository;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.repository.MisraCppGuidelineLikeRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.stereotype.Service;

@Service
public class MisraCppGuidelineLikeService {
    private final MisraCppGuidelineLikeRepository misraCppGuidelineLikeRepository;
    private final MisraCppGuidelineLikeRepositoryImpl misraCppGuidelineLikeRepositoryImpl;

    public MisraCppGuidelineLikeService(MisraCppGuidelineLikeRepository misraCppGuidelineLikeRepository,
                                        MisraCppGuidelineLikeRepositoryImpl misraCppGuidelineLikeRepositoryImpl) {
        this.misraCppGuidelineLikeRepository = misraCppGuidelineLikeRepository;
        this.misraCppGuidelineLikeRepositoryImpl = misraCppGuidelineLikeRepositoryImpl;
    }

    /**
     * 좋아요 조회
     *
     * @param misraCppGuidelineDto
     * @return
     */
    public MisraCppGuidelineDto findMisraCppGuidelineLike(MisraCppGuidelineDto misraCppGuidelineDto) {
        MisraCppGuidelineLikeDto misraCppGuidelineLikeDto = misraCppGuidelineLikeRepositoryImpl.findByUsernameAndMisraCppGuidelineIdx(AuthorityUtil.getCurrentUsername(), misraCppGuidelineDto.getIdx());
        boolean isEmptyMisraCppGuidelineLikeDto = EmptyUtil.isEmpty(misraCppGuidelineLikeDto);

        // misraCppGuidelineLikeDto가 empty 인 경우, 객체 생성
        if (isEmptyMisraCppGuidelineLikeDto) {
            misraCppGuidelineLikeDto = new MisraCppGuidelineLikeDto();
        }

        misraCppGuidelineLikeDto.setMisraCppGuidelineIdx(misraCppGuidelineDto.getIdx());
        misraCppGuidelineLikeDto.setLike(!isEmptyMisraCppGuidelineLikeDto);
        // 좋아요 갯수 조회
        misraCppGuidelineLikeDto.setLikeCount(misraCppGuidelineLikeRepository.countByMisraCppGuidelineIdx(misraCppGuidelineDto.getIdx()));
        misraCppGuidelineDto.setLikeDto(misraCppGuidelineLikeDto);

        return misraCppGuidelineDto;
    }

    /**
     * 등록
     *
     * @param misraCppGuidelineIdx
     * @return
     */
    public long insertMisraCppGuidelineLike(long misraCppGuidelineIdx) {
        return misraCppGuidelineLikeRepository.save(MisraCppGuidelineLike.builder()
                .activeStatus(ActiveStatus.ACTIVE)
                .misraCppGuidelineIdx(misraCppGuidelineIdx)
                .build()).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteMisraCppGuidelineLikeByIdx(long idx) {
        misraCppGuidelineLikeRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param idx
     */
    public void deleteAllByMisraCppGuidelineIdx(long idx) {
        misraCppGuidelineLikeRepositoryImpl.deleteAllByMisraCppGuidelineIdx(idx);
    }
}