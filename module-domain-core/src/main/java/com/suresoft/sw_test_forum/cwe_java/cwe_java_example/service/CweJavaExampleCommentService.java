package com.suresoft.sw_test_forum.cwe_java.cwe_java_example.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.domain.CweJavaExampleComment;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.CweJavaExampleCommentDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.CweJavaExampleDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.mapper.CweJavaExampleCommentMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.mapper.CweJavaExampleMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.repository.CweJavaExampleCommentRepository;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.repository.CweJavaExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CweJavaExampleCommentService {
    private final CweJavaExampleCommentRepository cweJavaExampleCommentRepository;
    private final CweJavaExampleCommentRepositoryImpl cweJavaExampleCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public CweJavaExampleCommentService(CweJavaExampleCommentRepository cweJavaExampleCommentRepository,
                                        CweJavaExampleCommentRepositoryImpl cweJavaExampleCommentRepositoryImpl,
                                        UserRepositoryImpl userRepositoryImpl) {
        this.cweJavaExampleCommentRepository = cweJavaExampleCommentRepository;
        this.cweJavaExampleCommentRepositoryImpl = cweJavaExampleCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param cweJavaExampleDto
     * @return
     */
    public CweJavaExampleDto findAllByCweJavaExampleIdxOrderByIdxDesc(CweJavaExampleDto cweJavaExampleDto) {
        List<CweJavaExampleCommentDto> cweJavaExampleCommentDtoList = CweJavaExampleCommentMapper.INSTANCE.toDto(cweJavaExampleCommentRepository.findAllByCweJavaExampleIdxOrderByIdxDesc(cweJavaExampleDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (CweJavaExampleCommentDto cweJavaExampleCommentDto : cweJavaExampleCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(cweJavaExampleCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            cweJavaExampleCommentDto.setNewIcon(NewIconCheck.isNew(cweJavaExampleCommentDto.getCreatedDate()));
            cweJavaExampleCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            cweJavaExampleCommentDto.setCreatedByUser(createdByUser);
        }

        cweJavaExampleDto = CweJavaExampleMapper.INSTANCE.toDtoByCommentList(cweJavaExampleDto, cweJavaExampleCommentDtoList);

        return cweJavaExampleDto;
    }

    /**
     * 등록
     *
     * @param CweJavaExampleCommentDto
     * @return
     */
    public long insertCweJavaExampleComment(CweJavaExampleCommentDto CweJavaExampleCommentDto) {
        return cweJavaExampleCommentRepository.save(CweJavaExampleCommentMapper.INSTANCE.toEntity(CweJavaExampleCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param CweJavaExampleCommentDto
     * @return
     */
    @Transactional
    public long updateCweJavaExampleComment(long idx, CweJavaExampleCommentDto CweJavaExampleCommentDto) {
        CweJavaExampleComment persistCweJavaExampleComment = cweJavaExampleCommentRepository.getReferenceById(idx);
        CweJavaExampleComment CweJavaExampleComment = CweJavaExampleCommentMapper.INSTANCE.toEntity(CweJavaExampleCommentDto);

        persistCweJavaExampleComment.update(CweJavaExampleComment);

        return cweJavaExampleCommentRepository.save(persistCweJavaExampleComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteCweJavaExampleCommentByIdx(long idx) {
        cweJavaExampleCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param cweJavaExampleIdx
     */
    public void deleteAllByCweJavaExampleIdx(long cweJavaExampleIdx) {
        cweJavaExampleCommentRepositoryImpl.deleteAllByCweJavaExampleIdx(cweJavaExampleIdx);
    }
}