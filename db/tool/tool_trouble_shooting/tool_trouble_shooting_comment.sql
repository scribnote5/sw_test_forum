/* tool_trouble_shooting_comment table */
DROP TABLE tool_trouble_shooting_comment;

CREATE TABLE tool_trouble_shooting_comment
(
    idx                  bigint auto_increment primary key,
    created_by_idx       bigint null,
    created_date         datetime null,
    last_modified_by_idx bigint null,
    last_modified_date   datetime null,
    active_status        varchar(255) null,
    views                bigint null,

    tool_trouble_shooting_idx        long null,
    content              mediumtext null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE tool_trouble_shooting_comment
    AUTO_INCREMENT = 1;