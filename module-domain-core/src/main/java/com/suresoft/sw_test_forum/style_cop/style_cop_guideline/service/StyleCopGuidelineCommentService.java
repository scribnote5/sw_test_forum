package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuidelineComment;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineCommentDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.mapper.StyleCopGuidelineCommentMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.mapper.StyleCopGuidelineMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository.StyleCopGuidelineCommentRepository;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository.StyleCopGuidelineCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class StyleCopGuidelineCommentService {
    private final StyleCopGuidelineCommentRepository styleCopGuidelineCommentRepository;
    private final StyleCopGuidelineCommentRepositoryImpl styleCopGuidelineCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public StyleCopGuidelineCommentService(StyleCopGuidelineCommentRepository styleCopGuidelineCommentRepository,
                                           StyleCopGuidelineCommentRepositoryImpl styleCopGuidelineCommentRepositoryImpl,
                                           UserRepositoryImpl userRepositoryImpl) {
        this.styleCopGuidelineCommentRepository = styleCopGuidelineCommentRepository;
        this.styleCopGuidelineCommentRepositoryImpl = styleCopGuidelineCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param styleCopGuidelineDto
     * @return
     */
    public StyleCopGuidelineDto findAllByStyleCopGuidelineIdxOrderByIdxDesc(StyleCopGuidelineDto styleCopGuidelineDto) {
        List<StyleCopGuidelineCommentDto> styleCopGuidelineCommentDtoList = StyleCopGuidelineCommentMapper.INSTANCE.toDto(styleCopGuidelineCommentRepository.findAllByStyleCopGuidelineIdxOrderByIdxDesc(styleCopGuidelineDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (StyleCopGuidelineCommentDto styleCopGuidelineCommentDto : styleCopGuidelineCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(styleCopGuidelineCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            styleCopGuidelineCommentDto.setNewIcon(NewIconCheck.isNew(styleCopGuidelineCommentDto.getCreatedDate()));
            styleCopGuidelineCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            styleCopGuidelineCommentDto.setCreatedByUser(createdByUser);
        }

        styleCopGuidelineDto = StyleCopGuidelineMapper.INSTANCE.toDtoByCommentList(styleCopGuidelineDto, styleCopGuidelineCommentDtoList);

        return styleCopGuidelineDto;
    }

    /**
     * 등록
     *
     * @param StyleCopGuidelineCommentDto
     * @return
     */
    public long insertStyleCopGuidelineComment(StyleCopGuidelineCommentDto StyleCopGuidelineCommentDto) {
        return styleCopGuidelineCommentRepository.save(StyleCopGuidelineCommentMapper.INSTANCE.toEntity(StyleCopGuidelineCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param StyleCopGuidelineCommentDto
     * @return
     */
    @Transactional
    public long updateStyleCopGuidelineComment(long idx, StyleCopGuidelineCommentDto StyleCopGuidelineCommentDto) {
        StyleCopGuidelineComment persistStyleCopGuidelineComment = styleCopGuidelineCommentRepository.getReferenceById(idx);
        StyleCopGuidelineComment StyleCopGuidelineComment = StyleCopGuidelineCommentMapper.INSTANCE.toEntity(StyleCopGuidelineCommentDto);

        persistStyleCopGuidelineComment.update(StyleCopGuidelineComment);

        return styleCopGuidelineCommentRepository.save(persistStyleCopGuidelineComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteStyleCopGuidelineCommentByIdx(long idx) {
        styleCopGuidelineCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param idx
     */
    public void deleteAllByStyleCopGuidelineIdx(long idx) {
        styleCopGuidelineCommentRepositoryImpl.deleteAllByStyleCopGuidelineIdx(idx);
    }
}