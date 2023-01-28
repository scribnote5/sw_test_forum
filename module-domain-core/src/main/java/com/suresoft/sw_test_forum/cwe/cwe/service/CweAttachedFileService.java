package com.suresoft.sw_test_forum.cwe.cwe.service;

import com.suresoft.sw_test_forum.cwe.cwe.domain.CweAttachedFile;
import com.suresoft.sw_test_forum.cwe.cwe.dto.CweDto;
import com.suresoft.sw_test_forum.cwe.cwe.dto.mapper.CweMapper;
import com.suresoft.sw_test_forum.cwe.cwe.repository.CweAttachedFileRepository;
import com.suresoft.sw_test_forum.cwe.cwe.repository.CweAttachedFileRepositoryImpl;
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
public class CweAttachedFileService {
    private final CweAttachedFileRepository cweAttachedFileRepository;
    private final CweAttachedFileRepositoryImpl cweAttachedFileRepositoryImpl;

    public CweAttachedFileService(CweAttachedFileRepository cweAttachedFileRepository, CweAttachedFileRepositoryImpl cweAttachedFileRepositoryImpl) {
        this.cweAttachedFileRepository = cweAttachedFileRepository;
        this.cweAttachedFileRepositoryImpl = cweAttachedFileRepositoryImpl;
    }

    public CweDto findAttachedFileByCweIdx(CweDto cweDto) {
        return CweMapper.INSTANCE.toDtoByAttachedFileList(cweDto, cweAttachedFileRepositoryImpl.findAttachedFileByCweIdx(cweDto.getIdx()));
    }

    public void insertAttachedFile(CweAttachedFile attachedFile) {
        cweAttachedFileRepository.save(attachedFile);
    }

    public CweAttachedFile findAttachedFileByIdx(long idx) {
        return cweAttachedFileRepository.findById(idx).orElse(new CweAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        cweAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByCweIdx(long idx) {
        return cweAttachedFileRepositoryImpl.deleteAttachedFileByCweIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param cweIdx
     * @param files
     */
    public void uploadAttachedFile(long cweIdx, MultipartFile[] files) throws Exception {
        CweAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = CweAttachedFile.builder()
                    .cweIdx(cweIdx)
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
            CweAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param cweIdx
     */
    public void deleteAllAttachedFile(long cweIdx) throws Exception {
        List<CweAttachedFile> attachedFileList = cweAttachedFileRepositoryImpl.findAttachedFileByCweIdx(cweIdx);

        for (CweAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByCweIdx(cweIdx);
    }
}