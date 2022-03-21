package com.suresoft.sw_test_forum.controller.trouble_shooting;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.ToolchainDto;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.ToolchainSearchDto;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.service.ToolchainAttachedFileService;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.service.ToolchainCommentService;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.service.ToolchainService;
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
@RequestMapping("/api/toolchains")
public class ToolchainController {
    private final ToolchainService toolchainService;
    private final ToolchainAttachedFileService toolchainAttachedFileService;
    private final ToolchainCommentService toolchainCommentService;

    public ToolchainController(ToolchainService toolchainService,
                               ToolchainAttachedFileService toolchainAttachedFileService,
                               ToolchainCommentService toolchainCommentService) {
        this.toolchainService = toolchainService;
        this.toolchainAttachedFileService = toolchainAttachedFileService;
        this.toolchainCommentService = toolchainCommentService;
    }

    /**
     * 리스트 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getToolchainListByPriorityAsc() {
        List<ToolchainDto> toolchainDtoList = toolchainService.findAllByHighPriorityAsc();

        return new ResponseEntity(toolchainDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param toolchainSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getToolchainList(Pageable pageable, ToolchainSearchDto toolchainSearchDto) {
        Page<ToolchainDto> toolchainDtoList = toolchainService.findAll(pageable, toolchainSearchDto);

        return new ResponseEntity(toolchainDtoList, HttpStatus.OK);
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
    public ResponseEntity getToolchainWhenRead(@PathVariable("idx") long idx) {
        ToolchainDto toolchainDto = toolchainService.findToolchainByIdx(idx);

        toolchainDto = toolchainAttachedFileService.findAttachedFileByToolchainIdx(toolchainDto);
        toolchainDto = toolchainCommentService.findAllByToolchainIdxOrderByCreatedDateDesc(toolchainDto);

        return new ResponseEntity(toolchainDto, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getToolchainWhenForm(@PathVariable("idx") long idx) {
        ToolchainDto toolchainDto = toolchainService.findToolchainByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (toolchainDto.isAccess()) {
            toolchainDto = toolchainAttachedFileService.findAttachedFileByToolchainIdx(toolchainDto);
            toolchainDto = toolchainService.findToolchainAutoComplete(toolchainDto);
            responseEntity = new ResponseEntity(toolchainDto, HttpStatus.OK);
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
        return new ResponseEntity(toolchainService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(toolchainService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }


    /**
     * 등록 페이지에서, 등록
     *
     * @param toolchainDto
     * @return
     */
    @PostMapping
    public ResponseEntity postToolchain(@RequestBody @Valid ToolchainDto toolchainDto) {
        long idx = toolchainService.insertToolchain(toolchainDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param toolchainDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putToolchain(@PathVariable("idx") long idx, @RequestBody @Valid ToolchainDto toolchainDto) {
        toolchainService.updateToolchain(idx, toolchainDto);

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
    public ResponseEntity deleteToolchain(@PathVariable("idx") long idx) throws Exception {
        toolchainService.deleteToolchainByIdx(idx);
        toolchainAttachedFileService.deleteAllAttachedFile(idx);
        toolchainCommentService.deleteAllByToolchainIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param toolchainIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long toolchainIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        toolchainAttachedFileService.uploadAttachedFile(toolchainIdx, files);

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
        toolchainAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}