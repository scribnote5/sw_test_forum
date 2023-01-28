package com.suresoft.sw_test_forum.question_answer.service;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.service.UserService;
import com.suresoft.sw_test_forum.question_answer.domain.QuestionAnswer;
import com.suresoft.sw_test_forum.question_answer.dto.QuestionAnswerDto;
import com.suresoft.sw_test_forum.question_answer.dto.QuestionAnswerSearchDto;
import com.suresoft.sw_test_forum.question_answer.dto.mapper.QuestionAnswerMapper;
import com.suresoft.sw_test_forum.question_answer.repository.QuestionAnswerCommentRepositoryImpl;
import com.suresoft.sw_test_forum.question_answer.repository.QuestionAnswerRepository;
import com.suresoft.sw_test_forum.question_answer.repository.QuestionAnswerRepositoryImpl;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class QuestionAnswerService {
    private final QuestionAnswerRepository questionAnswerRepository;
    private final QuestionAnswerRepositoryImpl questionAnswerRepositoryImpl;
    private final QuestionAnswerCommentRepositoryImpl questionAnswerCommentRepositoryImpl;
    private final UserService userService;
    @Value("${module.name}")
    private String moduleName;

    public QuestionAnswerService(QuestionAnswerRepository questionAnswerRepository, QuestionAnswerRepositoryImpl questionAnswerRepositoryImpl, QuestionAnswerCommentRepositoryImpl questionAnswerCommentRepositoryImpl, UserService userService) {
        this.questionAnswerRepository = questionAnswerRepository;
        this.questionAnswerRepositoryImpl = questionAnswerRepositoryImpl;
        this.questionAnswerCommentRepositoryImpl = questionAnswerCommentRepositoryImpl;
        this.userService = userService;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param questionAnswerSearchDto
     * @return
     */
    public Page<QuestionAnswerDto> findQuestionAnswerList(Pageable pageable, QuestionAnswerSearchDto questionAnswerSearchDto) {
        Page<QuestionAnswerDto> questionAnswerDtoList;

        pageable = PageRequest.of(pageable.getPageNumber() <= 0 ? 0 : pageable.getPageNumber() - 1, pageable.getPageSize());
        questionAnswerDtoList = questionAnswerRepositoryImpl.findAll(pageable, questionAnswerSearchDto);

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (QuestionAnswerDto questionAnswerDto : questionAnswerDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(questionAnswerDto.getCreatedByIdx());

            questionAnswerDto.setNewIcon(NewIconCheck.isNew(questionAnswerDto.getCreatedDate()));
            questionAnswerDto.setCreatedByUser(createdByUser);
            questionAnswerDto.setCommentDtoCount(questionAnswerCommentRepositoryImpl.countAllByQuestionAnswerIdx(questionAnswerDto.getIdx()));
        }

        return questionAnswerDtoList;
    }

    /**
     * 최근에 등록된 10개 리스트 조회
     *
     * @return
     */
    public List<QuestionAnswerDto> findTop10() {
        return questionAnswerRepositoryImpl.findTop10();
    }

    /**
     * Auto Complete 조회
     *
     * @return
     */
    public QuestionAnswerDto findQuestionAnswerAutoComplete(QuestionAnswerDto questionAnswerDto) {
        // title 설정
        for (String title : questionAnswerRepositoryImpl.findDistinctTitle()) {
            questionAnswerDto.getAutoCompleteTitle().add(title);
        }

        return questionAnswerDto;
    }

    /**
     * 질문 등록
     *
     * @param questionAnswerDto
     * @return
     */
    @Transactional
    public long insertQuestion(QuestionAnswerDto questionAnswerDto) {
        QuestionAnswer questionAnswer = QuestionAnswerMapper.INSTANCE.toEntity(questionAnswerDto);
        long idx = questionAnswerRepository.save(questionAnswer).getIdx();

        questionAnswer.setGroupIdx(idx);

        QuestionAnswer persistQuestionAnswer = questionAnswerRepository.getReferenceById(idx);
        persistQuestionAnswer.update(questionAnswer);

        return questionAnswerRepository.save(persistQuestionAnswer).getIdx();
    }

    /**
     * 답변 등록
     *
     * @param questionAnswerDto
     * @return
     */
    @Transactional
    public long insertAnswer(QuestionAnswerDto questionAnswerDto) {
        return questionAnswerRepository.save(QuestionAnswerMapper.INSTANCE.toEntity(questionAnswerDto)).getIdx();
    }

    /**
     * 현재 게시글 이외 질문과 연관된 게시글 리스트 조회
     *
     * @param idx
     * @param groupIdx
     * @return
     */
    public List<QuestionAnswerDto> findAllByIdxAndGroupIdx(long idx, long groupIdx) {
        List<QuestionAnswerDto> questionAnswerDtoList = questionAnswerRepositoryImpl.findAllByIdxAndGroupIdx(idx, groupIdx);

        // NewIcon 판별, createdBy 설정, comment 갯수 설정
        for (QuestionAnswerDto questionAnswerDto : questionAnswerDtoList) {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(questionAnswerDto.getCreatedByIdx());

            questionAnswerDto.setNewIcon(NewIconCheck.isNew(questionAnswerDto.getCreatedDate()));
            questionAnswerDto.setCreatedByUser(createdByUser);
        }

        return questionAnswerDtoList;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public QuestionAnswerDto findQuestionAnswerByIdx(long idx) {
        QuestionAnswerDto questionAnswerDto = QuestionAnswerMapper.INSTANCE.toDto(questionAnswerRepository.findById(idx).orElse(new QuestionAnswer()));

        // 권한 설정
        // Register: 로그인한 사용자 Register 접근 가능
        if (idx == 0) {
            questionAnswerDto.setAccess(true);
        }
        // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
        // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
        else {
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userService.findUserByIdxAndSetUserWhenEmpty(questionAnswerDto.getCreatedByIdx());
            User lastModifiedByUser = userService.findUserByIdxAndSetUserWhenEmpty(questionAnswerDto.getLastModifiedByIdx());

            questionAnswerDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            questionAnswerDto.setCreatedByUser(createdByUser);
            questionAnswerDto.setLastModifiedByUser(lastModifiedByUser);
        }

        questionAnswerRepositoryImpl.updateViewsByIdx(idx);
        questionAnswerDto.setViews(questionAnswerDto.getViews() + 1);

        return questionAnswerDto;
    }

    /**
     * 수정
     *
     * @param idx
     * @param questionAnswerDto
     * @return
     */
    @Transactional
    public long updateQuestionAnswer(long idx, QuestionAnswerDto questionAnswerDto) {
        QuestionAnswer persistQuestionAnswer = questionAnswerRepository.getReferenceById(idx);
        QuestionAnswer questionAnswer = QuestionAnswerMapper.INSTANCE.toEntity(questionAnswerDto);

        persistQuestionAnswer.update(questionAnswer);

        return questionAnswerRepository.save(persistQuestionAnswer).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteQuestionAnswerByIdx(long idx) {
        questionAnswerRepository.deleteById(idx);
    }
}