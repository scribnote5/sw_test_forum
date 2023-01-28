package com.suresoft.sw_test_forum.controller;

import com.suresoft.sw_test_forum.common.dto.PriorityDto;
import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.admin_page.notice.dto.NoticeCommentDto;
import com.suresoft.sw_test_forum.admin_page.notice.dto.NoticeDto;
//import com.suresoft.sw_test_forum.admin_page.notice.service.NoticeAttachedFileService;
import com.suresoft.sw_test_forum.admin_page.notice.dto.SearchDto;
import com.suresoft.sw_test_forum.admin_page.notice.service.NoticeAttachedFileService;
import com.suresoft.sw_test_forum.admin_page.notice.service.NoticeCommentService;
import com.suresoft.sw_test_forum.admin_page.notice.service.NoticeService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notices")
public class NoticeRestController {
    private final NoticeService noticeService;
    private final NoticeAttachedFileService noticeAttachedFileService;
    private final NoticeCommentService noticeCommentService;

    public NoticeRestController(NoticeService noticeService,
                                NoticeAttachedFileService noticeAttachedFileService,
                                NoticeCommentService noticeCommentService
    ) {
        this.noticeService = noticeService;
        this.noticeAttachedFileService = noticeAttachedFileService;
        this.noticeCommentService = noticeCommentService;
    }

    // List priority
    @GetMapping("/high-priority-list")
    public ResponseEntity getNoticeListByPriorityAsc() {
        List<NoticeDto> noticeDtoList = noticeService.findAllByHighPriorityAsc();

        return new ResponseEntity(noticeDtoList, HttpStatus.OK);
    }

    // List
    @GetMapping("/list")
    public ResponseEntity getNoticeList(Pageable pageable, SearchDto searchDto) {
        Page<NoticeDto> noticeDtoList = noticeService.findNoticeList(pageable, searchDto);

        return new ResponseEntity(noticeDtoList, HttpStatus.OK);
    }

    // Read, defaultValue = "0"
    @GetMapping({"/read{idx}"})
    public ResponseEntity getNoticeWhenRead(@RequestParam(value = "idx") long idx, Model model) {
        NoticeDto noticeDto = noticeService.findNoticeByIdx(idx);

        noticeDto = noticeAttachedFileService.findAttachedFileByNoticeIdx(noticeDto);
        noticeDto = noticeCommentService.findAllByNoticeIdxOrderByIdxDesc(noticeDto);

        return new ResponseEntity(noticeDto, HttpStatus.OK);
    }

    // Form
    @GetMapping({"/form{idx}"})
    public ResponseEntity getNoticeWhenForm(@RequestParam(value = "idx") long idx) {
        NoticeDto noticeDto = noticeService.findNoticeByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (noticeDto.isAccess()) {
            noticeDto = noticeAttachedFileService.findAttachedFileByNoticeIdx(noticeDto);
            responseEntity = new ResponseEntity(noticeDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("필요한 권한이 없는 상태에서 접근하여 오류가 발생하였습니다.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }

    // Priority list when write
    @GetMapping({"/priority-list-write"})
    public ResponseEntity getPriorityListWhenWrite(){
        return new ResponseEntity(noticeService.findPriorityListByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    // Priority list when write
    @GetMapping({"/priority-list-update"})
    public ResponseEntity getPriorityListWhenUpdate(@RequestParam(value = "idx", defaultValue = "0") long idx) {
        return new ResponseEntity(noticeService.findPriorityListByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }


    @PostMapping
    public ResponseEntity postNotice(@RequestBody @Valid NoticeDto noticeDto) {
        long idx = noticeService.insertNotice(noticeDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    @PutMapping("/{idx}")
    public ResponseEntity<?> putNotice(@PathVariable("idx") long idx, @RequestBody @Valid NoticeDto noticeDto) {
        noticeService.updateNotice(idx, noticeDto);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    @DeleteMapping("/{idx}")
    public ResponseEntity deleteNotice(@PathVariable("idx") long idx) throws Exception {
        noticeService.deleteNoticeByIdx(idx);
        noticeAttachedFileService.deleteAllAttachedFile(idx);
        noticeCommentService.deleteAllByNoticeIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    // 첨부 파일 업로드
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long noticeIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        noticeAttachedFileService.uploadAttachedFile(noticeIdx, files);

        return new ResponseEntity("{}", HttpStatus.CREATED);
    }

    // 첨부 파일 삭제
    @DeleteMapping("/attached-file") // @RequestBody String deleteAttachedFileIdxList
    public ResponseEntity deleteAttachedFile(@RequestBody List<Long> deleteAttachedFileIdxList) throws Exception {
        noticeAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}