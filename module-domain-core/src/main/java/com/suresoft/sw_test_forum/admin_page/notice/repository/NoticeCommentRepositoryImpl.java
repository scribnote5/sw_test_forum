package com.suresoft.sw_test_forum.admin_page.notice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.admin_page.notice.domain.NoticeComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.admin_page.notice.domain.QNoticeComment.noticeComment;

@Repository
@Transactional
public class NoticeCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public NoticeCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(NoticeComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param noticeIdx
     * @return
     */
    public long countAllByNoticeIdx(long noticeIdx) {
        return queryFactory
                .selectFrom(noticeComment)
                .where(noticeComment.noticeIdx.eq(noticeIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param noticeIdx
     * @return
     */
    public long deleteAllByNoticeIdx(long noticeIdx) {
        return queryFactory
                .delete(noticeComment)
                .where(noticeComment.noticeIdx.eq(noticeIdx))
                .execute();
    }
}