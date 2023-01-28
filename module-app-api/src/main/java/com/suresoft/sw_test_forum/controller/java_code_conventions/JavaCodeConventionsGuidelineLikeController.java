package com.suresoft.sw_test_forum.controller.java_code_conventions;

import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.service.JavaCodeConventionsGuidelineLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/java-code-conventions-guideline-likes")
public class JavaCodeConventionsGuidelineLikeController {
    private final JavaCodeConventionsGuidelineLikeService javaCodeConventionsGuidelineLikeService;

    public JavaCodeConventionsGuidelineLikeController(JavaCodeConventionsGuidelineLikeService javaCodeConventionsGuidelineLikeService) {
        this.javaCodeConventionsGuidelineLikeService = javaCodeConventionsGuidelineLikeService;
    }

    /**
     * 읽기 페이지에서, 등록
     *
     * @param javaCodeConventionsGuidelineIdx
     * @return
     */
    @PostMapping("/{javaCodeConventionsGuidelineIdx}")
    public ResponseEntity postLike(@PathVariable("javaCodeConventionsGuidelineIdx") long javaCodeConventionsGuidelineIdx) {
        long idx = javaCodeConventionsGuidelineLikeService.insertJavaCodeConventionsGuidelineLike(javaCodeConventionsGuidelineIdx);

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
        javaCodeConventionsGuidelineLikeService.deleteJavaCodeConventionsGuidelineLikeByIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}