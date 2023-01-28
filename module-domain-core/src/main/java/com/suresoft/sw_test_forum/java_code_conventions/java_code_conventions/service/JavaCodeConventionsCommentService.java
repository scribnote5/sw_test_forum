package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.JavaCodeConventionsComment;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.JavaCodeConventionsCommentDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.JavaCodeConventionsDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.mapper.JavaCodeConventionsCommentMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.mapper.JavaCodeConventionsMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.repository.JavaCodeConventionsCommentRepository;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.repository.JavaCodeConventionsCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class JavaCodeConventionsCommentService {
    private final JavaCodeConventionsCommentRepository javaCodeConventionsCommentRepository;
    private final JavaCodeConventionsCommentRepositoryImpl javaCodeConventionsCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public JavaCodeConventionsCommentService(JavaCodeConventionsCommentRepository javaCodeConventionsCommentRepository,
                                             JavaCodeConventionsCommentRepositoryImpl javaCodeConventionsCommentRepositoryImpl,
                                             UserRepositoryImpl userRepositoryImpl) {
        this.javaCodeConventionsCommentRepository = javaCodeConventionsCommentRepository;
        this.javaCodeConventionsCommentRepositoryImpl = javaCodeConventionsCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param javaCodeConventionsDto
     * @return
     */
    public JavaCodeConventionsDto findAllByJavaCodeConventionsIdxOrderByIdxDesc(JavaCodeConventionsDto javaCodeConventionsDto) {
        List<JavaCodeConventionsCommentDto> javaCodeConventionsCommentDtoList = JavaCodeConventionsCommentMapper.INSTANCE.toDto(javaCodeConventionsCommentRepository.findAllByJavaCodeConventionsIdxOrderByIdxDesc(javaCodeConventionsDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (JavaCodeConventionsCommentDto javaCodeConventionsCommentDto : javaCodeConventionsCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(javaCodeConventionsCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            javaCodeConventionsCommentDto.setNewIcon(NewIconCheck.isNew(javaCodeConventionsCommentDto.getCreatedDate()));
            javaCodeConventionsCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            javaCodeConventionsCommentDto.setCreatedByUser(createdByUser);
        }

        javaCodeConventionsDto = JavaCodeConventionsMapper.INSTANCE.toDtoByCommentList(javaCodeConventionsDto, javaCodeConventionsCommentDtoList);

        return javaCodeConventionsDto;
    }

    /**
     * 등록
     *
     * @param JavaCodeConventionsCommentDto
     * @return
     */
    public long insertJavaCodeConventionsComment(JavaCodeConventionsCommentDto JavaCodeConventionsCommentDto) {
        return javaCodeConventionsCommentRepository.save(JavaCodeConventionsCommentMapper.INSTANCE.toEntity(JavaCodeConventionsCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param JavaCodeConventionsCommentDto
     * @return
     */
    @Transactional
    public long updateJavaCodeConventionsComment(long idx, JavaCodeConventionsCommentDto JavaCodeConventionsCommentDto) {
        JavaCodeConventionsComment persistJavaCodeConventionsComment = javaCodeConventionsCommentRepository.getReferenceById(idx);
        JavaCodeConventionsComment JavaCodeConventionsComment = JavaCodeConventionsCommentMapper.INSTANCE.toEntity(JavaCodeConventionsCommentDto);

        persistJavaCodeConventionsComment.update(JavaCodeConventionsComment);

        return javaCodeConventionsCommentRepository.save(persistJavaCodeConventionsComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteJavaCodeConventionsCommentByIdx(long idx) {
        javaCodeConventionsCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param javaCodeConventionsIdx
     */
    public void deleteAllByJavaCodeConventionsIdx(long javaCodeConventionsIdx) {
        javaCodeConventionsCommentRepositoryImpl.deleteAllByJavaCodeConventionsIdx(javaCodeConventionsIdx);
    }
}