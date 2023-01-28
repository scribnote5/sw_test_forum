package com.suresoft.sw_test_forum.question_answer.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.question_answer.domain.QuestionAnswerComment;
import com.suresoft.sw_test_forum.question_answer.dto.QuestionAnswerCommentDto;
import com.suresoft.sw_test_forum.question_answer.dto.QuestionAnswerDto;
import com.suresoft.sw_test_forum.question_answer.dto.mapper.QuestionAnswerCommentMapper;
import com.suresoft.sw_test_forum.question_answer.dto.mapper.QuestionAnswerMapper;
import com.suresoft.sw_test_forum.question_answer.repository.QuestionAnswerCommentRepository;
import com.suresoft.sw_test_forum.question_answer.repository.QuestionAnswerCommentRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class QuestionAnswerCommentService {
    private final QuestionAnswerCommentRepository questionAnswerCommentRepository;
    private final QuestionAnswerCommentRepositoryImpl questionAnswerCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public QuestionAnswerCommentService(QuestionAnswerCommentRepository questionAnswerCommentRepository, QuestionAnswerCommentRepositoryImpl questionAnswerCommentRepositoryImpl, UserRepositoryImpl userRepositoryImpl) {
        this.questionAnswerCommentRepository = questionAnswerCommentRepository;
        this.questionAnswerCommentRepositoryImpl = questionAnswerCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param questionAnswerDto
     * @return
     */
    public QuestionAnswerDto findAllByQuestionAnswerIdxOrderByIdxDesc(QuestionAnswerDto questionAnswerDto) {
        List<QuestionAnswerCommentDto> questionAnswerCommentDtoList = QuestionAnswerCommentMapper.INSTANCE.toDto(questionAnswerCommentRepository.findAllByQuestionAnswerIdxOrderByIdxDesc(questionAnswerDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (QuestionAnswerCommentDto questionAnswerCommentDto : questionAnswerCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(questionAnswerCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            questionAnswerCommentDto.setNewIcon(NewIconCheck.isNew(questionAnswerCommentDto.getCreatedDate()));
            questionAnswerCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            questionAnswerCommentDto.setCreatedByUser(createdByUser);
        }

        questionAnswerDto = QuestionAnswerMapper.INSTANCE.toDtoByCommentList(questionAnswerDto, questionAnswerCommentDtoList);

        return questionAnswerDto;
    }

    /**
     * 등록
     *
     * @param questionAnswerCommentDto
     * @return
     */
    public long insertQuestionAnswerComment(QuestionAnswerCommentDto questionAnswerCommentDto) {
        return questionAnswerCommentRepository.save(QuestionAnswerCommentMapper.INSTANCE.toEntity(questionAnswerCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param questionAnswerCommentDto
     * @return
     */
    @Transactional
    public long updateQuestionAnswerComment(long idx, QuestionAnswerCommentDto questionAnswerCommentDto) {
        QuestionAnswerComment persistQuestionAnswerComment = questionAnswerCommentRepository.getReferenceById(idx);
        QuestionAnswerComment QuestionAnswerComment = QuestionAnswerCommentMapper.INSTANCE.toEntity(questionAnswerCommentDto);

        persistQuestionAnswerComment.update(QuestionAnswerComment);

        return questionAnswerCommentRepository.save(persistQuestionAnswerComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteQuestionAnswerCommentByIdx(long idx) {
        questionAnswerCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param questionAnswerIdx
     */
    public void deleteAllByQuestionAnswerIdx(long questionAnswerIdx) {
        questionAnswerCommentRepositoryImpl.deleteAllByQuestionAnswerIdx(questionAnswerIdx);
    }
}