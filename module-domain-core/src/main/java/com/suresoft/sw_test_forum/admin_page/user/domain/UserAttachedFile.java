package com.suresoft.sw_test_forum.admin_page.user.domain;

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
public class UserAttachedFile extends AttachedFileAudit {
    private long userIdx;

    @Builder
    public UserAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                            long userIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.userIdx = userIdx;
    }
}