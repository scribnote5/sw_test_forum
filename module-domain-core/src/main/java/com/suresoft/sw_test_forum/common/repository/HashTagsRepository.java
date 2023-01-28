package com.suresoft.sw_test_forum.common.repository;

import com.suresoft.sw_test_forum.common.domain.HashTags;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HashTagsRepository extends JpaRepository<HashTags, Long> {

}