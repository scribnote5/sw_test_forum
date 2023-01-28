/* fx_cop_comment table */
DROP TABLE fx_cop_comment;

CREATE TABLE fx_cop_comment
(
    idx                  bigint auto_increment primary key,
    created_by_idx       bigint null,
    created_date         datetime null,
    last_modified_by_idx bigint null,
    last_modified_date   datetime null,
    active_status        varchar(255) null,
    views                bigint null,
    fx_cop_idx          bigint null,
    content              mediumtext null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE fx_cop_comment
    AUTO_INCREMENT = 1;