package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.service;

import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.JavaCodeConventionsGuidelineAttachedFile;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.JavaCodeConventionsGuidelineDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.dto.mapper.JavaCodeConventionsGuidelineMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.repository.JavaCodeConventionsGuidelineAttachedFileRepository;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.repository.JavaCodeConventionsGuidelineAttachedFileRepositoryImpl;
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
public class JavaCodeConventionsGuidelineAttachedFileService {
    private final JavaCodeConventionsGuidelineAttachedFileRepository javaCodeConventionsGuidelineAttachedFileRepository;
    private final JavaCodeConventionsGuidelineAttachedFileRepositoryImpl javaCodeConventionsGuidelineAttachedFileRepositoryImpl;

    public JavaCodeConventionsGuidelineAttachedFileService(JavaCodeConventionsGuidelineAttachedFileRepository javaCodeConventionsGuidelineAttachedFileRepository, JavaCodeConventionsGuidelineAttachedFileRepositoryImpl javaCodeConventionsGuidelineAttachedFileRepositoryImpl) {
        this.javaCodeConventionsGuidelineAttachedFileRepository = javaCodeConventionsGuidelineAttachedFileRepository;
        this.javaCodeConventionsGuidelineAttachedFileRepositoryImpl = javaCodeConventionsGuidelineAttachedFileRepositoryImpl;
    }

    public JavaCodeConventionsGuidelineDto findAttachedFileByJavaCodeConventionsGuidelineIdx(JavaCodeConventionsGuidelineDto javaCodeConventionsGuidelineDto) {
        return JavaCodeConventionsGuidelineMapper.INSTANCE.toDtoByAttachedFileList(javaCodeConventionsGuidelineDto, javaCodeConventionsGuidelineAttachedFileRepositoryImpl.findAttachedFileByJavaCodeConventionsGuidelineIdx(javaCodeConventionsGuidelineDto.getIdx()));
    }

    public void insertAttachedFile(JavaCodeConventionsGuidelineAttachedFile attachedFile) {
        javaCodeConventionsGuidelineAttachedFileRepository.save(attachedFile);
    }

    public JavaCodeConventionsGuidelineAttachedFile findAttachedFileByIdx(long idx) {
        return javaCodeConventionsGuidelineAttachedFileRepository.findById(idx).orElse(new JavaCodeConventionsGuidelineAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        javaCodeConventionsGuidelineAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByJavaCodeConventionsGuidelineIdx(long idx) {
        return javaCodeConventionsGuidelineAttachedFileRepositoryImpl.deleteAttachedFileByJavaCodeConventionsGuidelineIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param javaCodeConventionsGuidelineIdx
     * @param files
     */
    public void uploadAttachedFile(long javaCodeConventionsGuidelineIdx, MultipartFile[] files) throws Exception {
        JavaCodeConventionsGuidelineAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = JavaCodeConventionsGuidelineAttachedFile.builder()
                    .javaCodeConventionsGuidelineIdx(javaCodeConventionsGuidelineIdx)
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
            JavaCodeConventionsGuidelineAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param javaCodeConventionsGuidelineIdx
     */
    public void deleteAllAttachedFile(long javaCodeConventionsGuidelineIdx) throws Exception {
        List<JavaCodeConventionsGuidelineAttachedFile> attachedFileList = javaCodeConventionsGuidelineAttachedFileRepositoryImpl.findAttachedFileByJavaCodeConventionsGuidelineIdx(javaCodeConventionsGuidelineIdx);

        for (JavaCodeConventionsGuidelineAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByJavaCodeConventionsGuidelineIdx(javaCodeConventionsGuidelineIdx);
    }
}