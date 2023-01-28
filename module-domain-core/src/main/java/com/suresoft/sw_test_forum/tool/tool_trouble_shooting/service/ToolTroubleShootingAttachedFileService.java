package com.suresoft.sw_test_forum.tool.tool_trouble_shooting.service;

import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.domain.ToolTroubleShootingAttachedFile;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.ToolTroubleShootingDto;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.dto.mapper.ToolTroubleShootingMapper;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.repository.ToolTroubleShootingAttachedFileRepository;
import com.suresoft.sw_test_forum.tool.tool_trouble_shooting.repository.ToolTroubleShootingAttachedFileRepositoryImpl;
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
public class ToolTroubleShootingAttachedFileService {
    private final ToolTroubleShootingAttachedFileRepository toolTroubleShootingAAttachedFileRepository;
    private final ToolTroubleShootingAttachedFileRepositoryImpl toolTroubleShootingAAttachedFileRepositoryImpl;

    public ToolTroubleShootingAttachedFileService(ToolTroubleShootingAttachedFileRepository toolTroubleShootingAAttachedFileRepository, ToolTroubleShootingAttachedFileRepositoryImpl toolTroubleShootingAAttachedFileRepositoryImpl) {
        this.toolTroubleShootingAAttachedFileRepository = toolTroubleShootingAAttachedFileRepository;
        this.toolTroubleShootingAAttachedFileRepositoryImpl = toolTroubleShootingAAttachedFileRepositoryImpl;
    }

    public ToolTroubleShootingDto findAttachedFileByTroubleShootingIdx(ToolTroubleShootingDto toolTroubleShootingDto) {
        return ToolTroubleShootingMapper.INSTANCE.toDtoByAttachedFileList(toolTroubleShootingDto, toolTroubleShootingAAttachedFileRepositoryImpl.findAttachedFileByTroubleShootingIdx(toolTroubleShootingDto.getIdx()));
    }

    public void insertAttachedFile(ToolTroubleShootingAttachedFile attachedFile) {
        toolTroubleShootingAAttachedFileRepository.save(attachedFile);
    }

    public ToolTroubleShootingAttachedFile findAttachedFileByIdx(long idx) {
        return toolTroubleShootingAAttachedFileRepository.findById(idx).orElse(new ToolTroubleShootingAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        toolTroubleShootingAAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByTroubleShootingIdx(long idx) {
        return toolTroubleShootingAAttachedFileRepositoryImpl.deleteAttachedFileByTroubleShootingIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param toolTroubleShootingIdx
     * @param files
     */
    public void uploadAttachedFile(long toolTroubleShootingIdx, MultipartFile[] files) throws Exception {
        ToolTroubleShootingAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = ToolTroubleShootingAttachedFile.builder()
                    .toolTroubleShootingIdx(toolTroubleShootingIdx)
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
            ToolTroubleShootingAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param toolTroubleShootingIdx
     */
    public void deleteAllAttachedFile(long toolTroubleShootingIdx) throws Exception {
        List<ToolTroubleShootingAttachedFile> attachedFileList = toolTroubleShootingAAttachedFileRepositoryImpl.findAttachedFileByTroubleShootingIdx(toolTroubleShootingIdx);

        for (ToolTroubleShootingAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByTroubleShootingIdx(toolTroubleShootingIdx);
    }
}