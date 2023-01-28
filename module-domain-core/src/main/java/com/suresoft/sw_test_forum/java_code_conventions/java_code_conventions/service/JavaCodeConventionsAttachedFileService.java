package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.service;

import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.JavaCodeConventionsAttachedFile;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.JavaCodeConventionsDto;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.dto.mapper.JavaCodeConventionsMapper;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.repository.JavaCodeConventionsAttachedFileRepository;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.repository.JavaCodeConventionsAttachedFileRepositoryImpl;
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
public class JavaCodeConventionsAttachedFileService {
    private final JavaCodeConventionsAttachedFileRepository javaCodeConventionsAttachedFileRepository;
    private final JavaCodeConventionsAttachedFileRepositoryImpl javaCodeConventionsAttachedFileRepositoryImpl;

    public JavaCodeConventionsAttachedFileService(JavaCodeConventionsAttachedFileRepository javaCodeConventionsAttachedFileRepository, JavaCodeConventionsAttachedFileRepositoryImpl javaCodeConventionsAttachedFileRepositoryImpl) {
        this.javaCodeConventionsAttachedFileRepository = javaCodeConventionsAttachedFileRepository;
        this.javaCodeConventionsAttachedFileRepositoryImpl = javaCodeConventionsAttachedFileRepositoryImpl;
    }

    public JavaCodeConventionsDto findAttachedFileByJavaCodeConventionsIdx(JavaCodeConventionsDto javaCodeConventionsDto) {
        return JavaCodeConventionsMapper.INSTANCE.toDtoByAttachedFileList(javaCodeConventionsDto, javaCodeConventionsAttachedFileRepositoryImpl.findAttachedFileByJavaCodeConventionsIdx(javaCodeConventionsDto.getIdx()));
    }

    public void insertAttachedFile(JavaCodeConventionsAttachedFile attachedFile) {
        javaCodeConventionsAttachedFileRepository.save(attachedFile);
    }

    public JavaCodeConventionsAttachedFile findAttachedFileByIdx(long idx) {
        return javaCodeConventionsAttachedFileRepository.findById(idx).orElse(new JavaCodeConventionsAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        javaCodeConventionsAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByJavaCodeConventionsIdx(long idx) {
        return javaCodeConventionsAttachedFileRepositoryImpl.deleteAttachedFileByJavaCodeConventionsIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param javaCodeConventionsIdx
     * @param files
     */
    public void uploadAttachedFile(long javaCodeConventionsIdx, MultipartFile[] files) throws Exception {
        JavaCodeConventionsAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = JavaCodeConventionsAttachedFile.builder()
                    .javaCodeConventionsIdx(javaCodeConventionsIdx)
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
            JavaCodeConventionsAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param javaCodeConventionsIdx
     */
    public void deleteAllAttachedFile(long javaCodeConventionsIdx) throws Exception {
        List<JavaCodeConventionsAttachedFile> attachedFileList = javaCodeConventionsAttachedFileRepositoryImpl.findAttachedFileByJavaCodeConventionsIdx(javaCodeConventionsIdx);

        for (JavaCodeConventionsAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByJavaCodeConventionsIdx(javaCodeConventionsIdx);
    }
}