package com.suresoft.sw_test_forum.controller.fx_cop;

import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.FxCopExampleDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.dto.FxCopExampleSearchDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.service.FxCopExampleCommentService;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_example.service.FxCopExampleService;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/fx-cop-examples")
public class FxCopExampleController {
    private final FxCopExampleService fxCopExampleService;
    private final FxCopExampleCommentService fxCopExampleCommentService;

    public FxCopExampleController(FxCopExampleService fxCopExampleService,
                                  FxCopExampleCommentService fxCopExampleCommentService) {
        this.fxCopExampleService = fxCopExampleService;
        this.fxCopExampleCommentService = fxCopExampleCommentService;
    }

    /**
     * 리스트 페이지에서, 리스트 조회
     *
     * @param pageable
     * @param fxCopExampleSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getFxCopExampleList(Pageable pageable, FxCopExampleSearchDto fxCopExampleSearchDto) {
        Page<FxCopExampleDto> fxCopExampleDtoList = fxCopExampleService.findFxCopExampleList(pageable, fxCopExampleSearchDto);

        return new ResponseEntity(fxCopExampleDtoList, HttpStatus.OK);
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
    public ResponseEntity getFxCopExampleWhenRead(@PathVariable("idx") long idx) {
        FxCopExampleDto fxCopExampleDto = fxCopExampleService.findFxCopExampleByIdx(idx);

        fxCopExampleDto = fxCopExampleCommentService.findAllByFxCopExampleIdxOrderByIdxDesc(fxCopExampleDto);

        return new ResponseEntity(fxCopExampleDto, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, FxCop 규칙 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getFxCopExampleWhenForm(@PathVariable("idx") long idx) {
        FxCopExampleDto fxCopExampleDto = fxCopExampleService.findFxCopExampleByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (fxCopExampleDto.isAccess()) {
            fxCopExampleDto = fxCopExampleService.findFxCopExampleAutoComplete(fxCopExampleDto);
            responseEntity = new ResponseEntity(fxCopExampleDto, HttpStatus.OK);
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
    @GetMapping({"/priority-list-write/{fxCopIdx}"})
    public ResponseEntity getPriorityListWhenWrite(@PathVariable("fxCopIdx") long fxCopIdx) {
        return new ResponseEntity(fxCopExampleService.findPriorityListByHighPriorityAscWhenWrite(fxCopIdx), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}/{fxCopIdx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx, @PathVariable("fxCopIdx") long fxCopIdx) {
        return new ResponseEntity(fxCopExampleService.findPriorityListByHighPriorityAscWhenUpdate(idx, fxCopIdx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param fxCopExampleDto
     * @return
     */
    @PostMapping
    public ResponseEntity postFxCopExample(@RequestBody @Valid FxCopExampleDto fxCopExampleDto) {
        long idx = fxCopExampleService.insertFxCopExample(fxCopExampleDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param fxCopExampleDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putFxCopExample(@PathVariable("idx") long idx, @RequestBody @Valid FxCopExampleDto fxCopExampleDto) {
        fxCopExampleService.updateFxCopExample(idx, fxCopExampleDto);

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
    public ResponseEntity deleteFxCopExample(@PathVariable("idx") long idx) throws Exception {
        fxCopExampleService.deleteFxCopExampleByIdx(idx);
        fxCopExampleCommentService.deleteAllByFxCopExampleIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}