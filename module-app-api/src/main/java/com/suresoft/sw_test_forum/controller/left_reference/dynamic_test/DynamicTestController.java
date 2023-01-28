package com.suresoft.sw_test_forum.controller.left_reference.dynamic_test;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.DynamicTestDto;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.DynamicTestSearchDto;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.service.DynamicTestAttachedFileService;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.service.DynamicTestCommentService;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.service.DynamicTestLikeService;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.service.DynamicTestService;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/dynamic-tests")
public class DynamicTestController {
    private final DynamicTestService dynamicTestService;
    private final DynamicTestAttachedFileService dynamicTestAttachedFileService;
    private final DynamicTestCommentService dynamicTestCommentService;
    private final DynamicTestLikeService dynamicTestLikeService;

    public DynamicTestController(DynamicTestService dynamicTestService,
                                 DynamicTestAttachedFileService dynamicTestAttachedFileService,
                                 DynamicTestCommentService dynamicTestCommentService,
                                 DynamicTestLikeService dynamicTestLikeService) {
        this.dynamicTestService = dynamicTestService;
        this.dynamicTestAttachedFileService = dynamicTestAttachedFileService;
        this.dynamicTestCommentService = dynamicTestCommentService;
        this.dynamicTestLikeService = dynamicTestLikeService;
    }

    /**
     * 리스트 페이지에서, 리스트 조회
     *
     * @param pageable
     * @param dynamicTestSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getDynamicTestList(Pageable pageable, DynamicTestSearchDto dynamicTestSearchDto) {
        Page<DynamicTestDto> dynamicTestDtoList = dynamicTestService.findDynamicTestList(pageable, dynamicTestSearchDto);

        return new ResponseEntity(dynamicTestDtoList, HttpStatus.OK);
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
    public ResponseEntity getDynamicTestWhenRead(@PathVariable("idx") long idx) {
        DynamicTestDto dynamicTestDto = dynamicTestService.findDynamicTestByIdx(idx);

        dynamicTestDto = dynamicTestAttachedFileService.findAttachedFileByDynamicTestIdx(dynamicTestDto);
        dynamicTestDto = dynamicTestCommentService.findAllByDynamicTestIdxOrderByIdxDesc(dynamicTestDto);
        dynamicTestDto = dynamicTestLikeService.findDynamicTestLike(dynamicTestDto);

        return new ResponseEntity(dynamicTestDto, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getDynamicTestWhenForm(@PathVariable("idx") long idx) {
        DynamicTestDto dynamicTestDto = dynamicTestService.findDynamicTestByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (dynamicTestDto.isAccess()) {
            dynamicTestDto = dynamicTestAttachedFileService.findAttachedFileByDynamicTestIdx(dynamicTestDto);
            dynamicTestDto = dynamicTestService.findDynamicTestAutoComplete(dynamicTestDto);
            responseEntity = new ResponseEntity(dynamicTestDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("필요한 권한이 없는 상태에서 접근하여 오류가 발생하였습니다.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param dynamicTestDto
     * @return
     */
    @PostMapping
    public ResponseEntity postDynamicTest(@RequestBody @Valid DynamicTestDto dynamicTestDto) {
        long idx = dynamicTestService.insertDynamicTest(dynamicTestDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param dynamicTestDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putDynamicTest(@PathVariable("idx") long idx, @RequestBody @Valid DynamicTestDto dynamicTestDto) {
        dynamicTestService.updateDynamicTest(idx, dynamicTestDto);

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
    public ResponseEntity deleteDynamicTest(@PathVariable("idx") long idx) throws Exception {
        dynamicTestService.deleteDynamicTestByIdx(idx);
        dynamicTestAttachedFileService.deleteAllAttachedFile(idx);
        dynamicTestCommentService.deleteAllByDynamicTestIdx(idx);
        dynamicTestLikeService.deleteAllByDynamicTestIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param dynamicTestIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long dynamicTestIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        dynamicTestAttachedFileService.uploadAttachedFile(dynamicTestIdx, files);

        return new ResponseEntity("{}", HttpStatus.CREATED);
    }

    /**
     * 삭제 페이지에서, 첨부파일 삭제
     *
     * @param deleteAttachedFileIdxList
     * @return
     * @throws Exception
     */
    @DeleteMapping("/attached-file") // @RequestBody String deleteAttachedFileIdxList
    public ResponseEntity deleteAttachedFile(@RequestBody List<Long> deleteAttachedFileIdxList) throws Exception {
        dynamicTestAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}