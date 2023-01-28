package com.suresoft.sw_test_forum.controller.cwe_java;

import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.CweJavaExampleDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.dto.CweJavaExampleSearchDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.service.CweJavaExampleCommentService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_example.service.CweJavaExampleService;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/cwe-java-examples")
public class CweJavaExampleController {
    private final CweJavaExampleService cweJavaExampleService;
    private final CweJavaExampleCommentService cweJavaExampleCommentService;

    public CweJavaExampleController(CweJavaExampleService cweJavaExampleService,
                                    CweJavaExampleCommentService cweJavaExampleCommentService) {
        this.cweJavaExampleService = cweJavaExampleService;
        this.cweJavaExampleCommentService = cweJavaExampleCommentService;
    }

    /**
     * 리스트 페이지에서, 리스트 조회
     *
     * @param pageable
     * @param cweJavaExampleSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getCweJavaExampleList(Pageable pageable, CweJavaExampleSearchDto cweJavaExampleSearchDto) {
        Page<CweJavaExampleDto> cweJavaExampleDtoList = cweJavaExampleService.findCweJavaExampleList(pageable, cweJavaExampleSearchDto);

        return new ResponseEntity(cweJavaExampleDtoList, HttpStatus.OK);
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
        CweJavaExampleDto cweJavaExampleDto = cweJavaExampleService.findCweJavaExampleByIdx(idx);

        cweJavaExampleDto = cweJavaExampleCommentService.findAllByCweJavaExampleIdxOrderByIdxDesc(cweJavaExampleDto);

        return new ResponseEntity(cweJavaExampleDto, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, CWE 규칙 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getCweExampleWhenForm(@PathVariable("idx") long idx) {
        CweJavaExampleDto cweJavaExampleDto = cweJavaExampleService.findCweJavaExampleByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (cweJavaExampleDto.isAccess()) {
            cweJavaExampleDto = cweJavaExampleService.findCweJavaExampleAutoComplete(cweJavaExampleDto);
            responseEntity = new ResponseEntity(cweJavaExampleDto, HttpStatus.OK);
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
    @GetMapping({"/priority-list-write/{cweJavaIdx}"})
    public ResponseEntity getPriorityListWhenWrite(@PathVariable("cweJavaIdx") long cweJavaIdx) {
        return new ResponseEntity(cweJavaExampleService.findPriorityListByHighPriorityAscWhenWrite(cweJavaIdx), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}/{cweJavaIdx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx, @PathVariable("cweJavaIdx") long cweJavaIdx) {
        return new ResponseEntity(cweJavaExampleService.findPriorityListByHighPriorityAscWhenUpdate(idx, cweJavaIdx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param cweJavaExampleDto
     * @return
     */
    @PostMapping
    public ResponseEntity postCweExample(@RequestBody @Valid CweJavaExampleDto cweJavaExampleDto) {
        long idx = cweJavaExampleService.insertCweJavaExample(cweJavaExampleDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param cweJavaExampleDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putCweExample(@PathVariable("idx") long idx, @RequestBody @Valid CweJavaExampleDto cweJavaExampleDto) {
        cweJavaExampleService.updateCweJavaExample(idx, cweJavaExampleDto);

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
        cweJavaExampleService.deleteCweJavaExampleByIdx(idx);
        cweJavaExampleCommentService.deleteAllByCweJavaExampleIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}