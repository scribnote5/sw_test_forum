package com.suresoft.sw_test_forum.controller.misra_c;

import com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.MisraCExampleDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.dto.MisraCExampleSearchDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.service.MisraCExampleCommentService;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.service.MisraCExampleService;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/misra-c-examples")
public class MisraCExampleController {
    private final MisraCExampleService misraCExampleService;
    private final MisraCExampleCommentService misraCExampleCommentService;

    public MisraCExampleController(MisraCExampleService misraCExampleService,
                                   MisraCExampleCommentService misraCExampleCommentService) {
        this.misraCExampleService = misraCExampleService;
        this.misraCExampleCommentService = misraCExampleCommentService;
    }

    /**
     * 리스트 페이지에서, 리스트 조회
     *
     * @param pageable
     * @param misraCExampleSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getMisraCExampleList(Pageable pageable, MisraCExampleSearchDto misraCExampleSearchDto) {
        Page<MisraCExampleDto> misraCExampleDtoList = misraCExampleService.findMisraCExampleList(pageable, misraCExampleSearchDto);

        return new ResponseEntity(misraCExampleDtoList, HttpStatus.OK);
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
    public ResponseEntity getMisraCExampleWhenRead(@PathVariable("idx") long idx) {
        MisraCExampleDto misraCExampleDto = misraCExampleService.findMisraCExampleByIdx(idx);

        misraCExampleDto = misraCExampleCommentService.findAllByMisraCExampleIdxOrderByIdxDesc(misraCExampleDto);

        return new ResponseEntity(misraCExampleDto, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, MISRA C 규칙 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getMisraCExampleWhenForm(@PathVariable("idx") long idx) {
        MisraCExampleDto misraCExampleDto = misraCExampleService.findMisraCExampleByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (misraCExampleDto.isAccess()) {
            misraCExampleDto = misraCExampleService.findMisraCExampleAutoComplete(misraCExampleDto);
            responseEntity = new ResponseEntity(misraCExampleDto, HttpStatus.OK);
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
    @GetMapping({"/priority-list-write/{misraCIdx}"})
    public ResponseEntity getPriorityListWhenWrite(@PathVariable("misraCIdx") long misraCIdx) {
        return new ResponseEntity(misraCExampleService.findPriorityListByHighPriorityAscWhenWrite(misraCIdx), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}/{misraCIdx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx, @PathVariable("misraCIdx") long misraCIdx) {
        return new ResponseEntity(misraCExampleService.findPriorityListByHighPriorityAscWhenUpdate(idx, misraCIdx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param misraCExampleDto
     * @return
     */
    @PostMapping
    public ResponseEntity postMisraCExample(@RequestBody @Valid MisraCExampleDto misraCExampleDto) {
        long idx = misraCExampleService.insertMisraCExample(misraCExampleDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param misraCExampleDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMisraCExample(@PathVariable("idx") long idx, @RequestBody @Valid MisraCExampleDto misraCExampleDto) {
        misraCExampleService.updateMisraCExample(idx, misraCExampleDto);

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
    public ResponseEntity deleteMisraCExample(@PathVariable("idx") long idx) throws Exception {
        misraCExampleService.deleteMisraCExampleByIdx(idx);
        misraCExampleCommentService.deleteAllByMisraCExampleIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}