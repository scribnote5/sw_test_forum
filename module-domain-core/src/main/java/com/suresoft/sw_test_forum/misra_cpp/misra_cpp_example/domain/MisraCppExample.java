package com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.misra_cpp.misra_cpp_example.listener.MisraCppExampleListener;
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
@Table(name = "misra_cpp_example")
@EntityListeners(MisraCppExampleListener.class)
public class MisraCppExample extends CommonAudit {
    @Column(name = "misra_cpp_idx")
    private long misraCppIdx;

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
    public MisraCppExample(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                           long misraCppIdx,
                           String title,
                           long priority,
                           long toolInformationIdx,
                           long compilerInformationIdx,
                           String content,
                           String compliantExample,
                           String nonCompliantExample,
                           String badCasePositionList,
                           String goodCasePositionList) {
        setIdx(idx);
        setCreatedDate(createdDate);
        setLastModifiedDate(lastModifiedDate);
        setCreatedByIdx(createdByIdx);
        setLastModifiedByIdx(lastModifiedByIdx);
        setActiveStatus(activeStatus);
        this.misraCppIdx = misraCppIdx;
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

    public void update(MisraCppExample misraCppExample) {
        setActiveStatus(misraCppExample.getActiveStatus());
        this.misraCppIdx = misraCppExample.getMisraCppIdx();
        this.title = misraCppExample.getTitle();
        this.priority = misraCppExample.getPriority();
        this.toolInformationIdx = misraCppExample.getToolInformationIdx();
        this.compilerInformationIdx = misraCppExample.getCompilerInformationIdx();
        this.content = misraCppExample.getContent();
        this.nonCompliantExample = misraCppExample.getNonCompliantExample();
        this.compliantExample = misraCppExample.getCompliantExample();
        this.badCasePositionList = misraCppExample.getBadCasePositionList();
        this.goodCasePositionList = misraCppExample.getGoodCasePositionList();
    }
}
