package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.domain.MisraCppExampleComment;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.MisraCppExampleCommentDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.MisraCppExampleDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.mapper.MisraCppExampleCommentMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.mapper.MisraCppExampleMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.repository.MisraCppExampleCommentRepository;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.repository.MisraCppExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MisraCppExampleCommentService {
    private final MisraCppExampleCommentRepository misraCppExampleCommentRepository;
    private final MisraCppExampleCommentRepositoryImpl misraCppExampleCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public MisraCppExampleCommentService(MisraCppExampleCommentRepository misraCppExampleCommentRepository,
                                       MisraCppExampleCommentRepositoryImpl misraCppExampleCommentRepositoryImpl,
                                       UserRepositoryImpl userRepositoryImpl) {
        this.misraCppExampleCommentRepository = misraCppExampleCommentRepository;
        this.misraCppExampleCommentRepositoryImpl = misraCppExampleCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param misraCppExampleDto
     * @return
     */
    public MisraCppExampleDto findAllByMisraCppExampleIdxOrderByIdxDesc(MisraCppExampleDto misraCppExampleDto) {
        List<MisraCppExampleCommentDto> misraCppExampleCommentDtoList = MisraCppExampleCommentMapper.INSTANCE.toDto(misraCppExampleCommentRepository.findAllByMisraCppExampleIdxOrderByIdxDesc(misraCppExampleDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (MisraCppExampleCommentDto misraCppExampleCommentDto : misraCppExampleCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(misraCppExampleCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            misraCppExampleCommentDto.setNewIcon(NewIconCheck.isNew(misraCppExampleCommentDto.getCreatedDate()));
            misraCppExampleCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            misraCppExampleCommentDto.setCreatedByUser(createdByUser);
        }

        misraCppExampleDto = MisraCppExampleMapper.INSTANCE.toDtoByCommentList(misraCppExampleDto, misraCppExampleCommentDtoList);

        return misraCppExampleDto;
    }

    /**
     * 등록
     *
     * @param MisraCppExampleCommentDto
     * @return
     */
    public long insertMisraCppExampleComment(MisraCppExampleCommentDto MisraCppExampleCommentDto) {
        return misraCppExampleCommentRepository.save(MisraCppExampleCommentMapper.INSTANCE.toEntity(MisraCppExampleCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param MisraCppExampleCommentDto
     * @return
     */
    @Transactional
    public long updateMisraCppExampleComment(long idx, MisraCppExampleCommentDto MisraCppExampleCommentDto) {
        MisraCppExampleComment persistMisraCppExampleComment = misraCppExampleCommentRepository.getReferenceById(idx);
        MisraCppExampleComment MisraCppExampleComment = MisraCppExampleCommentMapper.INSTANCE.toEntity(MisraCppExampleCommentDto);

        persistMisraCppExampleComment.update(MisraCppExampleComment);

        return misraCppExampleCommentRepository.save(persistMisraCppExampleComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteMisraCppExampleCommentByIdx(long idx) {
        misraCppExampleCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param misraCppExampleIdx
     */
    public void deleteAllByMisraCppExampleIdx(long misraCppExampleIdx) {
        misraCppExampleCommentRepositoryImpl.deleteAllByMisraCppExampleIdx(misraCppExampleIdx);
    }
}