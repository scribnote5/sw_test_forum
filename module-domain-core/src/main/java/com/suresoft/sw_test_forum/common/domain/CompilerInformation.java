package com.suresoft.sw_test_forum.common.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "compiler_information")
public class CompilerInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;

    private String tableName;

    private String compilerName;

    private String compilerNote;

    @Builder
    public CompilerInformation(long idx,
                               String tableName,
                               String compilerName,
                               String compilerNote) {
        setIdx(idx);
        this.tableName = tableName;
        this.compilerName = compilerName;
        this.compilerNote = compilerNote;
    }

    public void update(CompilerInformation hashTags) {
        this.tableName = hashTags.getTableName();
        this.compilerName = hashTags.getCompilerName();
        this.compilerNote = hashTags.getCompilerNote();
    }
}
