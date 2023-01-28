package com.suresoft.sw_test_forum.misra_cpp.misra_cpp.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.MisraCppComment;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.MisraCppCommentDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.MisraCppDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.mapper.MisraCppCommentMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.mapper.MisraCppMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.repository.MisraCppCommentRepository;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.repository.MisraCppCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MisraCppCommentService {
    private final MisraCppCommentRepository misraCppCommentRepository;
    private final MisraCppCommentRepositoryImpl misraCppCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public MisraCppCommentService(MisraCppCommentRepository misraCppCommentRepository,
                                  MisraCppCommentRepositoryImpl misraCppCommentRepositoryImpl,
                                  UserRepositoryImpl userRepositoryImpl) {
        this.misraCppCommentRepository = misraCppCommentRepository;
        this.misraCppCommentRepositoryImpl = misraCppCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param misraCppDto
     * @return
     */
    public MisraCppDto findAllByMisraCppIdxOrderByIdxDesc(MisraCppDto misraCppDto) {
        List<MisraCppCommentDto> misraCppCommentDtoList = MisraCppCommentMapper.INSTANCE.toDto(misraCppCommentRepository.findAllByMisraCppIdxOrderByIdxDesc(misraCppDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (MisraCppCommentDto misraCppCommentDto : misraCppCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(misraCppCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            misraCppCommentDto.setNewIcon(NewIconCheck.isNew(misraCppCommentDto.getCreatedDate()));
            misraCppCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            misraCppCommentDto.setCreatedByUser(createdByUser);
        }

        misraCppDto = MisraCppMapper.INSTANCE.toDtoByCommentList(misraCppDto, misraCppCommentDtoList);

        return misraCppDto;
    }

    /**
     * 등록
     *
     * @param MisraCppCommentDto
     * @return
     */
    public long insertMisraCppComment(MisraCppCommentDto MisraCppCommentDto) {
        return misraCppCommentRepository.save(MisraCppCommentMapper.INSTANCE.toEntity(MisraCppCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param MisraCppCommentDto
     * @return
     */
    @Transactional
    public long updateMisraCppComment(long idx, MisraCppCommentDto MisraCppCommentDto) {
        MisraCppComment persistMisraCppComment = misraCppCommentRepository.getReferenceById(idx);
        MisraCppComment MisraCppComment = MisraCppCommentMapper.INSTANCE.toEntity(MisraCppCommentDto);

        persistMisraCppComment.update(MisraCppComment);

        return misraCppCommentRepository.save(persistMisraCppComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteMisraCppCommentByIdx(long idx) {
        misraCppCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param misraCppIdx
     */
    public void deleteAllByMisraCppIdx(long misraCppIdx) {
        misraCppCommentRepositoryImpl.deleteAllByMisraCppIdx(misraCppIdx);
    }
}