package com.suresoft.sw_test_forum.common.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "tool_information")
public class ToolInformation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;

    private String tableName;

    private String toolName;

    private String toolNote;

    @Builder
    public ToolInformation(long idx,
                           String tableName,
                           String toolName,
                           String toolNote) {
        setIdx(idx);
        this.tableName = tableName;
        this.toolName = toolName;
        this.toolNote = toolNote;
    }

    public void update(ToolInformation hashTags) {
        this.tableName = hashTags.getTableName();
        this.toolName = hashTags.getToolName();
        this.toolNote = hashTags.getToolNote();
    }
}