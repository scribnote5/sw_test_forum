package com.suresoft.sw_test_forum.cwe.cwe.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.cwe.cwe.domain.CweComment;
import com.suresoft.sw_test_forum.cwe.cwe.dto.CweCommentDto;
import com.suresoft.sw_test_forum.cwe.cwe.dto.CweDto;
import com.suresoft.sw_test_forum.cwe.cwe.dto.mapper.CweCommentMapper;
import com.suresoft.sw_test_forum.cwe.cwe.dto.mapper.CweMapper;
import com.suresoft.sw_test_forum.cwe.cwe.repository.CweCommentRepository;
import com.suresoft.sw_test_forum.cwe.cwe.repository.CweCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CweCommentService {
    private final CweCommentRepository cweCommentRepository;
    private final CweCommentRepositoryImpl cweCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public CweCommentService(CweCommentRepository cweCommentRepository,
                                  CweCommentRepositoryImpl cweCommentRepositoryImpl,
                                  UserRepositoryImpl userRepositoryImpl) {
        this.cweCommentRepository = cweCommentRepository;
        this.cweCommentRepositoryImpl = cweCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param cweDto
     * @return
     */
    public CweDto findAllByCweIdxOrderByIdxDesc(CweDto cweDto) {
        List<CweCommentDto> cweCommentDtoList = CweCommentMapper.INSTANCE.toDto(cweCommentRepository.findAllByCweIdxOrderByIdxDesc(cweDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (CweCommentDto cweCommentDto : cweCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(cweCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            cweCommentDto.setNewIcon(NewIconCheck.isNew(cweCommentDto.getCreatedDate()));
            cweCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            cweCommentDto.setCreatedByUser(createdByUser);
        }

        cweDto = CweMapper.INSTANCE.toDtoByCommentList(cweDto, cweCommentDtoList);

        return cweDto;
    }

    /**
     * 등록
     *
     * @param CweCommentDto
     * @return
     */
    public long insertCweComment(CweCommentDto CweCommentDto) {
        return cweCommentRepository.save(CweCommentMapper.INSTANCE.toEntity(CweCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param CweCommentDto
     * @return
     */
    @Transactional
    public long updateCweComment(long idx, CweCommentDto CweCommentDto) {
        CweComment persistCweComment = cweCommentRepository.getReferenceById(idx);
        CweComment CweComment = CweCommentMapper.INSTANCE.toEntity(CweCommentDto);

        persistCweComment.update(CweComment);

        return cweCommentRepository.save(persistCweComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteCweCommentByIdx(long idx) {
        cweCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param cweIdx
     */
    public void deleteAllByCweIdx(long cweIdx) {
        cweCommentRepositoryImpl.deleteAllByCweIdx(cweIdx);
    }
}