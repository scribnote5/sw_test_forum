package com.suresoft.sw_test_forum.misra_c.misra_c.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.Frequency;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.enums.MisraCCategory;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.enums.MisraCDecidability;
import com.suresoft.sw_test_forum.misra_c.misra_c.domain.enums.MisraCScope;
import com.suresoft.sw_test_forum.misra_c.misra_c.listener.MisraCListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "misra_c")
@EntityListeners(MisraCListener.class)
public class MisraC extends CommonAudit {
    private String title;

    private long priority;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private long hashTagsIdx;

    @Enumerated(EnumType.STRING)
    private MisraCCategory category;

    @Enumerated(EnumType.STRING)
    private MisraCScope scope;

    @Enumerated(EnumType.STRING)
    private MisraCDecidability decidability;

    private String content;

    @Builder
    public MisraC(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                  String title,
                  long priority,
                  Frequency frequency,
                  long hashTagsIdx,
                  MisraCCategory category,
                  MisraCScope scope,
                  MisraCDecidability decidability,
                  String content) {
        setIdx(idx);
        setCreatedDate(createdDate);
        setLastModifiedDate(lastModifiedDate);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.title = title;
        this.priority = priority;
        this.frequency = frequency;
        this.hashTagsIdx = hashTagsIdx;
        this.category = category;
        this.scope = scope;
        this.decidability = decidability;
        this.content = content;
    }

    public void update(MisraC misraC) {
        setActiveStatus(misraC.getActiveStatus());
        this.title = misraC.getTitle();
        this.priority = misraC.getPriority();
        this.frequency = misraC.getFrequency();
        this.hashTagsIdx = misraC.getHashTagsIdx();
        this.category = misraC.getCategory();
        this.scope = misraC.getScope();
        this.decidability = misraC.getDecidability();
        this.content = misraC.getContent();
    }
}
