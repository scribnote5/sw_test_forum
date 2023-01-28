package com.suresoft.sw_test_forum.left_reference.dynamic_test.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.left_reference.dynamic_test.listener.DynamicTestLikeListener;
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
@Table(name = "dynamic_test_like")
@EntityListeners(DynamicTestLikeListener.class)
public class DynamicTestLike extends CommonAudit {
    @Column(name = "dynamic_test_idx")
    private long dynamicTestIdx;

    @Builder
    public DynamicTestLike(long idx, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                           long dynamicTestIdx) {
        setIdx(idx);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.dynamicTestIdx = dynamicTestIdx;
    }
}