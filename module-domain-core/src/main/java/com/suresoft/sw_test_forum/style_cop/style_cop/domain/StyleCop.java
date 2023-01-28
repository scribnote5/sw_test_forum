package com.suresoft.sw_test_forum.style_cop.style_cop.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.Frequency;
import com.suresoft.sw_test_forum.style_cop.style_cop.domain.enums.StyleCopCategory;
import com.suresoft.sw_test_forum.style_cop.style_cop.listener.StyleCopListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "style_cop")
@EntityListeners(StyleCopListener.class)
public class StyleCop extends CommonAudit {
    private String title;

    private long priority;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private long hashTagsIdx;

    @Enumerated(EnumType.STRING)
    private StyleCopCategory category;

    private String content;

    @Builder
    public StyleCop(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                    String title,
                    long priority,
                    Frequency frequency,
                    long hashTagsIdx,
                    StyleCopCategory category,
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
        this.content = content;
    }

    public void update(StyleCop styleCop) {
        setActiveStatus(styleCop.getActiveStatus());
        this.title = styleCop.getTitle();
        this.priority = styleCop.getPriority();
        this.frequency = styleCop.getFrequency();
        this.hashTagsIdx = styleCop.getHashTagsIdx();
        this.category = styleCop.getCategory();
        this.content = styleCop.getContent();
    }
}
