package com.suresoft.sw_test_forum.style_cop.style_cop_example.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.style_cop.style_cop.domain.StyleCopComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.style_cop.style_cop_example.domain.QStyleCopExampleComment.styleCopExampleComment;

@Repository
@Transactional
public class StyleCopExampleCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public StyleCopExampleCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(StyleCopComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param styleCopExampleIdx
     * @return
     */
    public long countAllByStyleCopExampleIdx(long styleCopExampleIdx) {
        return queryFactory
                .selectFrom(styleCopExampleComment)
                .where(styleCopExampleComment.styleCopExampleIdx.eq(styleCopExampleIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param styleCopExampleIdx
     * @return
     */
    public long deleteAllByStyleCopExampleIdx(long styleCopExampleIdx) {
        return queryFactory
                .delete(styleCopExampleComment)
                .where(styleCopExampleComment.styleCopExampleIdx.eq(styleCopExampleIdx))
                .execute();
    }
}