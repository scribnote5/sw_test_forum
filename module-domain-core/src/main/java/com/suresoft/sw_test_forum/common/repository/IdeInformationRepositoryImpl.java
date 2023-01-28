package com.suresoft.sw_test_forum.common.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.common.domain.IdeInformation;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.common.domain.QIdeInformation.ideInformation;

@Repository
@Transactional
public class IdeInformationRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public IdeInformationRepositoryImpl(JPAQueryFactory queryFactory) {
        super(IdeInformation.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 조회
     *
     * @param idx
     * @return
     */
    public IdeInformation findIdeInformationByIdx(long idx) {
        return queryFactory.select(
                        Projections.bean(
                                IdeInformation.class,
                                ideInformation.ideName
                        )
                )
                .from(ideInformation)
                .where(ideInformation.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete ide 명 조회
     *
     * @return
     */
    public List<String> findDistinctIdeName() {
        return queryFactory.select(
                        ideInformation.ideName
                )
                .distinct().from(ideInformation)
                .fetch();
    }

}