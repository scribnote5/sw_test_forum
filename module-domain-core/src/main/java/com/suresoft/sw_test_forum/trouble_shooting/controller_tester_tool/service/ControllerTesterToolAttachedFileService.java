package com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.service;

import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.domain.ControllerTesterToolAttachedFile;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.ControllerTesterToolDto;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.dto.mapper.ControllerTesterToolMapper;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.repository.ControllerTesterToolAttachedFileRepository;
import com.suresoft.sw_test_forum.trouble_shooting.controller_tester_tool.repository.ControllerTesterToolAttachedFileRepositoryImpl;
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
public class ControllerTesterToolAttachedFileService {
    private final ControllerTesterToolAttachedFileRepository controllerTesterToolAAttachedFileRepository;
    private final ControllerTesterToolAttachedFileRepositoryImpl controllerTesterToolAAttachedFileRepositoryImpl;

    public ControllerTesterToolAttachedFileService(ControllerTesterToolAttachedFileRepository controllerTesterToolAAttachedFileRepository, ControllerTesterToolAttachedFileRepositoryImpl controllerTesterToolAAttachedFileRepositoryImpl) {
        this.controllerTesterToolAAttachedFileRepository = controllerTesterToolAAttachedFileRepository;
        this.controllerTesterToolAAttachedFileRepositoryImpl = controllerTesterToolAAttachedFileRepositoryImpl;
    }

    public ControllerTesterToolDto findAttachedFileByControllerTesterToolIdx(ControllerTesterToolDto controllerTesterToolDto) {
        return ControllerTesterToolMapper.INSTANCE.toDtoByAttachedFileList(controllerTesterToolDto, controllerTesterToolAAttachedFileRepositoryImpl.findAttachedFileByControllerTesterToolIdx(controllerTesterToolDto.getIdx()));
    }

    public void insertAttachedFile(ControllerTesterToolAttachedFile attachedFile) {
        controllerTesterToolAAttachedFileRepository.save(attachedFile);
    }

    public ControllerTesterToolAttachedFile findAttachedFileByIdx(long idx) {
        return controllerTesterToolAAttachedFileRepository.findById(idx).orElse(new ControllerTesterToolAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        controllerTesterToolAAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByControllerTesterToolIdx(long idx) {
        return controllerTesterToolAAttachedFileRepositoryImpl.deleteAttachedFileByControllerTesterToolIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param controllerTesterToolIdx
     * @param files
     */
    public void uploadAttachedFile(long controllerTesterToolIdx, MultipartFile[] files) throws Exception {
        ControllerTesterToolAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = ControllerTesterToolAttachedFile.builder()
                    .controllerTesterToolIdx(controllerTesterToolIdx)
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
            ControllerTesterToolAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param controllerTesterToolIdx
     */
    public void deleteAllAttachedFile(long controllerTesterToolIdx) throws Exception {
        List<ControllerTesterToolAttachedFile> attachedFileList = controllerTesterToolAAttachedFileRepositoryImpl.findAttachedFileByControllerTesterToolIdx(controllerTesterToolIdx);

        for (ControllerTesterToolAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByControllerTesterToolIdx(controllerTesterToolIdx);
    }
}