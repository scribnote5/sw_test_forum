package com.suresoft.sw_test_forum.question_answer.dto.mapper;

import com.suresoft.sw_test_forum.common.dto.mapper.EntityMapper;
import com.suresoft.sw_test_forum.question_answer.domain.QuestionAnswer;
import com.suresoft.sw_test_forum.question_answer.domain.QuestionAnswerAttachedFile;
import com.suresoft.sw_test_forum.question_answer.dto.QuestionAnswerCommentDto;
import com.suresoft.sw_test_forum.question_answer.dto.QuestionAnswerDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface QuestionAnswerMapper extends EntityMapper<QuestionAnswerDto, QuestionAnswer> {
    QuestionAnswerMapper INSTANCE = Mappers.getMapper(QuestionAnswerMapper.class);

    default QuestionAnswerDto toDtoByAttachedFileList(QuestionAnswerDto questionAnswerDto, List<QuestionAnswerAttachedFile> attachedFileList) {
        for (QuestionAnswerAttachedFile attachedFile : attachedFileList) {
            questionAnswerDto.getAttachedFileList().add(attachedFile);
        }

        return questionAnswerDto;
    }

    default QuestionAnswerDto toDtoByCommentList(QuestionAnswerDto questionAnswerDto, List<QuestionAnswerCommentDto> commentDtoList) {
        for (QuestionAnswerCommentDto questionAnswerCommentDto : commentDtoList) {
            questionAnswerDto.getCommentDtoList().add(questionAnswerCommentDto);
        }

        return questionAnswerDto;
    }
}