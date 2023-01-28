package com.suresoft.sw_test_forum.admin_page.notice.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.admin_page.notice.domain.NoticeAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.notice.domain.QNoticeAttachedFile.noticeAttachedFile;

@Repository
@Transactional
public class NoticeAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public NoticeAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(NoticeAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param noticeIdx
     * @return
     */
    public List<NoticeAttachedFile> findAttachedFileByNoticeIdx(long noticeIdx) {
        return queryFactory
                .selectFrom(noticeAttachedFile)
                .where(noticeAttachedFile.noticeIdx.eq(noticeIdx))
                .orderBy(noticeAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param noticeIdx
     * @return
     */
    public long deleteAttachedFileByNoticeIdx(long noticeIdx) {
        return queryFactory
                .delete(noticeAttachedFile)
                .where(noticeAttachedFile.noticeIdx.eq(noticeIdx))
                .execute();
    }
}