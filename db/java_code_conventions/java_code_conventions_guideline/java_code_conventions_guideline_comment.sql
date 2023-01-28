/* java_code_conventions_guideline_comment table */
DROP TABLE java_code_conventions_guideline_comment;

CREATE TABLE java_code_conventions_guideline_comment
(
    idx                                 bigint auto_increment primary key,
    created_by_idx                      bigint null,
    created_date                        datetime null,
    last_modified_by_idx                bigint null,
    last_modified_date                  datetime null,
    active_status                       varchar(255) null,
    views                               bigint null,

    java_code_conventions_guideline_idx bigint null,
    content                             mediumtext null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE java_code_conventions_guideline_comment
    AUTO_INCREMENT = 1;