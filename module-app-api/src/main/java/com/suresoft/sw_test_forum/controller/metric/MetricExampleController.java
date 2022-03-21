package com.suresoft.sw_test_forum.controller.metric;

import com.suresoft.sw_test_forum.metric.metric_example.dto.MetricExampleDto;
import com.suresoft.sw_test_forum.metric.metric_example.dto.MetricExampleSearchDto;
import com.suresoft.sw_test_forum.metric.metric_example.service.MetricExampleCommentService;
import com.suresoft.sw_test_forum.metric.metric_example.service.MetricExampleService;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/metric-examples")
public class MetricExampleController {
    private final MetricExampleService metricExampleService;
    private final MetricExampleCommentService metricExampleCommentService;

    public MetricExampleController(MetricExampleService metricExampleService,
                                   MetricExampleCommentService metricExampleCommentService) {
        this.metricExampleService = metricExampleService;
        this.metricExampleCommentService = metricExampleCommentService;
    }

    /**
     * 리스트 페이지에서, 리스트 조회
     *
     * @param pageable
     * @param metricExampleSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getMetricExampleList(Pageable pageable, MetricExampleSearchDto metricExampleSearchDto) {
        Page<MetricExampleDto> metricExampleDtoList = metricExampleService.findMetricExampleList(pageable, metricExampleSearchDto);

        return new ResponseEntity(metricExampleDtoList, HttpStatus.OK);
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
    public ResponseEntity getMetricExampleWhenRead(@PathVariable("idx") long idx) {
        MetricExampleDto metricExampleDto = metricExampleService.findMetricExampleByIdx(idx);

        metricExampleDto = metricExampleCommentService.findAllByMetricExampleIdxOrderByIdxDesc(metricExampleDto);

        return new ResponseEntity(metricExampleDto, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, 메트릭 규칙 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getMetricExampleWhenForm(@PathVariable("idx") long idx) {
        MetricExampleDto metricExampleDto = metricExampleService.findMetricExampleByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (metricExampleDto.isAccess()) {
            metricExampleDto = metricExampleService.findMetricExampleAutoComplete(metricExampleDto);
            responseEntity = new ResponseEntity(metricExampleDto, HttpStatus.OK);
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
    @GetMapping({"/priority-list-write/{metricIdx}"})
    public ResponseEntity getPriorityListWhenWrite(@PathVariable("metricIdx") long metricIdx) {
        return new ResponseEntity(metricExampleService.findPriorityListByHighPriorityAscWhenWrite(metricIdx), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}/{metricIdx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx, @PathVariable("metricIdx") long metricIdx) {
        return new ResponseEntity(metricExampleService.findPriorityListByHighPriorityAscWhenUpdate(idx, metricIdx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param metricExampleDto
     * @return
     */
    @PostMapping
    public ResponseEntity postMetricExample(@RequestBody @Valid MetricExampleDto metricExampleDto) {
        long idx = metricExampleService.insertMetricExample(metricExampleDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param metricExampleDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putMetricExample(@PathVariable("idx") long idx, @RequestBody @Valid MetricExampleDto metricExampleDto) {
        metricExampleService.updateMetricExample(idx, metricExampleDto);

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
    public ResponseEntity deleteMetricExample(@PathVariable("idx") long idx) throws Exception {
        metricExampleService.deleteMetricExampleByIdx(idx);
        metricExampleCommentService.deleteAllByMetricExampleIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}