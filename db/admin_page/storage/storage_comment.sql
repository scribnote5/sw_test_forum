/* storage_comment table */
DROP TABLE storage_comment;

CREATE TABLE storage_comment
(
    idx                  bigint auto_increment primary key,
    created_by_idx       bigint null,
    created_date         datetime null,
    last_modified_by_idx bigint null,
    last_modified_date   datetime null,
    active_status        varchar(255) null,
    views                bigint null,

    storage_idx          long null,
    content              mediumtext null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE storage_comment
    AUTO_INCREMENT = 1;