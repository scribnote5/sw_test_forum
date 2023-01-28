package com.suresoft.sw_test_forum.controller.left_reference.storage;

import com.suresoft.sw_test_forum.left_reference.storage.dto.StorageCommentDto;
import com.suresoft.sw_test_forum.left_reference.storage.service.StorageCommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/storage-comments")
public class StorageCommentController {
    private final StorageCommentService storageCommentService;

    public StorageCommentController(StorageCommentService storageCommentService) {
        this.storageCommentService = storageCommentService;
    }

    /**
     * 등록 페이지에서, 등록
     * @param storageCommentDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postStorageComment(@RequestBody @Valid StorageCommentDto storageCommentDto) {
        long idx = storageCommentService.insertStorageComment(storageCommentDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     * @param idx
     * @param storageCommentDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putStorageComment(@PathVariable("idx") long idx, @RequestBody @Valid StorageCommentDto storageCommentDto) {
        storageCommentService.updateStorageComment(idx, storageCommentDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     * @param idx
     * @return
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteStorageComment(@PathVariable("idx") long idx) {
        storageCommentService.deleteStorageCommentByIdx(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }
}