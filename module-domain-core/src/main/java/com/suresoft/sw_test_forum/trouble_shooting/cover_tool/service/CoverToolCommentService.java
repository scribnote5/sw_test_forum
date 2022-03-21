package com.suresoft.sw_test_forum.trouble_shooting.cover_tool.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain.CoverToolComment;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.CoverToolCommentDto;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.CoverToolDto;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.mapper.CoverToolCommentMapper;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.mapper.CoverToolMapper;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.repository.CoverToolCommentRepository;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.repository.CoverToolCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CoverToolCommentService {
    private final CoverToolCommentRepository coverToolCommentRepository;
    private final CoverToolCommentRepositoryImpl coverToolCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public CoverToolCommentService(CoverToolCommentRepository coverToolCommentRepository,
                                   CoverToolCommentRepositoryImpl coverToolCommentRepositoryImpl,
                                   UserRepositoryImpl userRepositoryImpl) {
        this.coverToolCommentRepository = coverToolCommentRepository;
        this.coverToolCommentRepositoryImpl = coverToolCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    public CoverToolDto findAllByCoverToolIdxOrderByCreatedDateDesc(CoverToolDto coverToolDto) {
        List<CoverToolCommentDto> coverToolCommentDtoList = CoverToolCommentMapper.INSTANCE.toDto(coverToolCommentRepository.findAllByCoverToolIdxOrderByCreatedDateDesc(coverToolDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (CoverToolCommentDto coverToolCommentDto : coverToolCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(coverToolCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            coverToolCommentDto.setNewIcon(NewIconCheck.isNew(coverToolCommentDto.getCreatedDate()));
            coverToolCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            coverToolCommentDto.setCreatedByUser(createdByUser);
        }

        coverToolDto = CoverToolMapper.INSTANCE.toDtoByCommentList(coverToolDto, coverToolCommentDtoList);

        return coverToolDto;
    }

    public long insertCoverToolComment(CoverToolCommentDto CoverToolCommentDto) {
        return coverToolCommentRepository.save(CoverToolCommentMapper.INSTANCE.toEntity(CoverToolCommentDto)).getIdx();
    }

    @Transactional
    public long updateCoverToolComment(long idx, CoverToolCommentDto CoverToolCommentDto) {
        CoverToolComment persistCoverToolComment = coverToolCommentRepository.getOne(idx);
        CoverToolComment CoverToolComment = CoverToolCommentMapper.INSTANCE.toEntity(CoverToolCommentDto);

        persistCoverToolComment.update(CoverToolComment);

        return coverToolCommentRepository.save(persistCoverToolComment).getIdx();
    }

    public void deleteCoverToolCommentByIdx(long idx) {
        coverToolCommentRepository.deleteById(idx);
    }

    public void deleteAllByCoverToolIdx(long idx) {
        coverToolCommentRepositoryImpl.deleteAllByCoverToolIdx(idx);
    }
}