package com.suresoft.sw_test_forum.trouble_shooting.toolchain.service;

import com.suresoft.sw_test_forum.trouble_shooting.toolchain.domain.ToolchainAttachedFile;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.ToolchainDto;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.dto.mapper.ToolchainMapper;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.repository.ToolchainAttachedFileRepository;
import com.suresoft.sw_test_forum.trouble_shooting.toolchain.repository.ToolchainAttachedFileRepositoryImpl;
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
public class ToolchainAttachedFileService {
    private final ToolchainAttachedFileRepository toolchainAAttachedFileRepository;
    private final ToolchainAttachedFileRepositoryImpl toolchainAAttachedFileRepositoryImpl;

    public ToolchainAttachedFileService(ToolchainAttachedFileRepository toolchainAAttachedFileRepository, ToolchainAttachedFileRepositoryImpl toolchainAAttachedFileRepositoryImpl) {
        this.toolchainAAttachedFileRepository = toolchainAAttachedFileRepository;
        this.toolchainAAttachedFileRepositoryImpl = toolchainAAttachedFileRepositoryImpl;
    }

    public ToolchainDto findAttachedFileByToolchainIdx(ToolchainDto toolchainDto) {
        return ToolchainMapper.INSTANCE.toDtoByAttachedFileList(toolchainDto, toolchainAAttachedFileRepositoryImpl.findAttachedFileByToolchainIdx(toolchainDto.getIdx()));
    }

    public void insertAttachedFile(ToolchainAttachedFile attachedFile) {
        toolchainAAttachedFileRepository.save(attachedFile);
    }

    public ToolchainAttachedFile findAttachedFileByIdx(long idx) {
        return toolchainAAttachedFileRepository.findById(idx).orElse(new ToolchainAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        toolchainAAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByToolchainIdx(long idx) {
        return toolchainAAttachedFileRepositoryImpl.deleteAttachedFileByToolchainIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param toolchainIdx
     * @param files
     */
    public void uploadAttachedFile(long toolchainIdx, MultipartFile[] files) throws Exception {
        ToolchainAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = ToolchainAttachedFile.builder()
                    .toolchainIdx(toolchainIdx)
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
            ToolchainAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param toolchainIdx
     */
    public void deleteAllAttachedFile(long toolchainIdx) throws Exception {
        List<ToolchainAttachedFile> attachedFileList = toolchainAAttachedFileRepositoryImpl.findAttachedFileByToolchainIdx(toolchainIdx);

        for (ToolchainAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByToolchainIdx(toolchainIdx);
    }
}