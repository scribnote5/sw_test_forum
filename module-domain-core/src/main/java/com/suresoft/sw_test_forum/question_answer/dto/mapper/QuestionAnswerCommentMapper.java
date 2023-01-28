package com.suresoft.sw_test_forum.question_answer.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.question_answer.domain.QuestionAnswerComment;
import com.suresoft.sw_test_forum.question_answer.dto.QuestionAnswerCommentDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface QuestionAnswerCommentMapper extends EntityMapper<QuestionAnswerCommentDto, QuestionAnswerComment> {
    QuestionAnswerCommentMapper INSTANCE = Mappers.getMapper(QuestionAnswerCommentMapper.class);
}