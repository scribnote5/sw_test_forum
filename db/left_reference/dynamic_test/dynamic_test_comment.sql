/* dynamic_test_comment table */
DROP TABLE dynamic_test_comment;

CREATE TABLE dynamic_test_comment
(
    idx                  bigint auto_increment primary key,
    created_by_idx       bigint null,
    created_date         datetime null,
    last_modified_by_idx bigint null,
    last_modified_date   datetime null,
    active_status        varchar(255) null,
    views                bigint null,

    dynamic_test_idx     bigint null,
    content              mediumtext null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE dynamic_test_comment
    AUTO_INCREMENT = 1;