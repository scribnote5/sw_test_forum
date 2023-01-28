package com.suresoft.sw_test_forum.common.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "target_environment_information")
public class TargetEnvironmentInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;

    private String tableName;

    private String targetEnvironmentName;

    @Builder
    public TargetEnvironmentInformation(long idx,
                                        String tableName,
                                        String targetEnvironmentName) {
        setIdx(idx);
        this.tableName = tableName;
        this.targetEnvironmentName = targetEnvironmentName;
    }

    public void update(TargetEnvironmentInformation environmentInformation) {
        this.tableName = environmentInformation.getTableName();
        this.targetEnvironmentName = environmentInformation.getTargetEnvironmentName();
    }
}