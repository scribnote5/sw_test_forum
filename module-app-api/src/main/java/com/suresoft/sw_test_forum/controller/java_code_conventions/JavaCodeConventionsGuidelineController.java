package com.suresoft.sw_test_forum.controller.java_code_conventions;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineSearchDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.service.JavaCodeConventionsGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.service.JavaCodeConventionsGuidelineCommentService;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.service.JavaCodeConventionsGuidelineLikeService;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.service.JavaCodeConventionsGuidelineService;
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
@RequestMapping("/api/java-code-conventions-guidelines")
public class JavaCodeConventionsGuidelineController {
    private final JavaCodeConventionsGuidelineService javaCodeConventionsGuidelineService;
    private final JavaCodeConventionsGuidelineAttachedFileService javaCodeConventionsGuidelineAttachedFileService;
    private final JavaCodeConventionsGuidelineCommentService javaCodeConventionsGuidelineCommentService;
    private final JavaCodeConventionsGuidelineLikeService javaCodeConventionsGuidelineLikeService;

    public JavaCodeConventionsGuidelineController(JavaCodeConventionsGuidelineService javaCodeConventionsGuidelineService,
                                                  JavaCodeConventionsGuidelineAttachedFileService javaCodeConventionsGuidelineAttachedFileService,
                                                  JavaCodeConventionsGuidelineCommentService javaCodeConventionsGuidelineCommentService,
                                                  JavaCodeConventionsGuidelineLikeService javaCodeConventionsGuidelineLikeService) {
        this.javaCodeConventionsGuidelineService = javaCodeConventionsGuidelineService;
        this.javaCodeConventionsGuidelineAttachedFileService = javaCodeConventionsGuidelineAttachedFileService;
        this.javaCodeConventionsGuidelineCommentService = javaCodeConventionsGuidelineCommentService;
        this.javaCodeConventionsGuidelineLikeService = javaCodeConventionsGuidelineLikeService;
    }

    /**
     * 리스트 페이지에서, 리스트 조회
     *
     * @param pageable
     * @param javaCodeConventionsGuidelineSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getJavaCodeConventionsGuidelineList(Pageable pageable, JavaCodeConventionsGuidelineSearchDto javaCodeConventionsGuidelineSearchDto) {
        Page<JavaCodeConventionsGuidelineDto> javaCodeConventionsGuidelineDtoList = javaCodeConventionsGuidelineService.findJavaCodeConventionsGuidelineList(pageable, javaCodeConventionsGuidelineSearchDto);

        return new ResponseEntity(javaCodeConventionsGuidelineDtoList, HttpStatus.OK);
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
    public ResponseEntity getJavaCodeConventionsGuidelineWhenRead(@PathVariable("idx") long idx) {
        JavaCodeConventionsGuidelineDto javaCodeConventionsGuidelineDto = javaCodeConventionsGuidelineService.findJavaCodeConventionsGuidelineByIdx(idx);

        javaCodeConventionsGuidelineDto = javaCodeConventionsGuidelineAttachedFileService.findAttachedFileByJavaCodeConventionsGuidelineIdx(javaCodeConventionsGuidelineDto);
        javaCodeConventionsGuidelineDto = javaCodeConventionsGuidelineCommentService.findAllByJavaCodeConventionsGuidelineIdxOrderByIdxDesc(javaCodeConventionsGuidelineDto);
        javaCodeConventionsGuidelineDto = javaCodeConventionsGuidelineLikeService.findJavaCodeConventionsGuidelineLike(javaCodeConventionsGuidelineDto);

        return new ResponseEntity(javaCodeConventionsGuidelineDto, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getJavaCodeConventionsGuidelineWhenForm(@PathVariable("idx") long idx) {
        JavaCodeConventionsGuidelineDto javaCodeConventionsGuidelineDto = javaCodeConventionsGuidelineService.findJavaCodeConventionsGuidelineByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (javaCodeConventionsGuidelineDto.isAccess()) {
            javaCodeConventionsGuidelineDto = javaCodeConventionsGuidelineAttachedFileService.findAttachedFileByJavaCodeConventionsGuidelineIdx(javaCodeConventionsGuidelineDto);
            javaCodeConventionsGuidelineDto = javaCodeConventionsGuidelineService.findJavaCodeConventionsGuidelineAutoComplete(javaCodeConventionsGuidelineDto);
            responseEntity = new ResponseEntity(javaCodeConventionsGuidelineDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("필요한 권한이 없는 상태에서 접근하여 오류가 발생하였습니다.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param javaCodeConventionsGuidelineDto
     * @return
     */
    @PostMapping
    public ResponseEntity postJavaCodeConventionsGuideline(@RequestBody @Valid JavaCodeConventionsGuidelineDto javaCodeConventionsGuidelineDto) {
        long idx = javaCodeConventionsGuidelineService.insertJavaCodeConventionsGuideline(javaCodeConventionsGuidelineDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param javaCodeConventionsGuidelineDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putJavaCodeConventionsGuideline(@PathVariable("idx") long idx, @RequestBody @Valid JavaCodeConventionsGuidelineDto javaCodeConventionsGuidelineDto) {
        javaCodeConventionsGuidelineService.updateJavaCodeConventionsGuideline(idx, javaCodeConventionsGuidelineDto);

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
    public ResponseEntity deleteJavaCodeConventionsGuideline(@PathVariable("idx") long idx) throws Exception {
        javaCodeConventionsGuidelineService.deleteJavaCodeConventionsGuidelineByIdx(idx);
        javaCodeConventionsGuidelineAttachedFileService.deleteAllAttachedFile(idx);
        javaCodeConventionsGuidelineCommentService.deleteAllByJavaCodeConventionsGuidelineIdx(idx);
        javaCodeConventionsGuidelineLikeService.deleteAllByJavaCodeConventionsGuidelineIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param javaCodeConventionsGuidelineIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long javaCodeConventionsGuidelineIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        javaCodeConventionsGuidelineAttachedFileService.uploadAttachedFile(javaCodeConventionsGuidelineIdx, files);

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
        javaCodeConventionsGuidelineAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}