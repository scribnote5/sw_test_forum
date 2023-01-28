package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.service;

import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuidelineAttachedFile;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.StyleCopGuidelineDto;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.dto.mapper.StyleCopGuidelineMapper;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository.StyleCopGuidelineAttachedFileRepository;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository.StyleCopGuidelineAttachedFileRepositoryImpl;
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
public class StyleCopGuidelineAttachedFileService {
    private final StyleCopGuidelineAttachedFileRepository styleCopGuidelineAttachedFileRepository;
    private final StyleCopGuidelineAttachedFileRepositoryImpl styleCopGuidelineAttachedFileRepositoryImpl;

    public StyleCopGuidelineAttachedFileService(StyleCopGuidelineAttachedFileRepository styleCopGuidelineAttachedFileRepository, StyleCopGuidelineAttachedFileRepositoryImpl styleCopGuidelineAttachedFileRepositoryImpl) {
        this.styleCopGuidelineAttachedFileRepository = styleCopGuidelineAttachedFileRepository;
        this.styleCopGuidelineAttachedFileRepositoryImpl = styleCopGuidelineAttachedFileRepositoryImpl;
    }

    public StyleCopGuidelineDto findAttachedFileByStyleCopGuidelineIdx(StyleCopGuidelineDto styleCopGuidelineDto) {
        return StyleCopGuidelineMapper.INSTANCE.toDtoByAttachedFileList(styleCopGuidelineDto, styleCopGuidelineAttachedFileRepositoryImpl.findAttachedFileByStyleCopGuidelineIdx(styleCopGuidelineDto.getIdx()));
    }

    public void insertAttachedFile(StyleCopGuidelineAttachedFile attachedFile) {
        styleCopGuidelineAttachedFileRepository.save(attachedFile);
    }

    public StyleCopGuidelineAttachedFile findAttachedFileByIdx(long idx) {
        return styleCopGuidelineAttachedFileRepository.findById(idx).orElse(new StyleCopGuidelineAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        styleCopGuidelineAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByStyleCopGuidelineIdx(long idx) {
        return styleCopGuidelineAttachedFileRepositoryImpl.deleteAttachedFileByStyleCopGuidelineIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param styleCopGuidelineIdx
     * @param files
     */
    public void uploadAttachedFile(long styleCopGuidelineIdx, MultipartFile[] files) throws Exception {
        StyleCopGuidelineAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = StyleCopGuidelineAttachedFile.builder()
                    .styleCopGuidelineIdx(styleCopGuidelineIdx)
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
            StyleCopGuidelineAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param styleCopGuidelineIdx
     */
    public void deleteAllAttachedFile(long styleCopGuidelineIdx) throws Exception {
        List<StyleCopGuidelineAttachedFile> attachedFileList = styleCopGuidelineAttachedFileRepositoryImpl.findAttachedFileByStyleCopGuidelineIdx(styleCopGuidelineIdx);

        for (StyleCopGuidelineAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByStyleCopGuidelineIdx(styleCopGuidelineIdx);
    }
}