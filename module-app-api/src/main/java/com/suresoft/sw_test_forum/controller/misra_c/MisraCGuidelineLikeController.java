package com.suresoft.sw_test_forum.controller.misra_c;

import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.service.MisraCGuidelineLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/misra-c-guideline-likes")
public class MisraCGuidelineLikeController {
    private final MisraCGuidelineLikeService misraCGuidelineLikeService;

    public MisraCGuidelineLikeController(MisraCGuidelineLikeService misraCGuidelineLikeService) {
        this.misraCGuidelineLikeService = misraCGuidelineLikeService;
    }

    /**
     * 읽기 페이지에서, 등록
     *
     * @param misraCGuidelineIdx
     * @return
     */
    @PostMapping("/{misraCGuidelineIdx}")
    public ResponseEntity postLike(@PathVariable("misraCGuidelineIdx") long misraCGuidelineIdx) {
        long idx = misraCGuidelineLikeService.insertMisraCGuidelineLike(misraCGuidelineIdx);

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
        misraCGuidelineLikeService.deleteMisraCGuidelineLikeByIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}