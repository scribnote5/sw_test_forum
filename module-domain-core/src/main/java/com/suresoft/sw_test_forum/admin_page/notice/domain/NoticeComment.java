package com.suresoft.sw_test_forum.admin_page.notice.domain;

import com.suresoft.sw_test_forum.admin_page.notice.listener.NoticeCommentListener;
import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
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
@EntityListeners(NoticeCommentListener.class)
public class NoticeComment extends CommonAudit {
    private long noticeIdx;

    private String content;

    @Builder
    public NoticeComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                         long noticeIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.noticeIdx = noticeIdx;
        this.content = content;
    }

    public void update(NoticeComment noticeComment) {
        setActiveStatus(noticeComment.getActiveStatus());
        this.content = noticeComment.getContent();
    }
}