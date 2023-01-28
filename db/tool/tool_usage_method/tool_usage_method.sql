/* tool_usage_method table */
DROP TABLE tool_usage_method;

CREATE TABLE tool_usage_method
(
    idx                  bigint auto_increment primary key,
    created_date         datetime null,
    last_modified_date   datetime null,
    created_by_idx       bigint default 0,
    last_modified_by_idx bigint default 0,
    active_status        varchar(255) null,
    views                bigint default 0,

    title                varchar(255) null,
    priority             bigint default 0,
    hash_tags_idx        bigint default 0,
    tool_category        varchar(255) null,
    target_tool_name     varchar(255) null,
    content              mediumtext null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE tool_usage_method
    AUTO_INCREMENT = 1;
