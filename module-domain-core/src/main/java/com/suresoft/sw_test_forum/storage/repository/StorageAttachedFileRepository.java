package com.suresoft.sw_test_forum.storage.repository;

import com.suresoft.sw_test_forum.storage.domain.StorageAttachedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StorageAttachedFileRepository extends JpaRepository<StorageAttachedFile, Long> {

}