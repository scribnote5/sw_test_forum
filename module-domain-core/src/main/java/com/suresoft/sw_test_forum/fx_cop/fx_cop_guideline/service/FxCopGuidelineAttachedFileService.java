package com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.service;

import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.domain.FxCopGuidelineAttachedFile;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.FxCopGuidelineDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.dto.mapper.FxCopGuidelineMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.repository.FxCopGuidelineAttachedFileRepository;
import com.suresoft.sw_test_forum.fx_cop.fx_cop_guideline.repository.FxCopGuidelineAttachedFileRepositoryImpl;
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
public class FxCopGuidelineAttachedFileService {
    private final FxCopGuidelineAttachedFileRepository fxCopGuidelineAttachedFileRepository;
    private final FxCopGuidelineAttachedFileRepositoryImpl fxCopGuidelineAttachedFileRepositoryImpl;

    public FxCopGuidelineAttachedFileService(FxCopGuidelineAttachedFileRepository fxCopGuidelineAttachedFileRepository, FxCopGuidelineAttachedFileRepositoryImpl fxCopGuidelineAttachedFileRepositoryImpl) {
        this.fxCopGuidelineAttachedFileRepository = fxCopGuidelineAttachedFileRepository;
        this.fxCopGuidelineAttachedFileRepositoryImpl = fxCopGuidelineAttachedFileRepositoryImpl;
    }

    public FxCopGuidelineDto findAttachedFileByFxCopGuidelineIdx(FxCopGuidelineDto fxCopGuidelineDto) {
        return FxCopGuidelineMapper.INSTANCE.toDtoByAttachedFileList(fxCopGuidelineDto, fxCopGuidelineAttachedFileRepositoryImpl.findAttachedFileByFxCopGuidelineIdx(fxCopGuidelineDto.getIdx()));
    }

    public void insertAttachedFile(FxCopGuidelineAttachedFile attachedFile) {
        fxCopGuidelineAttachedFileRepository.save(attachedFile);
    }

    public FxCopGuidelineAttachedFile findAttachedFileByIdx(long idx) {
        return fxCopGuidelineAttachedFileRepository.findById(idx).orElse(new FxCopGuidelineAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        fxCopGuidelineAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByFxCopGuidelineIdx(long idx) {
        return fxCopGuidelineAttachedFileRepositoryImpl.deleteAttachedFileByFxCopGuidelineIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param fxCopGuidelineIdx
     * @param files
     */
    public void uploadAttachedFile(long fxCopGuidelineIdx, MultipartFile[] files) throws Exception {
        FxCopGuidelineAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = FxCopGuidelineAttachedFile.builder()
                    .fxCopGuidelineIdx(fxCopGuidelineIdx)
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
            FxCopGuidelineAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param fxCopGuidelineIdx
     */
    public void deleteAllAttachedFile(long fxCopGuidelineIdx) throws Exception {
        List<FxCopGuidelineAttachedFile> attachedFileList = fxCopGuidelineAttachedFileRepositoryImpl.findAttachedFileByFxCopGuidelineIdx(fxCopGuidelineIdx);

        for (FxCopGuidelineAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByFxCopGuidelineIdx(fxCopGuidelineIdx);
    }
}