package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.FxCopGuidelineComment;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineCommentDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.mapper.FxCopGuidelineCommentMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.mapper.FxCopGuidelineMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.repository.FxCopGuidelineCommentRepository;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.repository.FxCopGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FxCopGuidelineCommentService {
    private final FxCopGuidelineCommentRepository fxCopGuidelineCommentRepository;
    private final FxCopGuidelineCommentRepositoryImpl fxCopGuidelineCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public FxCopGuidelineCommentService(FxCopGuidelineCommentRepository fxCopGuidelineCommentRepository,
                                           FxCopGuidelineCommentRepositoryImpl fxCopGuidelineCommentRepositoryImpl,
                                           UserRepositoryImpl userRepositoryImpl) {
        this.fxCopGuidelineCommentRepository = fxCopGuidelineCommentRepository;
        this.fxCopGuidelineCommentRepositoryImpl = fxCopGuidelineCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param fxCopGuidelineDto
     * @return
     */
    public FxCopGuidelineDto findAllByFxCopGuidelineIdxOrderByIdxDesc(FxCopGuidelineDto fxCopGuidelineDto) {
        List<FxCopGuidelineCommentDto> fxCopGuidelineCommentDtoList = FxCopGuidelineCommentMapper.INSTANCE.toDto(fxCopGuidelineCommentRepository.findAllByFxCopGuidelineIdxOrderByIdxDesc(fxCopGuidelineDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (FxCopGuidelineCommentDto fxCopGuidelineCommentDto : fxCopGuidelineCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(fxCopGuidelineCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            fxCopGuidelineCommentDto.setNewIcon(NewIconCheck.isNew(fxCopGuidelineCommentDto.getCreatedDate()));
            fxCopGuidelineCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            fxCopGuidelineCommentDto.setCreatedByUser(createdByUser);
        }

        fxCopGuidelineDto = FxCopGuidelineMapper.INSTANCE.toDtoByCommentList(fxCopGuidelineDto, fxCopGuidelineCommentDtoList);

        return fxCopGuidelineDto;
    }

    /**
     * 등록
     *
     * @param FxCopGuidelineCommentDto
     * @return
     */
    public long insertFxCopGuidelineComment(FxCopGuidelineCommentDto FxCopGuidelineCommentDto) {
        return fxCopGuidelineCommentRepository.save(FxCopGuidelineCommentMapper.INSTANCE.toEntity(FxCopGuidelineCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param FxCopGuidelineCommentDto
     * @return
     */
    @Transactional
    public long updateFxCopGuidelineComment(long idx, FxCopGuidelineCommentDto FxCopGuidelineCommentDto) {
        FxCopGuidelineComment persistFxCopGuidelineComment = fxCopGuidelineCommentRepository.getReferenceById(idx);
        FxCopGuidelineComment FxCopGuidelineComment = FxCopGuidelineCommentMapper.INSTANCE.toEntity(FxCopGuidelineCommentDto);

        persistFxCopGuidelineComment.update(FxCopGuidelineComment);

        return fxCopGuidelineCommentRepository.save(persistFxCopGuidelineComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteFxCopGuidelineCommentByIdx(long idx) {
        fxCopGuidelineCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param idx
     */
    public void deleteAllByFxCopGuidelineIdx(long idx) {
        fxCopGuidelineCommentRepositoryImpl.deleteAllByFxCopGuidelineIdx(idx);
    }
}