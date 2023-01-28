package com.suresoft.sw_test_forum.common.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "project_information")
public class ProjectInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;

    private String tableName;

    private String projectName;

    @Builder
    public ProjectInformation(long idx,
                              String tableName,
                              String projectName) {
        setIdx(idx);
        this.tableName = tableName;
        this.projectName = projectName;
    }

    public void update(ProjectInformation hashTags) {
        this.tableName = hashTags.getTableName();
        this.projectName = hashTags.getProjectName();
    }
}