package com.suresoft.sw_test_forum.misra_c.misra_c.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCComment;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.MisraCCommentDto;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.MisraCDto;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.mapper.MisraCCommentMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.mapper.MisraCMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c.repository.MisraCCommentRepository;
import com.suresoft.sw_test_forum.misra_c.misra_c.repository.MisraCCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MisraCCommentService {
    private final MisraCCommentRepository misraCCommentRepository;
    private final MisraCCommentRepositoryImpl misraCCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public MisraCCommentService(MisraCCommentRepository misraCCommentRepository,
                                MisraCCommentRepositoryImpl misraCCommentRepositoryImpl,
                                UserRepositoryImpl userRepositoryImpl) {
        this.misraCCommentRepository = misraCCommentRepository;
        this.misraCCommentRepositoryImpl = misraCCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param misraCDto
     * @return
     */
    public MisraCDto findAllByMisraCIdxOrderByIdxDesc(MisraCDto misraCDto) {
        List<MisraCCommentDto> misraCCommentDtoList = MisraCCommentMapper.INSTANCE.toDto(misraCCommentRepository.findAllByMisraCIdxOrderByIdxDesc(misraCDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (MisraCCommentDto misraCCommentDto : misraCCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(misraCCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            misraCCommentDto.setNewIcon(NewIconCheck.isNew(misraCCommentDto.getCreatedDate()));
            misraCCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            misraCCommentDto.setCreatedByUser(createdByUser);
        }

        misraCDto = MisraCMapper.INSTANCE.toDtoByCommentList(misraCDto, misraCCommentDtoList);

        return misraCDto;
    }

    /**
     * 등록
     *
     * @param MisraCCommentDto
     * @return
     */
    public long insertMisraCComment(MisraCCommentDto MisraCCommentDto) {
        return misraCCommentRepository.save(MisraCCommentMapper.INSTANCE.toEntity(MisraCCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param MisraCCommentDto
     * @return
     */
    @Transactional
    public long updateMisraCComment(long idx, MisraCCommentDto MisraCCommentDto) {
        MisraCComment persistMisraCComment = misraCCommentRepository.getReferenceById(idx);
        MisraCComment MisraCComment = MisraCCommentMapper.INSTANCE.toEntity(MisraCCommentDto);

        persistMisraCComment.update(MisraCComment);

        return misraCCommentRepository.save(persistMisraCComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteMisraCCommentByIdx(long idx) {
        misraCCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param misraCIdx
     */
    public void deleteAllByMisraCIdx(long misraCIdx) {
        misraCCommentRepositoryImpl.deleteAllByMisraCIdx(misraCIdx);
    }
}