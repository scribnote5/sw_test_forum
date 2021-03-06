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
     * ????????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getMetricListByPriorityAsc() {
        List<MetricDto> metricDtoList = metricService.findAllByHighPriorityAsc();

        return new ResponseEntity(metricDtoList, HttpStatus.OK);
    }

    /**
     * ????????? ???????????????, ???????????? ?????? ????????? ??????
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
    public ResponseEntity getMetricWhenRead(@PathVariable("idx") long idx) {
        MetricDto metricDto = metricService.findMetricByIdx(idx);

        metricDto = metricAttachedFileService.findAttachedFileByMetricIdx(metricDto);
        metricDto = metricCommentService.findAllByMetricIdxOrderByIdxDesc(metricDto);
        metricDto = metricExampleService.findMetricExampleList(idx, metricDto);
        metricDto = metricGuidelineService.findMetricGuidelineList(idx, metricDto);

        return new ResponseEntity(metricDto, HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ????????? ?????? ??????
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
     * ?????? ??? ?????? ???????????????, ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getMetricWhenForm(@PathVariable("idx") long idx) {
        MetricDto metricDto = metricService.findMetricByIdx(idx);
        ResponseEntity responseEntity;

        // ?????? ??????
        if (metricDto.isAccess()) {
            metricDto = metricAttachedFileService.findAttachedFileByMetricIdx(metricDto);
            metricDto = metricService.findMetricAutoComplete(metricDto);
            responseEntity = new ResponseEntity(metricDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("????????? ????????? ?????? ???????????? ???????????? ????????? ?????????????????????.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }

    /**
     * ?????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping({"/priority-list-write"})
    public ResponseEntity getPriorityListWhenWrite() {
        return new ResponseEntity(metricService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(metricService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ??????
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
     * ?????? ???????????????, ??????
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
     * ?????? ???????????????, ??????
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
     * ?????? ??? ?????? ???????????????, ???????????? ?????????
     *
     * @param metricIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long metricIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // ?????? mime type ??????
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        metricAttachedFileService.uploadAttachedFile(metricIdx, files);

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
        metricAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}