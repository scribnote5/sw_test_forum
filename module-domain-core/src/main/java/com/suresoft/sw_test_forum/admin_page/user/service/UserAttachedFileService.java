package com.suresoft.sw_test_forum.admin_page.user.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.UserAttachedFile;
import com.suresoft.sw_test_forum.admin_page.user.dto.UserDto;
import com.suresoft.sw_test_forum.admin_page.user.dto.mapper.UserMapper;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserAttachedFileRepository;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserAttachedFileRepositoryImpl;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;

@Service
public class UserAttachedFileService {
    private final UserAttachedFileRepository userAttachedFileRepository;
    private final UserAttachedFileRepositoryImpl userAttachedFileRepositoryImpl;

    public UserAttachedFileService(UserAttachedFileRepository userAttachedFileRepository, UserAttachedFileRepositoryImpl userAttachedFileRepositoryImpl) {
        this.userAttachedFileRepository = userAttachedFileRepository;
        this.userAttachedFileRepositoryImpl = userAttachedFileRepositoryImpl;
    }

    public UserDto findAttachedFileByUserIdx(UserDto userDto) {
        return UserMapper.INSTANCE.toDtoByAttachedFileList(userDto, userAttachedFileRepositoryImpl.findAttachedFileByUserIdx(userDto.getIdx()));
    }

    public void insertAttachedFile(UserAttachedFile userAttachedFile) {
        userAttachedFileRepository.save(userAttachedFile);
    }

    public UserAttachedFile findAttachedFileByIdx(long idx) {
        return userAttachedFileRepository.findById(idx).orElse(new UserAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        userAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByUserIdx(long idx) {
        return userAttachedFileRepositoryImpl.deleteAttachedFileByUserIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param userIdx
     * @param files
     */
    public void uploadAttachedFile(long userIdx, MultipartFile[] files) throws Exception {
        UserAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = UserAttachedFile.builder()
                    .userIdx(userIdx)
                    .fileName(file.getOriginalFilename())
                    .savedFileName(savedFileName)
                    .fileSize(file.getSize())
                    .build();

            insertAttachedFile(uploadFile);
        }
    }

    /**
     * 첨부 파일 삭제
     *
     * @param deleteAttachedFileIdxList
     */
    public void deleteAttachedFile(List<Long> deleteAttachedFileIdxList) throws Exception {
        for (long idx : deleteAttachedFileIdxList) {
            UserAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param userIdx
     */
    public void deleteAllAttachedFile(long userIdx) throws Exception {
        List<UserAttachedFile> attachedFileList = userAttachedFileRepositoryImpl.findAttachedFileByUserIdx(userIdx);

        for (UserAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByUserIdx(userIdx);
    }
}
