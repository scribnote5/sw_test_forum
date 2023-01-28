package com.suresoft.sw_test_forum.admin_page.data_history.domain;

import com.suresoft.sw_test_forum.admin_page.data_history.domain.enums.AuditType;
import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@ToString
@NoArgsConstructor
@Entity
@Table
public class DataHistory extends CommonAudit {
    private long auditIdx;

    private String auditBoard;

    @Enumerated(EnumType.STRING)
    private AuditType auditType;

    private String message;

    private String detailedMessage;

    private String content;

    @Builder
    public DataHistory(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                       long auditIdx,
                       String auditBoard,
                       AuditType auditType,
                       String message,
                       String detailedMessage,
                       String content) {
        setIdx(idx);
        setCreatedDate(createdDate);
        setLastModifiedDate(lastModifiedDate);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.auditIdx = auditIdx;
        this.auditBoard = auditBoard;
        this.auditType = auditType;
        this.message = message;
        this.detailedMessage = detailedMessage;
        this.content = content;
    }

    public void update(DataHistory dataHistory) {
        setActiveStatus(dataHistory.getActiveStatus());
        this.auditIdx = dataHistory.getAuditIdx();
        this.auditBoard = dataHistory.getAuditBoard();
        this.auditType = dataHistory.getAuditType();
        this.message = dataHistory.getMessage();
        this.detailedMessage = dataHistory.getDetailedMessage();
        this.content = dataHistory.getContent();
    }
}
