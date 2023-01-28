package com.suresoft.sw_test_forum.tool.tool_configuration.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.tool.tool_configuration.domain.ToolConfigurationComment;
import com.suresoft.sw_test_forum.tool.tool_configuration.dto.ToolConfigurationCommentDto;
import com.suresoft.sw_test_forum.tool.tool_configuration.dto.ToolConfigurationDto;
import com.suresoft.sw_test_forum.tool.tool_configuration.dto.mapper.ConfigurationCommentMapper;
import com.suresoft.sw_test_forum.tool.tool_configuration.dto.mapper.ConfigurationMapper;
import com.suresoft.sw_test_forum.tool.tool_configuration.repository.ToolConfigurationCommentRepository;
import com.suresoft.sw_test_forum.tool.tool_configuration.repository.ToolConfigurationCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ToolConfigurationCommentService {
    private final ToolConfigurationCommentRepository toolConfigurationCommentRepository;
    private final ToolConfigurationCommentRepositoryImpl toolConfigurationCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public ToolConfigurationCommentService(ToolConfigurationCommentRepository toolConfigurationCommentRepository,
                                           ToolConfigurationCommentRepositoryImpl toolConfigurationCommentRepositoryImpl,
                                           UserRepositoryImpl userRepositoryImpl) {
        this.toolConfigurationCommentRepository = toolConfigurationCommentRepository;
        this.toolConfigurationCommentRepositoryImpl = toolConfigurationCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    public ToolConfigurationDto findAllByToolConfigurationIdxOrderByCreatedDateDesc(ToolConfigurationDto toolConfigurationDto) {
        List<ToolConfigurationCommentDto> toolConfigurationCommentDtoList = ConfigurationCommentMapper.INSTANCE.toDto(toolConfigurationCommentRepository.findAllByToolConfigurationIdxOrderByCreatedDateDesc(toolConfigurationDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (ToolConfigurationCommentDto toolConfigurationCommentDto : toolConfigurationCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(toolConfigurationCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            toolConfigurationCommentDto.setNewIcon(NewIconCheck.isNew(toolConfigurationCommentDto.getCreatedDate()));
            toolConfigurationCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            toolConfigurationCommentDto.setCreatedByUser(createdByUser);
        }

        toolConfigurationDto = ConfigurationMapper.INSTANCE.toDtoByCommentList(toolConfigurationDto, toolConfigurationCommentDtoList);

        return toolConfigurationDto;
    }

    public long insertConfigurationComment(ToolConfigurationCommentDto ToolConfigurationCommentDto) {
        return toolConfigurationCommentRepository.save(ConfigurationCommentMapper.INSTANCE.toEntity(ToolConfigurationCommentDto)).getIdx();
    }

    @Transactional
    public long updateConfigurationComment(long idx, ToolConfigurationCommentDto ToolConfigurationCommentDto) {
        ToolConfigurationComment persistToolConfigurationComment = toolConfigurationCommentRepository.getOne(idx);
        ToolConfigurationComment ToolConfigurationComment = ConfigurationCommentMapper.INSTANCE.toEntity(ToolConfigurationCommentDto);

        persistToolConfigurationComment.update(ToolConfigurationComment);

        return toolConfigurationCommentRepository.save(persistToolConfigurationComment).getIdx();
    }

    public void deleteConfigurationCommentByIdx(long idx) {
        toolConfigurationCommentRepository.deleteById(idx);
    }

    public void deleteAllByConfigurationIdx(long idx) {
        toolConfigurationCommentRepositoryImpl.deleteAllByConfigurationIdx(idx);
    }
}