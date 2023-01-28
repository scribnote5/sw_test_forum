package com.suresoft.sw_test_forum.controller.cwe;

import com.suresoft.sw_test_forum.cwe.cwe_guideline.service.CweGuidelineLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cwe-guideline-likes")
public class CweGuidelineLikeController {
    private final CweGuidelineLikeService cweGuidelineLikeService;

    public CweGuidelineLikeController(CweGuidelineLikeService cweGuidelineLikeService) {
        this.cweGuidelineLikeService = cweGuidelineLikeService;
    }

    /**
     * 읽기 페이지에서, 등록
     *
     * @param cweGuidelineIdx
     * @return
     */
    @PostMapping("/{cweGuidelineIdx}")
    public ResponseEntity postLike(@PathVariable("cweGuidelineIdx") long cweGuidelineIdx) {
        long idx = cweGuidelineLikeService.insertCweGuidelineLike(cweGuidelineIdx);

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
        cweGuidelineLikeService.deleteCweGuidelineLikeByIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}