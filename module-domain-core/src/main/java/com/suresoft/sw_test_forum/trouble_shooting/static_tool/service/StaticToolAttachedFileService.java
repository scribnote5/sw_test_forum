package com.suresoft.sw_test_forum.trouble_shooting.static_tool.service;

import com.suresoft.sw_test_forum.trouble_shooting.static_tool.domain.StaticToolAttachedFile;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.StaticToolDto;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.dto.mapper.StaticToolMapper;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.repository.StaticToolAttachedFileRepository;
import com.suresoft.sw_test_forum.trouble_shooting.static_tool.repository.StaticToolAttachedFileRepositoryImpl;
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
public class StaticToolAttachedFileService {
    private final StaticToolAttachedFileRepository staticToolAAttachedFileRepository;
    private final StaticToolAttachedFileRepositoryImpl staticToolAAttachedFileRepositoryImpl;

    public StaticToolAttachedFileService(StaticToolAttachedFileRepository staticToolAAttachedFileRepository, StaticToolAttachedFileRepositoryImpl staticToolAAttachedFileRepositoryImpl) {
        this.staticToolAAttachedFileRepository = staticToolAAttachedFileRepository;
        this.staticToolAAttachedFileRepositoryImpl = staticToolAAttachedFileRepositoryImpl;
    }

    public StaticToolDto findAttachedFileByStaticToolIdx(StaticToolDto staticToolDto) {
        return StaticToolMapper.INSTANCE.toDtoByAttachedFileList(staticToolDto, staticToolAAttachedFileRepositoryImpl.findAttachedFileByStaticToolIdx(staticToolDto.getIdx()));
    }

    public void insertAttachedFile(StaticToolAttachedFile attachedFile) {
        staticToolAAttachedFileRepository.save(attachedFile);
    }

    public StaticToolAttachedFile findAttachedFileByIdx(long idx) {
        return staticToolAAttachedFileRepository.findById(idx).orElse(new StaticToolAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        staticToolAAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByStaticToolIdx(long idx) {
        return staticToolAAttachedFileRepositoryImpl.deleteAttachedFileByStaticToolIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param staticToolIdx
     * @param files
     */
    public void uploadAttachedFile(long staticToolIdx, MultipartFile[] files) throws Exception {
        StaticToolAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = StaticToolAttachedFile.builder()
                    .staticToolIdx(staticToolIdx)
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
            StaticToolAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param staticToolIdx
     */
    public void deleteAllAttachedFile(long staticToolIdx) throws Exception {
        List<StaticToolAttachedFile> attachedFileList = staticToolAAttachedFileRepositoryImpl.findAttachedFileByStaticToolIdx(staticToolIdx);

        for (StaticToolAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByStaticToolIdx(staticToolIdx);
    }
}