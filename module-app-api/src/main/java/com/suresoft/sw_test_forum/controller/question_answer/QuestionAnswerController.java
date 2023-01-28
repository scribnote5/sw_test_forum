package com.suresoft.sw_test_forum.controller.question_answer;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.question_answer.dto.QuestionAnswerDto;
import com.suresoft.sw_test_forum.question_answer.dto.QuestionAnswerSearchDto;
import com.suresoft.sw_test_forum.question_answer.service.QuestionAnswerAttachedFileService;
import com.suresoft.sw_test_forum.question_answer.service.QuestionAnswerCommentService;
import com.suresoft.sw_test_forum.question_answer.service.QuestionAnswerService;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/question-answers")
public class QuestionAnswerController {
    private final QuestionAnswerService questionAnswerService;
    private final QuestionAnswerAttachedFileService questionAnswerAttachedFileService;
    private final QuestionAnswerCommentService questionAnswerCommentService;

    public QuestionAnswerController(QuestionAnswerService questionAnswerService,
                                    QuestionAnswerAttachedFileService questionAnswerAttachedFileService,
                                    QuestionAnswerCommentService questionAnswerCommentService) {
        this.questionAnswerService = questionAnswerService;
        this.questionAnswerAttachedFileService = questionAnswerAttachedFileService;
        this.questionAnswerCommentService = questionAnswerCommentService;
    }

    /**
     * 리스트 페이지에서, 리스트 조회
     *
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getQuestionAnswerList(Pageable pageable, QuestionAnswerSearchDto questionAnswerSearchDto) {
        Page<QuestionAnswerDto> questionAnswerDtoList = questionAnswerService.findQuestionAnswerList(pageable, questionAnswerSearchDto);

        return new ResponseEntity(questionAnswerDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 접근 권한 조회
     *
     * @return
     */
    @GetMapping("/list-access-authority")
    public ResponseEntity getAccessAuthority() {
        Boolean isAccess = AuthorityUtil.isAccessInRegister();

        return new ResponseEntity(isAccess, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, 현재 게시글 이외 질문과 연관된 게시글 리스트 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/read/{idx}/{groupIdx}"})
    public ResponseEntity getQuestionAnswerListWhenRead(@PathVariable("idx") long idx, @PathVariable("groupIdx") long groupIdx) {
        List<QuestionAnswerDto> questionAnswerDtoList = questionAnswerService.findAllByIdxAndGroupIdx(idx, groupIdx);

        for(QuestionAnswerDto questionAnswerDto: questionAnswerDtoList) {
            questionAnswerDto = questionAnswerAttachedFileService.findAttachedFileByQuestionAnswerIdx(questionAnswerDto);
        }

        return new ResponseEntity(questionAnswerDtoList, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/read/{idx}"})
    public ResponseEntity getQuestionAnswerWhenRead(@PathVariable("idx") long idx) {
        QuestionAnswerDto questionAnswerDto = questionAnswerService.findQuestionAnswerByIdx(idx);

        questionAnswerDto = questionAnswerAttachedFileService.findAttachedFileByQuestionAnswerIdx(questionAnswerDto);
        questionAnswerDto = questionAnswerCommentService.findAllByQuestionAnswerIdxOrderByIdxDesc(questionAnswerDto);

        return new ResponseEntity(questionAnswerDto, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getQuestionAnswerWhenForm(@PathVariable("idx") long idx) {
        QuestionAnswerDto questionAnswerDto = questionAnswerService.findQuestionAnswerByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (questionAnswerDto.isAccess()) {
            questionAnswerDto = questionAnswerAttachedFileService.findAttachedFileByQuestionAnswerIdx(questionAnswerDto);
            questionAnswerDto = questionAnswerService.findQuestionAnswerAutoComplete(questionAnswerDto);
            responseEntity = new ResponseEntity(questionAnswerDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("필요한 권한이 없는 상태에서 접근하여 오류가 발생하였습니다.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }

    /**
     * 질문 등록 페이지에서, 질문 등록
     *
     * @param questionAnswerDto
     * @return
     */
    @PostMapping("/question")
    public ResponseEntity postQuestion(@RequestBody @Valid QuestionAnswerDto questionAnswerDto) {
        long idx = questionAnswerService.insertQuestion(questionAnswerDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 질문 등록 페이지에서, 답변 등록
     *
     * @param questionAnswerDto
     * @return
     */
    @PostMapping("/answer")
    public ResponseEntity postAnswer(@RequestBody @Valid QuestionAnswerDto questionAnswerDto) {
        long idx = questionAnswerService.insertAnswer(questionAnswerDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param questionAnswerDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putQuestionAnswer(@PathVariable("idx") long idx, @RequestBody @Valid QuestionAnswerDto questionAnswerDto) {
        questionAnswerService.updateQuestionAnswer(idx, questionAnswerDto);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     * @throws Exception
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity deleteQuestionAnswer(@PathVariable("idx") long idx) throws Exception {
        questionAnswerService.deleteQuestionAnswerByIdx(idx);
        questionAnswerAttachedFileService.deleteAllAttachedFile(idx);
        questionAnswerCommentService.deleteAllByQuestionAnswerIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param questionAnswerIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long questionAnswerIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        questionAnswerAttachedFileService.uploadAttachedFile(questionAnswerIdx, files);

        return new ResponseEntity("{}", HttpStatus.CREATED);
    }

    /**
     * 삭제 페이지에서, 첨부파일 삭제
     *
     * @param deleteAttachedFileIdxList
     * @return
     * @throws Exception
     */
    @DeleteMapping("/attached-file") // @RequestBody String deleteAttachedFileIdxList
    public ResponseEntity deleteAttachedFile(@RequestBody List<Long> deleteAttachedFileIdxList) throws Exception {
        questionAnswerAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}