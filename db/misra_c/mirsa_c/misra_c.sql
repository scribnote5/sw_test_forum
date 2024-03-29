/* misra_c table */
DROP TABLE misra_c;

CREATE TABLE misra_c
(
    idx                  bigint auto_increment primary key,
    created_date         datetime      null,
    last_modified_date   datetime      null,
    created_by_idx       bigint default 0,
    last_modified_by_idx bigint default 0,
    active_status        varchar(255)  null,
    views                bigint default 0,

    title                varchar(255)  null,
    priority             bigint default 0,
    frequency            varchar(255)  null,
    hash_tags_idx        bigint default 0,
    category             varchar(255)  null,
    scope                varchar(255)  null,
    decidability         varchar(255)  null,
    applies_to           varchar(255)  null,
    qac_title            varchar(2048) null,
    content              mediumtext    null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE misra_c
    AUTO_INCREMENT = 1;