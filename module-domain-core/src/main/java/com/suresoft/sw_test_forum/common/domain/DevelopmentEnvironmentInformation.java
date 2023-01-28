package com.suresoft.sw_test_forum.common.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "development_environment_information")
public class DevelopmentEnvironmentInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;

    private String tableName;

    private String developmentEnvironmentName;

    @Builder
    public DevelopmentEnvironmentInformation(long idx,
                                             String tableName,
                                             String developmentEnvironmentName) {
        setIdx(idx);
        this.tableName = tableName;
        this.developmentEnvironmentName = developmentEnvironmentName;
    }

    public void update(DevelopmentEnvironmentInformation developmentEnvironmentInformation) {
        this.tableName = developmentEnvironmentInformation.getTableName();
        this.developmentEnvironmentName = developmentEnvironmentInformation.getDevelopmentEnvironmentName();
    }
}