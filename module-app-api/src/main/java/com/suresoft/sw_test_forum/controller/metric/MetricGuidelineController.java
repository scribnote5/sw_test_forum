package com.suresoft.sw_test_forum.controller.metric;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineDto;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineSearchDto;
import com.suresoft.sw_test_forum.metric.metric_guideline.service.MetricGuidelineAttachedFileService;
import com.suresoft.sw_test_forum.metric.metric_guideline.service.MetricGuidelineCommentService;
import com.suresoft.sw_test_forum.metric.metric_guideline.service.MetricGuidelineLikeService;
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
@RequestMapping("/api/metric-guidelines")
public class MetricGuidelineController {
    private final MetricGuidelineService metricGuidelineService;
    private final MetricGuidelineAttachedFileService metricGuidelineAttachedFileService;
    private final MetricGuidelineCommentService metricGuidelineCommentService;
    private final MetricGuidelineLikeService metricGuidelineLikeService;
    
    public MetricGuidelineController(MetricGuidelineService metricGuidelineService,
                                     MetricGuidelineAttachedFileService metricGuidelineAttachedFileService,
                                     MetricGuidelineCommentService metricGuidelineCommentService,
                                     MetricGuidelineLikeService metricGuidelineLikeService) {
        this.metricGuidelineService = metricGuidelineService;
        this.metricGuidelineAttachedFileService = metricGuidelineAttachedFileService;
        this.metricGuidelineCommentService = metricGuidelineCommentService;
        this.metricGuidelineLikeService = metricGuidelineLikeService;
    }

    /**
     * ????????? ???????????????, ????????? ??????
     *
     * @param pageable
     * @param metricGuidelineSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getMetricGuidelineList(Pageable pageable, MetricGuidelineSearchDto metricGuidelineSearchDto) {
        Page<MetricGuidelineDto> metricGuidelineDtoList = metricGuidelineService.findMetricGuidelineList(pageable, metricGuidelineSearchDto);

        return new ResponseEntity(metricGuidelineDtoList, HttpStatus.OK);
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
    public ResponseEntity getMetricGuidelineWhenRead(@PathVariable("idx") long idx) {
        MetricGuidelineDto metricGuidelineDto = metricGuidelineService.findMetricGuidelineByIdx(idx);

        metricGuidelineDto = metricGuidelineAttachedFileService.findAttachedFileByMetricGuidelineIdx(metricGuidelineDto);
        metricGuidelineDto = metricGuidelineCommentService.findAllByMetricGuidelineIdxOrderByIdxDesc(metricGuidelineDto);
        metricGuidelineDto = metricGuidelineLikeService.findMetricGuidelineLike(metricGuidelineDto);

        return new ResponseEntity(metricGuidelineDto, HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getMetricGuidelineWhenForm(@PathVariable("idx") long idx) {
        MetricGuidelineDto metricGuidelineDto = metricGuidelineService.findMetricGuidelineByIdx(idx);
        ResponseEntity responseEntity;

        // ?????? ??????
        if (metricGuidelineDto.isAccess()) {
            metricGuidelineDto = metricGuidelineAttachedFileService.findAttachedFileByMetricGuidelineIdx(metricGuidelineDto);
            metricGuidelineDto = metricGuidelineService.findMetricGuidelineAutoComplete(metricGuidelineDto);
            responseEntity = new ResponseEntity(metricGuidelineDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("????????? ????????? ?????? ???????????? ???????????? ????????? ?????????????????????.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param metricGuidelineDto
     * @return
     */
    @PostMapping
    public ResponseEntity postMetricGuideline(@RequestBody @Valid MetricGuidelineDto metricGuidelineDto) {
        long idx = metricGuidelineService.insertMetricGuideline(metricGuidelineDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param idx
     * @param metricGuidelineDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMetricGuideline(@PathVariable("idx") long idx, @RequestBody @Valid MetricGuidelineDto metricGuidelineDto) {
        metricGuidelineService.updateMetricGuideline(idx, metricGuidelineDto);

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
    public ResponseEntity deleteMetricGuideline(@PathVariable("idx") long idx) throws Exception {
        metricGuidelineService.deleteMetricGuidelineByIdx(idx);
        metricGuidelineAttachedFileService.deleteAllAttachedFile(idx);
        metricGuidelineCommentService.deleteAllByMetricGuidelineIdx(idx);
        metricGuidelineLikeService.deleteAllByMetricGuidelineIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ???????????? ?????????
     *
     * @param metricGuidelineIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long metricGuidelineIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // ?????? mime type ??????
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        metricGuidelineAttachedFileService.uploadAttachedFile(metricGuidelineIdx, files);

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
        metricGuidelineAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}