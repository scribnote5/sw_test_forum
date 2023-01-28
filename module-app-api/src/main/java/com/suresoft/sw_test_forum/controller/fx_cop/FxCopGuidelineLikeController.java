package com.suresoft.sw_test_forum.controller.fx_cop;

import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.service.FxCopGuidelineLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/fx-cop-guideline-likes")
public class FxCopGuidelineLikeController {
    private final FxCopGuidelineLikeService fxCopGuidelineLikeService;

    public FxCopGuidelineLikeController(FxCopGuidelineLikeService fxCopGuidelineLikeService) {
        this.fxCopGuidelineLikeService = fxCopGuidelineLikeService;
    }

    /**
     * 읽기 페이지에서, 등록
     *
     * @param fxCopGuidelineIdx
     * @return
     */
    @PostMapping("/{fxCopGuidelineIdx}")
    public ResponseEntity postLike(@PathVariable("fxCopGuidelineIdx") long fxCopGuidelineIdx) {
        long idx = fxCopGuidelineLikeService.insertFxCopGuidelineLike(fxCopGuidelineIdx);

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
        fxCopGuidelineLikeService.deleteFxCopGuidelineLikeByIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}