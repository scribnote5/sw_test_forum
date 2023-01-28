package com.suresoft.sw_test_forum.style_cop.style_cop.service;

import com.suresoft.sw_test_forum.style_cop.style_cop.domain.StyleCopAttachedFile;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.StyleCopDto;
import com.suresoft.sw_test_forum.style_cop.style_cop.dto.mapper.StyleCopMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop.repository.StyleCopAttachedFileRepository;
import com.suresoft.sw_test_forum.style_cop.style_cop.repository.StyleCopAttachedFileRepositoryImpl;
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
public class StyleCopAttachedFileService {
    private final StyleCopAttachedFileRepository styleCopAttachedFileRepository;
    private final StyleCopAttachedFileRepositoryImpl styleCopAttachedFileRepositoryImpl;

    public StyleCopAttachedFileService(StyleCopAttachedFileRepository styleCopAttachedFileRepository, StyleCopAttachedFileRepositoryImpl styleCopAttachedFileRepositoryImpl) {
        this.styleCopAttachedFileRepository = styleCopAttachedFileRepository;
        this.styleCopAttachedFileRepositoryImpl = styleCopAttachedFileRepositoryImpl;
    }

    public StyleCopDto findAttachedFileByStyleCopIdx(StyleCopDto styleCopDto) {
        return StyleCopMapper.INSTANCE.toDtoByAttachedFileList(styleCopDto, styleCopAttachedFileRepositoryImpl.findAttachedFileByStyleCopIdx(styleCopDto.getIdx()));
    }

    public void insertAttachedFile(StyleCopAttachedFile attachedFile) {
        styleCopAttachedFileRepository.save(attachedFile);
    }

    public StyleCopAttachedFile findAttachedFileByIdx(long idx) {
        return styleCopAttachedFileRepository.findById(idx).orElse(new StyleCopAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        styleCopAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByStyleCopIdx(long idx) {
        return styleCopAttachedFileRepositoryImpl.deleteAttachedFileByStyleCopIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param styleCopIdx
     * @param files
     */
    public void uploadAttachedFile(long styleCopIdx, MultipartFile[] files) throws Exception {
        StyleCopAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = StyleCopAttachedFile.builder()
                    .styleCopIdx(styleCopIdx)
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
            StyleCopAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param styleCopIdx
     */
    public void deleteAllAttachedFile(long styleCopIdx) throws Exception {
        List<StyleCopAttachedFile> attachedFileList = styleCopAttachedFileRepositoryImpl.findAttachedFileByStyleCopIdx(styleCopIdx);

        for (StyleCopAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByStyleCopIdx(styleCopIdx);
    }
}