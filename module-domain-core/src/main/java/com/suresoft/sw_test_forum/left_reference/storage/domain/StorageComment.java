package com.suresoft.sw_test_forum.left_reference.storage.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.storage.listener.StorageCommentListener;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table
@EntityListeners(StorageCommentListener.class)
public class StorageComment extends CommonAudit {
    private long storageIdx;

    private String content;

    @Builder
    public StorageComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                          long storageIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.storageIdx = storageIdx;
        this.content = content;
    }

    public void update(StorageComment storageComment) {
        setActiveStatus(storageComment.getActiveStatus());
        this.content = storageComment.getContent();
    }
}