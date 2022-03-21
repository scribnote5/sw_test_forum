package com.suresoft.sw_test_forum.metric.metric.service;

import com.suresoft.sw_test_forum.metric.metric.domain.MetricAttachedFile;
import com.suresoft.sw_test_forum.metric.metric.dto.MetricDto;
import com.suresoft.sw_test_forum.metric.metric.dto.mapper.MetricMapper;
import com.suresoft.sw_test_forum.metric.metric.repository.MetricAttachedFileRepository;
import com.suresoft.sw_test_forum.metric.metric.repository.MetricAttachedFileRepositoryImpl;
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
public class MetricAttachedFileService {
    private final MetricAttachedFileRepository metricAttachedFileRepository;
    private final MetricAttachedFileRepositoryImpl metricAttachedFileRepositoryImpl;

    public MetricAttachedFileService(MetricAttachedFileRepository metricAttachedFileRepository, MetricAttachedFileRepositoryImpl metricAttachedFileRepositoryImpl) {
        this.metricAttachedFileRepository = metricAttachedFileRepository;
        this.metricAttachedFileRepositoryImpl = metricAttachedFileRepositoryImpl;
    }

    public MetricDto findAttachedFileByMetricIdx(MetricDto metricDto) {
        return MetricMapper.INSTANCE.toDtoByAttachedFileList(metricDto, metricAttachedFileRepositoryImpl.findAttachedFileByMetricIdx(metricDto.getIdx()));
    }

    public void insertAttachedFile(MetricAttachedFile attachedFile) {
        metricAttachedFileRepository.save(attachedFile);
    }

    public MetricAttachedFile findAttachedFileByIdx(long idx) {
        return metricAttachedFileRepository.findById(idx).orElse(new MetricAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        metricAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByMetricIdx(long idx) {
        return metricAttachedFileRepositoryImpl.deleteAttachedFileByMetricIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param metricIdx
     * @param files
     */
    public void uploadAttachedFile(long metricIdx, MultipartFile[] files) throws Exception {
        MetricAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = MetricAttachedFile.builder()
                    .metricIdx(metricIdx)
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
            MetricAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param metricIdx
     */
    public void deleteAllAttachedFile(long metricIdx) throws Exception {
        List<MetricAttachedFile> attachedFileList = metricAttachedFileRepositoryImpl.findAttachedFileByMetricIdx(metricIdx);

        for (MetricAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByMetricIdx(metricIdx);
    }
}