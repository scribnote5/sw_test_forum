package com.suresoft.sw_test_forum.fx_cop.fx_cop.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.FxCopComment;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.FxCopCommentDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.FxCopDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.mapper.FxCopCommentMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.mapper.FxCopMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.repository.FxCopCommentRepository;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.repository.FxCopCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FxCopCommentService {
    private final FxCopCommentRepository fxCopCommentRepository;
    private final FxCopCommentRepositoryImpl fxCopCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public FxCopCommentService(FxCopCommentRepository fxCopCommentRepository,
                                  FxCopCommentRepositoryImpl fxCopCommentRepositoryImpl,
                                  UserRepositoryImpl userRepositoryImpl) {
        this.fxCopCommentRepository = fxCopCommentRepository;
        this.fxCopCommentRepositoryImpl = fxCopCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param fxCopDto
     * @return
     */
    public FxCopDto findAllByFxCopIdxOrderByIdxDesc(FxCopDto fxCopDto) {
        List<FxCopCommentDto> fxCopCommentDtoList = FxCopCommentMapper.INSTANCE.toDto(fxCopCommentRepository.findAllByFxCopIdxOrderByIdxDesc(fxCopDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (FxCopCommentDto fxCopCommentDto : fxCopCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(fxCopCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            fxCopCommentDto.setNewIcon(NewIconCheck.isNew(fxCopCommentDto.getCreatedDate()));
            fxCopCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            fxCopCommentDto.setCreatedByUser(createdByUser);
        }

        fxCopDto = FxCopMapper.INSTANCE.toDtoByCommentList(fxCopDto, fxCopCommentDtoList);

        return fxCopDto;
    }

    /**
     * 등록
     *
     * @param FxCopCommentDto
     * @return
     */
    public long insertFxCopComment(FxCopCommentDto FxCopCommentDto) {
        return fxCopCommentRepository.save(FxCopCommentMapper.INSTANCE.toEntity(FxCopCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param FxCopCommentDto
     * @return
     */
    @Transactional
    public long updateFxCopComment(long idx, FxCopCommentDto FxCopCommentDto) {
        FxCopComment persistFxCopComment = fxCopCommentRepository.getReferenceById(idx);
        FxCopComment FxCopComment = FxCopCommentMapper.INSTANCE.toEntity(FxCopCommentDto);

        persistFxCopComment.update(FxCopComment);

        return fxCopCommentRepository.save(persistFxCopComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteFxCopCommentByIdx(long idx) {
        fxCopCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param fxCopIdx
     */
    public void deleteAllByFxCopIdx(long fxCopIdx) {
        fxCopCommentRepositoryImpl.deleteAllByFxCopIdx(fxCopIdx);
    }
}