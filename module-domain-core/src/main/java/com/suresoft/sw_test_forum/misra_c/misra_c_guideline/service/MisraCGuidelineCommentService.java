package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.MisraCGuidelineComment;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineCommentDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.mapper.MisraCGuidelineCommentMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.mapper.MisraCGuidelineMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.repository.MisraCGuidelineCommentRepository;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.repository.MisraCGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MisraCGuidelineCommentService {
    private final MisraCGuidelineCommentRepository misraCGuidelineCommentRepository;
    private final MisraCGuidelineCommentRepositoryImpl misraCGuidelineCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public MisraCGuidelineCommentService(MisraCGuidelineCommentRepository misraCGuidelineCommentRepository,
                                         MisraCGuidelineCommentRepositoryImpl misraCGuidelineCommentRepositoryImpl,
                                         UserRepositoryImpl userRepositoryImpl) {
        this.misraCGuidelineCommentRepository = misraCGuidelineCommentRepository;
        this.misraCGuidelineCommentRepositoryImpl = misraCGuidelineCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param misraCGuidelineDto
     * @return
     */
    public MisraCGuidelineDto findAllByMisraCGuidelineIdxOrderByIdxDesc(MisraCGuidelineDto misraCGuidelineDto) {
        List<MisraCGuidelineCommentDto> misraCGuidelineCommentDtoList = MisraCGuidelineCommentMapper.INSTANCE.toDto(misraCGuidelineCommentRepository.findAllByMisraCGuidelineIdxOrderByIdxDesc(misraCGuidelineDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (MisraCGuidelineCommentDto misraCGuidelineCommentDto : misraCGuidelineCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(misraCGuidelineCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            misraCGuidelineCommentDto.setNewIcon(NewIconCheck.isNew(misraCGuidelineCommentDto.getCreatedDate()));
            misraCGuidelineCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            misraCGuidelineCommentDto.setCreatedByUser(createdByUser);
        }

        misraCGuidelineDto = MisraCGuidelineMapper.INSTANCE.toDtoByCommentList(misraCGuidelineDto, misraCGuidelineCommentDtoList);

        return misraCGuidelineDto;
    }

    /**
     * 등록
     *
     * @param MisraCGuidelineCommentDto
     * @return
     */
    public long insertMisraCGuidelineComment(MisraCGuidelineCommentDto MisraCGuidelineCommentDto) {
        return misraCGuidelineCommentRepository.save(MisraCGuidelineCommentMapper.INSTANCE.toEntity(MisraCGuidelineCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param MisraCGuidelineCommentDto
     * @return
     */
    @Transactional
    public long updateMisraCGuidelineComment(long idx, MisraCGuidelineCommentDto MisraCGuidelineCommentDto) {
        MisraCGuidelineComment persistMisraCGuidelineComment = misraCGuidelineCommentRepository.getReferenceById(idx);
        MisraCGuidelineComment MisraCGuidelineComment = MisraCGuidelineCommentMapper.INSTANCE.toEntity(MisraCGuidelineCommentDto);

        persistMisraCGuidelineComment.update(MisraCGuidelineComment);

        return misraCGuidelineCommentRepository.save(persistMisraCGuidelineComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteMisraCGuidelineCommentByIdx(long idx) {
        misraCGuidelineCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param idx
     */
    public void deleteAllByMisraCGuidelineIdx(long idx) {
        misraCGuidelineCommentRepositoryImpl.deleteAllByMisraCGuidelineIdx(idx);
    }
}