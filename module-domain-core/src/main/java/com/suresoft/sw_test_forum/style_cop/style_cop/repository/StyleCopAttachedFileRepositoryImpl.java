package com.suresoft.sw_test_forum.style_cop.style_cop.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.style_cop.style_cop.domain.StyleCopAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.style_cop.style_cop.domain.QStyleCopAttachedFile.styleCopAttachedFile;

@Repository
@Transactional
public class StyleCopAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public StyleCopAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(StyleCopAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param styleCopIdx
     * @return
     */
    public List<StyleCopAttachedFile> findAttachedFileByStyleCopIdx(long styleCopIdx) {
        return queryFactory
                .selectFrom(styleCopAttachedFile)
                .where(styleCopAttachedFile.styleCopIdx.eq(styleCopIdx))
                .orderBy(styleCopAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param styleCopIdx
     * @return
     */
    public long deleteAttachedFileByStyleCopIdx(long styleCopIdx) {
        return queryFactory
                .delete(styleCopAttachedFile)
                .where(styleCopAttachedFile.styleCopIdx.eq(styleCopIdx))
                .execute();
    }
}