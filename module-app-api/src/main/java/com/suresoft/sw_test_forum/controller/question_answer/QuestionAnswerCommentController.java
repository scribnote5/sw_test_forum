package com.suresoft.sw_test_forum.controller.question_answer;

import com.suresoft.sw_test_forum.question_answer.dto.QuestionAnswerCommentDto;
import com.suresoft.sw_test_forum.question_answer.service.QuestionAnswerCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/question-answer-comments")
public class QuestionAnswerCommentController {
    private final QuestionAnswerCommentService questionAnswerCommentService;

    public QuestionAnswerCommentController(QuestionAnswerCommentService questionAnswerCommentService) {
        this.questionAnswerCommentService = questionAnswerCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param questionAnswerCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postQuestionAnswerComment(@RequestBody @Valid QuestionAnswerCommentDto questionAnswerCommentDto) {
        long idx = questionAnswerCommentService.insertQuestionAnswerComment(questionAnswerCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param questionAnswerCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putQuestionAnswerComment(@PathVariable("idx") long idx, @RequestBody @Valid QuestionAnswerCommentDto questionAnswerCommentDto) {
        questionAnswerCommentService.updateQuestionAnswerComment(idx, questionAnswerCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteQuestionAnswerComment(@PathVariable("idx") long idx) {
        questionAnswerCommentService.deleteQuestionAnswerCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}