package com.suresoft.sw_test_forum.cwe.cwe_example.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe.cwe_example.domain.CweExampleComment;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.CweExampleCommentDto;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.CweExampleDto;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.mapper.CweExampleCommentMapper;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.mapper.CweExampleMapper;
import com.suresoft.sw_test_forum.cwe.cwe_example.repository.CweExampleCommentRepository;
import com.suresoft.sw_test_forum.cwe.cwe_example.repository.CweExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CweExampleCommentService {
    private final CweExampleCommentRepository cweExampleCommentRepository;
    private final CweExampleCommentRepositoryImpl cweExampleCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public CweExampleCommentService(CweExampleCommentRepository cweExampleCommentRepository,
                                       CweExampleCommentRepositoryImpl cweExampleCommentRepositoryImpl,
                                       UserRepositoryImpl userRepositoryImpl) {
        this.cweExampleCommentRepository = cweExampleCommentRepository;
        this.cweExampleCommentRepositoryImpl = cweExampleCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param cweExampleDto
     * @return
     */
    public CweExampleDto findAllByCweExampleIdxOrderByIdxDesc(CweExampleDto cweExampleDto) {
        List<CweExampleCommentDto> cweExampleCommentDtoList = CweExampleCommentMapper.INSTANCE.toDto(cweExampleCommentRepository.findAllByCweExampleIdxOrderByIdxDesc(cweExampleDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (CweExampleCommentDto cweExampleCommentDto : cweExampleCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(cweExampleCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            cweExampleCommentDto.setNewIcon(NewIconCheck.isNew(cweExampleCommentDto.getCreatedDate()));
            cweExampleCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            cweExampleCommentDto.setCreatedByUser(createdByUser);
        }

        cweExampleDto = CweExampleMapper.INSTANCE.toDtoByCommentList(cweExampleDto, cweExampleCommentDtoList);

        return cweExampleDto;
    }

    /**
     * 등록
     *
     * @param CweExampleCommentDto
     * @return
     */
    public long insertCweExampleComment(CweExampleCommentDto CweExampleCommentDto) {
        return cweExampleCommentRepository.save(CweExampleCommentMapper.INSTANCE.toEntity(CweExampleCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param CweExampleCommentDto
     * @return
     */
    @Transactional
    public long updateCweExampleComment(long idx, CweExampleCommentDto CweExampleCommentDto) {
        CweExampleComment persistCweExampleComment = cweExampleCommentRepository.getReferenceById(idx);
        CweExampleComment CweExampleComment = CweExampleCommentMapper.INSTANCE.toEntity(CweExampleCommentDto);

        persistCweExampleComment.update(CweExampleComment);

        return cweExampleCommentRepository.save(persistCweExampleComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteCweExampleCommentByIdx(long idx) {
        cweExampleCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param cweExampleIdx
     */
    public void deleteAllByCweExampleIdx(long cweExampleIdx) {
        cweExampleCommentRepositoryImpl.deleteAllByCweExampleIdx(cweExampleIdx);
    }
}