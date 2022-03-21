package com.suresoft.sw_test_forum.common.domain;

import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class CommonAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;

    @Column(nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdDate;

    @Column(nullable = false)
    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    @Column(nullable = false)
    private long createdByIdx;

    @LastModifiedBy
    @Column(nullable = false)
    private long lastModifiedByIdx;

    @Enumerated(EnumType.STRING)
    private ActiveStatus activeStatus;

    private long views = 0;
}