/* tool_usage_method_comment table */
DROP TABLE tool_usage_method_comment;

CREATE TABLE tool_usage_method_comment
(
    idx                   bigint auto_increment primary key,
    created_by_idx        bigint null,
    created_date          datetime null,
    last_modified_by_idx  bigint null,
    last_modified_date    datetime null,
    active_status         varchar(255) null,
    views                 bigint null,

    tool_usage_method_idx long null,
    content               mediumtext null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE tool_usage_method_comment
    AUTO_INCREMENT = 1;