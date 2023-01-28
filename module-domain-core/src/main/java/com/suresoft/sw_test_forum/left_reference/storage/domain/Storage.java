package com.suresoft.sw_test_forum.left_reference.storage.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.storage.listener.StorageListener;
import com.suresoft.sw_test_forum.left_reference.storage.domain.enums.Category;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table
@EntityListeners(StorageListener.class)
public class Storage extends CommonAudit {
    private String title;

    private long priority;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String content;

    @Builder
    public Storage(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                   String title,
                   long priority,
                   Category category,
                   String content) {
        setIdx(idx);
        setCreatedDate(createdDate);
        setLastModifiedDate(lastModifiedDate);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.title = title;
        this.priority = priority;
        this.category = category;
        this.content = content;
    }

    public void update(Storage storage) {
        setActiveStatus(storage.getActiveStatus());
        this.title = storage.getTitle();
        this.priority = storage.getPriority();
        this.category = storage.getCategory();
        this.content = storage.getContent();
    }
}
