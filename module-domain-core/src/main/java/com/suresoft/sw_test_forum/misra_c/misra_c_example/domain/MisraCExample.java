package com.suresoft.sw_test_forum.misra_c.misra_c_example.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_c.misra_c_example.listener.MisraCExampleListener;
import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "misra_c_example")
@EntityListeners(MisraCExampleListener.class)
public class MisraCExample extends CommonAudit {
    @Column(name = "misra_c_idx")
    private long misraCIdx;

    private String title;

    private long priority;

    private long toolInformationIdx;

    private long compilerInformationIdx;

    private String content;

    private String nonCompliantExample;

    private String compliantExample;

    private String badCasePositionList;

    private String goodCasePositionList;

    @Builder
    public MisraCExample(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                         long misraCIdx,
                         String title,
                         long priority,
                         long toolInformationIdx,
                         long compilerInformationIdx,
                         String content,
                         String nonCompliantExample,
                         String compliantExample,
                         String badCasePositionList,
                         String goodCasePositionList) {
        setIdx(idx);
        setCreatedDate(createdDate);
        setLastModifiedDate(lastModifiedDate);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.misraCIdx = misraCIdx;
        this.title = title;
        this.priority = priority;
        this.toolInformationIdx = toolInformationIdx;
        this.compilerInformationIdx = compilerInformationIdx;
        this.content = content;
        this.nonCompliantExample = nonCompliantExample;
        this.compliantExample = compliantExample;
        this.badCasePositionList = badCasePositionList;
        this.goodCasePositionList = goodCasePositionList;
    }

    public void update(MisraCExample misraCExample) {
        setActiveStatus(misraCExample.getActiveStatus());
        this.misraCIdx = misraCExample.getMisraCIdx();
        this.title = misraCExample.getTitle();
        this.priority = misraCExample.getPriority();
        this.toolInformationIdx = misraCExample.getToolInformationIdx();
        this.compilerInformationIdx = misraCExample.getCompilerInformationIdx();
        this.content = misraCExample.getContent();
        this.nonCompliantExample = misraCExample.getNonCompliantExample();
        this.compliantExample = misraCExample.getCompliantExample();
        this.badCasePositionList = misraCExample.getBadCasePositionList();
        this.goodCasePositionList = misraCExample.getGoodCasePositionList();
    }
}
