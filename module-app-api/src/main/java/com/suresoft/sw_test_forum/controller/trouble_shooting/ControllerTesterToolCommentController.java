package com.suresoft.sw_test_forum.controller.trouble_shooting;

import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.ControllerTesterToolCommentDto;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.service.ControllerTesterToolCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/controller-tester-tool-comments")
public class ControllerTesterToolCommentController {
    private final ControllerTesterToolCommentService controllerTesterToolCommentService;

    public ControllerTesterToolCommentController(ControllerTesterToolCommentService controllerTesterToolCommentService) {
        this.controllerTesterToolCommentService = controllerTesterToolCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param controllerTesterToolCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postControllerTesterToolComment(@RequestBody @Valid ControllerTesterToolCommentDto controllerTesterToolCommentDto) {
        long idx = controllerTesterToolCommentService.insertControllerTesterToolComment(controllerTesterToolCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param controllerTesterToolCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putControllerTesterToolComment(@PathVariable("idx") long idx, @RequestBody @Valid ControllerTesterToolCommentDto controllerTesterToolCommentDto) {
        controllerTesterToolCommentService.updateControllerTesterToolComment(idx, controllerTesterToolCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteControllerTesterToolComment(@PathVariable("idx") long idx) {
        controllerTesterToolCommentService.deleteControllerTesterToolCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}