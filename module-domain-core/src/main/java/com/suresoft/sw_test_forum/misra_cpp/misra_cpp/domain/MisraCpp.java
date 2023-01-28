package com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.common.domain.enums.Frequency;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.domain.enums.MisraCppCategory;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp.listener.MisraCppListener;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "misra_cpp")
@EntityListeners(MisraCppListener.class)
public class MisraCpp extends CommonAudit {
    private String title;

    private String originalTitle;

    private long priority;

    @Enumerated(EnumType.STRING)
    private Frequency frequency;

    private long hashTagsIdx;

    @Enumerated(EnumType.STRING)
    private MisraCppCategory category;

    private String qacTitle;

    private String content;

    @Builder
    public MisraCpp(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                    String title,
                    String originalTitle,
                    long priority,
                    Frequency frequency,
                    long hashTagsIdx,
                    MisraCppCategory category,
                    String qacTitle,
                    String content) {
        setIdx(idx);
        setCreatedDate(createdDate);
        setLastModifiedDate(lastModifiedDate);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.title = title;
        this.originalTitle = originalTitle;
        this.priority = priority;
        this.frequency = frequency;
        this.hashTagsIdx = hashTagsIdx;
        this.category = category;
        this.qacTitle = qacTitle;
        this.content = content;
    }

    public void update(MisraCpp misraCpp) {
        setActiveStatus(misraCpp.getActiveStatus());
        this.title = misraCpp.getTitle();
        this.originalTitle = misraCpp.getOriginalTitle();
        this.priority = misraCpp.getPriority();
        this.frequency = misraCpp.getFrequency();
        this.hashTagsIdx = misraCpp.getHashTagsIdx();
        this.category = misraCpp.getCategory();
        this.qacTitle = misraCpp.getQacTitle();
        this.content = misraCpp.getContent();
    }
}
