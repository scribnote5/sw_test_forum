package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.JavaCodeConventionsGuidelineComment;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineCommentDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.mapper.JavaCodeConventionsGuidelineCommentMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.mapper.JavaCodeConventionsGuidelineMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.repository.JavaCodeConventionsGuidelineCommentRepository;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.repository.JavaCodeConventionsGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class JavaCodeConventionsGuidelineCommentService {
    private final JavaCodeConventionsGuidelineCommentRepository javaCodeConventionsGuidelineCommentRepository;
    private final JavaCodeConventionsGuidelineCommentRepositoryImpl javaCodeConventionsGuidelineCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public JavaCodeConventionsGuidelineCommentService(JavaCodeConventionsGuidelineCommentRepository javaCodeConventionsGuidelineCommentRepository,
                                                      JavaCodeConventionsGuidelineCommentRepositoryImpl javaCodeConventionsGuidelineCommentRepositoryImpl,
                                                      UserRepositoryImpl userRepositoryImpl) {
        this.javaCodeConventionsGuidelineCommentRepository = javaCodeConventionsGuidelineCommentRepository;
        this.javaCodeConventionsGuidelineCommentRepositoryImpl = javaCodeConventionsGuidelineCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param javaCodeConventionsGuidelineDto
     * @return
     */
    public JavaCodeConventionsGuidelineDto findAllByJavaCodeConventionsGuidelineIdxOrderByIdxDesc(JavaCodeConventionsGuidelineDto javaCodeConventionsGuidelineDto) {
        List<JavaCodeConventionsGuidelineCommentDto> javaCodeConventionsGuidelineCommentDtoList = JavaCodeConventionsGuidelineCommentMapper.INSTANCE.toDto(javaCodeConventionsGuidelineCommentRepository.findAllByJavaCodeConventionsGuidelineIdxOrderByIdxDesc(javaCodeConventionsGuidelineDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (JavaCodeConventionsGuidelineCommentDto javaCodeConventionsGuidelineCommentDto : javaCodeConventionsGuidelineCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(javaCodeConventionsGuidelineCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            javaCodeConventionsGuidelineCommentDto.setNewIcon(NewIconCheck.isNew(javaCodeConventionsGuidelineCommentDto.getCreatedDate()));
            javaCodeConventionsGuidelineCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            javaCodeConventionsGuidelineCommentDto.setCreatedByUser(createdByUser);
        }

        javaCodeConventionsGuidelineDto = JavaCodeConventionsGuidelineMapper.INSTANCE.toDtoByCommentList(javaCodeConventionsGuidelineDto, javaCodeConventionsGuidelineCommentDtoList);

        return javaCodeConventionsGuidelineDto;
    }

    /**
     * 등록
     *
     * @param JavaCodeConventionsGuidelineCommentDto
     * @return
     */
    public long insertJavaCodeConventionsGuidelineComment(JavaCodeConventionsGuidelineCommentDto JavaCodeConventionsGuidelineCommentDto) {
        return javaCodeConventionsGuidelineCommentRepository.save(JavaCodeConventionsGuidelineCommentMapper.INSTANCE.toEntity(JavaCodeConventionsGuidelineCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param JavaCodeConventionsGuidelineCommentDto
     * @return
     */
    @Transactional
    public long updateJavaCodeConventionsGuidelineComment(long idx, JavaCodeConventionsGuidelineCommentDto JavaCodeConventionsGuidelineCommentDto) {
        JavaCodeConventionsGuidelineComment persistJavaCodeConventionsGuidelineComment = javaCodeConventionsGuidelineCommentRepository.getReferenceById(idx);
        JavaCodeConventionsGuidelineComment JavaCodeConventionsGuidelineComment = JavaCodeConventionsGuidelineCommentMapper.INSTANCE.toEntity(JavaCodeConventionsGuidelineCommentDto);

        persistJavaCodeConventionsGuidelineComment.update(JavaCodeConventionsGuidelineComment);

        return javaCodeConventionsGuidelineCommentRepository.save(persistJavaCodeConventionsGuidelineComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteJavaCodeConventionsGuidelineCommentByIdx(long idx) {
        javaCodeConventionsGuidelineCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param idx
     */
    public void deleteAllByJavaCodeConventionsGuidelineIdx(long idx) {
        javaCodeConventionsGuidelineCommentRepositoryImpl.deleteAllByJavaCodeConventionsGuidelineIdx(idx);
    }
}