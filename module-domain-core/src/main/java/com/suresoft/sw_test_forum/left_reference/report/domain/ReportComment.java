package com.suresoft.sw_test_forum.left_reference.report.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.report.listener.ReportCommentListener;
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
@EntityListeners(ReportCommentListener.class)
public class ReportComment extends CommonAudit {
    private long reportIdx;

    private String content;

    @Builder
    public ReportComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                         long reportIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.reportIdx = reportIdx;
        this.content = content;
    }

    public void update(ReportComment reportComment) {
        setActiveStatus(reportComment.getActiveStatus());
        this.content = reportComment.getContent();
    }
}