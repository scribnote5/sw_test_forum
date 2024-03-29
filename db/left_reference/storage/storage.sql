/* storage table */
DROP TABLE storage;

CREATE TABLE storage
(
    idx                  bigint auto_increment primary key,
    created_date         datetime     null,
    last_modified_date   datetime     null,
    created_by_idx       bigint default 0,
    last_modified_by_idx bigint default 0,
    active_status        varchar(255) null,
    views                bigint default 0,

    title                varchar(255) null,
    category             varchar(255) null,
    priority             bigint default 0,
    content              mediumtext   null
) ENGINE = InnoDB
  DEFAULT CHARSET = UTF8;

ALTER TABLE storage
    AUTO_INCREMENT = 1;