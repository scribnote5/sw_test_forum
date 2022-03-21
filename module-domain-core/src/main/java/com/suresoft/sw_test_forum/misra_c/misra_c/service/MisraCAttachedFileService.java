package com.suresoft.sw_test_forum.misra_c.misra_c.service;

import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCAttachedFile;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.MisraCDto;
import com.suresoft.sw_test_forum.misra_c.misra_c.dto.mapper.MisraCMapper;
import com.suresoft.sw_test_forum.misra_c.misra_c.repository.MisraCAttachedFileRepository;
import com.suresoft.sw_test_forum.misra_c.misra_c.repository.MisraCAttachedFileRepositoryImpl;
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
public class MisraCAttachedFileService {
    private final MisraCAttachedFileRepository misraCAttachedFileRepository;
    private final MisraCAttachedFileRepositoryImpl misraCAttachedFileRepositoryImpl;

    public MisraCAttachedFileService(MisraCAttachedFileRepository misraCAttachedFileRepository, MisraCAttachedFileRepositoryImpl misraCAttachedFileRepositoryImpl) {
        this.misraCAttachedFileRepository = misraCAttachedFileRepository;
        this.misraCAttachedFileRepositoryImpl = misraCAttachedFileRepositoryImpl;
    }

    public MisraCDto findAttachedFileByMisraCIdx(MisraCDto misraCDto) {
        return MisraCMapper.INSTANCE.toDtoByAttachedFileList(misraCDto, misraCAttachedFileRepositoryImpl.findAttachedFileByMisraCIdx(misraCDto.getIdx()));
    }

    public void insertAttachedFile(MisraCAttachedFile attachedFile) {
        misraCAttachedFileRepository.save(attachedFile);
    }

    public MisraCAttachedFile findAttachedFileByIdx(long idx) {
        return misraCAttachedFileRepository.findById(idx).orElse(new MisraCAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        misraCAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByMisraCIdx(long idx) {
        return misraCAttachedFileRepositoryImpl.deleteAttachedFileByMisraCIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param misraCIdx
     * @param files
     */
    public void uploadAttachedFile(long misraCIdx, MultipartFile[] files) throws Exception {
        MisraCAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = MisraCAttachedFile.builder()
                    .misraCIdx(misraCIdx)
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
            MisraCAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param misraCIdx
     */
    public void deleteAllAttachedFile(long misraCIdx) throws Exception {
        List<MisraCAttachedFile> attachedFileList = misraCAttachedFileRepositoryImpl.findAttachedFileByMisraCIdx(misraCIdx);

        for (MisraCAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByMisraCIdx(misraCIdx);
    }
}