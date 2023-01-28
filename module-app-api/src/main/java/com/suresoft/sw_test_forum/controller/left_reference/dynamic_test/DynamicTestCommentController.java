package com.suresoft.sw_test_forum.controller.left_reference.dynamic_test;

import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.DynamicTestCommentDto;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.service.DynamicTestCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/dynamic-test-comments")
public class DynamicTestCommentController {
    private final DynamicTestCommentService dynamicTestCommentService;

    public DynamicTestCommentController(DynamicTestCommentService dynamicTestCommentService) {
        this.dynamicTestCommentService = dynamicTestCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param dynamicTestCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postDynamicTestComment(@RequestBody @Valid DynamicTestCommentDto dynamicTestCommentDto) {
        long idx = dynamicTestCommentService.insertDynamicTestComment(dynamicTestCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param dynamicTestCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putDynamicTestComment(@PathVariable("idx") long idx, @RequestBody @Valid DynamicTestCommentDto dynamicTestCommentDto) {
        dynamicTestCommentService.updateDynamicTestComment(idx, dynamicTestCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteDynamicTestComment(@PathVariable("idx") long idx) {
        dynamicTestCommentService.deleteDynamicTestCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}