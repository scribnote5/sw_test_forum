package com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.cwe_java.cwe_java.domain.CweJavaComment;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import static com.suresoft.sw_test_forum.cwe_java.cwe_java_guideline.domain.QCweJavaGuidelineComment.cweJavaGuidelineComment;

@Repository
@Transactional
public class CweJavaGuidelineCommentRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public CweJavaGuidelineCommentRepositoryImpl(JPAQueryFactory queryFactory) {
        super(CweJavaComment.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 개수 조회
     *
     * @param cweJavaGuidelineIdx
     * @return
     */
    public long countAllByCweJavaGuidelineCIdx(long cweJavaGuidelineIdx) {
        return queryFactory
                .selectFrom(cweJavaGuidelineComment)
                .where(cweJavaGuidelineComment.cweJavaGuidelineIdx.eq(cweJavaGuidelineIdx))
                .fetch().size();
    }

    /**
     * 전체 삭제
     *
     * @param cweJavaGuidelineIdx
     * @return
     */
    public long deleteAllByCweJavaGuidelineIdx(long cweJavaGuidelineIdx) {
        return queryFactory
                .delete(cweJavaGuidelineComment)
                .where(cweJavaGuidelineComment.cweJavaGuidelineIdx.eq(cweJavaGuidelineIdx))
                .execute();
    }
}