package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.MisraCppGuidelineComment;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineCommentDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.mapper.MisraCppGuidelineCommentMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.mapper.MisraCppGuidelineMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.repository.MisraCppGuidelineCommentRepository;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.repository.MisraCppGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class MisraCppGuidelineCommentService {
    private final MisraCppGuidelineCommentRepository misraCppGuidelineCommentRepository;
    private final MisraCppGuidelineCommentRepositoryImpl misraCppGuidelineCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public MisraCppGuidelineCommentService(MisraCppGuidelineCommentRepository misraCppGuidelineCommentRepository,
                                         MisraCppGuidelineCommentRepositoryImpl misraCppGuidelineCommentRepositoryImpl,
                                         UserRepositoryImpl userRepositoryImpl) {
        this.misraCppGuidelineCommentRepository = misraCppGuidelineCommentRepository;
        this.misraCppGuidelineCommentRepositoryImpl = misraCppGuidelineCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param misraCppGuidelineDto
     * @return
     */
    public MisraCppGuidelineDto findAllByMisraCppGuidelineIdxOrderByIdxDesc(MisraCppGuidelineDto misraCppGuidelineDto) {
        List<MisraCppGuidelineCommentDto> misraCppGuidelineCommentDtoList = MisraCppGuidelineCommentMapper.INSTANCE.toDto(misraCppGuidelineCommentRepository.findAllByMisraCppGuidelineIdxOrderByIdxDesc(misraCppGuidelineDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (MisraCppGuidelineCommentDto misraCppGuidelineCommentDto : misraCppGuidelineCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(misraCppGuidelineCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            misraCppGuidelineCommentDto.setNewIcon(NewIconCheck.isNew(misraCppGuidelineCommentDto.getCreatedDate()));
            misraCppGuidelineCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            misraCppGuidelineCommentDto.setCreatedByUser(createdByUser);
        }

        misraCppGuidelineDto = MisraCppGuidelineMapper.INSTANCE.toDtoByCommentList(misraCppGuidelineDto, misraCppGuidelineCommentDtoList);

        return misraCppGuidelineDto;
    }

    /**
     * 등록
     *
     * @param MisraCppGuidelineCommentDto
     * @return
     */
    public long insertMisraCppGuidelineComment(MisraCppGuidelineCommentDto MisraCppGuidelineCommentDto) {
        return misraCppGuidelineCommentRepository.save(MisraCppGuidelineCommentMapper.INSTANCE.toEntity(MisraCppGuidelineCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param MisraCppGuidelineCommentDto
     * @return
     */
    @Transactional
    public long updateMisraCppGuidelineComment(long idx, MisraCppGuidelineCommentDto MisraCppGuidelineCommentDto) {
        MisraCppGuidelineComment persistMisraCppGuidelineComment = misraCppGuidelineCommentRepository.getReferenceById(idx);
        MisraCppGuidelineComment MisraCppGuidelineComment = MisraCppGuidelineCommentMapper.INSTANCE.toEntity(MisraCppGuidelineCommentDto);

        persistMisraCppGuidelineComment.update(MisraCppGuidelineComment);

        return misraCppGuidelineCommentRepository.save(persistMisraCppGuidelineComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteMisraCppGuidelineCommentByIdx(long idx) {
        misraCppGuidelineCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param idx
     */
    public void deleteAllByMisraCppGuidelineIdx(long idx) {
        misraCppGuidelineCommentRepositoryImpl.deleteAllByMisraCppGuidelineIdx(idx);
    }
}