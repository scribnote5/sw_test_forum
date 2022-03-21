package com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain.ControllerTesterToolComment;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.ControllerTesterToolCommentDto;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.ControllerTesterToolDto;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.mapper.ControllerTesterToolCommentMapper;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.mapper.ControllerTesterToolMapper;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.repository.ControllerTesterToolCommentRepository;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.repository.ControllerTesterToolCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class ControllerTesterToolCommentService {
    private final ControllerTesterToolCommentRepository controllerTesterToolCommentRepository;
    private final ControllerTesterToolCommentRepositoryImpl controllerTesterToolCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public ControllerTesterToolCommentService(ControllerTesterToolCommentRepository controllerTesterToolCommentRepository,
                                   ControllerTesterToolCommentRepositoryImpl controllerTesterToolCommentRepositoryImpl,
                                   UserRepositoryImpl userRepositoryImpl) {
        this.controllerTesterToolCommentRepository = controllerTesterToolCommentRepository;
        this.controllerTesterToolCommentRepositoryImpl = controllerTesterToolCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    public ControllerTesterToolDto findAllByControllerTesterToolIdxOrderByCreatedDateDesc(ControllerTesterToolDto controllerTesterToolDto) {
        List<ControllerTesterToolCommentDto> controllerTesterToolCommentDtoList = ControllerTesterToolCommentMapper.INSTANCE.toDto(controllerTesterToolCommentRepository.findAllByControllerTesterToolIdxOrderByCreatedDateDesc(controllerTesterToolDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (ControllerTesterToolCommentDto controllerTesterToolCommentDto : controllerTesterToolCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(controllerTesterToolCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            controllerTesterToolCommentDto.setNewIcon(NewIconCheck.isNew(controllerTesterToolCommentDto.getCreatedDate()));
            controllerTesterToolCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            controllerTesterToolCommentDto.setCreatedByUser(createdByUser);
        }

        controllerTesterToolDto = ControllerTesterToolMapper.INSTANCE.toDtoByCommentList(controllerTesterToolDto, controllerTesterToolCommentDtoList);

        return controllerTesterToolDto;
    }

    public long insertControllerTesterToolComment(ControllerTesterToolCommentDto ControllerTesterToolCommentDto) {
        return controllerTesterToolCommentRepository.save(ControllerTesterToolCommentMapper.INSTANCE.toEntity(ControllerTesterToolCommentDto)).getIdx();
    }

    @Transactional
    public long updateControllerTesterToolComment(long idx, ControllerTesterToolCommentDto ControllerTesterToolCommentDto) {
        ControllerTesterToolComment persistControllerTesterToolComment = controllerTesterToolCommentRepository.getOne(idx);
        ControllerTesterToolComment ControllerTesterToolComment = ControllerTesterToolCommentMapper.INSTANCE.toEntity(ControllerTesterToolCommentDto);

        persistControllerTesterToolComment.update(ControllerTesterToolComment);

        return controllerTesterToolCommentRepository.save(persistControllerTesterToolComment).getIdx();
    }

    public void deleteControllerTesterToolCommentByIdx(long idx) {
        controllerTesterToolCommentRepository.deleteById(idx);
    }

    public void deleteAllByControllerTesterToolIdx(long idx) {
        controllerTesterToolCommentRepositoryImpl.deleteAllByControllerTesterToolIdx(idx);
    }
}