package com.suresoft.sw_test_forum.left_reference.dynamic_test.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.listener.DynamicTestCommentListener;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "dynamic_test_comment")
@EntityListeners(DynamicTestCommentListener.class)
public class DynamicTestComment extends CommonAudit {
    @Column(name = "dynamic_test_idx")
    private long dynamicTestIdx;

    private String content;

    @Builder
    public DynamicTestComment(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                              long dynamicTestIdx, String content) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.dynamicTestIdx = dynamicTestIdx;
        this.content = content;
    }

    public void update(DynamicTestComment dynamicTest) {
        setActiveStatus(dynamicTest.getActiveStatus());
        this.content = dynamicTest.getContent();
    }
}