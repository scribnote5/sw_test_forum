package com.suresoft.sw_test_forum.trouble_shooting.cover_tool.service;

import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.domain.CoverToolAttachedFile;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.CoverToolDto;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.dto.mapper.CoverToolMapper;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.repository.CoverToolAttachedFileRepository;
import com.suresoft.sw_test_forum.trouble_shooting.cover_tool.repository.CoverToolAttachedFileRepositoryImpl;
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
public class CoverToolAttachedFileService {
    private final CoverToolAttachedFileRepository coverToolAAttachedFileRepository;
    private final CoverToolAttachedFileRepositoryImpl coverToolAAttachedFileRepositoryImpl;

    public CoverToolAttachedFileService(CoverToolAttachedFileRepository coverToolAAttachedFileRepository, CoverToolAttachedFileRepositoryImpl coverToolAAttachedFileRepositoryImpl) {
        this.coverToolAAttachedFileRepository = coverToolAAttachedFileRepository;
        this.coverToolAAttachedFileRepositoryImpl = coverToolAAttachedFileRepositoryImpl;
    }

    public CoverToolDto findAttachedFileByCoverToolIdx(CoverToolDto coverToolDto) {
        return CoverToolMapper.INSTANCE.toDtoByAttachedFileList(coverToolDto, coverToolAAttachedFileRepositoryImpl.findAttachedFileByCoverToolIdx(coverToolDto.getIdx()));
    }

    public void insertAttachedFile(CoverToolAttachedFile attachedFile) {
        coverToolAAttachedFileRepository.save(attachedFile);
    }

    public CoverToolAttachedFile findAttachedFileByIdx(long idx) {
        return coverToolAAttachedFileRepository.findById(idx).orElse(new CoverToolAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        coverToolAAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByCoverToolIdx(long idx) {
        return coverToolAAttachedFileRepositoryImpl.deleteAttachedFileByCoverToolIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param coverToolIdx
     * @param files
     */
    public void uploadAttachedFile(long coverToolIdx, MultipartFile[] files) throws Exception {
        CoverToolAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = CoverToolAttachedFile.builder()
                    .coverToolIdx(coverToolIdx)
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
            CoverToolAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param coverToolIdx
     */
    public void deleteAllAttachedFile(long coverToolIdx) throws Exception {
        List<CoverToolAttachedFile> attachedFileList = coverToolAAttachedFileRepositoryImpl.findAttachedFileByCoverToolIdx(coverToolIdx);

        for (CoverToolAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByCoverToolIdx(coverToolIdx);
    }
}