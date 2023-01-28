/* report table */
DROP TABLE report;

CREATE TABLE report
(
    idx                     bigint auto_increment primary key,
    created_date            datetime null,
    last_modified_date      datetime null,
    created_by_idx          bigint default 0,
    last_modified_by_idx    bigint default 0,
    active_status           varchar(255) null,
    views                   bigint default 0,

    title                   varchar(255) null,
    priority                bigint default 0,
    category                varchar(255) null,
    content                 mediumtext null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE report
    AUTO_INCREMENT = 1;