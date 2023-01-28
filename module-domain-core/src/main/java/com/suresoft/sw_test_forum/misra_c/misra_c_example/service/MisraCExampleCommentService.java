package com.suresoft.sw_test_forum.misra_c.misra_c_example.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.domain.MisraCExampleComment;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.MisraCExampleCommentDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.MisraCExampleDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.mapper.MisraCExampleCommentMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.mapper.MisraCExampleMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.repository.MisraCExampleCommentRepository;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.repository.MisraCExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MisraCExampleCommentService {
    private final MisraCExampleCommentRepository misraCExampleCommentRepository;
    private final MisraCExampleCommentRepositoryImpl misraCExampleCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public MisraCExampleCommentService(MisraCExampleCommentRepository misraCExampleCommentRepository,
                                       MisraCExampleCommentRepositoryImpl misraCExampleCommentRepositoryImpl,
                                       UserRepositoryImpl userRepositoryImpl) {
        this.misraCExampleCommentRepository = misraCExampleCommentRepository;
        this.misraCExampleCommentRepositoryImpl = misraCExampleCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param misraCExampleDto
     * @return
     */
    public MisraCExampleDto findAllByMisraCExampleIdxOrderByIdxDesc(MisraCExampleDto misraCExampleDto) {
        List<MisraCExampleCommentDto> misraCExampleCommentDtoList = MisraCExampleCommentMapper.INSTANCE.toDto(misraCExampleCommentRepository.findAllByMisraCExampleIdxOrderByIdxDesc(misraCExampleDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (MisraCExampleCommentDto misraCExampleCommentDto : misraCExampleCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(misraCExampleCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            misraCExampleCommentDto.setNewIcon(NewIconCheck.isNew(misraCExampleCommentDto.getCreatedDate()));
            misraCExampleCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            misraCExampleCommentDto.setCreatedByUser(createdByUser);
        }

        misraCExampleDto = MisraCExampleMapper.INSTANCE.toDtoByCommentList(misraCExampleDto, misraCExampleCommentDtoList);

        return misraCExampleDto;
    }

    /**
     * 등록
     *
     * @param MisraCExampleCommentDto
     * @return
     */
    public long insertMisraCExampleComment(MisraCExampleCommentDto MisraCExampleCommentDto) {
        return misraCExampleCommentRepository.save(MisraCExampleCommentMapper.INSTANCE.toEntity(MisraCExampleCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param MisraCExampleCommentDto
     * @return
     */
    @Transactional
    public long updateMisraCExampleComment(long idx, MisraCExampleCommentDto MisraCExampleCommentDto) {
        MisraCExampleComment persistMisraCExampleComment = misraCExampleCommentRepository.getReferenceById(idx);
        MisraCExampleComment MisraCExampleComment = MisraCExampleCommentMapper.INSTANCE.toEntity(MisraCExampleCommentDto);

        persistMisraCExampleComment.update(MisraCExampleComment);

        return misraCExampleCommentRepository.save(persistMisraCExampleComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteMisraCExampleCommentByIdx(long idx) {
        misraCExampleCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param misraCExampleIdx
     */
    public void deleteAllByMisraCExampleIdx(long misraCExampleIdx) {
        misraCExampleCommentRepositoryImpl.deleteAllByMisraCExampleIdx(misraCExampleIdx);
    }
}