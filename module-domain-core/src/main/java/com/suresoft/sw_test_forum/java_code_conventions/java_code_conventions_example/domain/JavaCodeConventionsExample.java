package com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.domain;

import com.suresoft.sw_test_forum.common.domain.CommonAudit;
import com.suresoft.sw_test_forum.common.domain.enums.ActiveStatus;
import com.suresoft.sw_test_forum.java_code_conventions.java_code_conventions_example.listener.JavaCodeConventionsExampleListener;
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
@Table(name = "java_code_conventions_example")
@EntityListeners(JavaCodeConventionsExampleListener.class)
public class JavaCodeConventionsExample extends CommonAudit {
    @Column(name = "java_code_conventions_idx")
    private long javaCodeConventionsIdx;

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
    public JavaCodeConventionsExample(long idx, LocalDateTime createdDate, LocalDateTime lastModifiedDate, long createdByIdx, long lastModifiedByIdx, ActiveStatus activeStatus,
                                      long javaCodeConventionsIdx,
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
        this.javaCodeConventionsIdx = javaCodeConventionsIdx;
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

    public void update(JavaCodeConventionsExample javaCodeConventionsExample) {
        setActiveStatus(javaCodeConventionsExample.getActiveStatus());
        this.javaCodeConventionsIdx = javaCodeConventionsExample.getJavaCodeConventionsIdx();
        this.title = javaCodeConventionsExample.getTitle();
        this.priority = javaCodeConventionsExample.getPriority();
        this.toolInformationIdx = javaCodeConventionsExample.getToolInformationIdx();
        this.compilerInformationIdx = javaCodeConventionsExample.getCompilerInformationIdx();
        this.content = javaCodeConventionsExample.getContent();
        this.nonCompliantExample = javaCodeConventionsExample.getNonCompliantExample();
        this.compliantExample = javaCodeConventionsExample.getCompliantExample();
        this.badCasePositionList = javaCodeConventionsExample.getBadCasePositionList();
        this.goodCasePositionList = javaCodeConventionsExample.getGoodCasePositionList();
    }
}
