package com.suresoft.sw_test_forum.left_reference.report.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.report.domain.enums.Category;
import com.suresoft.sw_test_forum.left_reference.report.listener.ReportListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table
@EntityListeners(ReportListener.class)
public class Report extends CommonAudit {
    private String title;

    private long priority;

    @Enumerated(EnumType.STRING)
    private Category category;

    private String content;

    @Builder
    public Report(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
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

    public void update(Report report) {
        setActiveStatus(report.getActiveStatus());
        this.title = report.getTitle();
        this.priority = report.getPriority();
        this.category = report.getCategory();
        this.content = report.getContent();
    }
}
