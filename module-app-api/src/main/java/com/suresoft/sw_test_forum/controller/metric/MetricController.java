package com.suresoft.sw_test_forum.controller.metric;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.metric.metric.dto.MetricDto;
import com.suresoft.sw_test_forum.metric.metric.dto.MetricSearchDto;
import com.suresoft.sw_test_forum.metric.metric.service.MetricAttachedFileService;
import com.suresoft.sw_test_forum.metric.metric.service.MetricCommentService;
import com.suresoft.sw_test_forum.metric.metric.service.MetricService;
import com.suresoft.sw_test_forum.metric.metric_example.service.MetricExampleService;
import com.suresoft.sw_test_forum.metric.metric_guideline.service.MetricGuidelineService;
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
@RequestMapping("/api/metric")
public class MetricController {
    private final MetricService metricService;
    private final MetricAttachedFileService metricAttachedFileService;
    private final MetricCommentService metricCommentService;
    private final MetricExampleService metricExampleService;
    private final MetricGuidelineService metricGuidelineService;

    public MetricController(MetricService metricService,
                            MetricAttachedFileService metricAttachedFileService,
                            MetricCommentService metricCommentService,
                            MetricExampleService metricExampleService,
                            MetricGuidelineService metricGuidelineService) {
        this.metricService = metricService;
        this.metricAttachedFileService = metricAttachedFileService;
        this.metricCommentService = metricCommentService;
        this.metricExampleService = metricExampleService;
        this.metricGuidelineService = metricGuidelineService;
    }

    /**
     * 리스트 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getMetricListByPriorityAsc() {
        List<MetricDto> metricDtoList = metricService.findAllByHighPriorityAsc();

        return new ResponseEntity(metricDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param metricSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getMetricList(Pageable pageable, MetricSearchDto metricSearchDto) {
        Page<MetricDto> metricDtoList = metricService.findAll(pageable, metricSearchDto);

        return new ResponseEntity(metricDtoList, HttpStatus.OK);
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
    public ResponseEntity getMetricWhenRead(@PathVariable("idx") long idx) {
        MetricDto metricDto = metricService.findMetricByIdx(idx);

        metricDto = metricAttachedFileService.findAttachedFileByMetricIdx(metricDto);
        metricDto = metricCommentService.findAllByMetricIdxOrderByIdxDesc(metricDto);
        metricDto = metricExampleService.findMetricExampleList(idx, metricDto);
        metricDto = metricGuidelineService.findMetricGuidelineList(idx, metricDto);

        return new ResponseEntity(metricDto, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, 메트릭 규칙 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/metric-rule/{idx}"})
    public ResponseEntity getMetricRule(@PathVariable("idx") long idx) {
        String metricRule = metricService.findMetricRuleByIdx(idx);

        return new ResponseEntity(metricRule, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getMetricWhenForm(@PathVariable("idx") long idx) {
        MetricDto metricDto = metricService.findMetricByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (metricDto.isAccess()) {
            metricDto = metricAttachedFileService.findAttachedFileByMetricIdx(metricDto);
            metricDto = metricService.findMetricAutoComplete(metricDto);
            responseEntity = new ResponseEntity(metricDto, HttpStatus.OK);
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
        return new ResponseEntity(metricService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(metricService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param metricDto
     * @return
     */
    @PostMapping
    public ResponseEntity postMetric(@RequestBody @Valid MetricDto metricDto) {
        long idx = metricService.insertMetric(metricDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param metricDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMetric(@PathVariable("idx") long idx, @RequestBody @Valid MetricDto metricDto) {
        metricService.updateMetric(idx, metricDto);

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
    public ResponseEntity deleteMetric(@PathVariable("idx") long idx) throws Exception {
        metricService.deleteRelatedMetricByIdx(idx);
        metricAttachedFileService.deleteAllAttachedFile(idx);
        metricCommentService.deleteAllByMetricIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param metricIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long metricIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        metricAttachedFileService.uploadAttachedFile(metricIdx, files);

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
        metricAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}