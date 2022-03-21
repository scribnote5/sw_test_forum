package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.service;

import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.MisraCGuidelineAttachedFile;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.MisraCGuidelineDto;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.dto.mapper.MisraCGuidelineMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.repository.MisraCGuidelineAttachedFileRepository;
import com.suresoft.sw_test_forum.misra_c.misra_c_guideline.repository.MisraCGuidelineAttachedFileRepositoryImpl;
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
public class MisraCGuidelineAttachedFileService {
    private final MisraCGuidelineAttachedFileRepository misraCGuidelineAttachedFileRepository;
    private final MisraCGuidelineAttachedFileRepositoryImpl misraCGuidelineAttachedFileRepositoryImpl;

    public MisraCGuidelineAttachedFileService(MisraCGuidelineAttachedFileRepository misraCGuidelineAttachedFileRepository, MisraCGuidelineAttachedFileRepositoryImpl misraCGuidelineAttachedFileRepositoryImpl) {
        this.misraCGuidelineAttachedFileRepository = misraCGuidelineAttachedFileRepository;
        this.misraCGuidelineAttachedFileRepositoryImpl = misraCGuidelineAttachedFileRepositoryImpl;
    }

    public MisraCGuidelineDto findAttachedFileByMisraCGuidelineIdx(MisraCGuidelineDto misraCGuidelineDto) {
        return MisraCGuidelineMapper.INSTANCE.toDtoByAttachedFileList(misraCGuidelineDto, misraCGuidelineAttachedFileRepositoryImpl.findAttachedFileByMisraCGuidelineIdx(misraCGuidelineDto.getIdx()));
    }

    public void insertAttachedFile(MisraCGuidelineAttachedFile attachedFile) {
        misraCGuidelineAttachedFileRepository.save(attachedFile);
    }

    public MisraCGuidelineAttachedFile findAttachedFileByIdx(long idx) {
        return misraCGuidelineAttachedFileRepository.findById(idx).orElse(new MisraCGuidelineAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        misraCGuidelineAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByMisraCGuidelineIdx(long idx) {
        return misraCGuidelineAttachedFileRepositoryImpl.deleteAttachedFileByMisraCGuidelineIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param misraCGuidelineIdx
     * @param files
     */
    public void uploadAttachedFile(long misraCGuidelineIdx, MultipartFile[] files) throws Exception {
        MisraCGuidelineAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = MisraCGuidelineAttachedFile.builder()
                    .misraCGuidelineIdx(misraCGuidelineIdx)
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
            MisraCGuidelineAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param misraCGuidelineIdx
     */
    public void deleteAllAttachedFile(long misraCGuidelineIdx) throws Exception {
        List<MisraCGuidelineAttachedFile> attachedFileList = misraCGuidelineAttachedFileRepositoryImpl.findAttachedFileByMisraCGuidelineIdx(misraCGuidelineIdx);

        for (MisraCGuidelineAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByMisraCGuidelineIdx(misraCGuidelineIdx);
    }
}