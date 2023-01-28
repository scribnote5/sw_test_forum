package com.suresoft.sw_test_forum.metric.metric_guideline.service;

import com.suresoft.sw_test_forum.metric.metric_guideline.domain.MetricGuidelineAttachedFile;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.MetricGuidelineDto;
import com.suresoft.sw_test_forum.metric.metric_guideline.dto.mapper.MetricGuidelineMapper;
import com.suresoft.sw_test_forum.metric.metric_guideline.repository.MetricGuidelineAttachedFileRepository;
import com.suresoft.sw_test_forum.metric.metric_guideline.repository.MetricGuidelineAttachedFileRepositoryImpl;
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
public class MetricGuidelineAttachedFileService {
    private final MetricGuidelineAttachedFileRepository metricGuidelineAttachedFileRepository;
    private final MetricGuidelineAttachedFileRepositoryImpl metricGuidelineAttachedFileRepositoryImpl;

    public MetricGuidelineAttachedFileService(MetricGuidelineAttachedFileRepository metricGuidelineAttachedFileRepository, MetricGuidelineAttachedFileRepositoryImpl metricGuidelineAttachedFileRepositoryImpl) {
        this.metricGuidelineAttachedFileRepository = metricGuidelineAttachedFileRepository;
        this.metricGuidelineAttachedFileRepositoryImpl = metricGuidelineAttachedFileRepositoryImpl;
    }

    public MetricGuidelineDto findAttachedFileByMetricGuidelineIdx(MetricGuidelineDto metricGuidelineDto) {
        return MetricGuidelineMapper.INSTANCE.toDtoByAttachedFileList(metricGuidelineDto, metricGuidelineAttachedFileRepositoryImpl.findAttachedFileByMetricGuidelineIdx(metricGuidelineDto.getIdx()));
    }

    public void insertAttachedFile(MetricGuidelineAttachedFile attachedFile) {
        metricGuidelineAttachedFileRepository.save(attachedFile);
    }

    public MetricGuidelineAttachedFile findAttachedFileByIdx(long idx) {
        return metricGuidelineAttachedFileRepository.findById(idx).orElse(new MetricGuidelineAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        metricGuidelineAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByMetricGuidelineIdx(long idx) {
        return metricGuidelineAttachedFileRepositoryImpl.deleteAttachedFileByMetricGuidelineIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param metricGuidelineIdx
     * @param files
     */
    public void uploadAttachedFile(long metricGuidelineIdx, MultipartFile[] files) throws Exception {
        MetricGuidelineAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = MetricGuidelineAttachedFile.builder()
                    .metricGuidelineIdx(metricGuidelineIdx)
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
            MetricGuidelineAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param metricGuidelineIdx
     */
    public void deleteAllAttachedFile(long metricGuidelineIdx) throws Exception {
        List<MetricGuidelineAttachedFile> attachedFileList = metricGuidelineAttachedFileRepositoryImpl.findAttachedFileByMetricGuidelineIdx(metricGuidelineIdx);

        for (MetricGuidelineAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByMetricGuidelineIdx(metricGuidelineIdx);
    }
}