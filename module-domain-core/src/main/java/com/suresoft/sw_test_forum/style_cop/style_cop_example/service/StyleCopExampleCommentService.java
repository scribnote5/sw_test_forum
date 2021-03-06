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
     * ????????? ??????
     *
     * @param styleCopExampleDto
     * @return
     */
    public StyleCopExampleDto findAllByStyleCopExampleIdxOrderByIdxDesc(StyleCopExampleDto styleCopExampleDto) {
        List<StyleCopExampleCommentDto> styleCopExampleCommentDtoList = StyleCopExampleCommentMapper.INSTANCE.toDto(styleCopExampleCommentRepository.findAllByStyleCopExampleIdxOrderByIdxDesc(styleCopExampleDto.getIdx()));

        // NewIcon ??????, createdBy ??????
        for (StyleCopExampleCommentDto styleCopExampleCommentDto : styleCopExampleCommentDtoList) {
            // ?????? ??????
            // Update: isAccessInGeneral ???????????? ?????? ?????? ?????? ??? ??????
            // ?????? ????????? ????????? general??? ?????? ??? ????????? ?????????
            User createdByUser = userRepositoryImpl.findByIdx(styleCopExampleCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("?????? ??????")
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
     * ??????
     *
     * @param StyleCopExampleCommentDto
     * @return
     */
    public long insertStyleCopExampleComment(StyleCopExampleCommentDto StyleCopExampleCommentDto) {
        return styleCopExampleCommentRepository.save(StyleCopExampleCommentMapper.INSTANCE.toEntity(StyleCopExampleCommentDto)).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     * @param StyleCopExampleCommentDto
     * @return
     */
    @Transactional
    public long updateStyleCopExampleComment(long idx, StyleCopExampleCommentDto StyleCopExampleCommentDto) {
        StyleCopExampleComment persistStyleCopExampleComment = styleCopExampleCommentRepository.getById(idx);
        StyleCopExampleComment StyleCopExampleComment = StyleCopExampleCommentMapper.INSTANCE.toEntity(StyleCopExampleCommentDto);

        persistStyleCopExampleComment.update(StyleCopExampleComment);

        return styleCopExampleCommentRepository.save(persistStyleCopExampleComment).getIdx();
    }

    /**
     * ??????
     *
     * @param idx
     */
    public void deleteStyleCopExampleCommentByIdx(long idx) {
        styleCopExampleCommentRepository.deleteById(idx);
    }

    /**
     * ?????? ??????
     *
     * @param styleCopExampleIdx
     */
    public void deleteAllByStyleCopExampleIdx(long styleCopExampleIdx) {
        styleCopExampleCommentRepositoryImpl.deleteAllByStyleCopExampleIdx(styleCopExampleIdx);
    }
}