package com.suresoft.sw_test_forum.style_cop.style_cop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.style_cop.style_cop.domain.StyleCopComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.style_cop.style_cop.domain.QStyleCopComment.styleCopComment;

@Repository
@Transactional
public class StyleCopCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public StyleCopCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(StyleCopComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param styleCopIdx
     * @return
     */
    public long countAllByStyleCopIdx(long styleCopIdx) {
        return queryFactory
                .selectFrom(styleCopComment)
                .where(styleCopComment.styleCopIdx.eq(styleCopIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param styleCopIdx
     * @return
     */
    public long deleteAllByStyleCopIdx(long styleCopIdx) {
        return queryFactory
                .delete(styleCopComment)
                .where(styleCopComment.styleCopIdx.eq(styleCopIdx))
                .execute();
    }
}