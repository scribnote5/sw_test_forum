package com.suresoft.sw_test_forum.controller.tool.tool_usage_method;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.tool.tool_usage_method.dto.ToolUsageMethodDto;
import com.suresoft.sw_test_forum.tool.tool_usage_method.dto.ToolUsageMethodSearchDto;
import com.suresoft.sw_test_forum.tool.tool_usage_method.service.ToolUsageMethodAttachedFileService;
import com.suresoft.sw_test_forum.tool.tool_usage_method.service.ToolUsageMethodCommentService;
import com.suresoft.sw_test_forum.tool.tool_usage_method.service.ToolUsageMethodService;
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
@RequestMapping("/api/tool-usage-methods")
public class ToolUsageMethodController {
    private final ToolUsageMethodService toolUsageMethodService;
    private final ToolUsageMethodAttachedFileService toolUsageMethodAttachedFileService;
    private final ToolUsageMethodCommentService toolUsageMethodCommentService;

    public ToolUsageMethodController(ToolUsageMethodService toolUsageMethodService, ToolUsageMethodAttachedFileService toolUsageMethodAttachedFileService, ToolUsageMethodCommentService toolUsageMethodCommentService) {
        this.toolUsageMethodService = toolUsageMethodService;
        this.toolUsageMethodAttachedFileService = toolUsageMethodAttachedFileService;
        this.toolUsageMethodCommentService = toolUsageMethodCommentService;
    }

    /**
     * 리스트 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getToolUsageMethodListByPriorityAsc() {
        List<ToolUsageMethodDto> toolUsageMethodDtoList = toolUsageMethodService.findAllByHighPriorityAsc();

        return new ResponseEntity(toolUsageMethodDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param toolUsageMethodSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getToolUsageMethodList(Pageable pageable, ToolUsageMethodSearchDto toolUsageMethodSearchDto) {
        Page<ToolUsageMethodDto> toolUsageMethodDtoList = toolUsageMethodService.findAll(pageable, toolUsageMethodSearchDto);

        return new ResponseEntity(toolUsageMethodDtoList, HttpStatus.OK);
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
    public ResponseEntity getToolUsageMethodWhenRead(@PathVariable("idx") long idx) {
        ToolUsageMethodDto toolUsageMethodDto = toolUsageMethodService.findToolUsageMethodByIdx(idx);

        toolUsageMethodDto = toolUsageMethodAttachedFileService.findAttachedFileByToolUsageMethodIdx(toolUsageMethodDto);
        toolUsageMethodDto = toolUsageMethodCommentService.findAllByToolUsageMethodIdxOrderByCreatedDateDesc(toolUsageMethodDto);

        return new ResponseEntity(toolUsageMethodDto, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getToolUsageMethodWhenForm(@PathVariable("idx") long idx) {
        ToolUsageMethodDto toolUsageMethodDto = toolUsageMethodService.findToolUsageMethodByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (toolUsageMethodDto.isAccess()) {
            toolUsageMethodDto = toolUsageMethodAttachedFileService.findAttachedFileByToolUsageMethodIdx(toolUsageMethodDto);
            toolUsageMethodDto = toolUsageMethodService.findToolUsageMethodAutoComplete(toolUsageMethodDto);
            responseEntity = new ResponseEntity(toolUsageMethodDto, HttpStatus.OK);
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
        return new ResponseEntity(toolUsageMethodService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(toolUsageMethodService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param toolUsageMethodDto
     * @return
     */
    @PostMapping
    public ResponseEntity postToolUsageMethod(@RequestBody @Valid ToolUsageMethodDto toolUsageMethodDto) {
        long idx = toolUsageMethodService.insertToolUsageMethod(toolUsageMethodDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param toolUsageMethodDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putToolUsageMethod(@PathVariable("idx") long idx, @RequestBody @Valid ToolUsageMethodDto toolUsageMethodDto) {
        toolUsageMethodService.updateToolUsageMethod(idx, toolUsageMethodDto);

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
    public ResponseEntity deleteToolUsageMethod(@PathVariable("idx") long idx) throws Exception {
        toolUsageMethodService.deleteToolUsageMethodByIdx(idx);
        toolUsageMethodAttachedFileService.deleteAllAttachedFile(idx);
        toolUsageMethodCommentService.deleteAllByToolUsageMethodIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param toolUsageMethodIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long toolUsageMethodIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        toolUsageMethodAttachedFileService.uploadAttachedFile(toolUsageMethodIdx, files);

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
        toolUsageMethodAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}