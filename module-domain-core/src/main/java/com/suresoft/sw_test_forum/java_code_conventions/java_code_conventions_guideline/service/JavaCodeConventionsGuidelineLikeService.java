package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.service;

import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.JavaCodeConventionsGuidelineLike;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineLikeDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.repository.JavaCodeConventionsGuidelineLikeRepository;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.repository.JavaCodeConventionsGuidelineLikeRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import org.springframework.stereotype.Service;

@Service
public class JavaCodeConventionsGuidelineLikeService {
    private final JavaCodeConventionsGuidelineLikeRepository javaCodeConventionsGuidelineLikeRepository;
    private final JavaCodeConventionsGuidelineLikeRepositoryImpl javaCodeConventionsGuidelineLikeRepositoryImpl;

    public JavaCodeConventionsGuidelineLikeService(JavaCodeConventionsGuidelineLikeRepository javaCodeConventionsGuidelineLikeRepository,
                                                   JavaCodeConventionsGuidelineLikeRepositoryImpl javaCodeConventionsGuidelineLikeRepositoryImpl) {
        this.javaCodeConventionsGuidelineLikeRepository = javaCodeConventionsGuidelineLikeRepository;
        this.javaCodeConventionsGuidelineLikeRepositoryImpl = javaCodeConventionsGuidelineLikeRepositoryImpl;
    }

    /**
     * 좋아요 조회
     *
     * @param javaCodeConventionsGuidelineDto
     * @return
     */
    public JavaCodeConventionsGuidelineDto findJavaCodeConventionsGuidelineLike(JavaCodeConventionsGuidelineDto javaCodeConventionsGuidelineDto) {
        JavaCodeConventionsGuidelineLikeDto javaCodeConventionsGuidelineLikeDto = javaCodeConventionsGuidelineLikeRepositoryImpl.findByUsernameAndJavaCodeConventionsGuidelineIdx(AuthorityUtil.getCurrentUsername(), javaCodeConventionsGuidelineDto.getIdx());
        boolean isEmptyJavaCodeConventionsGuidelineLikeDto = EmptyUtil.isEmpty(javaCodeConventionsGuidelineLikeDto);

        // javaCodeConventionsGuidelineLikeDto가 empty 인 경우, 객체 생성
        if (isEmptyJavaCodeConventionsGuidelineLikeDto) {
            javaCodeConventionsGuidelineLikeDto = new JavaCodeConventionsGuidelineLikeDto();
        }

        javaCodeConventionsGuidelineLikeDto.setJavaCodeConventionsGuidelineIdx(javaCodeConventionsGuidelineDto.getIdx());
        javaCodeConventionsGuidelineLikeDto.setLike(!isEmptyJavaCodeConventionsGuidelineLikeDto);
        // 좋아요 갯수 조회
        javaCodeConventionsGuidelineLikeDto.setLikeCount(javaCodeConventionsGuidelineLikeRepository.countByJavaCodeConventionsGuidelineIdx(javaCodeConventionsGuidelineDto.getIdx()));
        javaCodeConventionsGuidelineDto.setLikeDto(javaCodeConventionsGuidelineLikeDto);

        return javaCodeConventionsGuidelineDto;
    }

    /**
     * 등록
     *
     * @param javaCodeConventionsGuidelineIdx
     * @return
     */
    public long insertJavaCodeConventionsGuidelineLike(long javaCodeConventionsGuidelineIdx) {
        return javaCodeConventionsGuidelineLikeRepository.save(JavaCodeConventionsGuidelineLike.builder()
                .activeStatus(ActiveStatus.ACTIVE)
                .javaCodeConventionsGuidelineIdx(javaCodeConventionsGuidelineIdx)
                .build()).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteJavaCodeConventionsGuidelineLikeByIdx(long idx) {
        javaCodeConventionsGuidelineLikeRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param idx
     */
    public void deleteAllByJavaCodeConventionsGuidelineIdx(long idx) {
        javaCodeConventionsGuidelineLikeRepositoryImpl.deleteAllByJavaCodeConventionsGuidelineIdx(idx);
    }
}