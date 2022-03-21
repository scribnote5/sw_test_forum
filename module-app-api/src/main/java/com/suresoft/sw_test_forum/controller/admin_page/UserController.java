package com.suresoft.sw_test_forum.controller.admin_page;

import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.dto.UserDto;
import com.suresoft.sw_test_forum.admin_page.user.dto.UserSearchDto;
import com.suresoft.sw_test_forum.admin_page.user.service.UserAttachedFileService;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.common.validation.FileValidator;
import com.suresoft.sw_test_forum.exception.FileTypeException;
import com.suresoft.sw_test_forum.exception.InvalidAuthorityTypeException;
import com.suresoft.sw_test_forum.exception.InvalidUsernameException;
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
@RequestMapping("/api/users")
public class UserController {
    private final UserService userService;
    private final UserAttachedFileService userAttachedFileService;

    public UserController(UserService userService,
                          UserAttachedFileService userAttachedFileService) {
        this.userService = userService;
        this.userAttachedFileService = userAttachedFileService;
    }

    /**
     * 리스트 페이지에서, 리스트 조회
     *
     * @param pageable
     * @param userSearchDto
     * @return
     */
    @GetMapping("/list")
    public ResponseEntity getUserList(Pageable pageable, UserSearchDto userSearchDto) {
        Page<UserDto> userDtoList = userService.findUserList(pageable, userSearchDto);

        return new ResponseEntity(userDtoList, HttpStatus.OK);
    }

    /**
     * 리스트 페이지에서, 접근 권한 조회
     *
     * @return
     */
    @GetMapping("/list-access-authority")
    public ResponseEntity getAccessAuthority() {
        Boolean isAccess = AuthorityUtil.isAccessInRootAndManager();

        return new ResponseEntity(isAccess, HttpStatus.OK);
    }

    /**
     * 읽기 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/read/{idx}"})
    public ResponseEntity getUserWhenRead(@PathVariable("idx") long idx) {
        UserDto userDto = userService.findUserByIdx(idx);

        userDto = userAttachedFileService.findAttachedFileByUserIdx(userDto);

        return new ResponseEntity(userDto, HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 조회
     *
     * @param idx
     * @return
     */
    @GetMapping({"/form/{idx}"})
    public ResponseEntity getUserWhenForm(@PathVariable("idx") long idx) {
        UserDto userDto = userService.findUserByIdx(idx);
        ResponseEntity responseEntity;

        // 권한 확인
        if (userDto.isAccess()) {
            userDto = userAttachedFileService.findAttachedFileByUserIdx(userDto);
            userDto = userService.findUserAutoComplete(userDto);
            responseEntity = new ResponseEntity(userDto, HttpStatus.OK);
        } else {
            responseEntity = new ResponseEntity("필요한 권한이 없는 상태에서 접근하여 오류가 발생하였습니다.", HttpStatus.FORBIDDEN);
        }

        return responseEntity;
    }

    /**
     * 로그인 페이지에서, 조회
     *
     * @return
     */
    @GetMapping({"/login/form"})
    public ResponseEntity getUserWhenLogin() {
        UserDto userDto = new UserDto();
        ResponseEntity responseEntity;

        userDto = userService.findUserAutoComplete(userDto);
        responseEntity = new ResponseEntity(userDto, HttpStatus.OK);

        return responseEntity;
    }

    /**
     * 등록 및 수정 페이지에서, 중복 아이디 조회
     *
     * @param username
     * @return
     */
    @GetMapping("/validation/username/{username}")
    public ResponseEntity<?> checkDuplicateUsername(@PathVariable("username") String username) {
        return new ResponseEntity<>(userService.isDuplicationUserByUsername(username), HttpStatus.OK);
    }

    /**
     * 로그인 페이지에서, 리스트 조회
     *
     * @param pageable
     * @param userSearchDto
     * @return
     */
    @GetMapping("/manager-list")
    public ResponseEntity getManagerUserList(Pageable pageable, UserSearchDto userSearchDto) {
        List<UserDto> userDtoList = userService.findUserByAuthorityType(AuthorityType.MANAGER);

        return new ResponseEntity(userDtoList, HttpStatus.OK);
    }

