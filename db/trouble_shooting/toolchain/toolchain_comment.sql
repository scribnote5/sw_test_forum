/* toolchain_comment table */
DROP TABLE toolchain_comment;

CREATE TABLE toolchain_comment
(
    idx                  bigint auto_increment primary key,
    created_by_idx       bigint null,
    created_date         datetime null,
    last_modified_by_idx bigint null,
    last_modified_date   datetime null,
    active_status        varchar(255) null,
    views                bigint null,

    toolchain_idx        long null,
    content              mediumtext null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE toolchain_comment
    AUTO_INCREMENT = 1;