package com.suresoft.sw_test_forum.controller.cwe;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineSearchDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.service.CweGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.service.CweGuidelineCommentService;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.service.CweGuidelineLikeService;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.service.CweGuidelineService;
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
@RequestMapping("/api/cwe-guidelines")
public class CweGuidelineController {
    private final CweGuidelineService cweGuidelineService;
    private final CweGuidelineAttachedFileService cweGuidelineAttachedFileService;
    private final CweGuidelineCommentService cweGuidelineCommentService;
    private final CweGuidelineLikeService cweGuidelineLikeService;

    public CweGuidelineController(CweGuidelineService cweGuidelineService,
                                  CweGuidelineAttachedFileService cweGuidelineAttachedFileService,
                                  CweGuidelineCommentService cweGuidelineCommentService,
                                  CweGuidelineLikeService cweGuidelineLikeService) {
        this.cweGuidelineService = cweGuidelineService;
        this.cweGuidelineAttachedFileService = cweGuidelineAttachedFileService;
        this.cweGuidelineCommentService = cweGuidelineCommentService;
        this.cweGuidelineLikeService = cweGuidelineLikeService;
    }

    /**
     * ????????? ???????????????, ????????? ??????
     *
     * @param pageable
     * @param cweGuidelineSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getCweGuidelineList(Pageable pageable, CweGuidelineSearchDto cweGuidelineSearchDto) {
        Page<CweGuidelineDto> cweGuidelineDtoList = cweGuidelineService.findCweGuidelineList(pageable, cweGuidelineSearchDto);

        return new ResponseEntity(cweGuidelineDtoList, HttpStatus.OK);
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
        CweGuidelineDto cweGuidelineDto = cweGuidelineService.findCweGuidelineByIdx(idx);

        cweGuidelineDto = cweGuidelineAttachedFileService.findAttachedFileByCweGuidelineIdx(cweGuidelineDto);
        cweGuidelineDto = cweGuidelineCommentService.findAllByCweGuidelineIdxOrderByIdxDesc(cweGuidelineDto);
        cweGuidelineDto = cweGuidelineLikeService.findCweGuidelineLike(cweGuidelineDto);

        return new ResponseEntity(cweGuidelineDto, HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getCweGuidelineWhenForm(@PathVariable("idx") long idx) {
        CweGuidelineDto cweGuidelineDto = cweGuidelineService.findCweGuidelineByIdx(idx);
        ResponseEntity responseEntity;

        // ?????? ??????
        if (cweGuidelineDto.isAccess()) {
            cweGuidelineDto = cweGuidelineAttachedFileService.findAttachedFileByCweGuidelineIdx(cweGuidelineDto);
            cweGuidelineDto = cweGuidelineService.findCweGuidelineAutoComplete(cweGuidelineDto);
            responseEntity = new ResponseEntity(cweGuidelineDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("????????? ????????? ?????? ???????????? ???????????? ????????? ?????????????????????.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param cweGuidelineDto
     * @return
     */
    @PostMapping
    public ResponseEntity postCweGuideline(@RequestBody @Valid CweGuidelineDto cweGuidelineDto) {
        long idx = cweGuidelineService.insertCweGuideline(cweGuidelineDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param idx
     * @param cweGuidelineDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putCweGuideline(@PathVariable("idx") long idx, @RequestBody @Valid CweGuidelineDto cweGuidelineDto) {
        cweGuidelineService.updateCweGuideline(idx, cweGuidelineDto);

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
        cweGuidelineService.deleteCweGuidelineByIdx(idx);
        cweGuidelineAttachedFileService.deleteAllAttachedFile(idx);
        cweGuidelineCommentService.deleteAllByCweGuidelineIdx(idx);
        cweGuidelineLikeService.deleteAllByCweGuidelineIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ???????????? ?????????
     *
     * @param cweGuidelineIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long cweGuidelineIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // ?????? mime type ??????
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        cweGuidelineAttachedFileService.uploadAttachedFile(cweGuidelineIdx, files);

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
        cweGuidelineAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}