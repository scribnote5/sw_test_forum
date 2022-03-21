package com.suresoft.sw_test_forum.controller.admin_page;

import com.suresoft.sw_test_forum.admin_page.data_history.dto.DataHistoryDto;
import com.suresoft.sw_test_forum.admin_page.data_history.dto.DataHistorySearchDto;
import com.suresoft.sw_test_forum.admin_page.data_history.service.DataHistoryService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/data-historys")
public class DataHistoryController {
    private final DataHistoryService dataHistoryService;

    public DataHistoryController(DataHistoryService dataHistoryService) {
        this.dataHistoryService = dataHistoryService;
    }

    /**
     * 리스트 페이지에서, 조회
     *
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getDataHistoryList(Pageable pageable, DataHistorySearchDto dataHistorySearchDto) {
        Page<DataHistoryDto> dataHistoryDtoList = dataHistoryService.findDataHistoryList(pageable, dataHistorySearchDto);

        return new ResponseEntity(dataHistoryDtoList, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/read/{idx}"})
    public ResponseEntity getDataHistoryWhenRead(@PathVariable("idx") long idx) {
        DataHistoryDto dataHistoryDto = dataHistoryService.findDataHistoryByIdx(idx);

        return new ResponseEntity(dataHistoryDto, HttpStatus.OK);
    }
}