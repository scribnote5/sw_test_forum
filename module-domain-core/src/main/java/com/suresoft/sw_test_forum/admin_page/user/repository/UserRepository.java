package com.suresoft.sw_test_forum.admin_page.user.repository;

import com.suresoft.sw_test_forum.admin_page.user.domain.User;
import com.suresoft.sw_test_forum.admin_page.user.domain.enums.AuthorityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    /**
     * username으로 조회
     *
     * @param username
     * @return
     */
    User findByUsername(String username);

    /**
     * 조회
     *
     * @param createdBy
     * @return
     */
    User findByIdx(long createdBy);

    /**
     * 권한이 포함되어 있는지, 조회
     *
     * @param username
     * @param authorityType
     * @return
     */
    User findByUsernameAndAuthorityTypeIn(String username, AuthorityType[] authorityType);


    /**
     * username이 중복되어 있는지, 조회
     *
     * @param username
     * @return
     */
    Long countByUsername(String username);
}