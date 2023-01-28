package com.suresoft.sw_test_forum.admin_page.notice.domain;

import com.suresoft.sw_test_forum.admin_page.notice.listener.NoticeListener;
import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table
@EntityListeners(NoticeListener.class)
public class Notice extends CommonAudit {
    private String title;


    private long priority;

    private String content;

    @Builder
    public Notice(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                  String title,
                  long priority,
                  String content) {
        setIdx(idx);
        setCreatedDate(createdDate);
        setLastModifiedDate(lastModifiedDate);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.title = title;
        this.priority = priority;
        this.content = content;
    }

    public void update(Notice notice) {
        setActiveStatus(notice.getActiveStatus());
        this.title = notice.getTitle();
        this.priority = notice.getPriority();
        this.content = notice.getContent();
    }
}
