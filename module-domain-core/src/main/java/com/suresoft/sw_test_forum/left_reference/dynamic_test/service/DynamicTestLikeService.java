package com.suresoft.sw_test_forum.left_reference.dynamic_test.service;

import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTestLike;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.DynamicTestDto;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.DynamicTestLikeDto;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.repository.DynamicTestLikeRepository;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.repository.DynamicTestLikeRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.stereotype.Service;

@Service
public class DynamicTestLikeService {
    private final DynamicTestLikeRepository dynamicTestLikeRepository;
    private final DynamicTestLikeRepositoryImpl dynamicTestLikeRepositoryImpl;

    public DynamicTestLikeService(DynamicTestLikeRepository dynamicTestLikeRepository,
                                  DynamicTestLikeRepositoryImpl dynamicTestLikeRepositoryImpl) {
        this.dynamicTestLikeRepository = dynamicTestLikeRepository;
        this.dynamicTestLikeRepositoryImpl = dynamicTestLikeRepositoryImpl;
    }

    /**
     * 좋아요 조회
     *
     * @param dynamicTestDto
     * @return
     */
    public DynamicTestDto findDynamicTestLike(DynamicTestDto dynamicTestDto) {
        DynamicTestLikeDto dynamicTestLikeDto = dynamicTestLikeRepositoryImpl.findByUsernameAndDynamicTestIdx(AuthorityUtil.getCurrentUsername(), dynamicTestDto.getIdx());
        boolean isEmptyDynamicTestLikeDto = EmptyUtil.isEmpty(dynamicTestLikeDto);

        // dynamicTestLikeDto가 empty 인 경우, 객체 생성
        if (isEmptyDynamicTestLikeDto) {
            dynamicTestLikeDto = new DynamicTestLikeDto();
        }

        dynamicTestLikeDto.setDynamicTestIdx(dynamicTestDto.getIdx());
        dynamicTestLikeDto.setLike(!isEmptyDynamicTestLikeDto);
        // 좋아요 갯수 조회
        dynamicTestLikeDto.setLikeCount(dynamicTestLikeRepository.countByDynamicTestIdx(dynamicTestDto.getIdx()));
        dynamicTestDto.setLikeDto(dynamicTestLikeDto);

        return dynamicTestDto;
    }

    /**
     * 등록
     *
     * @param dynamicTestIdx
     * @return
     */
    public long insertDynamicTestLike(long dynamicTestIdx) {
        return dynamicTestLikeRepository.save(DynamicTestLike.builder()
                .activeStatus(ActiveStatus.ACTIVE)
                .dynamicTestIdx(dynamicTestIdx)
                .build()).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteDynamicTestLikeByIdx(long idx) {
        dynamicTestLikeRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param idx
     */
    public void deleteAllByDynamicTestIdx(long idx) {
        dynamicTestLikeRepositoryImpl.deleteAllByDynamicTestIdx(idx);
    }
}