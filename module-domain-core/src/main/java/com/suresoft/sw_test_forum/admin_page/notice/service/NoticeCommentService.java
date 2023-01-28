package com.suresoft.sw_test_forum.admin_page.notice.service;

import com.suresoft.sw_test_forum.admin_page.notice.domain.NoticeComment;
import com.suresoft.sw_test_forum.admin_page.notice.dto.NoticeCommentDto;
import com.suresoft.sw_test_forum.admin_page.notice.dto.NoticeDto;
import com.suresoft.sw_test_forum.admin_page.notice.dto.mapper.NoticeCommentMapper;
import com.suresoft.sw_test_forum.admin_page.notice.dto.mapper.NoticeMapper;
import com.suresoft.sw_test_forum.admin_page.notice.repository.NoticeCommentRepository;
import com.suresoft.sw_test_forum.admin_page.notice.repository.NoticeCommentRepositoryImpl;
import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.Position;
import com.suresoft.sw_test_forum.admin_page.user.repository.UserRepositoryImpl;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.util.AuthorityUtil;
import com.suresoft.sw_test_forum.util.EmptyUtil;
import com.suresoft.sw_test_forum.util.NewIconCheck;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class NoticeCommentService {
    private final NoticeCommentRepository NoticeCommentRepository;
    private final NoticeCommentRepositoryImpl NoticeCommentRepositoryImpl;
    private final UserRepositoryImpl userRepositoryImpl;

    public NoticeCommentService(NoticeCommentRepository NoticeCommentRepository, NoticeCommentRepositoryImpl NoticeCommentRepositoryImpl, UserRepositoryImpl userRepositoryImpl) {
        this.NoticeCommentRepository = NoticeCommentRepository;
        this.NoticeCommentRepositoryImpl = NoticeCommentRepositoryImpl;
        this.userRepositoryImpl = userRepositoryImpl;
    }

    /**
     * 리스트 조회
     *
     * @param noticeDto
     * @return
     */
    public NoticeDto findAllByNoticeIdxOrderByIdxDesc(NoticeDto noticeDto) {
        List<NoticeCommentDto> noticeCommentDtoList = NoticeCommentMapper.INSTANCE.toDto(NoticeCommentRepository.findAllByNoticeIdxOrderByIdxDesc(noticeDto.getIdx()));

        // NewIcon 판별, createdBy 설정
        for (NoticeCommentDto noticeCommentDto : noticeCommentDtoList) {
            // 권한 설정
            // Update: isAccessInGeneral 메소드에 따라 접근 가능 및 불가
            // 탈퇴 회원은 권한을 general로 설정 후 권한을 검사함
            User createdByUser = userRepositoryImpl.findByIdx(noticeCommentDto.getCreatedByIdx());

            if (EmptyUtil.isEmpty(createdByUser)) {
                createdByUser = User.builder()
                        .username("withdraw member")
                        .name("탈퇴 회원")
                        .position(Position.K_ETC)
                        .authorityType(AuthorityType.GENERAL)
                        .activeStatus(ActiveStatus.ACTIVE)
                        .build();
            }

            noticeCommentDto.setNewIcon(NewIconCheck.isNew(noticeCommentDto.getCreatedDate()));
            noticeCommentDto.setAccess(AuthorityUtil.isAccessInGeneral(createdByUser.getUsername(), createdByUser.getAuthorityType().getAuthorityType()));
            noticeCommentDto.setCreatedByUser(createdByUser);
        }

        noticeDto = NoticeMapper.INSTANCE.toDtoByCommentList(noticeDto, noticeCommentDtoList);

        return noticeDto;
    }

    /**
     * 등록
     *
     * @param NoticeCommentDto
     * @return
     */
    public long insertNoticeComment(NoticeCommentDto NoticeCommentDto) {
        return NoticeCommentRepository.save(NoticeCommentMapper.INSTANCE.toEntity(NoticeCommentDto)).getIdx();
    }

    /**
     * 수정
     *
     * @param idx
     * @param NoticeCommentDto
     * @return
     */
    @Transactional
    public long updateNoticeComment(long idx, NoticeCommentDto NoticeCommentDto) {
        NoticeComment persistNoticeComment = NoticeCommentRepository.getReferenceById(idx);
        NoticeComment NoticeComment = NoticeCommentMapper.INSTANCE.toEntity(NoticeCommentDto);

        persistNoticeComment.update(NoticeComment);

        return NoticeCommentRepository.save(persistNoticeComment).getIdx();
    }

    /**
     * 삭제
     *
     * @param idx
     */
    public void deleteNoticeCommentByIdx(long idx) {
        NoticeCommentRepository.deleteById(idx);
    }

    /**
     * 전체 삭제
     *
     * @param noticeIdx
     */
    public void deleteAllByNoticeIdx(long noticeIdx) {
        NoticeCommentRepositoryImpl.deleteAllByNoticeIdx(noticeIdx);
    }
}