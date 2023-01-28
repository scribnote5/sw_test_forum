package com.suresoft.sw_test_forum.controller.misra_cpp;

import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.MisraCppExampleDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.dto.MisraCppExampleSearchDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.service.MisraCppExampleCommentService;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.service.MisraCppExampleService;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/misra-cpp-examples")
public class MisraCppExampleController {
    private final MisraCppExampleService misraCppExampleService;
    private final MisraCppExampleCommentService misraCppExampleCommentService;

    public MisraCppExampleController(MisraCppExampleService misraCppExampleService,
                                     MisraCppExampleCommentService misraCppExampleCommentService) {
        this.misraCppExampleService = misraCppExampleService;
        this.misraCppExampleCommentService = misraCppExampleCommentService;
    }

    /**
     * 리스트 페이지에서, 리스트 조회
     *
     * @param pageable
     * @param misraCppExampleSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getMisraCppExampleList(Pageable pageable, MisraCppExampleSearchDto misraCppExampleSearchDto) {
        Page<MisraCppExampleDto> misraCppExampleDtoList = misraCppExampleService.findMisraCppExampleList(pageable, misraCppExampleSearchDto);

        return new ResponseEntity(misraCppExampleDtoList, HttpStatus.OK);
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
    public ResponseEntity getMisraCppExampleWhenRead(@PathVariable("idx") long idx) {
        MisraCppExampleDto misraCppExampleDto = misraCppExampleService.findMisraCppExampleByIdx(idx);

        misraCppExampleDto = misraCppExampleCommentService.findAllByMisraCppExampleIdxOrderByIdxDesc(misraCppExampleDto);

        return new ResponseEntity(misraCppExampleDto, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, MISRA CPP 규칙 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getMisraCppExampleWhenForm(@PathVariable("idx") long idx) {
        MisraCppExampleDto misraCppExampleDto = misraCppExampleService.findMisraCppExampleByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (misraCppExampleDto.isAccess()) {
            misraCppExampleDto = misraCppExampleService.findMisraCppExampleAutoComplete(misraCppExampleDto);
            responseEntity = new ResponseEntity(misraCppExampleDto, HttpStatus.OK);
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
    @GetMapping({"/priority-list-write/{misraCppIdx}"})
    public ResponseEntity getPriorityListWhenWrite(@PathVariable("misraCppIdx") long misraCppIdx) {
        return new ResponseEntity(misraCppExampleService.findPriorityListByHighPriorityAscWhenWrite(misraCppIdx), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}/{misraCppIdx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx, @PathVariable("misraCppIdx") long misraCppIdx) {
        return new ResponseEntity(misraCppExampleService.findPriorityListByHighPriorityAscWhenUpdate(idx, misraCppIdx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param misraCppExampleDto
     * @return
     */
    @PostMapping
    public ResponseEntity postMisraCppExample(@RequestBody @Valid MisraCppExampleDto misraCppExampleDto) {
        long idx = misraCppExampleService.insertMisraCppExample(misraCppExampleDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param misraCppExampleDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMisraCppExample(@PathVariable("idx") long idx, @RequestBody @Valid MisraCppExampleDto misraCppExampleDto) {
        misraCppExampleService.updateMisraCppExample(idx, misraCppExampleDto);

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
    public ResponseEntity deleteMisraCppExample(@PathVariable("idx") long idx) throws Exception {
        misraCppExampleService.deleteMisraCppExampleByIdx(idx);
        misraCppExampleCommentService.deleteAllByMisraCppExampleIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}