package com.suresoft.sw_test_forum.controller.cwe_java;

import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service.CweJavaGuidelineLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cwe-java-guideline-likes")
public class CweJavaGuidelineLikeController {
    private final CweJavaGuidelineLikeService cweJavaGuidelineLikeService;

    public CweJavaGuidelineLikeController(CweJavaGuidelineLikeService cweJavaGuidelineLikeService) {
        this.cweJavaGuidelineLikeService = cweJavaGuidelineLikeService;
    }

    /**
     * 읽기 페이지에서, 등록
     *
     * @param cweJavaGuidelineIdx
     * @return
     */
    @PostMapping("/{cweJavaGuidelineIdx}")
    public ResponseEntity postLike(@PathVariable("cweJavaGuidelineIdx") long cweJavaGuidelineIdx) {
        long idx = cweJavaGuidelineLikeService.insertCweJavaGuidelineLike(cweJavaGuidelineIdx);

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
        cweJavaGuidelineLikeService.deleteCweJavaGuidelineLikeByIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}