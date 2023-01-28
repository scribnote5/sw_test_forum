package com.suresoft.sw_test_forum.controller.left_reference.storage;

import com.suresoft.sw_test_forum.left_reference.storage.dto.StorageDto;
import com.suresoft.sw_test_forum.left_reference.storage.dto.StorageSearchDto;
import com.suresoft.sw_test_forum.left_reference.storage.service.StorageAttachedFileService;
import com.suresoft.sw_test_forum.left_reference.storage.service.StorageCommentService;
import com.suresoft.sw_test_forum.left_reference.storage.service.StorageService;
import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
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
@RequestMapping("/api/storages")
public class StorageController {
    private final StorageService storageService;
    private final StorageAttachedFileService storageAttachedFileService;
    private final StorageCommentService storageCommentService;

    public StorageController(StorageService storageService,
                             StorageAttachedFileService storageAttachedFileService,
                             StorageCommentService storageCommentService) {
        this.storageService = storageService;
        this.storageAttachedFileService = storageAttachedFileService;
        this.storageCommentService = storageCommentService;
    }

    /**
     * 리스트 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping("/high-priority-list")
    public ResponseEntity getStorageListByPriorityAsc() {
        List<StorageDto> storageDtoList = storageService.findAllByHighPriorityAsc();

        return new ResponseEntity(storageDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 우선순위 낮은 리스트 조회
     *
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getStorageList(Pageable pageable, StorageSearchDto storageSearchDto) {
        Page<StorageDto> storageDtoList = storageService.findStorageList(pageable, storageSearchDto);

        return new ResponseEntity(storageDtoList, HttpStatus.OK);
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
    public ResponseEntity getStorageWhenRead(@PathVariable("idx") long idx) {
        StorageDto storageDto = storageService.findStorageByIdx(idx);

        storageDto = storageAttachedFileService.findAttachedFileByStorageIdx(storageDto);
        storageDto = storageCommentService.findAllByStorageIdxOrderByIdxDesc(storageDto);

        return new ResponseEntity(storageDto, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getStorageWhenForm(@PathVariable("idx") long idx) {
        StorageDto storageDto = storageService.findStorageByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (storageDto.isAccess()) {
            storageDto = storageAttachedFileService.findAttachedFileByStorageIdx(storageDto);
            storageDto = storageService.findStorageAutoComplete(storageDto);
            responseEntity = new ResponseEntity(storageDto, HttpStatus.OK);
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
        return new ResponseEntity(storageService.findPriorityListByHighPriorityAscWhenWrite(), HttpStatus.OK);
    }

    /**
     * 수정 페이지에서, 우선순위 높은 리스트 조회
     *
     * @return
     */
    @GetMapping({"/priority-list-update/{idx}"})
    public ResponseEntity getPriorityListWhenUpdate(@PathVariable("idx") long idx) {
        return new ResponseEntity(storageService.findPriorityListByHighPriorityAscWhenUpdate(idx), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param storageDto
     * @return
     */
    @PostMapping
    public ResponseEntity postStorage(@RequestBody @Valid StorageDto storageDto) {
        long idx = storageService.insertStorage(storageDto);

        return new ResponseEntity(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param storageDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putStorage(@PathVariable("idx") long idx, @RequestBody @Valid StorageDto storageDto) {
        storageService.updateStorage(idx, storageDto);

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
    public ResponseEntity deleteStorage(@PathVariable("idx") long idx) throws Exception {
        storageService.deleteStorageByIdx(idx);
        storageAttachedFileService.deleteAllAttachedFile(idx);
        storageCommentService.deleteAllByStorageIdx(idx);

        return new ResponseEntity("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param storageIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long storageIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        storageAttachedFileService.uploadAttachedFile(storageIdx, files);

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
        storageAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}