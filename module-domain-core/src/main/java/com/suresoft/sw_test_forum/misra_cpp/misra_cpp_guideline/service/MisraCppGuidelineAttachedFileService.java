package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.service;

import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.MisraCppGuidelineAttachedFile;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.MisraCppGuidelineDto;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.dto.mapper.MisraCppGuidelineMapper;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.repository.MisraCppGuidelineAttachedFileRepository;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.repository.MisraCppGuidelineAttachedFileRepositoryImpl;
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
public class MisraCppGuidelineAttachedFileService {
    private final MisraCppGuidelineAttachedFileRepository misraCppGuidelineAttachedFileRepository;
    private final MisraCppGuidelineAttachedFileRepositoryImpl misraCppGuidelineAttachedFileRepositoryImpl;

    public MisraCppGuidelineAttachedFileService(MisraCppGuidelineAttachedFileRepository misraCppGuidelineAttachedFileRepository, MisraCppGuidelineAttachedFileRepositoryImpl misraCppGuidelineAttachedFileRepositoryImpl) {
        this.misraCppGuidelineAttachedFileRepository = misraCppGuidelineAttachedFileRepository;
        this.misraCppGuidelineAttachedFileRepositoryImpl = misraCppGuidelineAttachedFileRepositoryImpl;
    }

    public MisraCppGuidelineDto findAttachedFileByMisraCppGuidelineIdx(MisraCppGuidelineDto misraCppGuidelineDto) {
        return MisraCppGuidelineMapper.INSTANCE.toDtoByAttachedFileList(misraCppGuidelineDto, misraCppGuidelineAttachedFileRepositoryImpl.findAttachedFileByMisraCppGuidelineIdx(misraCppGuidelineDto.getIdx()));
    }

    public void insertAttachedFile(MisraCppGuidelineAttachedFile attachedFile) {
        misraCppGuidelineAttachedFileRepository.save(attachedFile);
    }

    public MisraCppGuidelineAttachedFile findAttachedFileByIdx(long idx) {
        return misraCppGuidelineAttachedFileRepository.findById(idx).orElse(new MisraCppGuidelineAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        misraCppGuidelineAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByMisraCppGuidelineIdx(long idx) {
        return misraCppGuidelineAttachedFileRepositoryImpl.deleteAttachedFileByMisraCppGuidelineIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param misraCppGuidelineIdx
     * @param files
     */
    public void uploadAttachedFile(long misraCppGuidelineIdx, MultipartFile[] files) throws Exception {
        MisraCppGuidelineAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = MisraCppGuidelineAttachedFile.builder()
                    .misraCppGuidelineIdx(misraCppGuidelineIdx)
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
            MisraCppGuidelineAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param misraCppGuidelineIdx
     */
    public void deleteAllAttachedFile(long misraCppGuidelineIdx) throws Exception {
        List<MisraCppGuidelineAttachedFile> attachedFileList = misraCppGuidelineAttachedFileRepositoryImpl.findAttachedFileByMisraCppGuidelineIdx(misraCppGuidelineIdx);

        for (MisraCppGuidelineAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByMisraCppGuidelineIdx(misraCppGuidelineIdx);
    }
}