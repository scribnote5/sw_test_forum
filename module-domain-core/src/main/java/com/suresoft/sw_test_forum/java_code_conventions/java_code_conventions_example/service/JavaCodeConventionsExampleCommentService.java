package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.domain.JavaCodeConventionsExampleComment;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.JavaCodeConventionsExampleCommentDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.JavaCodeConventionsExampleDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.mapper.JavaCodeConventionsExampleCommentMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.dto.mapper.JavaCodeConventionsExampleMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.repository.JavaCodeConventionsExampleCommentRepository;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.repository.JavaCodeConventionsExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class JavaCodeConventionsExampleCommentService {
    private final JavaCodeConventionsExampleCommentRepository javaCodeConventionsExampleCommentRepository;
    private final JavaCodeConventionsExampleCommentRepositoryImpl javaCodeConventionsExampleCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public JavaCodeConventionsExampleCommentService(JavaCodeConventionsExampleCommentRepository javaCodeConventionsExampleCommentRepository,
                                                    JavaCodeConventionsExampleCommentRepositoryImpl javaCodeConventionsExampleCommentRepositoryImpl,
                                                    UserRepositoryImpl userRepositoryImpl) {
        this.javaCodeConventionsExampleCommentRepository = javaCodeConventionsExampleCommentRepository;
        this.javaCodeConventionsExampleCommentRepositoryImpl = javaCodeConventionsExampleCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param javaCodeConventionsExampleDto
     * @return
     */
    public JavaCodeConventionsExampleDto findAllByJavaCodeConventionsExampleIdxOrderByIdxDesc(JavaCodeConventionsExampleDto javaCodeConventionsExampleDto) {
        List<JavaCodeConventionsExampleCommentDto> javaCodeConventionsExampleCommentDtoList = JavaCodeConventionsExampleCommentMapper.INSTANCE.toDto(javaCodeConventionsExampleCommentRepository.findAllByJavaCodeConventionsExampleIdxOrderByIdxDesc(javaCodeConventionsExampleDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (JavaCodeConventionsExampleCommentDto javaCodeConventionsExampleCommentDto : javaCodeConventionsExampleCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(javaCodeConventionsExampleCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            javaCodeConventionsExampleCommentDto.setNewIcon(NewIconCheck.isNew(javaCodeConventionsExampleCommentDto.getCreatedDate()));
            javaCodeConventionsExampleCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            javaCodeConventionsExampleCommentDto.setCreatedByUser(createdByUser);
        }

        javaCodeConventionsExampleDto = JavaCodeConventionsExampleMapper.INSTANCE.toDtoByCommentList(javaCodeConventionsExampleDto, javaCodeConventionsExampleCommentDtoList);

        return javaCodeConventionsExampleDto;
    }

    /**
     * 등록
     *
     * @param JavaCodeConventionsExampleCommentDto
     * @return
     */
    public long insertJavaCodeConventionsExampleComment(JavaCodeConventionsExampleCommentDto JavaCodeConventionsExampleCommentDto) {
        return javaCodeConventionsExampleCommentRepository.save(JavaCodeConventionsExampleCommentMapper.INSTANCE.toEntity(JavaCodeConventionsExampleCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param JavaCodeConventionsExampleCommentDto
     * @return
     */
    @Transactional
    public long updateJavaCodeConventionsExampleComment(long idx, JavaCodeConventionsExampleCommentDto JavaCodeConventionsExampleCommentDto) {
        JavaCodeConventionsExampleComment persistJavaCodeConventionsExampleComment = javaCodeConventionsExampleCommentRepository.getReferenceById(idx);
        JavaCodeConventionsExampleComment JavaCodeConventionsExampleComment = JavaCodeConventionsExampleCommentMapper.INSTANCE.toEntity(JavaCodeConventionsExampleCommentDto);

        persistJavaCodeConventionsExampleComment.update(JavaCodeConventionsExampleComment);

        return javaCodeConventionsExampleCommentRepository.save(persistJavaCodeConventionsExampleComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteJavaCodeConventionsExampleCommentByIdx(long idx) {
        javaCodeConventionsExampleCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param javaCodeConventionsExampleIdx
     */
    public void deleteAllByJavaCodeConventionsExampleIdx(long javaCodeConventionsExampleIdx) {
        javaCodeConventionsExampleCommentRepositoryImpl.deleteAllByJavaCodeConventionsExampleIdx(javaCodeConventionsExampleIdx);
    }
}