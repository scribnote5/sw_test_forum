package com.suresoft.sw_test_forum.admin_page.notice.service;

import com.suresoft.sw_test_forum.admin_page.notice.domain.NoticeAttachedFile;
import com.suresoft.sw_test_forum.admin_page.notice.dto.NoticeDto;
import com.suresoft.sw_test_forum.admin_page.notice.dto.mapper.NoticeMapper;
import com.suresoft.sw_test_forum.admin_page.notice.repository.NoticeAttachedFileRepository;
import com.suresoft.sw_test_forum.admin_page.notice.repository.NoticeAttachedFileRepositoryImpl;
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
public class NoticeAttachedFileService {
    private final NoticeAttachedFileRepository noticeAttachedFileRepository;
    private final NoticeAttachedFileRepositoryImpl noticeAttachedFileRepositoryImpl;

    public NoticeAttachedFileService(NoticeAttachedFileRepository noticeAttachedFileRepository, NoticeAttachedFileRepositoryImpl noticeAttachedFileRepositoryImpl) {
        this.noticeAttachedFileRepository = noticeAttachedFileRepository;
        this.noticeAttachedFileRepositoryImpl = noticeAttachedFileRepositoryImpl;
    }

    public NoticeDto findAttachedFileByNoticeIdx(NoticeDto noticeDto) {
        return NoticeMapper.INSTANCE.toDtoByAttachedFileList(noticeDto, noticeAttachedFileRepositoryImpl.findAttachedFileByNoticeIdx(noticeDto.getIdx()));
    }

    public void insertAttachedFile(NoticeAttachedFile attachedFile) {
        noticeAttachedFileRepository.save(attachedFile);
    }

    public NoticeAttachedFile findAttachedFileByIdx(long idx) {
        return noticeAttachedFileRepository.findById(idx).orElse(new NoticeAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        noticeAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByNoticeIdx(long idx) {
        return noticeAttachedFileRepositoryImpl.deleteAttachedFileByNoticeIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param noticeIdx
     * @param files
     */
    public void uploadAttachedFile(long noticeIdx, MultipartFile[] files) throws Exception {
        NoticeAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = NoticeAttachedFile.builder()
                    .noticeIdx(noticeIdx)
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
            NoticeAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param noticeIdx
     */
    public void deleteAllAttachedFile(long noticeIdx) throws Exception {
        List<NoticeAttachedFile> attachedFileList = noticeAttachedFileRepositoryImpl.findAttachedFileByNoticeIdx(noticeIdx);

        for (NoticeAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByNoticeIdx(noticeIdx);
    }
}