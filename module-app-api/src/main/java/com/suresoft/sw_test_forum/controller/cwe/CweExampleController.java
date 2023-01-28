package com.suresoft.sw_test_forum.controller.cwe;

import com.suresoft.sw_test_forum.cwe.cwe_example.dto.CweExampleDto;
import com.suresoft.sw_test_forum.cwe.cwe_example.dto.CweExampleSearchDto;
import com.suresoft.sw_test_forum.cwe.cwe_example.service.CweExampleCommentService;
import com.suresoft.sw_test_forum.cwe.cwe_example.service.CweExampleService;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cwe-examples")
public class CweExampleController {
    private final CweExampleService cweExampleService;
    private final CweExampleCommentService cweExampleCommentService;

    public CweExampleController(CweExampleService cweExampleService,
                                CweExampleCommentService cweExampleCommentService) {
        this.cweExampleService = cweExampleService;
        this.cweExampleCommentService = cweExampleCommentService;
    }

    /**
     * 리스트 페이지에서, 리스트 조회
     *
     * @param pageable
     * @param cweExampleSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getCweExampleList(Pageable pageable, CweExampleSearchDto cweExampleSearchDto) {
        Page<CweExampleDto> cweExampleDtoList = cweExampleService.findCweExampleList(pageable, cweExampleSearchDto);

        return new ResponseEntity(cweExampleDtoList, HttpStatus.OK);
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
    public ResponseEntity getCweExampleWhenRead(@PathVariable("idx") long idx) {
        CweExampleDto cweExampleDto = cweExampleService.findCweExampleByIdx(idx);

        cweExampleDto = cweExampleCommentService.findAllByCweExampleIdxOrderByIdxDesc(cweExampleDto);

        return new ResponseEntity(cweExampleDto, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, CWE 규칙 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getCweExampleWhenForm(@PathVariable("idx") long idx) {
        CweExampleDto cweExampleDto = cweExampleService.findCweExampleByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (cweExampleDto.isAccess()) {
            cweExampleDto = cweExampleService.findCweExampleAutoComplete(cweExampleDto);
            responseEntity = new ResponseEntity(cweExampleDto, HttpStatus.OK);
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
    @GetMapping({"/priority-list-write/{cweIdx}"})
    public ResponseEntity getPriorityListWhenWrite(@PathVariable("cweIdx") long cweIdx) {
        return new ResponseEntity(cweExampleService.findPriorityListByHighPriorityAscWhenWrite(cweIdx), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}/{cweIdx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx, @PathVariable("cweIdx") long cweIdx) {
        return new ResponseEntity(cweExampleService.findPriorityListByHighPriorityAscWhenUpdate(idx, cweIdx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param cweExampleDto
     * @return
     */
    @PostMapping
    public ResponseEntity postCweExample(@RequestBody @Valid CweExampleDto cweExampleDto) {
        long idx = cweExampleService.insertCweExample(cweExampleDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param cweExampleDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putCweExample(@PathVariable("idx") long idx, @RequestBody @Valid CweExampleDto cweExampleDto) {
        cweExampleService.updateCweExample(idx, cweExampleDto);

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
    public ResponseEntity deleteCweExample(@PathVariable("idx") long idx) throws Exception {
        cweExampleService.deleteCweExampleByIdx(idx);
        cweExampleCommentService.deleteAllByCweExampleIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}