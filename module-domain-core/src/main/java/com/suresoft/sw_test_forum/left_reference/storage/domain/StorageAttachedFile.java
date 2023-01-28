package com.suresoft.sw_test_forum.left_reference.storage.domain;

import com.suresoft.sw_test_forum.common.domain.AttachedFileAudit;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table
public class StorageAttachedFile extends AttachedFileAudit {
    private long storageIdx;

    @Builder
    public StorageAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                               long storageIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.storageIdx = storageIdx;
    }
}
