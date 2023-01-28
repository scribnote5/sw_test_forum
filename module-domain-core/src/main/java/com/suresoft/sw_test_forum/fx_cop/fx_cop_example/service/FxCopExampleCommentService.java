package com.suresoft.sw_test_forum.fx_cop.fx_cop_example.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.domain.FxCopExampleComment;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.FxCopExampleCommentDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.FxCopExampleDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.mapper.FxCopExampleCommentMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.mapper.FxCopExampleMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.repository.FxCopExampleCommentRepository;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.repository.FxCopExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class FxCopExampleCommentService {
    private final FxCopExampleCommentRepository fxCopExampleCommentRepository;
    private final FxCopExampleCommentRepositoryImpl fxCopExampleCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public FxCopExampleCommentService(FxCopExampleCommentRepository fxCopExampleCommentRepository,
                                         FxCopExampleCommentRepositoryImpl fxCopExampleCommentRepositoryImpl,
                                         UserRepositoryImpl userRepositoryImpl) {
        this.fxCopExampleCommentRepository = fxCopExampleCommentRepository;
        this.fxCopExampleCommentRepositoryImpl = fxCopExampleCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param fxCopExampleDto
     * @return
     */
    public FxCopExampleDto findAllByFxCopExampleIdxOrderByIdxDesc(FxCopExampleDto fxCopExampleDto) {
        List<FxCopExampleCommentDto> fxCopExampleCommentDtoList = FxCopExampleCommentMapper.INSTANCE.toDto(fxCopExampleCommentRepository.findAllByFxCopExampleIdxOrderByIdxDesc(fxCopExampleDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (FxCopExampleCommentDto fxCopExampleCommentDto : fxCopExampleCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(fxCopExampleCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            fxCopExampleCommentDto.setNewIcon(NewIconCheck.isNew(fxCopExampleCommentDto.getCreatedDate()));
            fxCopExampleCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            fxCopExampleCommentDto.setCreatedByUser(createdByUser);
        }

        fxCopExampleDto = FxCopExampleMapper.INSTANCE.toDtoByCommentList(fxCopExampleDto, fxCopExampleCommentDtoList);

        return fxCopExampleDto;
    }

    /**
     * 등록
     *
     * @param FxCopExampleCommentDto
     * @return
     */
    public long insertFxCopExampleComment(FxCopExampleCommentDto FxCopExampleCommentDto) {
        return fxCopExampleCommentRepository.save(FxCopExampleCommentMapper.INSTANCE.toEntity(FxCopExampleCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param FxCopExampleCommentDto
     * @return
     */
    @Transactional
    public long updateFxCopExampleComment(long idx, FxCopExampleCommentDto FxCopExampleCommentDto) {
        FxCopExampleComment persistFxCopExampleComment = fxCopExampleCommentRepository.getReferenceById(idx);
        FxCopExampleComment FxCopExampleComment = FxCopExampleCommentMapper.INSTANCE.toEntity(FxCopExampleCommentDto);

        persistFxCopExampleComment.update(FxCopExampleComment);

        return fxCopExampleCommentRepository.save(persistFxCopExampleComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteFxCopExampleCommentByIdx(long idx) {
        fxCopExampleCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param fxCopExampleIdx
     */
    public void deleteAllByFxCopExampleIdx(long fxCopExampleIdx) {
        fxCopExampleCommentRepositoryImpl.deleteAllByFxCopExampleIdx(fxCopExampleIdx);
    }
}