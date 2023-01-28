package com.suresoft.sw_test_forum.controller.left_reference.dynamic_test;

import com.suresoft.sw_test_forum.left_reference.dynamic_test.service.DynamicTestLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dynamic-test-likes")
public class DynamicTestLikeController {
    private final DynamicTestLikeService dynamicTestLikeService;

    public DynamicTestLikeController(DynamicTestLikeService dynamicTestLikeService) {
        this.dynamicTestLikeService = dynamicTestLikeService;
    }

    /**
     * 읽기 페이지에서, 등록
     *
     * @param dynamicTestIdx
     * @return
     */
    @PostMapping("/{dynamicTestIdx}")
    public ResponseEntity postLike(@PathVariable("dynamicTestIdx") long dynamicTestIdx) {
        long idx = dynamicTestLikeService.insertDynamicTestLike(dynamicTestIdx);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 읽기 페이지에서, 삭제
     *
     * @param idx
     * @return
     * @throws Exception
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity deleteLike(@PathVariable("idx") long idx) throws Exception {
        dynamicTestLikeService.deleteDynamicTestLikeByIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}