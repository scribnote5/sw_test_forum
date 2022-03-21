package com.suresoft.sw_test_forum.trouble_shooting.static_tool.service;

import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain.StaticToolComment;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.StaticToolCommentDto;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.StaticToolDto;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.mapper.StaticToolCommentMapper;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.mapper.StaticToolMapper;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.repository.StaticToolCommentRepository;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.repository.StaticToolCommentRepositoryImpl;
import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class StaticToolCommentService {
    private final StaticToolCommentRepository staticToolCommentRepository;
    private final StaticToolCommentRepositoryImpl staticToolCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public StaticToolCommentService(StaticToolCommentRepository staticToolCommentRepository,
                                    StaticToolCommentRepositoryImpl staticToolCommentRepositoryImpl,
                                    UserRepositoryImpl userRepositoryImpl) {
        this.staticToolCommentRepository = staticToolCommentRepository;
        this.staticToolCommentRepositoryImpl = staticToolCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    public StaticToolDto findAllByStaticToolIdxOrderByCreatedDateDesc(StaticToolDto staticToolDto) {
        List<StaticToolCommentDto> staticToolCommentDtoList = StaticToolCommentMapper.INSTANCE.toDto(staticToolCommentRepository.findAllByStaticToolIdxOrderByCreatedDateDesc(staticToolDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (StaticToolCommentDto staticToolCommentDto : staticToolCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(staticToolCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            staticToolCommentDto.setNewIcon(NewIconCheck.isNew(staticToolCommentDto.getCreatedDate()));
            staticToolCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            staticToolCommentDto.setCreatedByUser(createdByUser);
        }

        staticToolDto = StaticToolMapper.INSTANCE.toDtoByCommentList(staticToolDto, staticToolCommentDtoList);

        return staticToolDto;
    }

    public long insertStaticToolComment(StaticToolCommentDto StaticToolCommentDto) {
        return staticToolCommentRepository.save(StaticToolCommentMapper.INSTANCE.toEntity(StaticToolCommentDto)).getIdx();
    }

    @Transactional
    public long updateStaticToolComment(long idx, StaticToolCommentDto StaticToolCommentDto) {
        StaticToolComment persistStaticToolComment = staticToolCommentRepository.getOne(idx);
        StaticToolComment StaticToolComment = StaticToolCommentMapper.INSTANCE.toEntity(StaticToolCommentDto);

        persistStaticToolComment.update(StaticToolComment);

        return staticToolCommentRepository.save(persistStaticToolComment).getIdx();
    }

    public void deleteStaticToolCommentByIdx(long idx) {
        staticToolCommentRepository.deleteById(idx);
    }

    public void deleteAllByStaticToolIdx(long idx) {
        staticToolCommentRepositoryImpl.deleteAllByStaticToolIdx(idx);
    }
}