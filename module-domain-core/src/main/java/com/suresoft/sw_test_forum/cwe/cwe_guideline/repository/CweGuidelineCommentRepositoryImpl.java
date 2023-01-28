package com.suresoft.sw_test_forum.cwe.cwe_guideline.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.cwe.cwe.domain.CweComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.cwe.cwe_guideline.domain.QCweGuidelineComment.cweGuidelineComment;

@Repository
@Transactional
public class CweGuidelineCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweGuidelineCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CweComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param cweGuidelineIdx
     * @return
     */
    public long countAllByCweGuidelineCIdx(long cweGuidelineIdx) {
        return queryFactory
                .selectFrom(cweGuidelineComment)
                .where(cweGuidelineComment.cweGuidelineIdx.eq(cweGuidelineIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param cweGuidelineIdx
     * @return
     */
    public long deleteAllByCweGuidelineIdx(long cweGuidelineIdx) {
        return queryFactory
                .delete(cweGuidelineComment)
                .where(cweGuidelineComment.cweGuidelineIdx.eq(cweGuidelineIdx))
                .execute();
    }
}