package com.suresoft.sw_test_forum.controller.admin_page;

import com.suresoft.sw_test_forum.admin_page.notice.dto.NoticeDto;
import com.suresoft.sw_test_forum.admin_page.notice.dto.NoticeSearchDto;
import com.suresoft.sw_test_forum.admin_page.notice.service.NoticeAttachedFileService;
import com.suresoft.sw_test_forum.admin_page.notice.service.NoticeCommentService;
import com.suresoft.sw_test_forum.admin_page.notice.service.NoticeService;
import com.suresoft.sw_test_forum.common.validation.FileValidator;
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
@RequestMapping("/api/notices")
public class NoticeController {
    private final NoticeService noticeService;
    private final NoticeAttachedFileService noticeAttachedFileService;
    private final NoticeCommentService noticeCommentService;

    public NoticeController(NoticeService noticeService,
                            NoticeAttachedFileService noticeAttachedFileService,
                            NoticeCommentService noticeCommentService) {
        this.noticeService = noticeService;
        this.noticeAttachedFileService = noticeAttachedFileService;
        this.noticeCommentService = noticeCommentService;
    }

    /**
     * 리스트 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getNoticeListByPriorityAsc() {
        List<NoticeDto> noticeDtoList = noticeService.findAllByHighPriorityAsc();

        return new ResponseEntity(noticeDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 우선순위 낮은 리스트 조회
     *
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getNoticeList(Pageable pageable, NoticeSearchDto noticeSearchDto) {
        Page<NoticeDto> noticeDtoList = noticeService.findNoticeList(pageable, noticeSearchDto);

        return new ResponseEntity(noticeDtoList, HttpStatus.OK);
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
    public ResponseEntity getNoticeWhenRead(@PathVariable("idx") long idx) {
        NoticeDto noticeDto = noticeService.findNoticeByIdx(idx);

        noticeDto = noticeAttachedFileService.findAttachedFileByNoticeIdx(noticeDto);
        noticeDto = noticeCommentService.findAllByNoticeIdxOrderByIdxDesc(noticeDto);

        return new ResponseEntity(noticeDto, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getNoticeWhenForm(@PathVariable("idx") long idx) {
        NoticeDto noticeDto = noticeService.findNoticeByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (noticeDto.isAccess()) {
            noticeDto = noticeAttachedFileService.findAttachedFileByNoticeIdx(noticeDto);
            noticeDto = noticeService.findNoticeAutoComplete(noticeDto);
            responseEntity = new ResponseEntity(noticeDto, HttpStatus.OK);
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
        return new ResponseEntity(noticeService.findPriorityListByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(noticeService.findPriorityListByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param noticeDto
     * @return
     */
    @PostMapping
    public ResponseEntity postNotice(@RequestBody @Valid NoticeDto noticeDto) {
        long idx = noticeService.insertNotice(noticeDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param noticeDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putNotice(@PathVariable("idx") long idx, @RequestBody @Valid NoticeDto noticeDto) {
        noticeService.updateNotice(idx, noticeDto);

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
    public ResponseEntity deleteNotice(@PathVariable("idx") long idx) throws Exception {
        noticeService.deleteNoticeByIdx(idx);
        noticeAttachedFileService.deleteAllAttachedFile(idx);
        noticeCommentService.deleteAllByNoticeIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param noticeIdx
     * @param files
     * @return
     * @throws Exception
     */
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

    /**
     * 삭제 페이지에서, 첨부파일 삭제
     *
     * @param deleteAttachedFileIdxList
     * @return
     * @throws Exception
     */
    @DeleteMapping("/attached-file") // @RequestBody String deleteAttachedFileIdxList
    public ResponseEntity deleteAttachedFile(@RequestBody List<Long> deleteAttachedFileIdxList) throws Exception {
        noticeAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}