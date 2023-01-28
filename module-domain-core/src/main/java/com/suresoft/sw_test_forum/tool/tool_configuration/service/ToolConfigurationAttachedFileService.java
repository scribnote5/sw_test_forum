package com.suresoft.sw_test_forum.tool.tool_configuration.service;

import com.suresoft.sw_test_forum.tool.tool_configuration.domain.ToolConfigurationAttachedFile;
import com.suresoft.sw_test_forum.tool.tool_configuration.dto.ToolConfigurationDto;
import com.suresoft.sw_test_forum.tool.tool_configuration.dto.mapper.ConfigurationMapper;
import com.suresoft.sw_test_forum.tool.tool_configuration.repository.ToolConfigurationAttachedFileRepository;
import com.suresoft.sw_test_forum.tool.tool_configuration.repository.ToolConfigurationAttachedFileRepositoryImpl;
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
public class ToolConfigurationAttachedFileService {
    private final ToolConfigurationAttachedFileRepository toolConfigurationAttachedFileRepository;
    private final ToolConfigurationAttachedFileRepositoryImpl toolConfigurationAttachedFileRepositoryImpl;

    public ToolConfigurationAttachedFileService(ToolConfigurationAttachedFileRepository toolConfigurationAttachedFileRepository, ToolConfigurationAttachedFileRepositoryImpl toolConfigurationAttachedFileRepositoryImpl) {
        this.toolConfigurationAttachedFileRepository = toolConfigurationAttachedFileRepository;
        this.toolConfigurationAttachedFileRepositoryImpl = toolConfigurationAttachedFileRepositoryImpl;
    }

    public ToolConfigurationDto findAttachedFileByConfigurationIdx(ToolConfigurationDto toolConfigurationDto) {
        return ConfigurationMapper.INSTANCE.toDtoByAttachedFileList(toolConfigurationDto, toolConfigurationAttachedFileRepositoryImpl.findAttachedFileByConfigurationIdx(toolConfigurationDto.getIdx()));
    }

    public void insertAttachedFile(ToolConfigurationAttachedFile attachedFile) {
        toolConfigurationAttachedFileRepository.save(attachedFile);
    }

    public ToolConfigurationAttachedFile findAttachedFileByIdx(long idx) {
        return toolConfigurationAttachedFileRepository.findById(idx).orElse(new ToolConfigurationAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        toolConfigurationAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByConfigurationIdx(long idx) {
        return toolConfigurationAttachedFileRepositoryImpl.deleteAttachedFileByConfigurationIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param toolConfigurationIdx
     * @param files
     */
    public void uploadAttachedFile(long toolConfigurationIdx, MultipartFile[] files) throws Exception {
        ToolConfigurationAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = ToolConfigurationAttachedFile.builder()
                    .toolConfigurationIdx(toolConfigurationIdx)
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
            ToolConfigurationAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param toolConfigurationIdx
     */
    public void deleteAllAttachedFile(long toolConfigurationIdx) throws Exception {
        List<ToolConfigurationAttachedFile> attachedFileList = toolConfigurationAttachedFileRepositoryImpl.findAttachedFileByConfigurationIdx(toolConfigurationIdx);

        for (ToolConfigurationAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByConfigurationIdx(toolConfigurationIdx);
    }
}