/* dynamic_test_like table */
DROP TABLE dynamic_test_like;

CREATE TABLE dynamic_test_like
(
    idx                  bigint auto_increment primary key,
    created_date         datetime null,
    last_modified_date   datetime null,
    created_by_idx       bigint default 0,
    last_modified_by_idx bigint default 0,
    active_status        varchar(255) null,
    views                bigint default 0,

    dynamic_test_idx     bigint default 0
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE dynamic_test_like
    AUTO_INCREMENT = 1;