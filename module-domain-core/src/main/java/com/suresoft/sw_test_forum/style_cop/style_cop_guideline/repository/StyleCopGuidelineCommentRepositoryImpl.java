package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.style_cop.style_cop.domain.StyleCopComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.QStyleCopGuidelineComment.styleCopGuidelineComment;

@Repository
@Transactional
public class StyleCopGuidelineCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public StyleCopGuidelineCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(StyleCopComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param styleCopGuidelineIdx
     * @return
     */
    public long countAllByStyleCopGuidelineCIdx(long styleCopGuidelineIdx) {
        return queryFactory
                .selectFrom(styleCopGuidelineComment)
                .where(styleCopGuidelineComment.styleCopGuidelineIdx.eq(styleCopGuidelineIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param styleCopGuidelineIdx
     * @return
     */
    public long deleteAllByStyleCopGuidelineIdx(long styleCopGuidelineIdx) {
        return queryFactory
                .delete(styleCopGuidelineComment)
                .where(styleCopGuidelineComment.styleCopGuidelineIdx.eq(styleCopGuidelineIdx))
                .execute();
    }
}