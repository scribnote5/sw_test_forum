package com.suresoft.sw_test_forum.cwe_java.cwe_java.service;

import com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.CweJavaAttachedFile;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.CweJavaDto;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.dto.mapper.CweJavaMapper;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.repository.CweJavaAttachedFileRepository;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.repository.CweJavaAttachedFileRepositoryImpl;
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
public class CweJavaAttachedFileService {
    private final CweJavaAttachedFileRepository cweJavaAttachedFileRepository;
    private final CweJavaAttachedFileRepositoryImpl cweJavaAttachedFileRepositoryImpl;

    public CweJavaAttachedFileService(CweJavaAttachedFileRepository cweJavaAttachedFileRepository, CweJavaAttachedFileRepositoryImpl cweJavaAttachedFileRepositoryImpl) {
        this.cweJavaAttachedFileRepository = cweJavaAttachedFileRepository;
        this.cweJavaAttachedFileRepositoryImpl = cweJavaAttachedFileRepositoryImpl;
    }

    public CweJavaDto findAttachedFileByCweJavaIdx(CweJavaDto cweJavaDto) {
        return CweJavaMapper.INSTANCE.toDtoByAttachedFileList(cweJavaDto, cweJavaAttachedFileRepositoryImpl.findAttachedFileByCweJavaIdx(cweJavaDto.getIdx()));
    }

    public void insertAttachedFile(CweJavaAttachedFile attachedFile) {
        cweJavaAttachedFileRepository.save(attachedFile);
    }

    public CweJavaAttachedFile findAttachedFileByIdx(long idx) {
        return cweJavaAttachedFileRepository.findById(idx).orElse(new CweJavaAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        cweJavaAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByCweJavaIdx(long idx) {
        return cweJavaAttachedFileRepositoryImpl.deleteAttachedFileByCweJavaIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param cweJavaIdx
     * @param files
     */
    public void uploadAttachedFile(long cweJavaIdx, MultipartFile[] files) throws Exception {
        CweJavaAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = CweJavaAttachedFile.builder()
                    .cweJavaIdx(cweJavaIdx)
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
            CweJavaAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param cweJavaIdx
     */
    public void deleteAllAttachedFile(long cweJavaIdx) throws Exception {
        List<CweJavaAttachedFile> attachedFileList = cweJavaAttachedFileRepositoryImpl.findAttachedFileByCweJavaIdx(cweJavaIdx);

        for (CweJavaAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByCweJavaIdx(cweJavaIdx);
    }
}