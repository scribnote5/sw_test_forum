package com.suresoft.sw_test_forum.controller.style_cop;

import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.service.StyleCopGuidelineLikeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/style-cop-guideline-likes")
public class StyleCopGuidelineLikeController {
    private final StyleCopGuidelineLikeService styleCopGuidelineLikeService;

    public StyleCopGuidelineLikeController(StyleCopGuidelineLikeService styleCopGuidelineLikeService) {
        this.styleCopGuidelineLikeService = styleCopGuidelineLikeService;
    }

    /**
     * 읽기 페이지에서, 등록
     *
     * @param styleCopGuidelineIdx
     * @return
     */
    @PostMapping("/{styleCopGuidelineIdx}")
    public ResponseEntity postLike(@PathVariable("styleCopGuidelineIdx") long styleCopGuidelineIdx) {
        long idx = styleCopGuidelineLikeService.insertStyleCopGuidelineLike(styleCopGuidelineIdx);

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
        styleCopGuidelineLikeService.deleteStyleCopGuidelineLikeByIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}