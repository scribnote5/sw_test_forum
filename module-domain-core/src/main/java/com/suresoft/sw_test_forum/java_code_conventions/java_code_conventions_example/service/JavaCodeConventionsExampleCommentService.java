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
     * ????????? ??????
     *
     * @param javaCodeConventionsExampleDto
     * @return
     */
    public JavaCodeConventionsExampleDto findAllByJavaCodeConventionsExampleIdxOrderByIdxDesc(JavaCodeConventionsExampleDto javaCodeConventionsExampleDto) {
        List<JavaCodeConventionsExampleCommentDto> javaCodeConventionsExampleCommentDtoList = JavaCodeConventionsExampleCommentMapper.INSTANCE.toDto(javaCodeConventionsExampleCommentRepository.findAllByJavaCodeConventionsExampleIdxOrderByIdxDesc(javaCodeConventionsExampleDto.getIdx()));

        // NewIcon ??????, createdBy ??????
        for (JavaCodeConventionsExampleCommentDto javaCodeConventionsExampleCommentDto : javaCodeConventionsExampleCommentDtoList) {
            // ?????? ??????
            // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userRepositoryImpl.findByIdx(javaCodeConventionsExampleCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("?????? ??????")
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
     * ??????
     *
     * @param JavaCodeConventionsExampleCommentDto
     * @return
     */
    public long insertJavaCodeConventionsExampleComment(JavaCodeConventionsExampleCommentDto JavaCodeConventionsExampleCommentDto) {
        return javaCodeConventionsExampleCommentRepository.save(JavaCodeConventionsExampleCommentMapper.INSTANCE.toEntity(JavaCodeConventionsExampleCommentDto)).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     * @param JavaCodeConventionsExampleCommentDto
     * @return
     */
    @Transactional
    public long updateJavaCodeConventionsExampleComment(long idx, JavaCodeConventionsExampleCommentDto JavaCodeConventionsExampleCommentDto) {
        JavaCodeConventionsExampleComment persistJavaCodeConventionsExampleComment = javaCodeConventionsExampleCommentRepository.getById(idx);
        JavaCodeConventionsExampleComment JavaCodeConventionsExampleComment = JavaCodeConventionsExampleCommentMapper.INSTANCE.toEntity(JavaCodeConventionsExampleCommentDto);

        persistJavaCodeConventionsExampleComment.update(JavaCodeConventionsExampleComment);

        return javaCodeConventionsExampleCommentRepository.save(persistJavaCodeConventionsExampleComment).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     */
    public void deleteJavaCodeConventionsExampleCommentByIdx(long idx) {
        javaCodeConventionsExampleCommentRepository.deleteById(idx);
    }

    /**
     * ?????? ??????
     *
     * @param javaCodeConventionsExampleIdx
     */
    public void deleteAllByJavaCodeConventionsExampleIdx(long javaCodeConventionsExampleIdx) {
        javaCodeConventionsExampleCommentRepositoryImpl.deleteAllByJavaCodeConventionsExampleIdx(javaCodeConventionsExampleIdx);
    }
}