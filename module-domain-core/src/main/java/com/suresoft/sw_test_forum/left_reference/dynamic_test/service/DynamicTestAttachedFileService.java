package com.suresoft.sw_test_forum.left_reference.dynamic_test.service;

import com.suresoft.sw_test_forum.left_reference.dynamic_test.domain.DynamicTestAttachedFile;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.DynamicTestDto;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.dto.mapper.DynamicTestMapper;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.repository.DynamicTestAttachedFileRepository;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.repository.DynamicTestAttachedFileRepositoryImpl;
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
public class DynamicTestAttachedFileService {
    private final DynamicTestAttachedFileRepository dynamicTestAttachedFileRepository;
    private final DynamicTestAttachedFileRepositoryImpl dynamicTestAttachedFileRepositoryImpl;

    public DynamicTestAttachedFileService(DynamicTestAttachedFileRepository dynamicTestAttachedFileRepository, DynamicTestAttachedFileRepositoryImpl dynamicTestAttachedFileRepositoryImpl) {
        this.dynamicTestAttachedFileRepository = dynamicTestAttachedFileRepository;
        this.dynamicTestAttachedFileRepositoryImpl = dynamicTestAttachedFileRepositoryImpl;
    }

    public DynamicTestDto findAttachedFileByDynamicTestIdx(DynamicTestDto dynamicTestDto) {
        return DynamicTestMapper.INSTANCE.toDtoByAttachedFileList(dynamicTestDto, dynamicTestAttachedFileRepositoryImpl.findAttachedFileByDynamicTestIdx(dynamicTestDto.getIdx()));
    }

    public void insertAttachedFile(DynamicTestAttachedFile attachedFile) {
        dynamicTestAttachedFileRepository.save(attachedFile);
    }

    public DynamicTestAttachedFile findAttachedFileByIdx(long idx) {
        return dynamicTestAttachedFileRepository.findById(idx).orElse(new DynamicTestAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        dynamicTestAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByDynamicTestIdx(long idx) {
        return dynamicTestAttachedFileRepositoryImpl.deleteAttachedFileByDynamicTestIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param dynamicTestIdx
     * @param files
     */
    public void uploadAttachedFile(long dynamicTestIdx, MultipartFile[] files) throws Exception {
        DynamicTestAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = DynamicTestAttachedFile.builder()
                    .dynamicTestIdx(dynamicTestIdx)
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
            DynamicTestAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param dynamicTestIdx
     */
    public void deleteAllAttachedFile(long dynamicTestIdx) throws Exception {
        List<DynamicTestAttachedFile> attachedFileList = dynamicTestAttachedFileRepositoryImpl.findAttachedFileByDynamicTestIdx(dynamicTestIdx);

        for (DynamicTestAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByDynamicTestIdx(dynamicTestIdx);
    }
}