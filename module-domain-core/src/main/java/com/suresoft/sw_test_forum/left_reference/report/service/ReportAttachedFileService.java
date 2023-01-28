package com.suresoft.sw_test_forum.left_reference.report.service;

import com.suresoft.sw_test_forum.left_reference.report.domain.ReportAttachedFile;
import com.suresoft.sw_test_forum.left_reference.report.dto.ReportDto;
import com.suresoft.sw_test_forum.left_reference.report.dto.mapper.ReportMapper;
import com.suresoft.sw_test_forum.left_reference.report.repository.ReportAttachedFileRepository;
import com.suresoft.sw_test_forum.left_reference.report.repository.ReportAttachedFileRepositoryImpl;
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
public class ReportAttachedFileService {
    private final ReportAttachedFileRepository reportAttachedFileRepository;
    private final ReportAttachedFileRepositoryImpl reportAttachedFileRepositoryImpl;

    public ReportAttachedFileService(ReportAttachedFileRepository reportAttachedFileRepository, ReportAttachedFileRepositoryImpl reportAttachedFileRepositoryImpl) {
        this.reportAttachedFileRepository = reportAttachedFileRepository;
        this.reportAttachedFileRepositoryImpl = reportAttachedFileRepositoryImpl;
    }

    public ReportDto findAttachedFileByReportIdx(ReportDto reportDto) {
        return ReportMapper.INSTANCE.toDtoByAttachedFileList(reportDto, reportAttachedFileRepositoryImpl.findAttachedFileByReportIdx(reportDto.getIdx()));
    }

    public void insertAttachedFile(ReportAttachedFile attachedFile) {
        reportAttachedFileRepository.save(attachedFile);
    }

    public ReportAttachedFile findAttachedFileByIdx(long idx) {
        return reportAttachedFileRepository.findById(idx).orElse(new ReportAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        reportAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByReportIdx(long idx) {
        return reportAttachedFileRepositoryImpl.deleteAttachedFileByReportIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param reportIdx
     * @param files
     */
    public void uploadAttachedFile(long reportIdx, MultipartFile[] files) throws Exception {
        ReportAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = ReportAttachedFile.builder()
                    .reportIdx(reportIdx)
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
            ReportAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param reportIdx
     */
    public void deleteAllAttachedFile(long reportIdx) throws Exception {
        List<ReportAttachedFile> attachedFileList = reportAttachedFileRepositoryImpl.findAttachedFileByReportIdx(reportIdx);

        for (ReportAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByReportIdx(reportIdx);
    }
}