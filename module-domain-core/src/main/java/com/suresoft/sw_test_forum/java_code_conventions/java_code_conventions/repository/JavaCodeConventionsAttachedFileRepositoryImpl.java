package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.JavaCodeConventionsAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions.domain.QJavaCodeConventionsAttachedFile.javaCodeConventionsAttachedFile;

@Repository
@Transactional
public class JavaCodeConventionsAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public JavaCodeConventionsAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(JavaCodeConventionsAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param javaCodeConventionsIdx
     * @return
     */
    public List<JavaCodeConventionsAttachedFile> findAttachedFileByJavaCodeConventionsIdx(long javaCodeConventionsIdx) {
        return queryFactory
                .selectFrom(javaCodeConventionsAttachedFile)
                .where(javaCodeConventionsAttachedFile.javaCodeConventionsIdx.eq(javaCodeConventionsIdx))
                .orderBy(javaCodeConventionsAttachedFile.idx.asc())
                .fetch();
    }

    /**
     * 삭제
     *
     * @param javaCodeConventionsIdx
     * @return
     */
    public long deleteAttachedFileByJavaCodeConventionsIdx(long javaCodeConventionsIdx) {
        return queryFactory
                .delete(javaCodeConventionsAttachedFile)
                .where(javaCodeConventionsAttachedFile.javaCodeConventionsIdx.eq(javaCodeConventionsIdx))
                .execute();
    }
}