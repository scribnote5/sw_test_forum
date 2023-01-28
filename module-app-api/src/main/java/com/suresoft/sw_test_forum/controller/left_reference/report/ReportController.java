package com.suresoft.sw_test_forum.controller.left_reference.report;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.left_reference.report.dto.ReportDto;
import com.suresoft.sw_test_forum.left_reference.report.dto.ReportSearchDto;
import com.suresoft.sw_test_forum.left_reference.report.service.ReportAttachedFileService;
import com.suresoft.sw_test_forum.left_reference.report.service.ReportCommentService;
import com.suresoft.sw_test_forum.left_reference.report.service.ReportService;
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
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;
    private final ReportAttachedFileService reportAttachedFileService;
    private final ReportCommentService reportCommentService;

    public ReportController(ReportService reportService,
                            ReportAttachedFileService reportAttachedFileService,
                            ReportCommentService reportCommentService) {
        this.reportService = reportService;
        this.reportAttachedFileService = reportAttachedFileService;
        this.reportCommentService = reportCommentService;
    }

    /**
     * 리스트 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getReportListByPriorityAsc() {
        List<ReportDto> reportDtoList = reportService.findAllByHighPriorityAsc();

        return new ResponseEntity(reportDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 우선순위 낮은 리스트 조회
     *
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getReportList(Pageable pageable, ReportSearchDto reportSearchDto) {
        Page<ReportDto> reportDtoList = reportService.findReportList(pageable, reportSearchDto);

        return new ResponseEntity(reportDtoList, HttpStatus.OK);
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
    public ResponseEntity getReportWhenRead(@PathVariable("idx") long idx) {
        ReportDto reportDto = reportService.findReportByIdx(idx);

        reportDto = reportAttachedFileService.findAttachedFileByReportIdx(reportDto);
        reportDto = reportCommentService.findAllByReportIdxOrderByIdxDesc(reportDto);

        return new ResponseEntity(reportDto, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getReportWhenForm(@PathVariable("idx") long idx) {
        ReportDto reportDto = reportService.findReportByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (reportDto.isAccess()) {
            reportDto = reportAttachedFileService.findAttachedFileByReportIdx(reportDto);
            reportDto = reportService.findReportAutoComplete(reportDto);
            responseEntity = new ResponseEntity(reportDto, HttpStatus.OK);
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
        return new ResponseEntity(reportService.findPriorityListByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(reportService.findPriorityListByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param reportDto
     * @return
     */
    @PostMapping
    public ResponseEntity postReport(@RequestBody @Valid ReportDto reportDto) {
        long idx = reportService.insertReport(reportDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param reportDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putReport(@PathVariable("idx") long idx, @RequestBody @Valid ReportDto reportDto) {
        reportService.updateReport(idx, reportDto);

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
    public ResponseEntity deleteReport(@PathVariable("idx") long idx) throws Exception {
        reportService.deleteReportByIdx(idx);
        reportAttachedFileService.deleteAllAttachedFile(idx);
        reportCommentService.deleteAllByReportIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param reportIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long reportIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        reportAttachedFileService.uploadAttachedFile(reportIdx, files);

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
        reportAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}