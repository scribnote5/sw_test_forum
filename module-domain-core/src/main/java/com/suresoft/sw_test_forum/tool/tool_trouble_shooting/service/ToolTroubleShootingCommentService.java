package com.suresoft.sw_test_forum.tool.tool_trouble_shooting.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain.ToolTroubleShootingComment;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.ToolTroubleShootingCommentDto;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.ToolTroubleShootingDto;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.mapper.ToolTroubleShootingCommentMapper;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.mapper.ToolTroubleShootingMapper;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.repository.ToolTroubleShootingCommentRepository;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.repository.ToolTroubleShootingCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ToolTroubleShootingCommentService {
    private final ToolTroubleShootingCommentRepository toolTroubleShootingCommentRepository;
    private final ToolTroubleShootingCommentRepositoryImpl toolTroubleShootingCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public ToolTroubleShootingCommentService(ToolTroubleShootingCommentRepository toolTroubleShootingCommentRepository,
                                             ToolTroubleShootingCommentRepositoryImpl toolTroubleShootingCommentRepositoryImpl,
                                             UserRepositoryImpl userRepositoryImpl) {
        this.toolTroubleShootingCommentRepository = toolTroubleShootingCommentRepository;
        this.toolTroubleShootingCommentRepositoryImpl = toolTroubleShootingCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    public ToolTroubleShootingDto findAllByToolTroubleShootingIdxOrderByCreatedDateDesc(ToolTroubleShootingDto toolTroubleShootingDto) {
        List<ToolTroubleShootingCommentDto> toolTroubleShootingCommentDtoList = ToolTroubleShootingCommentMapper.INSTANCE.toDto(toolTroubleShootingCommentRepository.findAllByToolTroubleShootingIdxOrderByCreatedDateDesc(toolTroubleShootingDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (ToolTroubleShootingCommentDto toolTroubleShootingCommentDto : toolTroubleShootingCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(toolTroubleShootingCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            toolTroubleShootingCommentDto.setNewIcon(NewIconCheck.isNew(toolTroubleShootingCommentDto.getCreatedDate()));
            toolTroubleShootingCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            toolTroubleShootingCommentDto.setCreatedByUser(createdByUser);
        }

        toolTroubleShootingDto = ToolTroubleShootingMapper.INSTANCE.toDtoByCommentList(toolTroubleShootingDto, toolTroubleShootingCommentDtoList);

        return toolTroubleShootingDto;
    }

    public long insertToolTroubleShootingComment(ToolTroubleShootingCommentDto ToolTroubleShootingCommentDto) {
        return toolTroubleShootingCommentRepository.save(ToolTroubleShootingCommentMapper.INSTANCE.toEntity(ToolTroubleShootingCommentDto)).getIdx();
    }

    @Transactional
    public long updateToolTroubleShootingComment(long idx, ToolTroubleShootingCommentDto ToolTroubleShootingCommentDto) {
        ToolTroubleShootingComment persistToolTroubleShootingComment = toolTroubleShootingCommentRepository.getOne(idx);
        ToolTroubleShootingComment ToolTroubleShootingComment = ToolTroubleShootingCommentMapper.INSTANCE.toEntity(ToolTroubleShootingCommentDto);

        persistToolTroubleShootingComment.update(ToolTroubleShootingComment);

        return toolTroubleShootingCommentRepository.save(persistToolTroubleShootingComment).getIdx();
    }

    public void deleteTroubleShootingCommentByIdx(long idx) {
        toolTroubleShootingCommentRepository.deleteById(idx);
    }

    public void deleteAllByToolTroubleShootingIdx(long idx) {
        toolTroubleShootingCommentRepositoryImpl.deleteAllByToolTroubleShootingIdx(idx);
    }
}