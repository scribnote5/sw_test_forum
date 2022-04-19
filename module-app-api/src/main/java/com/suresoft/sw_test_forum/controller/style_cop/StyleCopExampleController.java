package com.suresoft.sw_test_forum.controller.style_cop;

import com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.StyleCopExampleDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.dto.StyleCopExampleSearchDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.service.StyleCopExampleCommentService;
import com.suresoft.sw_test_forum.style_cop.style_cop_example.service.StyleCopExampleService;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/style-cop-examples")
public class StyleCopExampleController {
    private final StyleCopExampleService styleCopExampleService;
    private final StyleCopExampleCommentService styleCopExampleCommentService;

    public StyleCopExampleController(StyleCopExampleService styleCopExampleService,
                                     StyleCopExampleCommentService styleCopExampleCommentService) {
        this.styleCopExampleService = styleCopExampleService;
        this.styleCopExampleCommentService = styleCopExampleCommentService;
    }

    /**
     * 리스트 페이지에서, 리스트 조회
     *
     * @param pageable
     * @param styleCopExampleSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getStyleCopExampleList(Pageable pageable, StyleCopExampleSearchDto styleCopExampleSearchDto) {
        Page<StyleCopExampleDto> styleCopExampleDtoList = styleCopExampleService.findStyleCopExampleList(pageable, styleCopExampleSearchDto);

        return new ResponseEntity(styleCopExampleDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 접근 권한 조회
     *
     * @return
     */
    @GetMapping("/list-access-authority")
    public ResponseEntity getAccessAuthority() {
        Boolean isAccess = AuthorityUtil.isAccessInRegister();

        return new ResponseEntity(isAccess, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/read/{idx}"})
    public ResponseEntity getStyleCopExampleWhenRead(@PathVariable("idx") long idx) {
        StyleCopExampleDto styleCopExampleDto = styleCopExampleService.findStyleCopExampleByIdx(idx);

        styleCopExampleDto = styleCopExampleCommentService.findAllByStyleCopExampleIdxOrderByIdxDesc(styleCopExampleDto);

        return new ResponseEntity(styleCopExampleDto, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, MISRA C 규칙 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getStyleCopExampleWhenForm(@PathVariable("idx") long idx) {
        StyleCopExampleDto styleCopExampleDto = styleCopExampleService.findStyleCopExampleByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (styleCopExampleDto.isAccess()) {
            styleCopExampleDto = styleCopExampleService.findStyleCopExampleAutoComplete(styleCopExampleDto);
            responseEntity = new ResponseEntity(styleCopExampleDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("필요한 권한이 없는 상태에서 접근하여 오류가 발생하였습니다.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }

    /**
     * 등록 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-write/{styleCopIdx}"})
    public ResponseEntity getPriorityListWhenWrite(@PathVariable("styleCopIdx") long styleCopIdx) {
        return new ResponseEntity(styleCopExampleService.findPriorityListByHighPriorityAscWhenWrite(styleCopIdx), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}/{styleCopIdx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx, @PathVariable("styleCopIdx") long styleCopIdx) {
        return new ResponseEntity(styleCopExampleService.findPriorityListByHighPriorityAscWhenUpdate(idx, styleCopIdx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param styleCopExampleDto
     * @return
     */
    @PostMapping
    public ResponseEntity postStyleCopExample(@RequestBody @Valid StyleCopExampleDto styleCopExampleDto) {
        long idx = styleCopExampleService.insertStyleCopExample(styleCopExampleDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param styleCopExampleDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putStyleCopExample(@PathVariable("idx") long idx, @RequestBody @Valid StyleCopExampleDto styleCopExampleDto) {
        styleCopExampleService.updateStyleCopExample(idx, styleCopExampleDto);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     * @throws Exception
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity deleteStyleCopExample(@PathVariable("idx") long idx) throws Exception {
        styleCopExampleService.deleteStyleCopExampleByIdx(idx);
        styleCopExampleCommentService.deleteAllByStyleCopExampleIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}