package com.suresoft.sw_test_forum.controller.tool.tool_trouble_shooting;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.ToolTroubleShootingDto;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.ToolTroubleShootingSearchDto;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.service.ToolTroubleShootingAttachedFileService;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.service.ToolTroubleShootingCommentService;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.service.ToolTroubleShootingService;
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
@RequestMapping("/api/tool-trouble-shootings")
public class ToolTroubleShootingController {
    private final ToolTroubleShootingService toolTroubleShootingService;
    private final ToolTroubleShootingAttachedFileService toolTroubleShootingAttachedFileService;
    private final ToolTroubleShootingCommentService toolTroubleShootingCommentService;

    public ToolTroubleShootingController(ToolTroubleShootingService toolTroubleShootingService, ToolTroubleShootingAttachedFileService toolTroubleShootingAttachedFileService, ToolTroubleShootingCommentService toolTroubleShootingCommentService) {
        this.toolTroubleShootingService = toolTroubleShootingService;
        this.toolTroubleShootingAttachedFileService = toolTroubleShootingAttachedFileService;
        this.toolTroubleShootingCommentService = toolTroubleShootingCommentService;
    }

    /**
     * 리스트 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getToolTroubleShootingListByPriorityAsc() {
        List<ToolTroubleShootingDto> toolTroubleShootingDtoList = toolTroubleShootingService.findAllByHighPriorityAsc();

        return new ResponseEntity(toolTroubleShootingDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param toolTroubleShootingSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getToolTroubleShootingList(Pageable pageable, ToolTroubleShootingSearchDto toolTroubleShootingSearchDto) {
        Page<ToolTroubleShootingDto> toolTroubleShootingDtoList = toolTroubleShootingService.findAll(pageable, toolTroubleShootingSearchDto);

        return new ResponseEntity(toolTroubleShootingDtoList, HttpStatus.OK);
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
    public ResponseEntity getToolTroubleShootingWhenRead(@PathVariable("idx") long idx) {
        ToolTroubleShootingDto toolTroubleShootingDto = toolTroubleShootingService.findToolTroubleShootingByIdx(idx);

        toolTroubleShootingDto = toolTroubleShootingAttachedFileService.findAttachedFileByTroubleShootingIdx(toolTroubleShootingDto);
        toolTroubleShootingDto = toolTroubleShootingCommentService.findAllByToolTroubleShootingIdxOrderByCreatedDateDesc(toolTroubleShootingDto);

        return new ResponseEntity(toolTroubleShootingDto, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getToolTroubleShootingWhenForm(@PathVariable("idx") long idx) {
        ToolTroubleShootingDto toolTroubleShootingDto = toolTroubleShootingService.findToolTroubleShootingByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (toolTroubleShootingDto.isAccess()) {
            toolTroubleShootingDto = toolTroubleShootingAttachedFileService.findAttachedFileByTroubleShootingIdx(toolTroubleShootingDto);
            toolTroubleShootingDto = toolTroubleShootingService.findToolTroubleShootingAutoComplete(toolTroubleShootingDto);
            responseEntity = new ResponseEntity(toolTroubleShootingDto, HttpStatus.OK);
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
        return new ResponseEntity(toolTroubleShootingService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(toolTroubleShootingService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param toolTroubleShootingDto
     * @return
     */
    @PostMapping
    public ResponseEntity postToolTroubleShooting(@RequestBody @Valid ToolTroubleShootingDto toolTroubleShootingDto) {
        long idx = toolTroubleShootingService.insertToolTroubleShooting(toolTroubleShootingDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param toolTroubleShootingDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putToolTroubleShooting(@PathVariable("idx") long idx, @RequestBody @Valid ToolTroubleShootingDto toolTroubleShootingDto) {
        toolTroubleShootingService.updateToolTroubleShooting(idx, toolTroubleShootingDto);

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
    public ResponseEntity deleteToolTroubleShooting(@PathVariable("idx") long idx) throws Exception {
        toolTroubleShootingService.deleteToolTroubleShootingByIdx(idx);
        toolTroubleShootingAttachedFileService.deleteAllAttachedFile(idx);
        toolTroubleShootingCommentService.deleteAllByToolTroubleShootingIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param troubleShootingIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long troubleShootingIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        toolTroubleShootingAttachedFileService.uploadAttachedFile(troubleShootingIdx, files);

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
        toolTroubleShootingAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}