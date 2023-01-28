package com.suresoft.sw_test_forum.question_answer.service;

import com.suresoft.sw_test_forum.question_answer.domain.QuestionAnswerAttachedFile;
import com.suresoft.sw_test_forum.question_answer.dto.QuestionAnswerDto;
import com.suresoft.sw_test_forum.question_answer.dto.mapper.QuestionAnswerMapper;
import com.suresoft.sw_test_forum.question_answer.repository.QuestionAnswerAttachedFileRepository;
import com.suresoft.sw_test_forum.question_answer.repository.QuestionAnswerAttachedFileRepositoryImpl;
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
public class QuestionAnswerAttachedFileService {
    private final QuestionAnswerAttachedFileRepository questionAnswerAttachedFileRepository;
    private final QuestionAnswerAttachedFileRepositoryImpl questionAnswerAttachedFileRepositoryImpl;

    public QuestionAnswerAttachedFileService(QuestionAnswerAttachedFileRepository questionAnswerAttachedFileRepository, QuestionAnswerAttachedFileRepositoryImpl questionAnswerAttachedFileRepositoryImpl) {
        this.questionAnswerAttachedFileRepository = questionAnswerAttachedFileRepository;
        this.questionAnswerAttachedFileRepositoryImpl = questionAnswerAttachedFileRepositoryImpl;
    }

    public QuestionAnswerDto findAttachedFileByQuestionAnswerIdx(QuestionAnswerDto questionAnswerDto) {
        return QuestionAnswerMapper.INSTANCE.toDtoByAttachedFileList(questionAnswerDto, questionAnswerAttachedFileRepositoryImpl.findAttachedFileByQuestionAnswerIdx(questionAnswerDto.getIdx()));
    }

    public void insertAttachedFile(QuestionAnswerAttachedFile attachedFile) {
        questionAnswerAttachedFileRepository.save(attachedFile);
    }

    public QuestionAnswerAttachedFile findAttachedFileByIdx(long idx) {
        return questionAnswerAttachedFileRepository.findById(idx).orElse(new QuestionAnswerAttachedFile());
    }

    public void deleteAttachedFileByIdx(long idx) {
        questionAnswerAttachedFileRepository.deleteById(idx);
    }

    public long deleteAttachedFileByQuestionAnswerIdx(long idx) {
        return questionAnswerAttachedFileRepositoryImpl.deleteAttachedFileByQuestionAnswerIdx(idx);
    }

    /**
     * 첨부 파일 업로드
     *
     * @param questionAnswerIdx
     * @param files
     */
    public void uploadAttachedFile(long questionAnswerIdx, MultipartFile[] files) throws Exception {
        QuestionAnswerAttachedFile uploadFile;

        for (MultipartFile file : files) {
            String uuid = UUID.randomUUID().toString().replaceAll("-", "");
            String savedFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yy.MM.dd.")) + uuid + "_" + file.getOriginalFilename();

            Path path = Paths.get("./upload/" + savedFileName);
            Files.write(path, file.getBytes());

            uploadFile = QuestionAnswerAttachedFile.builder()
                    .questionAnswerIdx(questionAnswerIdx)
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
            QuestionAnswerAttachedFile attachedFile = findAttachedFileByIdx(idx);

            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);

            deleteAttachedFileByIdx(attachedFile.getIdx());
        }
    }

    /**
     * 모든 첨부 파일 삭제
     *
     * @param questionAnswerIdx
     */
    public void deleteAllAttachedFile(long questionAnswerIdx) throws Exception {
        List<QuestionAnswerAttachedFile> attachedFileList = questionAnswerAttachedFileRepositoryImpl.findAttachedFileByQuestionAnswerIdx(questionAnswerIdx);

        for (QuestionAnswerAttachedFile attachedFile : attachedFileList) {
            Path path = Paths.get("./upload/" + attachedFile.getSavedFileName());
            Files.delete(path);
        }

        deleteAttachedFileByQuestionAnswerIdx(questionAnswerIdx);
    }
}