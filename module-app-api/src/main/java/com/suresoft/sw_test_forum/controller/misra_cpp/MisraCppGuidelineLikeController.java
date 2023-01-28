package com.suresoft.sw_test_forum.controller.misra_cpp;

import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.service.MisraCppGuidelineLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/misra-cpp-guideline-likes")
public class MisraCppGuidelineLikeController {
    private final MisraCppGuidelineLikeService misraCppGuidelineLikeService;

    public MisraCppGuidelineLikeController(MisraCppGuidelineLikeService misraCppGuidelineLikeService) {
        this.misraCppGuidelineLikeService = misraCppGuidelineLikeService;
    }

    /**
     * 읽기 페이지에서, 등록
     *
     * @param misraCppGuidelineIdx
     * @return
     */
    @PostMapping("/{misraCppGuidelineIdx}")
    public ResponseEntity postLike(@PathVariable("misraCppGuidelineIdx") long misraCppGuidelineIdx) {
        long idx = misraCppGuidelineLikeService.insertMisraCppGuidelineLike(misraCppGuidelineIdx);

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
        misraCppGuidelineLikeService.deleteMisraCppGuidelineLikeByIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}