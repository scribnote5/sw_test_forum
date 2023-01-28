package com.suresoft.sw_test_forum.cwe.cwe_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuidelineComment;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineCommentDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.mapper.CweGuidelineCommentMapper;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.mapper.CweGuidelineMapper;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.repository.CweGuidelineCommentRepository;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.repository.CweGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CweGuidelineCommentService {
    private final CweGuidelineCommentRepository cweGuidelineCommentRepository;
    private final CweGuidelineCommentRepositoryImpl cweGuidelineCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public CweGuidelineCommentService(CweGuidelineCommentRepository cweGuidelineCommentRepository,
                                         CweGuidelineCommentRepositoryImpl cweGuidelineCommentRepositoryImpl,
                                         UserRepositoryImpl userRepositoryImpl) {
        this.cweGuidelineCommentRepository = cweGuidelineCommentRepository;
        this.cweGuidelineCommentRepositoryImpl = cweGuidelineCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param cweGuidelineDto
     * @return
     */
    public CweGuidelineDto findAllByCweGuidelineIdxOrderByIdxDesc(CweGuidelineDto cweGuidelineDto) {
        List<CweGuidelineCommentDto> cweGuidelineCommentDtoList = CweGuidelineCommentMapper.INSTANCE.toDto(cweGuidelineCommentRepository.findAllByCweGuidelineIdxOrderByIdxDesc(cweGuidelineDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (CweGuidelineCommentDto cweGuidelineCommentDto : cweGuidelineCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(cweGuidelineCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            cweGuidelineCommentDto.setNewIcon(NewIconCheck.isNew(cweGuidelineCommentDto.getCreatedDate()));
            cweGuidelineCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            cweGuidelineCommentDto.setCreatedByUser(createdByUser);
        }

        cweGuidelineDto = CweGuidelineMapper.INSTANCE.toDtoByCommentList(cweGuidelineDto, cweGuidelineCommentDtoList);

        return cweGuidelineDto;
    }

    /**
     * 등록
     *
     * @param CweGuidelineCommentDto
     * @return
     */
    public long insertCweGuidelineComment(CweGuidelineCommentDto CweGuidelineCommentDto) {
        return cweGuidelineCommentRepository.save(CweGuidelineCommentMapper.INSTANCE.toEntity(CweGuidelineCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param CweGuidelineCommentDto
     * @return
     */
    @Transactional
    public long updateCweGuidelineComment(long idx, CweGuidelineCommentDto CweGuidelineCommentDto) {
        CweGuidelineComment persistCweGuidelineComment = cweGuidelineCommentRepository.getReferenceById(idx);
        CweGuidelineComment CweGuidelineComment = CweGuidelineCommentMapper.INSTANCE.toEntity(CweGuidelineCommentDto);

        persistCweGuidelineComment.update(CweGuidelineComment);

        return cweGuidelineCommentRepository.save(persistCweGuidelineComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteCweGuidelineCommentByIdx(long idx) {
        cweGuidelineCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param idx
     */
    public void deleteAllByCweGuidelineIdx(long idx) {
        cweGuidelineCommentRepositoryImpl.deleteAllByCweGuidelineIdx(idx);
    }
}