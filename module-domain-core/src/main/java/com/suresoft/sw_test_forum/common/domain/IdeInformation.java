package com.suresoft.sw_test_forum.common.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "ide_information")
public class IdeInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;

    private String tableName;

    private String ideName;

    @Builder
    public IdeInformation(long idx,
                          String tableName,
                          String ideName) {
        setIdx(idx);
        this.tableName = tableName;
        this.ideName = ideName;
    }

    public void update(IdeInformation hashTags) {
        this.tableName = hashTags.getTableName();
        this.ideName = hashTags.getIdeName();
    }
}