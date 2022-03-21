package com.suresoft.sw_test_forum.admin_page.user.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.admin_page.user.domain.UserAttachedFile;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUserAttachedFile.userAttachedFile;

@Repository
@Transactional
public class UserAttachedFileRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public UserAttachedFileRepositoryImpl(JPAQueryFactory queryFactory) {
        super(UserAttachedFile.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param userIdx
     * @return
     */
    public List<UserAttachedFile> findAttachedFileByUserIdx(long userIdx) {
        return queryFactory
                .selectFrom(userAttachedFile)
                .where(userAttachedFile.userIdx.eq(userIdx))
                .fetch();
    }

    /**
     * 전체 삭제
     *
     * @param userIdx
     * @return
     */
    public long deleteAttachedFileByUserIdx(long userIdx) {
        return queryFactory
                .delete(userAttachedFile)
                .where(userAttachedFile.userIdx.eq(userIdx))
                .execute();
    }
}