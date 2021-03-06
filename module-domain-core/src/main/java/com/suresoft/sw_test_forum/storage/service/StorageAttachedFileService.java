package com.suresoft.sw_test_forum.storage.service;

import com.suresoft.sw_test_forum.storage.domain.StorageAttachedFile;
import com.suresoft.sw_test_forum.storage.dto.StorageDto;
import com.suresoft.sw_test_forum.storage.dto.mapper.StorageMapper;
import com.suresoft.sw_test_forum.storage.repository.StorageAttachedFileRepository;
import com.suresoft.sw_test_forum.storage.repository.StorageAttachedFileRepositoryImpl;
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
public class StorageAttachedFileService {
    private final StorageAttachedFileRepository storageAttachedFileRepository;
    private final StorageAttachedFileRepositoryImpl storageAttachedFileRepositoryImpl;

    public StorageAttachedFileService(StorageAttachedFileRepository storageAttachedFileRepository, StorageAttachedFileRepositoryImpl storageAttachedFileRepositoryImpl) {
        this.storageAttachedFileRepository = storageAttachedFileRepository;
        this.storageAttachedFileRepositoryImpl = storageAttachedFileRepositoryImpl;
    }

    public StorageDto findAttachedFileByStorageIdx(StorageDto storageDto) {
        return StorageMapper.INSTANCE.toDtoByAttachedFileList(storageDto, storageAttachedFileRepositoryImpl.findAttachedFileByStorageIdx(storageDto.getIdx()));
    }

    public void insertAttachedFile(StorageAttachedFile attachedFile) {
        storageAttachedFileRepository.save(attachedFile);
    }

    public StorageAttachedFile findAttachedFileByIdx(long idx) {
        return storageAttachedFileRepository.findById(idx).orElse(new StorageAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        storageAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByStorageIdx(long idx) {
        return storageAttachedFileRepositoryImpl.deleteAttachedFileByStorageIdx(idx);
    }

    /**
     * ?????? ?????? ?????????
     *
     * @param storageIdx
     * @param files
     */
    public void uploadAttachedFile(long storageIdx, MultipartFile[] files) throws Exception {
        StorageAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = StorageAttachedFile.builder()
                    .storageIdx(storageIdx)
                    .fileName(file.getOriginalFilename())
                    .savedFileName(savedFileName)
                    .fileSize(file.getSize())
                    .build();

            insertAttachedFile(uploadFile);
        }
    }

    /**
     * ?????? ?????? ??????
     *
     * @param deleteAttachedFileIdxList
     */
    public void deleteAttachedFile(List<Long> deleteAttachedFileIdxList) throws Exception {
        for (long idx : deleteAttachedFileIdxList) {
            StorageAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * ?????? ?????? ?????? ??????
     *
     * @param storageIdx
     */
    public void deleteAllAttachedFile(long storageIdx) throws Exception {
        List<StorageAttachedFile> attachedFileList = storageAttachedFileRepositoryImpl.findAttachedFileByStorageIdx(storageIdx);

        for (StorageAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByStorageIdx(storageIdx);
    }
}