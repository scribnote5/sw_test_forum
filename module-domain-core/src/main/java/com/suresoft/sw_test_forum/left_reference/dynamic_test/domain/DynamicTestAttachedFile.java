package com.suresoft.sw_test_forum.left_reference.dynamic_test.domain;

import com.suresoft.sw_test_forum.common.domain.AttachedFileAudit;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "dynamic_test_attached_file")
public class DynamicTestAttachedFile extends AttachedFileAudit {
    @Column(name = "dynamic_test_idx")
    private long dynamicTestIdx;

    @Builder
    public DynamicTestAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                                   long dynamicTestIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.dynamicTestIdx = dynamicTestIdx;
    }
}
