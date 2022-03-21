package com.suresoft.sw_test_forum.storage.repository;

import com.suresoft.sw_test_forum.storage.domain.Storage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageRepository extends JpaRepository<Storage, Long> {

}