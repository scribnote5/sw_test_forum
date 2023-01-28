package com.suresoft.sw_test_forum.tool.tool_usage_method.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.tool.tool_usage_method.domain.ToolUsageMethodComment;
import com.suresoft.sw_test_forum.tool.tool_usage_method.dto.ToolUsageMethodCommentDto;
import com.suresoft.sw_test_forum.tool.tool_usage_method.dto.ToolUsageMethodDto;
import com.suresoft.sw_test_forum.tool.tool_usage_method.dto.mapper.ToolUsageMethodCommentMapper;
import com.suresoft.sw_test_forum.tool.tool_usage_method.dto.mapper.ToolUsageMethodMapper;
import com.suresoft.sw_test_forum.tool.tool_usage_method.repository.ToolUsageMethodCommentRepository;
import com.suresoft.sw_test_forum.tool.tool_usage_method.repository.ToolUsageMethodCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ToolUsageMethodCommentService {
    private final ToolUsageMethodCommentRepository toolUsageMethodCommentRepository;
    private final ToolUsageMethodCommentRepositoryImpl toolUsageMethodCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public ToolUsageMethodCommentService(ToolUsageMethodCommentRepository toolUsageMethodCommentRepository,
                                         ToolUsageMethodCommentRepositoryImpl toolUsageMethodCommentRepositoryImpl,
                                         UserRepositoryImpl userRepositoryImpl) {
        this.toolUsageMethodCommentRepository = toolUsageMethodCommentRepository;
        this.toolUsageMethodCommentRepositoryImpl = toolUsageMethodCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    public ToolUsageMethodDto findAllByToolUsageMethodIdxOrderByCreatedDateDesc(ToolUsageMethodDto toolUsageMethodDto) {
        List<ToolUsageMethodCommentDto> toolUsageMethodCommentDtoList = ToolUsageMethodCommentMapper.INSTANCE.toDto(toolUsageMethodCommentRepository.findAllByToolUsageMethodIdxOrderByCreatedDateDesc(toolUsageMethodDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (ToolUsageMethodCommentDto toolUsageMethodCommentDto : toolUsageMethodCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(toolUsageMethodCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            toolUsageMethodCommentDto.setNewIcon(NewIconCheck.isNew(toolUsageMethodCommentDto.getCreatedDate()));
            toolUsageMethodCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            toolUsageMethodCommentDto.setCreatedByUser(createdByUser);
        }

        toolUsageMethodDto = ToolUsageMethodMapper.INSTANCE.toDtoByCommentList(toolUsageMethodDto, toolUsageMethodCommentDtoList);

        return toolUsageMethodDto;
    }

    public long insertToolUsageMethodComment(ToolUsageMethodCommentDto ToolUsageMethodCommentDto) {
        return toolUsageMethodCommentRepository.save(ToolUsageMethodCommentMapper.INSTANCE.toEntity(ToolUsageMethodCommentDto)).getIdx();
    }

    @Transactional
    public long updateToolUsageMethodComment(long idx, ToolUsageMethodCommentDto ToolUsageMethodCommentDto) {
        ToolUsageMethodComment persistToolUsageMethodComment = toolUsageMethodCommentRepository.getOne(idx);
        ToolUsageMethodComment ToolUsageMethodComment = ToolUsageMethodCommentMapper.INSTANCE.toEntity(ToolUsageMethodCommentDto);

        persistToolUsageMethodComment.update(ToolUsageMethodComment);

        return toolUsageMethodCommentRepository.save(persistToolUsageMethodComment).getIdx();
    }

    public void deleteToolUsageMethodCommentByIdx(long idx) {
        toolUsageMethodCommentRepository.deleteById(idx);
    }

    public void deleteAllByToolUsageMethodIdx(long idx) {
        toolUsageMethodCommentRepositoryImpl.deleteAllByToolUsageMethodIdx(idx);
    }
}