//package com.suresoft.forum.controller;
//
//import com.suresoft.forum.common.validation.FileValidator;
//import com.suresoft.forum.exception.FileTypeException;
//import com.suresoft.forum.exception.InvalidUsernameException;
//import com.suresoft.forum.user.dto.UserDto;
//import com.suresoft.forum.user.service.UserAttachedFileService;
//import com.suresoft.forum.user.service.UserService;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import org.springframework.web.multipart.MultipartFile;
//
//import javax.validation.Valid;
//
//@RestController
//@RequestMapping("/api/users")
//public class UserRestController {
//    private final UserService userService;
//    private final UserAttachedFileService userAttachedFileService;
//
//    public UserRestController(UserService userService, UserAttachedFileService userAttachedFileService) {
//        this.userService = userService;
//        this.userAttachedFileService = userAttachedFileService;
//    }
//
//    @PostMapping
//    public ResponseEntity<?> postUser(@RequestBody @Valid UserDto userDto) {
//        // 중복 ID 검사 및 ID 길이 제한
//        if (userService.isDuplicationUserByUsername(userDto.getUsername())) {
//            throw new InvalidUsernameException();
//        }
//
//        long idx = userService.joinUser(userDto);
//
//        return new ResponseEntity<>(idx, HttpStatus.CREATED);
//    }
//
//    @PutMapping("/{idx}")
//    public ResponseEntity<?> putUser(@PathVariable("idx") long idx, @RequestBody @Valid UserDto userDto) {
//        userService.updateUser(idx, userDto);
//
//        return new ResponseEntity<>("{}", HttpStatus.OK);
//    }
//
//    @DeleteMapping("/{idx}")
//    public ResponseEntity<?> deleteUser(@PathVariable("idx") long idx) {
//        userService.deleteUserByIdx(idx);
//
//        return new ResponseEntity<>("{}", HttpStatus.OK);
//    }
//
//    @GetMapping("/validation/username/{username}")
//    public ResponseEntity<?> checkDuplicateUsername(@PathVariable("username") String username) {
//
//        return new ResponseEntity<>(userService.isDuplicationUserByUsername(username), HttpStatus.CREATED);
//    }
//
//    // 첨부 파일 업로드
//    @PostMapping("/attached-file")
//    public ResponseEntity<?> uploadAttachedFile(long idx, String createdBy, MultipartFile[] files) throws Exception {
//        String fileValidationResult = FileValidator.isImageFileValid(files);
//
//        // 파일 mime type 검사
//        if (!"valid".equals(fileValidationResult)) {
//            throw new FileTypeException(fileValidationResult);
//        }
//
//        userAttachedFileService.uploadAttachedFile(idx, createdBy, files);
//
//        return new ResponseEntity<>("성공!", HttpStatus.CREATED);
//    }
//
//    // 첨부 파일 삭제
//    @DeleteMapping("/attached-file/{idx}")
//    public ResponseEntity<?> deleteAttachedFile(@PathVariable("idx") long idx) throws Exception {
//        userAttachedFileService.deleteAttachedFile(idx);
//
//        return new ResponseEntity<>("성공!", HttpStatus.OK);
//    }
//}