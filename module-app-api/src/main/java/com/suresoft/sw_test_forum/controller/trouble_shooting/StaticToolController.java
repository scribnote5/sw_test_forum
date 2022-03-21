package com.suresoft.sw_test_forum.controller.trouble_shooting;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.StaticToolDto;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.StaticToolSearchDto;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.service.StaticToolAttachedFileService;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.service.StaticToolCommentService;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.service.StaticToolService;
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
@RequestMapping("/api/static-tools")
public class StaticToolController {
    private final StaticToolService staticToolService;
    private final StaticToolAttachedFileService staticToolAttachedFileService;
    private final StaticToolCommentService staticToolCommentService;

    public StaticToolController(StaticToolService staticToolService,
                                StaticToolAttachedFileService staticToolAttachedFileService,
                                StaticToolCommentService staticToolCommentService) {
        this.staticToolService = staticToolService;
        this.staticToolAttachedFileService = staticToolAttachedFileService;
        this.staticToolCommentService = staticToolCommentService;
    }

    /**
     * 리스트 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getStaticToolListByPriorityAsc() {
        List<StaticToolDto> staticToolDtoList = staticToolService.findAllByHighPriorityAsc();

        return new ResponseEntity(staticToolDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 우선순위 낮은 리스트 조회
     *
     * @param pageable
     * @param staticToolSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getStaticToolList(Pageable pageable, StaticToolSearchDto staticToolSearchDto) {
        Page<StaticToolDto> staticToolDtoList = staticToolService.findAll(pageable, staticToolSearchDto);

        return new ResponseEntity(staticToolDtoList, HttpStatus.OK);
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
    public ResponseEntity getStaticToolWhenRead(@PathVariable("idx") long idx) {
        StaticToolDto staticToolDto = staticToolService.findStaticToolByIdx(idx);

        staticToolDto = staticToolAttachedFileService.findAttachedFileByStaticToolIdx(staticToolDto);
        staticToolDto = staticToolCommentService.findAllByStaticToolIdxOrderByCreatedDateDesc(staticToolDto);

        return new ResponseEntity(staticToolDto, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getStaticToolWhenForm(@PathVariable("idx") long idx) {
        StaticToolDto staticToolDto = staticToolService.findStaticToolByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (staticToolDto.isAccess()) {
            staticToolDto = staticToolAttachedFileService.findAttachedFileByStaticToolIdx(staticToolDto);
            staticToolDto = staticToolService.findStaticToolAutoComplete(staticToolDto);
            responseEntity = new ResponseEntity(staticToolDto, HttpStatus.OK);
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
        return new ResponseEntity(staticToolService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(staticToolService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }


    /**
     * 등록 페이지에서, 등록
     *
     * @param staticToolDto
     * @return
     */
    @PostMapping
    public ResponseEntity postStaticTool(@RequestBody @Valid StaticToolDto staticToolDto) {
        long idx = staticToolService.insertStaticTool(staticToolDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param staticToolDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putStaticTool(@PathVariable("idx") long idx, @RequestBody @Valid StaticToolDto staticToolDto) {
        staticToolService.updateStaticTool(idx, staticToolDto);

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
    public ResponseEntity deleteStaticTool(@PathVariable("idx") long idx) throws Exception {
        staticToolService.deleteStaticToolByIdx(idx);
        staticToolAttachedFileService.deleteAllAttachedFile(idx);
        staticToolCommentService.deleteAllByStaticToolIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param staticToolIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long staticToolIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        staticToolAttachedFileService.uploadAttachedFile(staticToolIdx, files);

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
        staticToolAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}