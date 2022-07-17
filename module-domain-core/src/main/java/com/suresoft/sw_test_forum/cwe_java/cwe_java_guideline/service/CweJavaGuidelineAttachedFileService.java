package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.service;

import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.CweJavaGuidelineAttachedFile;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.CweJavaGuidelineDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.dto.mapper.CweJavaGuidelineMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository.CweJavaGuidelineAttachedFileRepository;
import com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository.CweJavaGuidelineAttachedFileRepositoryImpl;
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
public class CweJavaGuidelineAttachedFileService {
    private final CweJavaGuidelineAttachedFileRepository cweJavaGuidelineAttachedFileRepository;
    private final CweJavaGuidelineAttachedFileRepositoryImpl cweJavaGuidelineAttachedFileRepositoryImpl;

    public CweJavaGuidelineAttachedFileService(CweJavaGuidelineAttachedFileRepository cweJavaGuidelineAttachedFileRepository, CweJavaGuidelineAttachedFileRepositoryImpl cweJavaGuidelineAttachedFileRepositoryImpl) {
        this.cweJavaGuidelineAttachedFileRepository = cweJavaGuidelineAttachedFileRepository;
        this.cweJavaGuidelineAttachedFileRepositoryImpl = cweJavaGuidelineAttachedFileRepositoryImpl;
    }

    public CweJavaGuidelineDto findAttachedFileByCweJavaGuidelineIdx(CweJavaGuidelineDto cweJavaGuidelineDto) {
        return CweJavaGuidelineMapper.INSTANCE.toDtoByAttachedFileList(cweJavaGuidelineDto, cweJavaGuidelineAttachedFileRepositoryImpl.findAttachedFileByCweJavaGuidelineIdx(cweJavaGuidelineDto.getIdx()));
    }

    public void insertAttachedFile(CweJavaGuidelineAttachedFile attachedFile) {
        cweJavaGuidelineAttachedFileRepository.save(attachedFile);
    }

    public CweJavaGuidelineAttachedFile findAttachedFileByIdx(long idx) {
        return cweJavaGuidelineAttachedFileRepository.findById(idx).orElse(new CweJavaGuidelineAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        cweJavaGuidelineAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByCweJavaGuidelineIdx(long idx) {
        return cweJavaGuidelineAttachedFileRepositoryImpl.deleteAttachedFileByCweJavaGuidelineIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param cweJavaGuidelineIdx
     * @param files
     */
    public void uploadAttachedFile(long cweJavaGuidelineIdx, MultipartFile[] files) throws Exception {
        CweJavaGuidelineAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = CweJavaGuidelineAttachedFile.builder()
                    .cweJavaGuidelineIdx(cweJavaGuidelineIdx)
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
            CweJavaGuidelineAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param cweJavaGuidelineIdx
     */
    public void deleteAllAttachedFile(long cweJavaGuidelineIdx) throws Exception {
        List<CweJavaGuidelineAttachedFile> attachedFileList = cweJavaGuidelineAttachedFileRepositoryImpl.findAttachedFileByCweJavaGuidelineIdx(cweJavaGuidelineIdx);

        for (CweJavaGuidelineAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByCweJavaGuidelineIdx(cweJavaGuidelineIdx);
    }
}