package com.suresoft.sw_test_forum.tool.tool_usage_method.service;

import com.suresoft.sw_test_forum.tool.tool_usage_method.domain.ToolUsageMethodAttachedFile;
import com.suresoft.sw_test_forum.tool.tool_usage_method.dto.ToolUsageMethodDto;
import com.suresoft.sw_test_forum.tool.tool_usage_method.dto.mapper.ToolUsageMethodMapper;
import com.suresoft.sw_test_forum.tool.tool_usage_method.repository.ToolUsageMethodAttachedFileRepository;
import com.suresoft.sw_test_forum.tool.tool_usage_method.repository.ToolUsageMethodAttachedFileRepositoryImpl;
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
public class ToolUsageMethodAttachedFileService {
    private final ToolUsageMethodAttachedFileRepository toolUsageMethodAAttachedFileRepository;
    private final ToolUsageMethodAttachedFileRepositoryImpl toolUsageMethodAAttachedFileRepositoryImpl;

    public ToolUsageMethodAttachedFileService(ToolUsageMethodAttachedFileRepository toolUsageMethodAAttachedFileRepository, ToolUsageMethodAttachedFileRepositoryImpl toolUsageMethodAAttachedFileRepositoryImpl) {
        this.toolUsageMethodAAttachedFileRepository = toolUsageMethodAAttachedFileRepository;
        this.toolUsageMethodAAttachedFileRepositoryImpl = toolUsageMethodAAttachedFileRepositoryImpl;
    }

    public ToolUsageMethodDto findAttachedFileByToolUsageMethodIdx(ToolUsageMethodDto toolUsageMethodDto) {
        return ToolUsageMethodMapper.INSTANCE.toDtoByAttachedFileList(toolUsageMethodDto, toolUsageMethodAAttachedFileRepositoryImpl.findAttachedFileByToolUsageMethodIdx(toolUsageMethodDto.getIdx()));
    }

    public void insertAttachedFile(ToolUsageMethodAttachedFile attachedFile) {
        toolUsageMethodAAttachedFileRepository.save(attachedFile);
    }

    public ToolUsageMethodAttachedFile findAttachedFileByIdx(long idx) {
        return toolUsageMethodAAttachedFileRepository.findById(idx).orElse(new ToolUsageMethodAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        toolUsageMethodAAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByToolUsageMethodIdx(long idx) {
        return toolUsageMethodAAttachedFileRepositoryImpl.deleteAttachedFileByToolUsageMethodIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param toolUsageMethodIdx
     * @param files
     */
    public void uploadAttachedFile(long toolUsageMethodIdx, MultipartFile[] files) throws Exception {
        ToolUsageMethodAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = ToolUsageMethodAttachedFile.builder()
                    .toolUsageMethodIdx(toolUsageMethodIdx)
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
            ToolUsageMethodAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param toolUsageMethodIdx
     */
    public void deleteAllAttachedFile(long toolUsageMethodIdx) throws Exception {
        List<ToolUsageMethodAttachedFile> attachedFileList = toolUsageMethodAAttachedFileRepositoryImpl.findAttachedFileByToolUsageMethodIdx(toolUsageMethodIdx);

        for (ToolUsageMethodAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByToolUsageMethodIdx(toolUsageMethodIdx);
    }
}