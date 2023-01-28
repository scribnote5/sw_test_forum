package com.suresoft.sw_test_forum.fx_cop.fx_cop.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.Frequency;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.enums.FxCopBreakingChange;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.domain.enums.FxCopCategory;
import com.suresoft.sw_test_forum.fx_cop.fx_cop.listener.FxCopListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "fx_cop")
@EntityListeners(FxCopListener.class)
public class FxCop extends CommonAudit {
    private String title;

    private long priority;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private long hashTagsIdx;

    @Enumerated(EnumType.STRING)
    private FxCopCategory category;

    @Enumerated(EnumType.STRING)
    private FxCopBreakingChange breakingChange;

    private String content;

    @Builder
    public FxCop(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                    String title,
                    long priority,
                    Frequency frequency,
                    long hashTagsIdx,
                    FxCopCategory category,
                    FxCopBreakingChange breakingChange,
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
        this.breakingChange = breakingChange;
        this.content = content;
    }

    public void update(FxCop fxCop) {
        setActiveStatus(fxCop.getActiveStatus());
        this.title = fxCop.getTitle();
        this.priority = fxCop.getPriority();
        this.frequency = fxCop.getFrequency();
        this.hashTagsIdx = fxCop.getHashTagsIdx();
        this.category = fxCop.getCategory();
        this.breakingChange = fxCop.getBreakingChange();
        this.content = fxCop.getContent();
    }
}
