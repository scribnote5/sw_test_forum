package com.suresoft.sw_test_forum.trouble_shooting.toolchain.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain.ToolchainComment;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.ToolchainCommentDto;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.ToolchainDto;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.mapper.ToolchainCommentMapper;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.mapper.ToolchainMapper;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.repository.ToolchainCommentRepository;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.repository.ToolchainCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ToolchainCommentService {
    private final ToolchainCommentRepository toolchainCommentRepository;
    private final ToolchainCommentRepositoryImpl toolchainCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public ToolchainCommentService(ToolchainCommentRepository toolchainCommentRepository,
                                   ToolchainCommentRepositoryImpl toolchainCommentRepositoryImpl,
                                   UserRepositoryImpl userRepositoryImpl) {
        this.toolchainCommentRepository = toolchainCommentRepository;
        this.toolchainCommentRepositoryImpl = toolchainCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    public ToolchainDto findAllByToolchainIdxOrderByCreatedDateDesc(ToolchainDto toolchainDto) {
        List<ToolchainCommentDto> toolchainCommentDtoList = ToolchainCommentMapper.INSTANCE.toDto(toolchainCommentRepository.findAllByToolchainIdxOrderByCreatedDateDesc(toolchainDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (ToolchainCommentDto toolchainCommentDto : toolchainCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(toolchainCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            toolchainCommentDto.setNewIcon(NewIconCheck.isNew(toolchainCommentDto.getCreatedDate()));
            toolchainCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            toolchainCommentDto.setCreatedByUser(createdByUser);
        }

        toolchainDto = ToolchainMapper.INSTANCE.toDtoByCommentList(toolchainDto, toolchainCommentDtoList);

        return toolchainDto;
    }

    public long insertToolchainComment(ToolchainCommentDto ToolchainCommentDto) {
        return toolchainCommentRepository.save(ToolchainCommentMapper.INSTANCE.toEntity(ToolchainCommentDto)).getIdx();
    }

    @Transactional
    public long updateToolchainComment(long idx, ToolchainCommentDto ToolchainCommentDto) {
        ToolchainComment persistToolchainComment = toolchainCommentRepository.getOne(idx);
        ToolchainComment ToolchainComment = ToolchainCommentMapper.INSTANCE.toEntity(ToolchainCommentDto);

        persistToolchainComment.update(ToolchainComment);

        return toolchainCommentRepository.save(persistToolchainComment).getIdx();
    }

    public void deleteToolchainCommentByIdx(long idx) {
        toolchainCommentRepository.deleteById(idx);
    }

    public void deleteAllByToolchainIdx(long idx) {
        toolchainCommentRepositoryImpl.deleteAllByToolchainIdx(idx);
    }
}