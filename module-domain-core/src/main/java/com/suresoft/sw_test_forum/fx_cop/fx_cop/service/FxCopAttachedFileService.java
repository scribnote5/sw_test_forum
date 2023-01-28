package com.suresoft.sw_test_forum.fx_cop.fx_cop.service;

import com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.FxCopAttachedFile;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.FxCopDto;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.dto.mapper.FxCopMapper;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.repository.FxCopAttachedFileRepository;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.repository.FxCopAttachedFileRepositoryImpl;
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
public class FxCopAttachedFileService {
    private final FxCopAttachedFileRepository fxCopAttachedFileRepository;
    private final FxCopAttachedFileRepositoryImpl fxCopAttachedFileRepositoryImpl;

    public FxCopAttachedFileService(FxCopAttachedFileRepository fxCopAttachedFileRepository, FxCopAttachedFileRepositoryImpl fxCopAttachedFileRepositoryImpl) {
        this.fxCopAttachedFileRepository = fxCopAttachedFileRepository;
        this.fxCopAttachedFileRepositoryImpl = fxCopAttachedFileRepositoryImpl;
    }

    public FxCopDto findAttachedFileByFxCopIdx(FxCopDto fxCopDto) {
        return FxCopMapper.INSTANCE.toDtoByAttachedFileList(fxCopDto, fxCopAttachedFileRepositoryImpl.findAttachedFileByFxCopIdx(fxCopDto.getIdx()));
    }

    public void insertAttachedFile(FxCopAttachedFile attachedFile) {
        fxCopAttachedFileRepository.save(attachedFile);
    }

    public FxCopAttachedFile findAttachedFileByIdx(long idx) {
        return fxCopAttachedFileRepository.findById(idx).orElse(new FxCopAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        fxCopAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByFxCopIdx(long idx) {
        return fxCopAttachedFileRepositoryImpl.deleteAttachedFileByFxCopIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param fxCopIdx
     * @param files
     */
    public void uploadAttachedFile(long fxCopIdx, MultipartFile[] files) throws Exception {
        FxCopAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = FxCopAttachedFile.builder()
                    .fxCopIdx(fxCopIdx)
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
            FxCopAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param fxCopIdx
     */
    public void deleteAllAttachedFile(long fxCopIdx) throws Exception {
        List<FxCopAttachedFile> attachedFileList = fxCopAttachedFileRepositoryImpl.findAttachedFileByFxCopIdx(fxCopIdx);

        for (FxCopAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByFxCopIdx(fxCopIdx);
    }
}