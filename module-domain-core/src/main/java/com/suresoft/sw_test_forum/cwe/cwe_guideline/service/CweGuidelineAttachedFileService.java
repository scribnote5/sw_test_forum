package com.suresoft.sw_test_forum.cwe.cwe_guideline.service;

import com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.CweGuidelineAttachedFile;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.CweGuidelineDto;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.dto.mapper.CweGuidelineMapper;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.repository.CweGuidelineAttachedFileRepository;
import com.suresoft.sw_test_forum.cwe.cwe_guideline.repository.CweGuidelineAttachedFileRepositoryImpl;
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
public class CweGuidelineAttachedFileService {
    private final CweGuidelineAttachedFileRepository cweGuidelineAttachedFileRepository;
    private final CweGuidelineAttachedFileRepositoryImpl cweGuidelineAttachedFileRepositoryImpl;

    public CweGuidelineAttachedFileService(CweGuidelineAttachedFileRepository cweGuidelineAttachedFileRepository, CweGuidelineAttachedFileRepositoryImpl cweGuidelineAttachedFileRepositoryImpl) {
        this.cweGuidelineAttachedFileRepository = cweGuidelineAttachedFileRepository;
        this.cweGuidelineAttachedFileRepositoryImpl = cweGuidelineAttachedFileRepositoryImpl;
    }

    public CweGuidelineDto findAttachedFileByCweGuidelineIdx(CweGuidelineDto cweGuidelineDto) {
        return CweGuidelineMapper.INSTANCE.toDtoByAttachedFileList(cweGuidelineDto, cweGuidelineAttachedFileRepositoryImpl.findAttachedFileByCweGuidelineIdx(cweGuidelineDto.getIdx()));
    }

    public void insertAttachedFile(CweGuidelineAttachedFile attachedFile) {
        cweGuidelineAttachedFileRepository.save(attachedFile);
    }

    public CweGuidelineAttachedFile findAttachedFileByIdx(long idx) {
        return cweGuidelineAttachedFileRepository.findById(idx).orElse(new CweGuidelineAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        cweGuidelineAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByCweGuidelineIdx(long idx) {
        return cweGuidelineAttachedFileRepositoryImpl.deleteAttachedFileByCweGuidelineIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param cweGuidelineIdx
     * @param files
     */
    public void uploadAttachedFile(long cweGuidelineIdx, MultipartFile[] files) throws Exception {
        CweGuidelineAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = CweGuidelineAttachedFile.builder()
                    .cweGuidelineIdx(cweGuidelineIdx)
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
            CweGuidelineAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param cweGuidelineIdx
     */
    public void deleteAllAttachedFile(long cweGuidelineIdx) throws Exception {
        List<CweGuidelineAttachedFile> attachedFileList = cweGuidelineAttachedFileRepositoryImpl.findAttachedFileByCweGuidelineIdx(cweGuidelineIdx);

        for (CweGuidelineAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByCweGuidelineIdx(cweGuidelineIdx);
    }
}