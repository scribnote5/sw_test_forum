package com.suresoft.sw_test_forum.style_cop.style_cop_example.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.domain.StyleCopExampleComment;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.StyleCopExampleCommentDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.StyleCopExampleDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.mapper.StyleCopExampleCommentMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.mapper.StyleCopExampleMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.repository.StyleCopExampleCommentRepository;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.repository.StyleCopExampleCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class StyleCopExampleCommentService {
    private final StyleCopExampleCommentRepository styleCopExampleCommentRepository;
    private final StyleCopExampleCommentRepositoryImpl styleCopExampleCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public StyleCopExampleCommentService(StyleCopExampleCommentRepository styleCopExampleCommentRepository,
                                       StyleCopExampleCommentRepositoryImpl styleCopExampleCommentRepositoryImpl,
                                       UserRepositoryImpl userRepositoryImpl) {
        this.styleCopExampleCommentRepository = styleCopExampleCommentRepository;
        this.styleCopExampleCommentRepositoryImpl = styleCopExampleCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param styleCopExampleDto
     * @return
     */
    public StyleCopExampleDto findAllByStyleCopExampleIdxOrderByIdxDesc(StyleCopExampleDto styleCopExampleDto) {
        List<StyleCopExampleCommentDto> styleCopExampleCommentDtoList = StyleCopExampleCommentMapper.INSTANCE.toDto(styleCopExampleCommentRepository.findAllByStyleCopExampleIdxOrderByIdxDesc(styleCopExampleDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (StyleCopExampleCommentDto styleCopExampleCommentDto : styleCopExampleCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(styleCopExampleCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            styleCopExampleCommentDto.setNewIcon(NewIconCheck.isNew(styleCopExampleCommentDto.getCreatedDate()));
            styleCopExampleCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            styleCopExampleCommentDto.setCreatedByUser(createdByUser);
        }

        styleCopExampleDto = StyleCopExampleMapper.INSTANCE.toDtoByCommentList(styleCopExampleDto, styleCopExampleCommentDtoList);

        return styleCopExampleDto;
    }

    /**
     * 등록
     *
     * @param StyleCopExampleCommentDto
     * @return
     */
    public long insertStyleCopExampleComment(StyleCopExampleCommentDto StyleCopExampleCommentDto) {
        return styleCopExampleCommentRepository.save(StyleCopExampleCommentMapper.INSTANCE.toEntity(StyleCopExampleCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param StyleCopExampleCommentDto
     * @return
     */
    @Transactional
    public long updateStyleCopExampleComment(long idx, StyleCopExampleCommentDto StyleCopExampleCommentDto) {
        StyleCopExampleComment persistStyleCopExampleComment = styleCopExampleCommentRepository.getReferenceById(idx);
        StyleCopExampleComment StyleCopExampleComment = StyleCopExampleCommentMapper.INSTANCE.toEntity(StyleCopExampleCommentDto);

        persistStyleCopExampleComment.update(StyleCopExampleComment);

        return styleCopExampleCommentRepository.save(persistStyleCopExampleComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteStyleCopExampleCommentByIdx(long idx) {
        styleCopExampleCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param styleCopExampleIdx
     */
    public void deleteAllByStyleCopExampleIdx(long styleCopExampleIdx) {
        styleCopExampleCommentRepositoryImpl.deleteAllByStyleCopExampleIdx(styleCopExampleIdx);
    }
}