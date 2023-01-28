package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.MisraCppComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.misra_cpp.misra_cpp_guideline.domain.QMisraCppGuidelineComment.misraCppGuidelineComment;

@Repository
@Transactional
public class MisraCppGuidelineCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public MisraCppGuidelineCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(MisraCppComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param misraCppGuidelineIdx
     * @return
     */
    public long countAllByMisraCppGuidelineCIdx(long misraCppGuidelineIdx) {
        return queryFactory
                .selectFrom(misraCppGuidelineComment)
                .where(misraCppGuidelineComment.misraCppGuidelineIdx.eq(misraCppGuidelineIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param misraCppGuidelineIdx
     * @return
     */
    public long deleteAllByMisraCppGuidelineIdx(long misraCppGuidelineIdx) {
        return queryFactory
                .delete(misraCppGuidelineComment)
                .where(misraCppGuidelineComment.misraCppGuidelineIdx.eq(misraCppGuidelineIdx))
                .execute();
    }
}