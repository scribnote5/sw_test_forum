package com.suresoft.sw_test_forum.controller.trouble_shooting;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.CoverToolDto;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.CoverToolSearchDto;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.service.CoverToolAttachedFileService;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.service.CoverToolCommentService;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.service.CoverToolService;
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
@RequestMapping("/api/cover-tools")
public class CoverToolController {
    private final CoverToolService coverToolService;
    private final CoverToolAttachedFileService coverToolAttachedFileService;
    private final CoverToolCommentService coverToolCommentService;

    public CoverToolController(CoverToolService coverToolService,
                               CoverToolAttachedFileService coverToolAttachedFileService,
                               CoverToolCommentService coverToolCommentService) {
        this.coverToolService = coverToolService;
        this.coverToolAttachedFileService = coverToolAttachedFileService;
        this.coverToolCommentService = coverToolCommentService;
    }

    /**
     * 리스트 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getCoverToolListByPriorityAsc() {
        List<CoverToolDto> coverToolDtoList = coverToolService.findAllByHighPriorityAsc();

        return new ResponseEntity(coverToolDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param coverToolSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getCoverToolList(Pageable pageable, CoverToolSearchDto coverToolSearchDto) {
        Page<CoverToolDto> coverToolDtoList = coverToolService.findAll(pageable, coverToolSearchDto);

        return new ResponseEntity(coverToolDtoList, HttpStatus.OK);
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
    public ResponseEntity getCoverToolWhenRead(@PathVariable("idx") long idx) {
        CoverToolDto coverToolDto = coverToolService.findCoverToolByIdx(idx);

        coverToolDto = coverToolAttachedFileService.findAttachedFileByCoverToolIdx(coverToolDto);
        coverToolDto = coverToolCommentService.findAllByCoverToolIdxOrderByCreatedDateDesc(coverToolDto);

        return new ResponseEntity(coverToolDto, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getCoverToolWhenForm(@PathVariable("idx") long idx) {
        CoverToolDto coverToolDto = coverToolService.findCoverToolByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (coverToolDto.isAccess()) {
            coverToolDto = coverToolAttachedFileService.findAttachedFileByCoverToolIdx(coverToolDto);
            coverToolDto = coverToolService.findCoverToolAutoComplete(coverToolDto);
            responseEntity = new ResponseEntity(coverToolDto, HttpStatus.OK);
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
    @GetMapping({"/priority-list-write"})
    public ResponseEntity getPriorityListWhenWrite() {
        return new ResponseEntity(coverToolService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(coverToolService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }


    /**
     * 등록 페이지에서, 등록
     *
     * @param coverToolDto
     * @return
     */
    @PostMapping
    public ResponseEntity postCoverTool(@RequestBody @Valid CoverToolDto coverToolDto) {
        long idx = coverToolService.insertCoverTool(coverToolDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param coverToolDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putCoverTool(@PathVariable("idx") long idx, @RequestBody @Valid CoverToolDto coverToolDto) {
        coverToolService.updateCoverTool(idx, coverToolDto);

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
    public ResponseEntity deleteCoverTool(@PathVariable("idx") long idx) throws Exception {
        coverToolService.deleteCoverToolByIdx(idx);
        coverToolAttachedFileService.deleteAllAttachedFile(idx);
        coverToolCommentService.deleteAllByCoverToolIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param coverToolIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long coverToolIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        coverToolAttachedFileService.uploadAttachedFile(coverToolIdx, files);

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
        coverToolAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}