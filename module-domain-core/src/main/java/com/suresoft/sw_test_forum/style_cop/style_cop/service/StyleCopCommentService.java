package com.suresoft.sw_test_forum.style_cop.style_cop.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.style_cop.style_cop.domain.StyleCopComment;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.StyleCopCommentDto;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.StyleCopDto;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.mapper.StyleCopCommentMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.mapper.StyleCopMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop.repository.StyleCopCommentRepository;
import com.suresoft.sw_test_forum.style_cop.style_cop.repository.StyleCopCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class StyleCopCommentService {
    private final StyleCopCommentRepository styleCopCommentRepository;
    private final StyleCopCommentRepositoryImpl styleCopCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public StyleCopCommentService(StyleCopCommentRepository styleCopCommentRepository,
                                  StyleCopCommentRepositoryImpl styleCopCommentRepositoryImpl,
                                  UserRepositoryImpl userRepositoryImpl) {
        this.styleCopCommentRepository = styleCopCommentRepository;
        this.styleCopCommentRepositoryImpl = styleCopCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param styleCopDto
     * @return
     */
    public StyleCopDto findAllByStyleCopIdxOrderByIdxDesc(StyleCopDto styleCopDto) {
        List<StyleCopCommentDto> styleCopCommentDtoList = StyleCopCommentMapper.INSTANCE.toDto(styleCopCommentRepository.findAllByStyleCopIdxOrderByIdxDesc(styleCopDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (StyleCopCommentDto styleCopCommentDto : styleCopCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(styleCopCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            styleCopCommentDto.setNewIcon(NewIconCheck.isNew(styleCopCommentDto.getCreatedDate()));
            styleCopCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            styleCopCommentDto.setCreatedByUser(createdByUser);
        }

        styleCopDto = StyleCopMapper.INSTANCE.toDtoByCommentList(styleCopDto, styleCopCommentDtoList);

        return styleCopDto;
    }

    /**
     * 등록
     *
     * @param StyleCopCommentDto
     * @return
     */
    public long insertStyleCopComment(StyleCopCommentDto StyleCopCommentDto) {
        return styleCopCommentRepository.save(StyleCopCommentMapper.INSTANCE.toEntity(StyleCopCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param StyleCopCommentDto
     * @return
     */
    @Transactional
    public long updateStyleCopComment(long idx, StyleCopCommentDto StyleCopCommentDto) {
        StyleCopComment persistStyleCopComment = styleCopCommentRepository.getReferenceById(idx);
        StyleCopComment StyleCopComment = StyleCopCommentMapper.INSTANCE.toEntity(StyleCopCommentDto);

        persistStyleCopComment.update(StyleCopComment);

        return styleCopCommentRepository.save(persistStyleCopComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteStyleCopCommentByIdx(long idx) {
        styleCopCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param styleCopIdx
     */
    public void deleteAllByStyleCopIdx(long styleCopIdx) {
        styleCopCommentRepositoryImpl.deleteAllByStyleCopIdx(styleCopIdx);
    }
}