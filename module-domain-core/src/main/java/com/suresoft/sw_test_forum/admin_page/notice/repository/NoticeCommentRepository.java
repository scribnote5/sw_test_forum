package com.suresoft.sw_test_forum.admin_page.notice.repository;

import com.suresoft.sw_test_forum.admin_page.notice.domain.NoticeComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeCommentRepository extends JpaRepository<NoticeComment, Long> {
    /**
     * 리스트 조회
     *
     * @param noticeIdx
     * @return
     */
    List<NoticeComment> findAllByNoticeIdxOrderByIdxDesc(long noticeIdx);
}