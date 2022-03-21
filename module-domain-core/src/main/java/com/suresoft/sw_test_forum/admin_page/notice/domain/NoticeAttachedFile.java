package com.suresoft.sw_test_forum.admin_page.notice.domain;

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
public class NoticeAttachedFile extends AttachedFileAudit {
    private long noticeIdx;

    @Builder
    public NoticeAttachedFile(long createdByIdx, String fileName, String savedFileName, long fileSize,
                              long noticeIdx) {
        setCreatedByIdx(createdByIdx);
        setFileName(fileName);
        setSavedFileName(savedFileName);
        setFileSize(fileSize);
        this.noticeIdx = noticeIdx;
    }
}
