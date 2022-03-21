package com.suresoft.sw_test_forum.misra_c.misra_c_guideline.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.MisraCComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.misra_c.misra_c_guideline.domain.QMisraCGuidelineComment.misraCGuidelineComment;

@Repository
@Transactional
public class MisraCGuidelineCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCGuidelineCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param misraCGuidelineIdx
     * @return
     */
    public long countAllByMisraCGuidelineCIdx(long misraCGuidelineIdx) {
        return queryFactory
                .selectFrom(misraCGuidelineComment)
                .where(misraCGuidelineComment.misraCGuidelineIdx.eq(misraCGuidelineIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param misraCGuidelineIdx
     * @return
     */
    public long deleteAllByMisraCGuidelineIdx(long misraCGuidelineIdx) {
        return queryFactory
                .delete(misraCGuidelineComment)
                .where(misraCGuidelineComment.misraCGuidelineIdx.eq(misraCGuidelineIdx))
                .execute();
    }
}