    /**
     * 로그인 페이지에서, 중복 아이디 조회
     *
     * @param username
     * @return
     */
    @GetMapping("/login/validation/username/{username}")
    public ResponseEntity<?> checkDuplicateUsernameWhenLogin(@PathVariable("username") String username) {
        return new ResponseEntity<>(userService.isDuplicationUserByUsername(username), HttpStatus.OK);
    }

    /**
     * 등록 페이지에서, 등록
     *
     * @param userDto
     * @return
     */
    @PostMapping
    public ResponseEntity<?> postUser(@RequestBody @Valid UserDto userDto) {
        // 중복 ID 검사 및 ID 길이 제한
        if (userService.isDuplicationUserByUsername(userDto.getUsername())) {
            throw new InvalidUsernameException();
        }

        long idx = userService.joinUser(userDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 로그인 페이지에서, 등록
     *
     * @param userDto
     * @return
     */
    @PostMapping("/login")
    public ResponseEntity<?> postUserWhenLogin(@RequestBody @Valid UserDto userDto) {
        // 중복 ID 검사 및 ID 길이 제한
        if (userService.isDuplicationUserByUsername(userDto.getUsername())) {
            throw new InvalidUsernameException();
        }
        // NON_USER 권한 이외 등록 불가
        if (!userService.isNonUserAuthorityType(userDto.getAuthorityType())) {
            throw new InvalidAuthorityTypeException();
        }

        long idx = userService.joinUser(userDto);

        return new ResponseEntity<>(idx, HttpStatus.CREATED);
    }

    /**
     * 수정 페이지에서, 수정
     *
     * @param idx
     * @param userDto
     * @return
     */
    @PutMapping("/{idx}")
    public ResponseEntity<?> putUser(@PathVariable("idx") long idx, @RequestBody @Valid UserDto userDto) {
        userService.updateUser(idx, userDto);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 삭제 페이지에서, 삭제
     *
     * @param idx
     * @return
     * @throws Exception
     */
    @DeleteMapping("/{idx}")
    public ResponseEntity<?> deleteUser(@PathVariable("idx") long idx) throws Exception {
        userService.deleteUserByIdx(idx);
        userAttachedFileService.deleteAllAttachedFile(idx);

        return new ResponseEntity<>("{}", HttpStatus.OK);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 업로드
     *
     * @param userIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/attached-file")
    public ResponseEntity uploadAttachedFile(long userIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        userAttachedFileService.uploadAttachedFile(userIdx, files);

        return new ResponseEntity("{}", HttpStatus.CREATED);
    }

    /**
     * 로그인 페이지에서, 첨부파일 업로드
     *
     * @param userIdx
     * @param files
     * @return
     * @throws Exception
     */
    @PostMapping("/login/attached-file")
    public ResponseEntity uploadAttachedFileWhenLogin(long userIdx, MultipartFile[] files) throws Exception {
        String fileValidationResult = FileValidator.isFileValid(files);

        // 파일 mime type 검사
        if (!"valid".equals(fileValidationResult)) {
            throw new FileTypeException(fileValidationResult);
        }

        userAttachedFileService.uploadAttachedFile(userIdx, files);

        return new ResponseEntity("{}", HttpStatus.CREATED);
    }

    /**
     * 등록 및 수정 페이지에서, 첨부파일 삭제
     *
     * @param deleteAttachedFileIdxList
     * @return
     * @throws Exception
     */
    @DeleteMapping("/attached-file") // @RequestBody String deleteAttachedFileIdxList
    public ResponseEntity deleteAttachedFile(@RequestBody List<Long> deleteAttachedFileIdxList) throws Exception {
        userAttachedFileService.deleteAttachedFile(deleteAttachedFileIdxList);

        return new ResponseEntity("{}", HttpStatus.OK);
    }
}