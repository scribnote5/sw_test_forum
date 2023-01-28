package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.JavaCodeConventionsGuidelineAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_guideline.domain.QJavaCodeConventionsGuidelineAttachedFile.javaCodeConventionsGuidelineAttachedFile;

@Repository
@Transactional
public class JavaCodeConventionsGuidelineAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public JavaCodeConventionsGuidelineAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(JavaCodeConventionsGuidelineAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param javaCodeConventionsGuidelineIdx
     * @return
     */
    public List<JavaCodeConventionsGuidelineAttachedFile> findAttachedFileByJavaCodeConventionsGuidelineIdx(long javaCodeConventionsGuidelineIdx) {
        return queryFactory
                .selectFrom(javaCodeConventionsGuidelineAttachedFile)
                .where(javaCodeConventionsGuidelineAttachedFile.javaCodeConventionsGuidelineIdx.eq(javaCodeConventionsGuidelineIdx))
                .orderBy(javaCodeConventionsGuidelineAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 전체 삭제
     *
     * @param javaCodeConventionsGuidelineIdx
     * @return
     */
    public long deleteAttachedFileByJavaCodeConventionsGuidelineIdx(long javaCodeConventionsGuidelineIdx) {
        return queryFactory
                .delete(javaCodeConventionsGuidelineAttachedFile)
                .where(javaCodeConventionsGuidelineAttachedFile.javaCodeConventionsGuidelineIdx.eq(javaCodeConventionsGuidelineIdx))
                .execute();
    }
}