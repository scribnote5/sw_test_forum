package com.suresoft.sw_test_forum.common.domain;

import lombok.*;

import javax.persistence.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@Entity
@Table(name = "hash_tags")
public class HashTags {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long idx;

    private String tableName;

    private String content;

    @Builder
    public HashTags(long idx,
                    String tableName,
                    String content) {
        setIdx(idx);
        this.tableName = tableName;
        this.content = content;
    }

    public void update(HashTags hashTags) {
        this.tableName = hashTags.getTableName();
        this.content = hashTags.getContent();
    }
}
