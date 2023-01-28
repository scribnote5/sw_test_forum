package com.suresoft.sw_test_forum.controller.tool.tool_configuration;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.tool.tool_configuration.dto.ToolConfigurationDto;
import com.suresoft.sw_test_forum.tool.tool_configuration.dto.ToolConfigurationSearchDto;
import com.suresoft.sw_test_forum.tool.tool_configuration.service.ToolConfigurationAttachedFileService;
import com.suresoft.sw_test_forum.tool.tool_configuration.service.ToolConfigurationCommentService;
import com.suresoft.sw_test_forum.tool.tool_configuration.service.ToolConfigurationService;
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
@RequestMapping("/api/tool-configurations")
public class ToolConfigurationController {
    private final ToolConfigurationService toolConfigurationService;
    private final ToolConfigurationAttachedFileService toolConfigurationAttachedFileService;
    private final ToolConfigurationCommentService toolConfigurationCommentService;

    public ToolConfigurationController(ToolConfigurationService toolConfigurationService,
                                       ToolConfigurationAttachedFileService toolConfigurationAttachedFileService,
                                       ToolConfigurationCommentService toolConfigurationCommentService) {
        this.toolConfigurationService = toolConfigurationService;
        this.toolConfigurationAttachedFileService = toolConfigurationAttachedFileService;
        this.toolConfigurationCommentService = toolConfigurationCommentService;
    }

    /**
     * 리스트 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getConfigurationListByPriorityAsc() {
        List<ToolConfigurationDto> toolConfigurationDtoList = toolConfigurationService.findAllByHighPriorityAsc();

        return new ResponseEntity(toolConfigurationDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param toolConfigurationSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getConfigurationList(Pageable pageable, ToolConfigurationSearchDto toolConfigurationSearchDto) {
        Page<ToolConfigurationDto> configurationDtoList = toolConfigurationService.findAll(pageable, toolConfigurationSearchDto);

        return new ResponseEntity(configurationDtoList, HttpStatus.OK);
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
    public ResponseEntity getConfigurationWhenRead(@PathVariable("idx") long idx) {
        ToolConfigurationDto toolConfigurationDto = toolConfigurationService.findConfigurationByIdx(idx);

        toolConfigurationDto = toolConfigurationAttachedFileService.findAttachedFileByConfigurationIdx(toolConfigurationDto);
        toolConfigurationDto = toolConfigurationCommentService.findAllByToolConfigurationIdxOrderByCreatedDateDesc(toolConfigurationDto);

        return new ResponseEntity(toolConfigurationDto, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getConfigurationWhenForm(@PathVariable("idx") long idx) {
        ToolConfigurationDto toolConfigurationDto = toolConfigurationService.findConfigurationByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (toolConfigurationDto.isAccess()) {
            toolConfigurationDto = toolConfigurationAttachedFileService.findAttachedFileByConfigurationIdx(toolConfigurationDto);
            toolConfigurationDto = toolConfigurationService.findConfigurationAutoComplete(toolConfigurationDto);
            responseEntity = new ResponseEntity(toolConfigurationDto, HttpStatus.OK);
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
        return new ResponseEntity(toolConfigurationService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(toolConfigurationService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }


    /**
     * 등록 페이지에서, 등록
     *
     * @param toolConfigurationDto
     * @return
     */
    @PostMapping
    public ResponseEntity postConfiguration(@RequestBody @Valid ToolConfigurationDto toolConfigurationDto) {
        long idx = toolConfigurationService.insertConfiguration(toolConfigurationDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param toolConfigurationDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putConfiguration(@PathVariable("idx") long idx, @RequestBody @Valid ToolConfigurationDto toolConfigurationDto) {
        toolConfigurationService.updateConfiguration(idx, toolConfigurationDto);

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
    public ResponseEntity deleteConfiguration(@PathVariable("idx") long idx) throws Exception {
        toolConfigurationService.deleteConfigurationByIdx(idx);
        toolConfigurationAttachedFileService.deleteAllAttachedFile(idx);
        toolConfigurationCommentService.deleteAllByConfigurationIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param toolConfigurationIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long toolConfigurationIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        toolConfigurationAttachedFileService.uploadAttachedFile(toolConfigurationIdx, files);

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
        toolConfigurationAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}