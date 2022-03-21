package com.suresoft.sw_test_forum.common.domain;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@ToString
@EntityListeners(AuditingEntityListener.class)
public abstract class AttachedFileAudit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;

    @CreatedDate
    private LocalDateTime createdDate;

    @CreatedBy
    @Column(nullable = false)
    private long createdByIdx;

    private String fileName;

    private String savedFileName;

    private long fileSize;

    private long downloadCount = 0;
}