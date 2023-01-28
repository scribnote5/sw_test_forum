package com.suresoft.sw_test_forum.misra_cpp.misra_cpp.service;

import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.MisraCppAttachedFile;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.MisraCppDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.dto.mapper.MisraCppMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.repository.MisraCppAttachedFileRepository;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.repository.MisraCppAttachedFileRepositoryImpl;
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
public class MisraCppAttachedFileService {
    private final MisraCppAttachedFileRepository misraCppAttachedFileRepository;
    private final MisraCppAttachedFileRepositoryImpl misraCppAttachedFileRepositoryImpl;

    public MisraCppAttachedFileService(MisraCppAttachedFileRepository misraCppAttachedFileRepository, MisraCppAttachedFileRepositoryImpl misraCppAttachedFileRepositoryImpl) {
        this.misraCppAttachedFileRepository = misraCppAttachedFileRepository;
        this.misraCppAttachedFileRepositoryImpl = misraCppAttachedFileRepositoryImpl;
    }

    public MisraCppDto findAttachedFileByMisraCppIdx(MisraCppDto misraCppDto) {
        return MisraCppMapper.INSTANCE.toDtoByAttachedFileList(misraCppDto, misraCppAttachedFileRepositoryImpl.findAttachedFileByMisraCppIdx(misraCppDto.getIdx()));
    }

    public void insertAttachedFile(MisraCppAttachedFile attachedFile) {
        misraCppAttachedFileRepository.save(attachedFile);
    }

    public MisraCppAttachedFile findAttachedFileByIdx(long idx) {
        return misraCppAttachedFileRepository.findById(idx).orElse(new MisraCppAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        misraCppAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByMisraCppIdx(long idx) {
        return misraCppAttachedFileRepositoryImpl.deleteAttachedFileByMisraCppIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param misraCppIdx
     * @param files
     */
    public void uploadAttachedFile(long misraCppIdx, MultipartFile[] files) throws Exception {
        MisraCppAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = MisraCppAttachedFile.builder()
                    .misraCppIdx(misraCppIdx)
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
            MisraCppAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param misraCppIdx
     */
    public void deleteAllAttachedFile(long misraCppIdx) throws Exception {
        List<MisraCppAttachedFile> attachedFileList = misraCppAttachedFileRepositoryImpl.findAttachedFileByMisraCppIdx(misraCppIdx);

        for (MisraCppAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByMisraCppIdx(misraCppIdx);
    }
}