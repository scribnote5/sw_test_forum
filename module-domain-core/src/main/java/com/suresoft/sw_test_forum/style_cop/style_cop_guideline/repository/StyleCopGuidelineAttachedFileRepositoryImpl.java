package com.suresoft.sw_test_forum.style_cop.style_cop_guideline.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.StyleCopGuidelineAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.style_cop.style_cop_guideline.domain.QStyleCopGuidelineAttachedFile.styleCopGuidelineAttachedFile;

@Repository
@Transactional
public class StyleCopGuidelineAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public StyleCopGuidelineAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(StyleCopGuidelineAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param styleCopGuidelineIdx
     * @return
     */
    public List<StyleCopGuidelineAttachedFile> findAttachedFileByStyleCopGuidelineIdx(long styleCopGuidelineIdx) {
        return queryFactory
                .selectFrom(styleCopGuidelineAttachedFile)
                .where(styleCopGuidelineAttachedFile.styleCopGuidelineIdx.eq(styleCopGuidelineIdx))
                .orderBy(styleCopGuidelineAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 전체 삭제
     *
     * @param styleCopGuidelineIdx
     * @return
     */
    public long deleteAttachedFileByStyleCopGuidelineIdx(long styleCopGuidelineIdx) {
        return queryFactory
                .delete(styleCopGuidelineAttachedFile)
                .where(styleCopGuidelineAttachedFile.styleCopGuidelineIdx.eq(styleCopGuidelineIdx))
                .execute();
    }
}