package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuidelineComment;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineCommentDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.mapper.CweJavaGuidelineCommentMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.mapper.CweJavaGuidelineMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository.CweJavaGuidelineCommentRepository;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository.CweJavaGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CweJavaGuidelineCommentService {
    private final CweJavaGuidelineCommentRepository cweJavaGuidelineCommentRepository;
    private final CweJavaGuidelineCommentRepositoryImpl cweJavaGuidelineCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public CweJavaGuidelineCommentService(CweJavaGuidelineCommentRepository cweJavaGuidelineCommentRepository,
                                          CweJavaGuidelineCommentRepositoryImpl cweJavaGuidelineCommentRepositoryImpl,
                                          UserRepositoryImpl userRepositoryImpl) {
        this.cweJavaGuidelineCommentRepository = cweJavaGuidelineCommentRepository;
        this.cweJavaGuidelineCommentRepositoryImpl = cweJavaGuidelineCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param cweJavaGuidelineDto
     * @return
     */
    public CweJavaGuidelineDto findAllByCweJavaGuidelineIdxOrderByIdxDesc(CweJavaGuidelineDto cweJavaGuidelineDto) {
        List<CweJavaGuidelineCommentDto> cweJavaGuidelineCommentDtoList = CweJavaGuidelineCommentMapper.INSTANCE.toDto(cweJavaGuidelineCommentRepository.findAllByCweJavaGuidelineIdxOrderByIdxDesc(cweJavaGuidelineDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (CweJavaGuidelineCommentDto cweJavaGuidelineCommentDto : cweJavaGuidelineCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(cweJavaGuidelineCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            cweJavaGuidelineCommentDto.setNewIcon(NewIconCheck.isNew(cweJavaGuidelineCommentDto.getCreatedDate()));
            cweJavaGuidelineCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            cweJavaGuidelineCommentDto.setCreatedByUser(createdByUser);
        }

        cweJavaGuidelineDto = CweJavaGuidelineMapper.INSTANCE.toDtoByCommentList(cweJavaGuidelineDto, cweJavaGuidelineCommentDtoList);

        return cweJavaGuidelineDto;
    }

    /**
     * 등록
     *
     * @param CweJavaGuidelineCommentDto
     * @return
     */
    public long insertCweJavaGuidelineComment(CweJavaGuidelineCommentDto CweJavaGuidelineCommentDto) {
        return cweJavaGuidelineCommentRepository.save(CweJavaGuidelineCommentMapper.INSTANCE.toEntity(CweJavaGuidelineCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param CweJavaGuidelineCommentDto
     * @return
     */
    @Transactional
    public long updateCweJavaGuidelineComment(long idx, CweJavaGuidelineCommentDto CweJavaGuidelineCommentDto) {
        CweJavaGuidelineComment persistCweJavaGuidelineComment = cweJavaGuidelineCommentRepository.getReferenceById(idx);
        CweJavaGuidelineComment CweJavaGuidelineComment = CweJavaGuidelineCommentMapper.INSTANCE.toEntity(CweJavaGuidelineCommentDto);

        persistCweJavaGuidelineComment.update(CweJavaGuidelineComment);

        return cweJavaGuidelineCommentRepository.save(persistCweJavaGuidelineComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteCweJavaGuidelineCommentByIdx(long idx) {
        cweJavaGuidelineCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param idx
     */
    public void deleteAllByCweJavaGuidelineIdx(long idx) {
        cweJavaGuidelineCommentRepositoryImpl.deleteAllByCweJavaGuidelineIdx(idx);
    }
}