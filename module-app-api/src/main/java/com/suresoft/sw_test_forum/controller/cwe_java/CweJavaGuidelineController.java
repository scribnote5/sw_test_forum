package com.suresoft.sw_test_forum.controller.cwe_java;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineSearchDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service.CweJavaGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service.CweJavaGuidelineCommentService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service.CweJavaGuidelineLikeService;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service.CweJavaGuidelineService;
import com.suresoft.sw_test_forum.exception.FileTypeException;
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
@RequestMapping("/api/cwe-java-guidelines")
public class CweJavaGuidelineController {
    private final CweJavaGuidelineService cweJavaGuidelineService;
    private final CweJavaGuidelineAttachedFileService cweJavaGuidelineAttachedFileService;
    private final CweJavaGuidelineCommentService cweJavaGuidelineCommentService;
    private final CweJavaGuidelineLikeService cweJavaGuidelineLikeService;

    public CweJavaGuidelineController(CweJavaGuidelineService cweJavaGuidelineService,
                                      CweJavaGuidelineAttachedFileService cweJavaGuidelineAttachedFileService,
                                      CweJavaGuidelineCommentService cweJavaGuidelineCommentService,
                                      CweJavaGuidelineLikeService cweJavaGuidelineLikeService) {
        this.cweJavaGuidelineService = cweJavaGuidelineService;
        this.cweJavaGuidelineAttachedFileService = cweJavaGuidelineAttachedFileService;
        this.cweJavaGuidelineCommentService = cweJavaGuidelineCommentService;
        this.cweJavaGuidelineLikeService = cweJavaGuidelineLikeService;
    }

    /**
     * ????????? ???????????????, ????????? ??????
     *
     * @param pageable
     * @param cweJavaGuidelineSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getCweJavaGuidelineList(Pageable pageable, CweJavaGuidelineSearchDto cweJavaGuidelineSearchDto) {
        Page<CweJavaGuidelineDto> cweJavaGuidelineDtoList = cweJavaGuidelineService.findCweJavaGuidelineList(pageable, cweJavaGuidelineSearchDto);

        return new ResponseEntity(cweJavaGuidelineDtoList, HttpStatus.OK);
    }

    /**
     * ????????? ???????????????, ?????? ?????? ??????
     *
     * @return
     */
    @GetMapping("/list-access-authority")
    public ResponseEntity getAccessAuthority() {
        Boolean isAccess = AuthorityUtil.isAccessInRegister();

        return new ResponseEntity(isAccess, HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/read/{idx}"})
    public ResponseEntity getCweGuidelineWhenRead(@PathVariable("idx") long idx) {
        CweJavaGuidelineDto cweJavaGuidelineDto = cweJavaGuidelineService.findCweJavaGuidelineByIdx(idx);

        cweJavaGuidelineDto = cweJavaGuidelineAttachedFileService.findAttachedFileByCweJavaGuidelineIdx(cweJavaGuidelineDto);
        cweJavaGuidelineDto = cweJavaGuidelineCommentService.findAllByCweJavaGuidelineIdxOrderByIdxDesc(cweJavaGuidelineDto);
        cweJavaGuidelineDto = cweJavaGuidelineLikeService.findCweJavaGuidelineLike(cweJavaGuidelineDto);

        return new ResponseEntity(cweJavaGuidelineDto, HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getCweGuidelineWhenForm(@PathVariable("idx") long idx) {
        CweJavaGuidelineDto cweJavaGuidelineDto = cweJavaGuidelineService.findCweJavaGuidelineByIdx(idx);
        ResponseEntity responseEntity;

        // ?????? ??????
        if (cweJavaGuidelineDto.isAccess()) {
            cweJavaGuidelineDto = cweJavaGuidelineAttachedFileService.findAttachedFileByCweJavaGuidelineIdx(cweJavaGuidelineDto);
            cweJavaGuidelineDto = cweJavaGuidelineService.findCweJavaGuidelineAutoComplete(cweJavaGuidelineDto);
            responseEntity = new ResponseEntity(cweJavaGuidelineDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("????????? ????????? ?????? ???????????? ???????????? ????????? ?????????????????????.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param cweJavaGuidelineDto
     * @return
     */
    @PostMapping
    public ResponseEntity postCweGuideline(@RequestBody @Valid CweJavaGuidelineDto cweJavaGuidelineDto) {
        long idx = cweJavaGuidelineService.insertCweJavaGuideline(cweJavaGuidelineDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param idx
     * @param cweJavaGuidelineDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putCweGuideline(@PathVariable("idx") long idx, @RequestBody @Valid CweJavaGuidelineDto cweJavaGuidelineDto) {
        cweJavaGuidelineService.updateCweJavaGuideline(idx, cweJavaGuidelineDto);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param idx
     * @return
     * @throws Exception
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity deleteCweGuideline(@PathVariable("idx") long idx) throws Exception {
        cweJavaGuidelineService.deleteCweJavaGuidelineByIdx(idx);
        cweJavaGuidelineAttachedFileService.deleteAllAttachedFile(idx);
        cweJavaGuidelineCommentService.deleteAllByCweJavaGuidelineIdx(idx);
        cweJavaGuidelineLikeService.deleteAllByCweJavaGuidelineIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ???????????? ?????????
     *
     * @param cweJavaGuidelineIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long cweJavaGuidelineIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // ?????? mime type ??????
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        cweJavaGuidelineAttachedFileService.uploadAttachedFile(cweJavaGuidelineIdx, files);

        return new ResponseEntity("{}", HttpStatus.CREATED);
    }

    /**
     * ?????? ???????????????, ???????????? ??????
     *
     * @param deleteAttachedFileIdxList
     * @return
     * @throws Exception
     */
    @DeleteMapping("/attached-file") // @RequestBody String deleteAttachedFileIdxList
    public ResponseEntity deleteAttachedFile(@RequestBody List<Long> deleteAttachedFileIdxList) throws Exception {
        cweJavaGuidelineAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}