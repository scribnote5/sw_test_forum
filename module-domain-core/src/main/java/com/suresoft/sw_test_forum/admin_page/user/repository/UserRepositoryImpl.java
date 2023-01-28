package com.suresoft.sw_test_forum.admin_page.user.repository;

import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import com.suresoft.sw_test_forum.admin_page.user.dto.UserDto;
import com.suresoft.sw_test_forum.admin_page.user.dto.UserSearchDto;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

import static com.suresoft.sw_test_forum.admin_page.user.domain.QUser.user;

@Repository
@Transactional
public class UserRepositoryImpl extends QuerydslRepositorySupport {
    private final JPAQueryFactory queryFactory;

    public UserRepositoryImpl(JPAQueryFactory queryFactory) {
        super(User.class);
        this.queryFactory = queryFactory;
    }

    /**
     * 리스트 조회
     *
     * @param pageable
     * @param userSearchDto
     * @return
     */
    public PageImpl<UserDto> findAll(Pageable pageable, UserSearchDto userSearchDto) {
        JPQLQuery<UserDto> query = queryFactory
                .select(
                        Projections.bean(
                                UserDto.class,
                                user.idx,
                                user.createdDate,
                                user.createdByIdx,
                                user.activeStatus,
                                user.views,

                                user.username,
                                user.name,
                                user.department,
                                user.position,
                                user.userStatus,
                                user.authorityType,
                                user.contribution
                        )
                )
                .from(user)
                .where(searchCondition(userSearchDto))
                .orderBy(user.idx.desc());

        long totalCount = query.fetchCount();
        List<UserDto> results = getQuerydsl().applyPagination(pageable, query).fetch();

        return new PageImpl<>(results, pageable, totalCount);
    }

    /**
     * 리스트 조회 조건
     *
     * @param userSearchDto
     * @return
     */
    private BooleanExpression searchCondition(UserSearchDto userSearchDto) {
        BooleanExpression result;

        switch (userSearchDto.getSearchType()) {
            case "NAME":
                result = user.name.contains(userSearchDto.getSearchKeyword());
                break;
            case "DEPARTMENT":
                result = user.department.contains(userSearchDto.getSearchKeyword());
                break;
            case "AUTHORITY_TYPE":
                result = user.authorityType.eq(AuthorityType.valueOf(userSearchDto.getSearchKeyword()));
                break;
            default:
                result = null;
        }

        return result;
    }

    /**
     * 권한에 따른, 리스트 조회
     *
     * @param authorityType
     * @return
     */
    public List<UserDto> findByAuthorityType(AuthorityType authorityType) {
        JPQLQuery<UserDto> query = queryFactory
                .select(
                        Projections.bean(
                                UserDto.class,
                                user.username,
                                user.name,
                                user.department,
                                user.position,
                                user.userStatus,
                                user.authorityType,
                                user.email
                        )
                )
                .from(user)
                .where(user.authorityType.eq(authorityType))
                .orderBy(user.idx.desc());

        return query.fetch();
    }

    /**
     * 권한을 확인할 때, 조회
     *
     * @param idx
     * @return
     */
    public User findByIdx(long idx) {
        return queryFactory
                .select(
                        Projections.bean(
                                User.class,
                                user.idx,
                                user.username,
                                user.name,
                                user.department,
                                user.position,
                                user.authorityType
                        )
                )
                .from(user)
                .where(user.idx.eq(idx))
                .fetchOne();
    }

    /**
     * Auto Complete 부서 조회
     *
     * @return
     */
    public List<String> findDistinctDepartment() {
        return queryFactory.select(
                        user.department
                )
                .distinct().from(user)
                .fetch();
    }

    /**
     * 조회수 수정
     *
     * @param idx
     * @return
     */
    public long updateViewsByIdx(long idx) {
        return queryFactory
                .update(user)
                .set(user.views, user.views.add(1))
                .where(user.idx.eq(idx))
                .execute();
    }
}