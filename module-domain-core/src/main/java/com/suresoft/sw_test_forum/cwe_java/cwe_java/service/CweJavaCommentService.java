package com.suresoft.sw_test_forum.cwe_java.cwe_java.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.CweJavaComment;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaCommentDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.mapper.CweJavaCommentMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.mapper.CweJavaMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.repository.CweJavaCommentRepository;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.repository.CweJavaCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CweJavaCommentService {
    private final CweJavaCommentRepository cweJavaCommentRepository;
    private final CweJavaCommentRepositoryImpl cweJavaCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public CweJavaCommentService(CweJavaCommentRepository cweJavaCommentRepository,
                                 CweJavaCommentRepositoryImpl cweJavaCommentRepositoryImpl,
                                 UserRepositoryImpl userRepositoryImpl) {
        this.cweJavaCommentRepository = cweJavaCommentRepository;
        this.cweJavaCommentRepositoryImpl = cweJavaCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param cweJavaDto
     * @return
     */
    public CweJavaDto findAllByCweJavaIdxOrderByIdxDesc(CweJavaDto cweJavaDto) {
        List<CweJavaCommentDto> cweJavaCommentDtoList = CweJavaCommentMapper.INSTANCE.toDto(cweJavaCommentRepository.findAllByCweJavaIdxOrderByIdxDesc(cweJavaDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (CweJavaCommentDto cweJavaCommentDto : cweJavaCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(cweJavaCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            cweJavaCommentDto.setNewIcon(NewIconCheck.isNew(cweJavaCommentDto.getCreatedDate()));
            cweJavaCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            cweJavaCommentDto.setCreatedByUser(createdByUser);
        }

        cweJavaDto = CweJavaMapper.INSTANCE.toDtoByCommentList(cweJavaDto, cweJavaCommentDtoList);

        return cweJavaDto;
    }

    /**
     * 등록
     *
     * @param CweJavaCommentDto
     * @return
     */
    public long insertCweJavaComment(CweJavaCommentDto CweJavaCommentDto) {
        return cweJavaCommentRepository.save(CweJavaCommentMapper.INSTANCE.toEntity(CweJavaCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param CweJavaCommentDto
     * @return
     */
    @Transactional
    public long updateCweJavaComment(long idx, CweJavaCommentDto CweJavaCommentDto) {
        CweJavaComment persistCweJavaComment = cweJavaCommentRepository.getReferenceById(idx);
        CweJavaComment CweJavaComment = CweJavaCommentMapper.INSTANCE.toEntity(CweJavaCommentDto);

        persistCweJavaComment.update(CweJavaComment);

        return cweJavaCommentRepository.save(persistCweJavaComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteCweJavaCommentByIdx(long idx) {
        cweJavaCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param cweJavaIdx
     */
    public void deleteAllByCweJavaIdx(long cweJavaIdx) {
        cweJavaCommentRepositoryImpl.deleteAllByCweJavaIdx(cweJavaIdx);
    }
}