package com.suresoft.sw_test_forum.controller.trouble_shooting;

import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.ControllerTesterToolDto;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.ControllerTesterToolSearchDto;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.service.ControllerTesterToolAttachedFileService;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.service.ControllerTesterToolCommentService;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.service.ControllerTesterToolService;
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
@RequestMapping("/api/controller-tester-tools")
public class ControllerTesterToolController {
    private final ControllerTesterToolService controllerTesterToolService;
    private final ControllerTesterToolAttachedFileService controllerTesterToolAttachedFileService;
    private final ControllerTesterToolCommentService controllerTesterToolCommentService;

    public ControllerTesterToolController(ControllerTesterToolService controllerTesterToolService,
                                          ControllerTesterToolAttachedFileService controllerTesterToolAttachedFileService,
                                          ControllerTesterToolCommentService controllerTesterToolCommentService) {
        this.controllerTesterToolService = controllerTesterToolService;
        this.controllerTesterToolAttachedFileService = controllerTesterToolAttachedFileService;
        this.controllerTesterToolCommentService = controllerTesterToolCommentService;
    }

    /**
     * ????????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getControllerTesterToolListByPriorityAsc() {
        List<ControllerTesterToolDto> controllerTesterToolDtoList = controllerTesterToolService.findAllByHighPriorityAsc();

        return new ResponseEntity(controllerTesterToolDtoList, HttpStatus.OK);
    }

    /**
     * ????????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @param pageable
     * @param controllerTesterToolSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getControllerTesterToolList(Pageable pageable, ControllerTesterToolSearchDto controllerTesterToolSearchDto) {
        Page<ControllerTesterToolDto> controllerTesterToolDtoList = controllerTesterToolService.findAll(pageable, controllerTesterToolSearchDto);

        return new ResponseEntity(controllerTesterToolDtoList, HttpStatus.OK);
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
    public ResponseEntity getControllerTesterToolWhenRead(@PathVariable("idx") long idx) {
        ControllerTesterToolDto controllerTesterToolDto = controllerTesterToolService.findControllerTesterToolByIdx(idx);

        controllerTesterToolDto = controllerTesterToolAttachedFileService.findAttachedFileByControllerTesterToolIdx(controllerTesterToolDto);
        controllerTesterToolDto = controllerTesterToolCommentService.findAllByControllerTesterToolIdxOrderByCreatedDateDesc(controllerTesterToolDto);

        return new ResponseEntity(controllerTesterToolDto, HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ??????
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getControllerTesterToolWhenForm(@PathVariable("idx") long idx) {
        ControllerTesterToolDto controllerTesterToolDto = controllerTesterToolService.findControllerTesterToolByIdx(idx);
        ResponseEntity responseEntity;

        // ?????? ??????
        if (controllerTesterToolDto.isAccess()) {
            controllerTesterToolDto = controllerTesterToolAttachedFileService.findAttachedFileByControllerTesterToolIdx(controllerTesterToolDto);
            controllerTesterToolDto = controllerTesterToolService.findControllerTesterToolAutoComplete(controllerTesterToolDto);
            responseEntity = new ResponseEntity(controllerTesterToolDto, HttpStatus.OK);
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
        return new ResponseEntity(controllerTesterToolService.findAllByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ???????????? ?????? ????????? ??????
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(controllerTesterToolService.findAllByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param controllerTesterToolDto
     * @return
     */
    @PostMapping
    public ResponseEntity postControllerTesterTool(@RequestBody @Valid ControllerTesterToolDto controllerTesterToolDto) {
        long idx = controllerTesterToolService.insertControllerTesterTool(controllerTesterToolDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * ?????? ???????????????, ??????
     *
     * @param idx
     * @param controllerTesterToolDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putControllerTesterTool(@PathVariable("idx") long idx, @RequestBody @Valid ControllerTesterToolDto controllerTesterToolDto) {
        controllerTesterToolService.updateControllerTesterTool(idx, controllerTesterToolDto);

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
    public ResponseEntity deleteControllerTesterTool(@PathVariable("idx") long idx) throws Exception {
        controllerTesterToolService.deleteControllerTesterToolByIdx(idx);
        controllerTesterToolAttachedFileService.deleteAllAttachedFile(idx);
        controllerTesterToolCommentService.deleteAllByControllerTesterToolIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * ?????? ??? ?????? ???????????????, ???????????? ?????????
     *
     * @param controllerTesterToolIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long controllerTesterToolIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // ?????? mime type ??????
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        controllerTesterToolAttachedFileService.uploadAttachedFile(controllerTesterToolIdx, files);

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
        controllerTesterToolAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}